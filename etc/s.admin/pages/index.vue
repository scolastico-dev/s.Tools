<template>
  <div id="body-wrapper" class="flex justify-center items-center bg-gradient-to-br from-gray-700 to-gray-900 text-white" @click="login = !login">
    <transition
      mode="out-in"
      leave-active-class="animate__animated animate__fadeOut"
      enter-active-class="animate__animated animate__fadeIn"
    >
      <login-component v-if="login"></login-component>
      <console-component v-if="!login"></console-component>
    </transition>
  </div>
</template>

<script>
export default {
  name: 'IndexPage',
  data() {
    return {
      login: true,
    }
  },
}
</script>
