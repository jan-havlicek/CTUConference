# Versioning of web sources

There is possible to change version of images in scss files (js and css files themselves not yet) when it is necessary to restore cache in clients browsers.
When watch is enabled, then it automatically recompile SASS and update image URLs with new version.

## How to change version?
1. Go to `package.json` and modify `version` parameter. This parameter appears in web resources URLs when they are linked as a query parameter `?v=X.Y.Z`

## What resources are versioned?
1. All URLs in stylesheets are versioned. They are modified every time when Gulp SASS task is performed.
