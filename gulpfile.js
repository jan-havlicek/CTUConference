/**
 * BASE USAGE:
 *
 * gulp [task] --no-production: no minification or js debug calls stripping
 * gulp [task] --no-watch: only disable watching of changes
 *
 */

// load package.json file - for resource serialize
var
  packageJson = require('./package.json'),

	sourcePath = './src/main/websrc',
	pathSass = sourcePath + '/stylesheets',
	pathCssStandalone = pathSass + '/standalone', //for files, that should be only copied and linked manually
	pathJs = sourcePath + '/scripts',
	pathImages = sourcePath + '/images',
	pathSpriteSources = pathImages + '/sprite',
	pathSvgSpriteSources = pathImages + '/svg-sprite',
	pathFonts = sourcePath + '/fonts',

	targetWwwPath = './src/main/webapp',
	targetAssetsPath = 'assets',
	targetPath = targetWwwPath + '/' + targetAssetsPath,
	targetPathJs = targetPath + '/js',
	targetPathCss = targetPath + '/css',
	targetPathImages = targetPath + '/images',
	targetPathFonts = targetPath + '/fonts',

        sassMainSource = pathSass + '/main.scss',
	targetSpriteImageFullPath = '../images/sprite.png',
	targetSvgSpriteImageFullPath = '../images/svg-sprite.png',

	gulp = require('gulp'),
	argv = require('yargs').argv,

	// dependencies are lazy loaded only if they are required (because of performance)
	concat = require('gulp-concat'),
	gulpif = require('gulp-if'),
	sass = null,
	sassLint = null,
	rename = null,
	imagemin = null,
	pngquant = null,
	sprite = null,
	svgSprite = null,
	gulpFilter = null,
	buffer = null,
	uglify = null,
	stripDebug = null,
	cssmin = null,
	autoPrefixer = null,
	del = null,
	modifyCssUrls = null,
	sourcemaps = null,
	source = null,
	browserify = null,
	ts = null,
	tsify = null;

/**
 * Fonts
 * Get fonts from websrc directory
 */
gulp.task('fonts', function () {
	gulpFilter = gulpFilter || require('gulp-filter');

	return gulp.src(pathFonts + '/**/*.*')
		.pipe(gulp.dest(targetPathFonts));
});

/**
 * Task for build typescript scripts into one minified file
 */
gulp.task('ts', function () {
	browserify = browserify || require('browserify');
	tsify = tsify || require('tsify');
	buffer = buffer || require('vinyl-buffer');
	uglify = uglify || require('gulp-uglify');
	stripDebug = stripDebug || require('gulp-strip-debug');
	source = source || require('vinyl-source-stream');
	sourcemaps = sourcemaps || require('gulp-sourcemaps');

	return browserify(pathJs + '/main.ts')
    .plugin(tsify)
		.bundle()
    	.on('error', function (error) { console.error(error.toString()); })
		.pipe(source('all.min.js'))
		.pipe(buffer())
		.pipe(sourcemaps.init({loadMaps: true}))
		.pipe(gulpif(argv.production, stripDebug()))
		//.pipe(gulpif(!argv.debug, uglify()))
		.pipe(sourcemaps.write('./'))
		.pipe(gulp.dest(targetPathJs));
});

/**
 * Sprite
 * Generating sprite and scss for sprite usage
 * - Pick all images from /images/sprite-sources directory and create sprite.png. Sprite then gets minified.
 * - Generate scss file, containing variables and mixins for usage simplification
 *
 * Example
 * If there is my-file.png picture in sprite-sources directory,
 * it can be used in main.scss like so:
 * #myelement {
 *	 .sprite($my-file);
 * }
 */
gulp.task('sprite', function () {
	imagemin = imagemin || require('gulp-imagemin');
	pngquant = pngquant || require('imagemin-pngquant');
	sprite = sprite || require('gulp.spritesmith');
	buffer = buffer || require('vinyl-buffer');

	// source path of the sprite images
	var spriteData = gulp.src(pathSpriteSources + '/**/*')
			.pipe(sprite({
				imgName: 'sprite.png',
				cssName: 'sprite.scss',
				imgPath: targetSpriteImageFullPath,
				padding: 10
			}));

	// output path for the CSS or SASS
	spriteData
		.css
		.pipe(gulp.dest(pathSass));

	spriteData
		.img
		.pipe(buffer())
		.pipe(imagemin({
			progressive: true,
			svgoPlugins: [{removeViewBox: false}],
			use: [pngquant({quality: '75-90', speed: 1})] //causes lossy compression
		}))
		.pipe(gulp.dest(targetPathImages)); // output path for the sprite

});

/**
 * Found here:
 * https://www.liquidlight.co.uk/blog/article/creating-svg-sprites-using-gulp-and-sass/
 */
gulp.task('svg-sprite', function () {
	svgSprite = svgSprite || require('gulp-svg-sprite');

	return gulp.src(pathSvgSpriteSources + '/**/*.svg')
		.pipe(svgSprite({
			shape: {
				spacing: {
					padding: 0
				}
			},
			mode: {
				css: {
					dest: './',
					layout: 'diagonal',
					sprite: targetPath+'/images/svg-sprite.svg',
					bust: false,
					render: {
						scss: {
							dest: sourcePath + '/stylesheets/svg-sprite.scss',
							template: sourcePath + '/tpl/svg-sprite-template.scss'
						}
					}
				},
				defs: {
					dest: './',
					sprite: targetPath + '/images/svg-defs-sprite.svg'
				}
			},
			variables: {
				mapname: 'icons'
			}
		}))
		.pipe(gulp.dest('.'));
});

/**
 * SASS
 * Linter - walk all SCSS files in stylesheets directory and lint them (only libs path ignored)
 */
gulp.task('sass-lint', function () {
	sassLint = sassLint || require('gulp-sass-lint');
	var sassSources = [pathSass + '/**/*.scss',
        '!' + pathSass + '/libs/*.*',
		'!' + pathSass + '/sprite.scss',
		'!' + pathSass + '/_icon-sprite.scss'];

	return gulp.src(sassSources)
		.pipe(sassLint({'config': '.sass-lint.yml'}))
		.pipe(sassLint.format())
		.pipe(sassLint.failOnError());
});

/**
 * SASS and CSS
 * Concatenation, compilation and minification
 * - pick only main.scss, that includes all necessary dependencies
 * - if in production mode, minify target file
 */
gulp.task('sass', [], function () {
	sass = sass || require('gulp-sass');
	rename = rename || require('gulp-rename');
	cssmin = cssmin || require('gulp-cssmin');
	autoPrefixer = autoPrefixer || require('gulp-autoprefixer');
	modifyCssUrls = modifyCssUrls || require('gulp-modify-css-urls');
	sourcemaps = sourcemaps || require('gulp-sourcemaps');

	//copy standalone styles
	gulp.src([pathCssStandalone+'/*.css'])
		.pipe(gulp.dest(targetPathCss));

	return gulp.src(sassMainSource)
		.pipe(sourcemaps.init())
		.pipe(sass().on('error', sass.logError))
		.pipe(concat('all.min.css'))
		.pipe(autoPrefixer({
			browsers: [
				'last 2 versions',
				'android 4',
				'opera 12',
				'ie >= 9'
			],
			cascade: false
		}))
		.pipe(modifyCssUrls({
			append: '?v=' + packageJson.version
		}))
		.pipe(gulpif(!argv.debug, cssmin()))
		.pipe(sourcemaps.write('./'))
		.pipe(gulp.dest(targetPathCss));
});

/**
 * Images
 * Shrink image file sizes and copy minified files into target directory.
 * It can use both lossless either lossy comporession (lossy compression by pngquant)
 */
gulp.task('imagemin', function () {
	imagemin = imagemin || require('gulp-imagemin');
	pngquant = pngquant || require('imagemin-pngquant');

	return gulp.src([pathImages + '/**/*.*', '!'+pathImages+'/sprite/*.*', '!'+pathImages+'/svg-sprite/*.*'])
		.pipe(imagemin({
			progressive: true,
			svgoPlugins: [{removeViewBox: false}],
			//use: [pngquant({quality: '75-90', speed: 1})] //causes lossy compression
		}))
		.pipe(gulp.dest(targetPathImages));
});

/**
 * Watch changes of source files
 */
gulp.task('watch', function () {
	if (!argv.production && argv.watch !== false) {
		gulp.watch('./package.json', ['sass']);
		gulp.watch(pathSpriteSources + '/**/*', ['sprite']);
		gulp.watch(pathSvgSpriteSources + '/**/*', ['svg-sprite']);
		gulp.watch([pathSass + '/**/*.scss'], ['sass']);
		gulp.watch([pathJs + '/*.js'], ['js']);
		// for watching imagemin it is necessary to have assets and target directory, or it will end up with infinite loop.
		gulp.watch(pathImages + '/**/*', ['imagemin']);
	}
});

/**
 * Clean all compiled assets
 */
gulp.task('clean', function() {
	del = del || require('del');

	return del([targetPath]);
});

/**
 * Default task
 * Options:
 *  --production: do minification and strip js debug calls. Also disable changes watching
 *  --no-watch: only disable changes watching
 */
gulp.task('default', function () {
	var colors = require('colors/safe'); // does not alter string prototype
	console.log(colors.red("Use 'npm run build' for project initialization instead. For watch use 'gulp watch'."));
	return true;
});

/**
 * Build task
 *
 * This task compile SASS, JavaScripts and linting them.
 */
gulp.task('build', ['sass', 'js', 'imagemin', 'fonts']);

/**
 * Build task
 *
 * Task for build sprites and svg sprites
 */
gulp.task('build-sprites', ['sprite', 'svg-sprite']);
