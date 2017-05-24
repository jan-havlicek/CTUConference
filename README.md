# <a value="prerequisities"></a>Prerequisities

1. Download **Node.js**
1. Download **Gulp** via npm globally so that they can be accessible in command line (`$ npm install -g gulp`)
1. Install EditorConfig plugin to your IDE

# <a value="howtorun"></a>How to run the project

1. Run `$ npm run build`. It should install all npm and typings and Gulp packages and build sprites, stylesheets and scripts via Gulp tasks.

Now the project should be prepared to run. Now you may want to use these basic commands for further development:

1. To compile source files run `$ gulp build` (`$ gulp build-sprites` should precede when sprite is changed).
1. To watch source files changes automatically run `$ gulp watch`.

# How to work with database.
Basics are described here: [Database](/readme/DATABASE.md)

# How to run basic build tasks
[Build tasks](/readme/BUILD_TASKS.md)

# How to build and use web sources

#### Sprites, image minification
`gulp sprite` - it creates `sprite.png` and `sprite.scss` from images stored in `/websrc/images/sprite` directory
`gulp imagemin` - it transfers all pictures from `/websrc/images` and minify them into `/www/images` directory
More about this: [Sprites, image minification](/readme/IMAGES.md)

#### Stylesheets, SASS
`gulp sass` - it check code standards in all custom SCSS files and compile them into single CSS file.
More about this: [Stylesheets, SASS](/readme/STYLESHEETS.md)

##### Online inject stylesheets
There is a possibility of injecting stylesheet changes into browser (without reloading browser). If you wish use this feature, you can run `gulp watch-sync`. Before it is required user's action for creation configuration file for BrowserSync. For more details, please check steps about BrowserSync in [How to run the project](#howtorun) section

#### Javascript
`gulp js` - it check code standards in all custom JS files and compile them into single JS file.
More about this: [JavaScript](/readme/SCRIPTS.md)

#### Versioning of web source
When you need to force recaching of web sources in user browsers
More about this: [Versioning of web sources](/readme/VERSIONING.md)

# Code Standards

There are some coding standars that should be followed. There are described CS for each language and how it is tested:
[Coding standards, settings](/readme/CODE_STANDARDS.md)
