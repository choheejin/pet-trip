import { createApp } from "vue";
import { createPinia } from "pinia";
import { createVuetify } from "vuetify";

import "vuetify/styles";

import App from "./App.vue";
import router from "./router";
import * as components from "vuetify/components";
import * as directives from "vuetify/directives";
import InfiniteScroll from 'vue-infinite-scroll';

import "bootstrap/dist/css/bootstrap.min.css"; // Bootstrap CSS
import "bootstrap/dist/js/bootstrap.bundle.min.js"; // Bootstrap JS (반응형 기능 포함)

const app = createApp(App);

const vuetify = createVuetify({
  components,
  directives,
});
app.use(createPinia());
app.use(router);
app.use(vuetify);
app.use(InfiniteScroll);

app.mount("#app");
