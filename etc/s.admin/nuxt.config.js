export default {
  ssr: true,
  target: 'static',
  head: {
    title: 's.Admin',
    htmlAttrs: {
      lang: 'en',
    },
    meta: [
      { charset: 'utf-8' },
      { name: 'viewport', content: 'width=device-width, initial-scale=1' },
      { hid: 'description', name: 'description', content: '' },
      { name: 'format-detection', content: 'telephone=no' },
    ],
    link: [{ rel: 'icon', type: 'image/x-icon', href: '/favicon.ico' }],
  },

  css: ['~assets/animate.min.css', '~assets/global.css'],

  plugins: ['~plugins/animation-helper.js'],

  components: true,

  buildModules: ['@nuxtjs/eslint-module', '@nuxtjs/tailwindcss'],

  modules: ['@nuxtjs/axios'],

  axios: {
    baseURL: '/.admin/api/',
    withCredentials: true,
  },

  router: {
    base: '/.admin/',
  },

  tailwindcss: {
    configPath: '~/tailwind.config.js',
  },

  eslint: {
    cache: false,
  },
}
