<template>
  <div
    ref="card"
    class="bg-gray-900 p-4 rounded"
    :class="{ 'border-2 border-red-600': warningC }"
  >
    <h1 class="text-3xl text-center mb-4">Login</h1>
    <form @submit="login" @submit.prevent>
      <input
        id="username"
        ref="username"
        type="text"
        placeholder="username"
        class="p-0.5 bg-gray-800 border-2 border-gray-600 mb-2 rounded-sm outline-none"
        :class="{
          'border-red-500': warningU,
          'transition-[border] duration-300': !warningU,
        }"
        @keydown="warningU = false"
        @keydown.enter="login"
      /><br />
      <input
        id="password"
        ref="password"
        type="password"
        placeholder="password"
        class="p-0.5 bg-gray-800 border-2 border-gray-600 mb-2 rounded-sm outline-none"
        :class="{
          'border-red-500': warningP,
          'transition-[border] duration-300': !warningP,
        }"
        @keydown="warningP = false"
        @keydown.enter="login"
      /><br />
      <div class="flex justify-center">
        <button
          id="submit"
          ref="submit"
          class="bg-gray-800 border-2 border-gray-600 rounded-sm p-1"
        >
          Login
        </button>
      </div>
    </form>
  </div>
</template>

<script>
import Vue from 'vue'
export default {
  name: 'LoginComponent',
  data() {
    return {
      warningU: false,
      warningP: false,
      warningC: false,
    }
  },
  methods: {
    login() {
      if (this.$refs.username.value === '') {
        Vue.nextTick(() => {
          Vue.nextTick(() => this.$animation(this.$refs.username, 'headShake'))
        })
        this.warningU = true
      }
      if (this.$refs.password.value === '') {
        Vue.nextTick(() => {
          Vue.nextTick(() => this.$animation(this.$refs.password, 'headShake'))
        })
        this.warningP = true
      }
      if (this.warningU || this.warningP) return
      const password = this.$refs.password.value
      this.$refs.password.value = ''
      this.$axios
        .$post('auth/login', {
          username: this.$refs.username.value,
          password,
        })
        .then(() => {
          this.$parent.checkIfAuthenticated()
        })
        .catch((error) => {
          if (error.response.status === 403) {
            Vue.nextTick(() => {
              Vue.nextTick(() =>
                this.$animation(this.$refs.username, 'headShake')
              )
            })
            this.warningU = true
            Vue.nextTick(() => {
              Vue.nextTick(() =>
                this.$animation(this.$refs.password, 'headShake')
              )
            })
            this.warningP = true
          } else {
            Vue.nextTick(() => {
              Vue.nextTick(() => this.$animation(this.$refs.card, 'headShake'))
            })
            this.warningC = true
            throw error
          }
        })
    },
  },
}
</script>
