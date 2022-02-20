<template>
  <div class="bg-gray-900 p-4 shadow-lg shadow-gray-800 rounded">
    <div class="flex items-start justify-end mb-4">
      <div class="flex-grow">
        <h1 class="text-4xl">s.Admin</h1>
        <p class="text-s">
          Version 1.0.0 - By
          <a href="https://scolasti.co/" class="text-cyan-700 hover:underline"
            >scolastico</a
          >.
        </p>
      </div>
      <button class="logout" @click="logout">
        <LogOutIcon size="20" class="mr-0.5"></LogOutIcon>
        Logout
      </button>
    </div>
    <div class="console">
      <div
        id="content"
        ref="content"
        class="flex-grow py-1 px-2 w-full console-scrollbar"
        @scroll="scroll"
      ></div>
      <div class="flex w-full py-1 px-2 border-t border-gray-500">
        <label for="input">$</label>
        <input
          id="input"
          ref="input"
          class="bg-black text-white flex-grow ml-0.5 outline-none"
          type="text"
          autocomplete="false"
          spellcheck="false"
          placeholder="command"
          @keydown.enter="submit"
        />
      </div>
    </div>
  </div>
</template>

<script>
import { LogOutIcon } from 'vue-feather-icons'
import Vue from 'vue'
export default {
  name: 'ConsoleComponent',
  components: {
    LogOutIcon,
  },
  data() {
    return {
      connection: null,
      ansi: require('ansispan'),
      code: null,
      authenticated: false,
      bottom: true,
      scheduler: null,
    }
  },
  fetchOnServer: false,
  fetch() {
    const parent = this
    this.$axios
      .$get('console/history')
      .then((result) => {
        result.forEach((e) => parent.log(e))
        this.scheduler = window.setInterval(() => {
          parent.$parent.checkIfAuthenticated()
        }, 10000)
        let newUri
        const loc = window.location
        if (loc.protocol === 'https:') {
          newUri = 'wss:'
        } else newUri = 'ws:'
        newUri += '//' + loc.host + '/.admin/api/console/live'
        parent.connection = new WebSocket(newUri)
        parent.connection.onmessage = function (event) {
          if (parent.code === null) {
            parent.code = event.data
            parent.$axios.$get('auth/live/' + event.data).catch((error) => {
              parent.$parent.checkIfAuthenticated()
              throw error
            })
          } else if (
            !parent.authenticated &&
            event.data.startsWith('authenticated with user ')
          ) {
            parent.authenticated = true
          } else {
            parent.log(event.data)
          }
        }
        this.connection.onclose = function (event) {
          this.$parent.checkIfAuthenticated()
        }
      })
      .catch((error) => {
        this.$parent.checkIfAuthenticated()
        throw error
      })
  },
  beforeDestroy() {
    try {
      this.connection.close()
    } catch (e) {}
    if (this.scheduler != null) {
      clearInterval(this.scheduler)
      this.scheduler = null
    }
  },
  methods: {
    submit() {
      const command = this.$refs.input.value
      this.$refs.input.value = ''
      this.$axios.$post('console/send', { command }).catch((error) => {
        if (error.response.status === 403) {
          this.$animation(this.$refs.input, 'shakeX')
        } else {
          this.$parent.checkIfAuthenticated()
          throw error
        }
      })
    },
    logout() {
      this.$axios
        .$get('auth/logout')
        .then(() => this.$parent.checkIfAuthenticated())
        .catch((error) => {
          this.$parent.checkIfAuthenticated()
          throw error
        })
    },
    log(line) {
      if (this.bottom) {
        Vue.nextTick(() => {
          this.$refs.content.scrollTop = this.$refs.content.scrollHeight
          this.bottom = true
        })
      }
      this.$refs.content.innerHTML +=
        this.ansi(line).replaceAll(
          // eslint-disable-next-line no-control-regex
          /\x1B(?:[@-Z\\-_]|\[[0-?]*[ -/]*[@-~])/g,
          ''
        ) + '<br>'
    },
    scroll(event) {
      this.bottom =
        event.target.offsetHeight + event.target.scrollTop >=
        event.target.scrollHeight
    },
  },
}
</script>

<style>
.console {
  @apply flex flex-col h-[65vh] w-[70vw] bg-black border border-gray-800 shadow font-[CascadiaMono] rounded text-sm;
}
.logout {
  @apply text-xs border border-stone-700 rounded bg-stone-800 flex justify-center items-center p-1.5 h-auto;
  @apply transition-colors duration-300 hover:text-red-600;
}
.console-scrollbar {
  @apply whitespace-pre overflow-scroll;
}
*::-webkit-scrollbar {
  @apply w-2 h-2;
}
*::-webkit-scrollbar-track {
  @apply w-0 h-0 bg-transparent;
}
*::-webkit-scrollbar-thumb {
  @apply border-none rounded-none bg-white;
}
*::-webkit-scrollbar-corner {
  @apply w-0 h-0;
}
</style>
