(window.webpackJsonp=window.webpackJsonp||[]).push([[5,3,4],{262:function(t,e,n){var content=n(264);content.__esModule&&(content=content.default),"string"==typeof content&&(content=[[t.i,content,""]]),content.locals&&(t.exports=content.locals);(0,n(59).default)("32e4c9fc",content,!0,{sourceMap:!1})},263:function(t,e,n){"use strict";n(262)},264:function(t,e,n){var o=n(58)((function(i){return i[1]}));o.push([t.i,".console{display:flex;height:65vh;width:70vw;flex-direction:column;border-radius:0.25rem;border-width:1px;--tw-border-opacity:1;border-color:rgb(31 41 55 / var(--tw-border-opacity));--tw-bg-opacity:1;background-color:rgb(0 0 0 / var(--tw-bg-opacity));font-family:CascadiaMono;font-size:0.875rem;line-height:1.25rem;--tw-shadow:0 1px 3px 0 rgb(0 0 0 / 0.1), 0 1px 2px -1px rgb(0 0 0 / 0.1);--tw-shadow-colored:0 1px 3px 0 var(--tw-shadow-color), 0 1px 2px -1px var(--tw-shadow-color);box-shadow:0 0 #0000, 0 0 #0000, var(--tw-shadow);box-shadow:var(--tw-ring-offset-shadow, 0 0 #0000), var(--tw-ring-shadow, 0 0 #0000), var(--tw-shadow)}.logout{display:flex;height:auto;align-items:center;justify-content:center;border-radius:0.25rem;border-width:1px;--tw-border-opacity:1;border-color:rgb(68 64 60 / var(--tw-border-opacity));--tw-bg-opacity:1;background-color:rgb(41 37 36 / var(--tw-bg-opacity));padding:0.375rem;font-size:0.75rem;line-height:1rem;transition-property:color, background-color, border-color, fill, stroke, -webkit-text-decoration-color;transition-property:color, background-color, border-color, text-decoration-color, fill, stroke;transition-property:color, background-color, border-color, text-decoration-color, fill, stroke, -webkit-text-decoration-color;transition-timing-function:cubic-bezier(0.4, 0, 0.2, 1);transition-duration:300ms}.logout:hover{--tw-text-opacity:1;color:rgb(220 38 38 / var(--tw-text-opacity))}.console-scrollbar{overflow:scroll;white-space:pre}::-webkit-scrollbar{height:0.5rem;width:0.5rem}::-webkit-scrollbar-track{height:0px;width:0px;background-color:transparent}::-webkit-scrollbar-thumb{border-radius:0px;border-style:none;--tw-bg-opacity:1;background-color:rgb(255 255 255 / var(--tw-bg-opacity))}::-webkit-scrollbar-corner{height:0px;width:0px}",""]),o.locals={},t.exports=o},265:function(t,e,n){var content=n(272);content.__esModule&&(content=content.default),"string"==typeof content&&(content=[[t.i,content,""]]),content.locals&&(t.exports=content.locals);(0,n(59).default)("1b30ee58",content,!0,{sourceMap:!1})},269:function(t,e,n){"use strict";n.r(e);var o=n(2),r={name:"LoginComponent",data:function(){return{warningU:!1,warningP:!1,warningC:!1}},methods:{login:function(){var t=this;if(""===this.$refs.username.value&&(o.a.nextTick((function(){o.a.nextTick((function(){return t.$animation(t.$refs.username,"headShake")}))})),this.warningU=!0),""===this.$refs.password.value&&(o.a.nextTick((function(){o.a.nextTick((function(){return t.$animation(t.$refs.password,"headShake")}))})),this.warningP=!0),!this.warningU&&!this.warningP){var e=this.$refs.password.value;this.$refs.password.value="",this.$axios.$post("auth/login",{username:this.$refs.username.value,password:e}).then((function(){t.$parent.checkIfAuthenticated()})).catch((function(e){if(403!==e.response.status)throw o.a.nextTick((function(){o.a.nextTick((function(){return t.$animation(t.$refs.card,"headShake")}))})),t.warningC=!0,e;o.a.nextTick((function(){o.a.nextTick((function(){return t.$animation(t.$refs.username,"headShake")}))})),t.warningU=!0,o.a.nextTick((function(){o.a.nextTick((function(){return t.$animation(t.$refs.password,"headShake")}))})),t.warningP=!0}))}}}},c=n(45),component=Object(c.a)(r,(function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("div",{ref:"card",staticClass:"bg-gray-900 p-4 rounded",class:{"border-2 border-red-600":t.warningC}},[n("h1",{staticClass:"text-3xl text-center mb-4"},[t._v("Login")]),t._v(" "),n("form",{on:{submit:[t.login,function(t){t.preventDefault()}]}},[n("input",{ref:"username",staticClass:"p-0.5 bg-gray-800 border-2 border-gray-600 mb-2 rounded-sm outline-none",class:{"border-red-500":t.warningU,"transition-[border] duration-300":!t.warningU},attrs:{id:"username",type:"text",placeholder:"username"},on:{keydown:[function(e){t.warningU=!1},function(e){return!e.type.indexOf("key")&&t._k(e.keyCode,"enter",13,e.key,"Enter")?null:t.login.apply(null,arguments)}]}}),n("br"),t._v(" "),n("input",{ref:"password",staticClass:"p-0.5 bg-gray-800 border-2 border-gray-600 mb-2 rounded-sm outline-none",class:{"border-red-500":t.warningP,"transition-[border] duration-300":!t.warningP},attrs:{id:"password",type:"password",placeholder:"password"},on:{keydown:[function(e){t.warningP=!1},function(e){return!e.type.indexOf("key")&&t._k(e.keyCode,"enter",13,e.key,"Enter")?null:t.login.apply(null,arguments)}]}}),n("br"),t._v(" "),n("div",{staticClass:"flex justify-center"},[n("button",{ref:"submit",staticClass:"bg-gray-800 border-2 border-gray-600 rounded-sm p-1",attrs:{id:"submit"}},[t._v("\n        Login\n      ")])])])])}),[],!1,null,null,null);e.default=component.exports},270:function(t,e,n){"use strict";n.r(e);n(12),n(31),n(133),n(28),n(77),n(266);var o=n(267),r=n(2),c={name:"ConsoleComponent",components:{LogOutIcon:o.a},data:function(){return{connection:null,ansi:n(268),code:null,authenticated:!1,bottom:!0}},fetchOnServer:!1,fetch:function(){var t=this,e=this;this.$axios.$get("console/history").then((function(n){var o;n.forEach((function(t){return e.log(t)}));var r=window.location;o="https:"===r.protocol?"wss:":"ws:",o+="//"+r.host+"/.admin/api/console/live",e.connection=new WebSocket(o),e.connection.onmessage=function(t){null===e.code?(e.code=t.data,e.$axios.$get("auth/live/"+t.data).catch((function(t){throw e.$parent.checkIfAuthenticated(),t}))):!e.authenticated&&t.data.startsWith("authenticated with user ")?e.authenticated=!0:e.log(t.data)},t.connection.onclose=function(t){this.$parent.checkIfAuthenticated()}})).catch((function(e){throw t.$parent.checkIfAuthenticated(),e}))},beforeUnmount:function(){try{this.connection.close()}catch(t){}},methods:{submit:function(){var t=this,e=this.$refs.input.value;this.$refs.input.value="",this.$axios.$post("console/send",{command:e}).catch((function(e){if(403!==e.response.status)throw t.$parent.checkIfAuthenticated(),e;t.$animation(t.$refs.input,"shakeX")}))},logout:function(){var t=this;this.$axios.$get("auth/logout").then((function(){return t.$parent.checkIfAuthenticated()})).catch((function(e){throw t.$parent.checkIfAuthenticated(),e}))},log:function(line){var t=this;this.bottom&&r.a.nextTick((function(){t.$refs.content.scrollTop=t.$refs.content.scrollHeight,t.bottom=!0})),this.$refs.content.innerHTML+=this.ansi(line).replaceAll(/\x1B(?:[@-Z\\-_]|\[[0-?]*[ -/]*[@-~])/g,"")+"<br>"},scroll:function(t){this.bottom=t.target.offsetHeight+t.target.scrollTop>=t.target.scrollHeight}}},l=(n(263),n(45)),component=Object(l.a)(c,(function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("div",{staticClass:"bg-gray-900 p-4 shadow-lg shadow-gray-800 rounded"},[n("div",{staticClass:"flex items-start justify-end mb-4"},[t._m(0),t._v(" "),n("button",{staticClass:"logout",on:{click:t.logout}},[n("LogOutIcon",{staticClass:"mr-0.5",attrs:{size:"20"}}),t._v("\n      Logout\n    ")],1)]),t._v(" "),n("div",{staticClass:"console"},[n("div",{ref:"content",staticClass:"flex-grow py-1 px-2 w-full console-scrollbar",attrs:{id:"content"},on:{scroll:t.scroll}}),t._v(" "),n("div",{staticClass:"flex w-full py-1 px-2 border-t border-gray-500"},[n("label",{attrs:{for:"input"}},[t._v("$")]),t._v(" "),n("input",{ref:"input",staticClass:"bg-black text-white flex-grow ml-0.5 outline-none",attrs:{id:"input",type:"text",autocomplete:"false",spellcheck:"false",placeholder:"command"},on:{keydown:function(e){return!e.type.indexOf("key")&&t._k(e.keyCode,"enter",13,e.key,"Enter")?null:t.submit.apply(null,arguments)}}})])])])}),[function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("div",{staticClass:"flex-grow"},[n("h1",{staticClass:"text-4xl"},[t._v("s.Admin")]),t._v(" "),n("p",{staticClass:"text-s"},[t._v("\n        Version 1.0.0 - By\n        "),n("a",{staticClass:"text-cyan-700 hover:underline",attrs:{href:"https://scolasti.co/"}},[t._v("scolastico")]),t._v(".\n      ")])])}],!1,null,null,null);e.default=component.exports},271:function(t,e,n){"use strict";n(265)},272:function(t,e,n){var o=n(58)((function(i){return i[1]}));o.push([t.i,".main-transition-speed{--animate-duration:400ms}",""]),o.locals={},t.exports=o},275:function(t,e,n){"use strict";n.r(e);var o=n(9),r=(n(60),{name:"IndexPage",data:function(){return{login:!1}},fetchOnServer:!1,fetch:function(){var t=this;return Object(o.a)(regeneratorRuntime.mark((function e(){return regeneratorRuntime.wrap((function(e){for(;;)switch(e.prev=e.next){case 0:return e.next=2,t.checkIfAuthenticated();case 2:case"end":return e.stop()}}),e)})))()},methods:{checkIfAuthenticated:function(){var t=this;return Object(o.a)(regeneratorRuntime.mark((function e(){return regeneratorRuntime.wrap((function(e){for(;;)switch(e.prev=e.next){case 0:return e.next=2,t.$axios.$get("status").then((function(){t.login=!0})).catch((function(){t.login=!1}));case 2:case"end":return e.stop()}}),e)})))()}}}),c=(n(271),n(45)),component=Object(c.a)(r,(function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("div",{staticClass:"flex justify-center items-center bg-gradient-to-br from-gray-700 to-gray-900 text-white",attrs:{id:"body-wrapper"}},[n("transition",{attrs:{mode:"out-in","leave-active-class":"animate__animated animate__fadeOut main-transition-speed","enter-active-class":"animate__animated animate__fadeIn main-transition-speed"}},[t.login?t._e():n("login-component"),t._v(" "),t.login?n("console-component"):t._e()],1),t._v(" "),n("div",{staticClass:"absolute bottom-0 left-0 h-6 w-6 bg-black",on:{click:function(e){t.login=!t.login}}})],1)}),[],!1,null,null,null);e.default=component.exports;installComponents(component,{LoginComponent:n(269).default,ConsoleComponent:n(270).default})}}]);