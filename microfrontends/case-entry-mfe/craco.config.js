module.exports = {
    webpack: {
      plugins: {
        remove: ['MiniCssExtractPlugin']
      },
      configure: (webpackConfig, { env, paths }) => {
        webpackConfig.module.rules[1].oneOf[5].use.shift()
        webpackConfig.module.rules[1].oneOf[6].use.shift()
        webpackConfig.module.rules[1].oneOf[7].use.shift()
        webpackConfig.module.rules[1].oneOf[8].use.shift()

        const styleLoaderConf = {
            loader: "style-loader",
            options: {
                injectType: "lazyStyleTag",
                insert: function insertIntoTarget(element, options) {
                  var parent = options.target || document.head;
  
                  parent.appendChild(element);
                },
                styleTagTransform: function transformCSS(css, style) {
                  // makes root styles (i.e. Bootstrap CSS variables) apply to shadow DOM host
                  style.innerHTML = css.replace(':root', ':host,:root');
                }
            }
        }

        webpackConfig.module.rules[1].oneOf[5].use.unshift(styleLoaderConf)
        webpackConfig.module.rules[1].oneOf[6].use.unshift(styleLoaderConf)
        webpackConfig.module.rules[1].oneOf[7].use.unshift(styleLoaderConf)
        webpackConfig.module.rules[1].oneOf[8].use.unshift(styleLoaderConf)

        // build config
        const entry = './src/index.js';
        const output = {
            filename: 'case-entry-mfe.umd.js',
            library: {
                type: 'umd',
                name: 'build',
            }
        };
        webpackConfig.entry = entry;
        webpackConfig.output = output;
 
        return webpackConfig;
      },
    },
  };
