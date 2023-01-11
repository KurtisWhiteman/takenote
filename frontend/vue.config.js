// vue.config.js
module.exports = {
  productionSourceMap: false,
  configureWebpack: {
    devtool: 'source-map',
    optimization: {
      runtimeChunk: 'single',
      splitChunks: {
        chunks: 'all',
        maxInitialRequests: Infinity,
        minSize: 0
      }
    }
  },

  // proxy all webpack dev-server requests starting with /api
  // to our Spring Boot backend (localhost:8088) using http-proxy-middleware
  // see https://cli.vuejs.org/config/#devserver-proxy
  devServer: {
    // Replicate headers in the webpack dev-server to what we inject via lambda function in aws
    headers: {
      'Strict-Transport-Security': 'max-age=31536000; includeSubdomains; preload',
      'Content-Security-Policy':
        "default-src 'self';" +
        "frame-src 'self' data: blob:;" +
        "object-src 'self' data: blob:;" +
        "img-src data: 'self' https://www.google-analytics.com https://www.googletagmanager.com; " +
        "connect-src 'self' https://www.google-analytics.com https://www.googletagmanager.com *;" +
        "script-src 'self' https://use.fontawesome.com https://www.google-analytics.com https://www.googletagmanager.com; " +
        "style-src 'self' data: https://fonts.googleapis.com https://use.fontawesome.com 'unsafe-inline'; " +
        "font-src 'self' https://fonts.gstatic.com https://use.fontawesome.com;",
      'X-Content-Type-Options': 'nosniff',
      'X-Frame-Options': 'DENY',
      'X-XSS-Protection': '1; mode=block',
      'Referrer-Policy': 'same-origin'
    },
    proxy: {
      '/api': {
        target: 'http://localhost:8098', // this configuration needs to correspond to the Spring Boot backends' application.properties server.port
        ws: true,
        changeOrigin: true
      }
    }
  },
  // Change build paths to make them Maven compatible
  // see https://cli.vuejs.org/config/
  outputDir: 'target/dist',
  assetsDir: 'static',
  css: {
    loaderOptions: {
      sass: {
        additionalData: '@import "@/styles/_global.scss";'
      }
    }
  }
}
