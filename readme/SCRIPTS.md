# Processing Typescript

There is supported Typescript language, that is compiled into plain ES5 javascript.

### Gulp task for JS processing
1. `gulp ts`performs minification, console logs are preserved

## Dependencies
1. Standard way how to add dependency is through ES6/TypeScript module import syntax.
It should be used for all npm dependencies (All Angular2 dependencies are loaded like so)
and also for all custom ts scripts.

## Type definition files
Typescript needs for its building process to know all type definitions. It means that for all dependencies
there is necessary to add this definition. In the `package.json` configuration file, you can add it as dev-dependency
like this "@types/jasmine".

You can find definition files for almost all most popular scripts. When you need to use library, that doesn't have typescript
support, you need to write your type definition file explicitly. You can store it as `types/package_name/index.d.ts` and then add
reference to this file in `types/index.d.ts` file.
