

# SASS Processing

## Dependencies
1. **Npm dependencies** - You can import css and scss from npm packages. It is necessary to import these styles into your `main.scss` file **manually**.
1. **When no Npm package is available**, you can put some external stylesheets into special directory `src/main/websrc/stylesshets/libs`.
Files stored in this directory are not linted. It is necessary to import these styles into your `main.scss` file **manually** (you can see example in "Import SASS files" section below).
1. **Conditional adding of dependency (when you need to add css hacks when IE9, when mobile device, etc.)** - You can put your CSS (pure CSS, not SASS)
out of all other stylesheets (no linting, compiling or minification is processed). These files are supposed to be small pieces of pure **CSS**,
so they will be only coppied from `src/main/websrc/stylesheets/standalone` to `www/css` directoy and you have to link them into layout manually.


## Import SASS files
It is possible to split your SASS into separate files. It is possible to create your SASS in `src/main/websrc/stylesheets` directory and its subdirectories.
All these files will be linted and if you import them into `main.scss`, it will be compiled into one target file.

```scss
@import 'common/variables';
```

## Simplified responsive styles
There is possible to use [`sass-mq library`](https://github.com/sass-mq/sass-mq) to simplify writing responsive styles.
It can be used like this:

```scss
@include mq($from: xsmall) { ... }

//it is simplification of:
//@media screen and (min-width: $screen-xs-min) { ... }
```

or

```scss
@include mq($from: small, $to: medium) { ... }

//it is simplification of:
//@media screen and (min-width: $screen-xs-min) and (max-width: $screen-md-max) { ... }
```

There are four breakpoints, that are defined in variables file in `src/main/websrc/stylesheets/common/_variables.scss`. The breakpoints are set in `src/main/websrc/stylesheets/common/_mq_settings.scss`.
They are 'xsmall', 'small', 'medium', 'large'.

