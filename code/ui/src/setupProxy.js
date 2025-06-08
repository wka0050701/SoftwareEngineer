const { createProxyMiddleware } = require('http-proxy-middleware');

module.exports = function(app) {
  app.use(
    '/api',
    createProxyMiddleware({
      target: 'http://localhost:8080',
      changeOrigin: true,
      // 如果后端接口没有 /api 前缀，可以重写路径
      // pathRewrite: {
      //   '^/api': ''
      // }
    })
  );
}; 