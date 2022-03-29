(window.webpackJsonp=window.webpackJsonp||[]).push([[3],{262:function(t,o,e){var content=e(264);content.__esModule&&(content=content.default),"string"==typeof content&&(content=[[t.i,content,""]]),content.locals&&(t.exports=content.locals);(0,e(59).default)("32e4c9fc",content,!0,{sourceMap:!1})},263:function(t,o,e){"use strict";e(262)},264:function(t,o,e){var r=e(58)((function(i){return i[1]}));r.push([t.i,".console{display:flex;height:65vh;width:70vw;flex-direction:column;border-radius:0.25rem;border-width:1px;--tw-border-opacity:1;border-color:rgb(31 41 55 / var(--tw-border-opacity));--tw-bg-opacity:1;background-color:rgb(0 0 0 / var(--tw-bg-opacity));font-family:CascadiaMono;font-size:0.875rem;line-height:1.25rem;--tw-shadow:0 1px 3px 0 rgb(0 0 0 / 0.1), 0 1px 2px -1px rgb(0 0 0 / 0.1);--tw-shadow-colored:0 1px 3px 0 var(--tw-shadow-color), 0 1px 2px -1px var(--tw-shadow-color);box-shadow:0 0 #0000, 0 0 #0000, var(--tw-shadow);box-shadow:var(--tw-ring-offset-shadow, 0 0 #0000), var(--tw-ring-shadow, 0 0 #0000), var(--tw-shadow)}.logout{display:flex;height:auto;align-items:center;justify-content:center;border-radius:0.25rem;border-width:1px;--tw-border-opacity:1;border-color:rgb(68 64 60 / var(--tw-border-opacity));--tw-bg-opacity:1;background-color:rgb(41 37 36 / var(--tw-bg-opacity));padding:0.375rem;font-size:0.75rem;line-height:1rem;transition-property:color, background-color, border-color, fill, stroke, -webkit-text-decoration-color;transition-property:color, background-color, border-color, text-decoration-color, fill, stroke;transition-property:color, background-color, border-color, text-decoration-color, fill, stroke, -webkit-text-decoration-color;transition-timing-function:cubic-bezier(0.4, 0, 0.2, 1);transition-duration:300ms}.logout:hover{--tw-text-opacity:1;color:rgb(220 38 38 / var(--tw-text-opacity))}.console-scrollbar{overflow:scroll;white-space:pre}::-webkit-scrollbar{height:0.5rem;width:0.5rem}::-webkit-scrollbar-track{height:0px;width:0px;background-color:transparent}::-webkit-scrollbar-thumb{border-radius:0px;border-style:none;--tw-bg-opacity:1;background-color:rgb(255 255 255 / var(--tw-bg-opacity))}::-webkit-scrollbar-corner{height:0px;width:0px}",""]),r.locals={},t.exports=r},270:function(t,o,e){"use strict";e.r(o);e(12),e(31),e(60),e(133),e(28),e(77),e(266);var r=e(267),n=e(2),c={name:"ConsoleComponent",components:{LogOutIcon:r.a},data:function(){return{connection:null,ansi:e(268),code:null,authenticated:!1,bottom:!0,scheduler:null}},fetchOnServer:!1,fetch:function(){var t=this,o=this;this.$axios.$get("console/history").then((function(e){var r;e.forEach((function(t){return o.log(t)})),t.scheduler=window.setInterval((function(){o.$parent.checkIfAuthenticated()}),1e4);var n=window.location;r="https:"===n.protocol?"wss:":"ws:",r+="//"+n.host+"/.admin/api/console/live",o.connection=new WebSocket(r),o.connection.onmessage=function(t){null===o.code?(o.code=t.data,o.$axios.$get("auth/live/"+t.data).catch((function(t){throw o.$parent.checkIfAuthenticated(),t}))):!o.authenticated&&t.data.startsWith("authenticated with user ")?o.authenticated=!0:o.log(t.data)},t.connection.onclose=function(t){o.$parent.checkIfAuthenticated()}})).catch((function(o){throw t.$parent.checkIfAuthenticated(),o}))},beforeDestroy:function(){try{this.connection.close()}catch(t){}null!=this.scheduler&&(clearInterval(this.scheduler),this.scheduler=null)},methods:{submit:function(){var t=this,o=this.$refs.input.value;this.$refs.input.value="",this.$axios.$post("console/send",{command:o}).catch((function(o){if(403!==o.response.status)throw t.$parent.checkIfAuthenticated(),o;t.$animation(t.$refs.input,"shakeX")}))},logout:function(){var t=this;this.$axios.$get("auth/logout").then((function(){return t.$parent.checkIfAuthenticated()})).catch((function(o){throw t.$parent.checkIfAuthenticated(),o}))},log:function(line){var t=this;this.bottom&&n.a.nextTick((function(){t.$refs.content.scrollTop=t.$refs.content.scrollHeight,t.bottom=!0})),this.$refs.content.innerHTML+=this.ansi(line).replaceAll(/\x1B(?:[@-Z\\-_]|\[[0-?]*[ -/]*[@-~])/g,"")+"<br>"},scroll:function(t){this.bottom=t.target.offsetHeight+t.target.scrollTop>=t.target.scrollHeight}}},l=(e(263),e(45)),component=Object(l.a)(c,(function(){var t=this,o=t.$createElement,e=t._self._c||o;return e("div",{staticClass:"bg-gray-900 p-4 shadow-lg shadow-gray-800 rounded"},[e("div",{staticClass:"flex items-start justify-end mb-4"},[t._m(0),t._v(" "),e("button",{staticClass:"logout",on:{click:t.logout}},[e("LogOutIcon",{staticClass:"mr-0.5",attrs:{size:"20"}}),t._v("\n      Logout\n    ")],1)]),t._v(" "),e("div",{staticClass:"console"},[e("div",{ref:"content",staticClass:"flex-grow py-1 px-2 w-full console-scrollbar",attrs:{id:"content"},on:{scroll:t.scroll}}),t._v(" "),e("div",{staticClass:"flex w-full py-1 px-2 border-t border-gray-500"},[e("label",{attrs:{for:"input"}},[t._v("$")]),t._v(" "),e("input",{ref:"input",staticClass:"bg-black text-white flex-grow ml-0.5 outline-none",attrs:{id:"input",type:"text",autocomplete:"false",spellcheck:"false",placeholder:"command"},on:{keydown:function(o){return!o.type.indexOf("key")&&t._k(o.keyCode,"enter",13,o.key,"Enter")?null:t.submit.apply(null,arguments)}}})])])])}),[function(){var t=this,o=t.$createElement,e=t._self._c||o;return e("div",{staticClass:"flex-grow"},[e("h1",{staticClass:"text-4xl"},[t._v("s.Admin")]),t._v(" "),e("p",{staticClass:"text-s"},[t._v("\n        Version 1.0.0 - By\n        "),e("a",{staticClass:"text-cyan-700 hover:underline",attrs:{href:"https://scolasti.co/"}},[t._v("scolastico")]),t._v(".\n      ")])])}],!1,null,null,null);o.default=component.exports}}]);