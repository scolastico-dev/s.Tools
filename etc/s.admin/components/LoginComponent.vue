<template>
  <div class="bg-gray-900 p-4 rounded">
    <h1 class="text-3xl text-center mb-4">Login</h1>
    <form @submit="login" @submit.prevent>
      <input
        id="username"
        ref="username"
        type="email"
        placeholder="username"
        class="p-0.5 bg-gray-800 border-2 border-gray-600 mb-2 rounded-sm outline-none"
        :class="{'border-red-500': warningU, 'transition-[border] duration-300': !warningU}"
        @keydown="warningU = false"
        @keydown.enter="login"
      ><br>
      <input
        id="password"
        ref="password"
        type="password"
        placeholder="password"
        class="p-0.5 bg-gray-800 border-2 border-gray-600 mb-2 rounded-sm outline-none"
        :class="{'border-red-500': warningP, 'transition-[border] duration-300': !warningP}"
        @keydown="warningP = false"
        @keydown.enter="login"
      ><br>
      <div class="flex justify-center">
        <button id="submit" ref="submit" class="bg-gray-800 border-2 border-gray-600 rounded-sm p-1">Login</button>
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
    }
  },
  methods: {
    login() {
      if (this.$refs.username.value === "") {
        Vue.nextTick(() => {
          Vue.nextTick(() => this.$animation(this.$refs.username, "headShake"))
        })
        this.warningU = true
      }
      if (this.$refs.password.value === "") {
        Vue.nextTick(() => {
          Vue.nextTick(() => this.$animation(this.$refs.password, "headShake"))
        })
        this.warningP = true
      }
      if (this.warningU || this.warningP) return
      this.$axios.$post('login', {
        username: this.$refs.username.value,
        password: this.$refs.password.value
      }).then(response => {
        console.log("200")
        console.log(response.data)
      }).catch(error => {
        if (error.response.status === 403) {
          console.log("403")
        } else {
          console.log("error")
        }
      });
    },
  },
}
</script>
