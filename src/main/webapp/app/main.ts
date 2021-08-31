// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.common with an alias.
import Vue from 'vue';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import App from './app.vue';
import Vue2Filters from 'vue2-filters';
import { ToastPlugin } from 'bootstrap-vue';
import router from './router';
import * as config from './shared/config/config';
import * as bootstrapVueConfig from './shared/config/config-bootstrap-vue';
import JhiItemCountComponent from './shared/jhi-item-count.vue';
import JhiSortIndicatorComponent from './shared/sort/jhi-sort-indicator.vue';
import InfiniteLoading from 'vue-infinite-loading';
import HealthService from './admin/health/health.service';
import MetricsService from './admin/metrics/metrics.service';
import LogsService from './admin/logs/logs.service';
import ConfigurationService from '@/admin/configuration/configuration.service';
import ActivateService from './account/activate/activate.service';
import RegisterService from './account/register/register.service';
import UserManagementService from '@/admin/user-management/user-management.service';
import LoginService from './account/login.service';
import AccountService from './account/account.service';

import '../content/scss/vendor.scss';
import TranslationService from '@/locale/translation.service';

import UserOAuth2Service from '@/entities/user/user.oauth2.service';
/* tslint:disable */

import ClienteService from '@/entities/cliente/cliente.service';
import CompraService from '@/entities/compra/compra.service';
import CompraProductoService from '@/entities/compra-producto/compra-producto.service';
import DepartamentoService from '@/entities/departamento/departamento.service';
import LaboratorioService from '@/entities/laboratorio/laboratorio.service';
import LineaProductoService from '@/entities/linea-producto/linea-producto.service';
import MunicipioService from '@/entities/municipio/municipio.service';
import PresentacionService from '@/entities/presentacion/presentacion.service';
import ProductoService from '@/entities/producto/producto.service';
import ProveedorService from '@/entities/proveedor/proveedor.service';
import GeneroService from '@/entities/genero/genero.service';
import VentaService from '@/entities/venta/venta.service';
import VentaProductoService from '@/entities/venta-producto/venta-producto.service';
// jhipster-needle-add-entity-service-to-main-import - JHipster will import entities services here

/* tslint:enable */
Vue.config.productionTip = false;
config.initVueApp(Vue);
config.initFortAwesome(Vue);
bootstrapVueConfig.initBootstrapVue(Vue);
Vue.use(Vue2Filters);
Vue.use(ToastPlugin);
Vue.component('font-awesome-icon', FontAwesomeIcon);
Vue.component('jhi-item-count', JhiItemCountComponent);
Vue.component('jhi-sort-indicator', JhiSortIndicatorComponent);
Vue.component('infinite-loading', InfiniteLoading);
const i18n = config.initI18N(Vue);
const store = config.initVueXStore(Vue);

const translationService = new TranslationService(store, i18n);
const loginService = new LoginService();
const accountService = new AccountService(store, translationService, router);

router.beforeEach((to, from, next) => {
  if (!to.matched.length) {
    next('/not-found');
  }

  if (to.meta && to.meta.authorities && to.meta.authorities.length > 0) {
    accountService.hasAnyAuthorityAndCheckAuth(to.meta.authorities).then(value => {
      if (!value) {
        sessionStorage.setItem('requested-url', to.fullPath);
        next('/forbidden');
      } else {
        next();
      }
    });
  } else {
    // no authorities, so just proceed
    next();
  }
});

/* tslint:disable */
new Vue({
  el: '#app',
  components: { App },
  template: '<App/>',
  router,
  provide: {
    loginService: () => loginService,
    activateService: () => new ActivateService(),
    registerService: () => new RegisterService(),
    userService: () => new UserManagementService(),
    healthService: () => new HealthService(),
    configurationService: () => new ConfigurationService(),
    logsService: () => new LogsService(),
    metricsService: () => new MetricsService(),

    userOAuth2Service: () => new UserOAuth2Service(),
    translationService: () => translationService,
    clienteService: () => new ClienteService(),
    compraService: () => new CompraService(),
    compraProductoService: () => new CompraProductoService(),
    departamentoService: () => new DepartamentoService(),
    laboratorioService: () => new LaboratorioService(),
    lineaProductoService: () => new LineaProductoService(),
    municipioService: () => new MunicipioService(),
    presentacionService: () => new PresentacionService(),
    productoService: () => new ProductoService(),
    proveedorService: () => new ProveedorService(),
    generoService: () => new GeneroService(),
    ventaService: () => new VentaService(),
    ventaProductoService: () => new VentaProductoService(),
    // jhipster-needle-add-entity-service-to-main - JHipster will import entities services here
    accountService: () => accountService,
  },
  i18n,
  store,
});
