# Sprite creation and minification of images

- All image sources are stored in `src/main/websrc/images` directory.

## Image minification
All image files in the root of  `src/main/websrc/images` or in any of its subdirectories except `websrc/images/sprite` and `websrc/images/svg-sprite`
are minified and copied to `src/main/webapp/assets/images` directory.
You just run `$ gulp imagemin` command.


## Sprite generation
Directory `src/main/websrc/images/sprite` is designated to store all pictures that should be compiled into single sprite file.
You can separate images in sprite directory logically into subdirectories. Images from all these subdirectories are processed into one file.
Run `$ gulp sprite` to generate sprite image. It is minified and stored directly into `src/main/webapp/assets/images` root directory. This also generates `sprite.scss`
in `src/main/websrc/stylesheets` directory, that is imported in `main.scss` file.


### Using sprite.scss in SASS
In the `sprite.scss` file, there are defined some mixins and variables for each picture.
If you add eg. my-image.png file into sprite directory, sprite.scss will contain variables such as $my-image, $my-image-width, $my-image-height etc.
You can use them in conjunction with sprite mixin, for example like this:

```scss
.my-icon {
	@include sprite($my-image);
}
```


## SVG Sprite generation
Directory `websrc/images/svg-sprite` is designated to store all pictures that should be compiled into single sprite file.
You can separate images in svg-sprite directory logically into subdirectories. Images from all these subdirectories are processed into one file.
Run `$ gulp svg-sprite` to generate sprite image. It is stored directly into `www/assets/images` root directory. This also generates `svg-sprite.scss`
in `websrc/stylesheets` directory, that is imported in `main.scss` file. It generates similar structure to sprite.scss so that the usage
is almost same as the usage of classical sprite.

### Using sprite.scss in SASS

In the `svg-sprite.scss` file, there are defined some mixins and variables for each picture.
If you add eg. my-image-2.svg file into sprite directory, svg-sprite.scss will contain variables such as $svg-my-image-2, $svg-my-image-2-width, $svg-my-image-2-height etc.
(it means the value of svg image with `svg-` prefix). You can use them in conjunction with sprite mixin, for example like this:

```scss
.my-icon {
	@include svg-sprite($svg-my-image);
}
```

The main difference is, that the size of the image is not fixed, but it is possible to use it dynamically. The size is connected to some defined font size
and the size of images in sprite is defined in **"em"** .
The sprite SCSS file defines $svg-icon-font-size, that you can override. When you use svg-sprite mixin on some element, then font-size of this
element is set to this value (or you can override this size eg. with "em" values with second parameter of svg-sprite mixin), and size of image
is set in **em** units **relative** to this defined font-size.
When you use svg-sprite with second parameter set e.g. to 1em, the size of image is relative to font size of parent element and is thus
dynamically resized every time parent font-size changes.

```scss
/* my basic font size definitions */
$basic-font-size: 16px;
$headline-font-size: 24px;

/* override svg-sprite variable with default font size */
$svg-icon-font-size: $basic-font-size;

@import 'svg-sprite';

.my-icon {
	@include svg-sprite($svg-my-image);
	/* is same like: @include svg-sprite($svg-my-image, 16px); and it displays image in its original size. */
}

h2 {
	font-size: $headline-font-size;

	.my-icon {
    	@include svg-sprite($svg-my-image, $headline-font-size);
    	/* for font-size: 16px, it has its 100% image size, so for 24px it has 150% of its original size */
    }
}

h2 {
	font-size: $headline-font-size;

	.my-icon {
    	@include svg-sprite($svg-my-image, 1em);
    	/* dynamic version - now its size is 150% of origina size (because parent element has font-size: 24px)
    	and when font-size of h2 is enlarged (eg. via media queries), image will enlarge automatically too */
    }
}

@media screen and (min-width: 768px) {
	h2 {
		font-size: $headline-font-size * 2;

		/* now .my-icon sprite is twice as large */
	}
}
```

###Use SVG sprite directly in HTML

Previous approach has one huge disadvantage - you can't change color of picture. It is possible only when you define SVG image inside html,
not as background image. There is possibility to use SVG image directly in HTML code.

At first, you need to add inline svg with path definitions. When you run `$ gulp svg-sprite`, another svg file with picture definitions
is also automatically generated in `www/assets/images/sprite-defs-sprite.svg`. This is special svg file containing &lt;defs&gt; with specific IDs,
so it is possible to refer to this definitions from svg 'use' element via their ID. There is no automatized way to inject this file into inline
SVG element into HTML page, but if you want to use SVG this way, you can copy content of this file inline into HTML on the begining of the page
and then later use it where you want. Another disadvantage of this approach is, you must define viewBox manually, so you need to look at source
SVG for its size. There is simple example.

```html
...
<body>
	<svg n:syntax="off" xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink"><defs><svg id="arrow" viewBox="-2 -2 121 65.5"><style>.ast0{fill:none;stroke:#b1b0b0;stroke-width:3.5;stroke-linecap:round;stroke-miterlimit:10}</style><path class="ast0" d="M11.9 17.9s16.6 19.9 46.3 26c28.5 5.8 48.7-1 48.7-1"/><path class="ast0" d="M26 47.9l-14.1-30 32.5 10"/></svg><svg id="leftArrow" viewBox="-2 -2 33 52"><style>.bst0{fill:none;stroke:#cecece;stroke-width:2;stroke-linecap:round;stroke-miterlimit:10}</style><path class="bst0" d="M25 5.7l-19 19 19 19"/></svg><svg preserveAspectRatio="xMidYMid" viewBox="-2 -2 39 26" id="menuIcon"><defs><style>.ccls-1{fill:#666;fill-rule:evenodd}</style></defs><path d="M0 22v-2h35v2H0zm0-12h35v2H0v-2zM0 0h35v2H0V0z" class="ccls-1"/></svg></defs></svg>
	...
	<h2>
		<svg viewBox="0 0 33 52" class="icon left-arrow">
          <use xlink:href="#leftArrow"></use>
        </svg>
	</h2>
```

Now you can style colors of SVG image by **fill** or **stroke** css properties.

### Other approaches (not cross-browser solutions) - not supported by skeleton

#### Fragment identifier approach
In the previous approach, it was necessary to create inline svg definition before you could refer to them via &lt;use&gt; element.
There is another solution via fragment identifier. In this approach, it is possible to use it e.g. in IMG element and you only
need to define viewBox or define VIEW elements. This approach is not cross-browser yet, so this skeleton doesn't use it.
You can find description of this approach here: https://css-tricks.com/svg-fragment-identifiers-work/.
```html
	<view id="icon-heart-view" viewBox="0 32 32 32" />
	...
	<img src="sprite.svg#svgView(viewBox(0, 0, 32, 32))" alt="">
	<img src="sprite.svg#icon-heart-view" alt="">
```

#### Masking approach
Another not cross-browser approach is to use svg as mask of background.
You can read about this here: http://codepen.io/noahblon/post/coloring-svgs-in-css-background-images

```css
.icon {
	background-color: red;
	mask: url(icon.svg) no-repeat 50% 50%;
}
```
