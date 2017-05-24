# FONTS

## How to add fonts to the project?

1. **From CDN** - There is used Google apis CDN. You can add import into `src/main/websrc/stylesheets/common/_fonts.scss`

2. **Add manually** - you can put font files into the `src/main/websrc/fonts` directory (you can use subdirectory for each font family too). All files from this
directory are copied into `src/main/webapp/assets/fonts` directory (when subdirectories are used, subdirectories will be preserved in assets directory too).
