<template>
  <div
    id="body-wrapper"
    class="flex justify-center items-center bg-gradient-to-br from-gray-700 to-gray-900 text-white"
  >
    <transition
      mode="out-in"
      leave-active-class="animate__animated animate__fadeOut main-transition-speed"
      enter-active-class="animate__animated animate__fadeIn main-transition-speed"
    >
      <login-component v-if="!login"></login-component>
      <console-component v-if="login"></console-component>
    </transition>
  </div>
</template>

<script>
export default {
  name: 'IndexPage',
  data() {
    return {
      login: false,
    }
  },
  fetchOnServer: false,
  async fetch() {
    await this.checkIfAuthenticated()
  },
  methods: {
    async checkIfAuthenticated() {
      await this.$axios
        .$get('status')
        .then(() => {
          this.login = true
        })
        .catch(() => {
          this.login = false
        })
    },
  },
}
</script>

<style>
.main-transition-speed {
  --animate-duration: 400ms;
}
</style>
