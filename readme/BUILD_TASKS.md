# Build tasks - how and when to run tasks

## First project build or whole project build
`$ npm run build` - it will install npm dependencies including gulp dev dependencies. Then it will finalize all web sources (compilation, minification, sprite creation etc) from `src/main/websrc` to `src/main/webapp` directory.
If all is done, your web sources are ready to use.

`$ gulp build-sprites`
It will performs sprite and svg sprite generation. It is necessary to run before sass build script.

## Process all frontend tasks
`$ gulp build`
It performs all frontend tasks except sprite minification. It is necessary to run it first. in one bundle:
- TS compilation
- Image minification
- CSS compilation
- Font preparing

You can call all these tasks separately. All of them are described in specific sections.
