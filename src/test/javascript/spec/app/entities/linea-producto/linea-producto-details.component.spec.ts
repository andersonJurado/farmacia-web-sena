/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import LineaProductoDetailComponent from '@/entities/linea-producto/linea-producto-details.vue';
import LineaProductoClass from '@/entities/linea-producto/linea-producto-details.component';
import LineaProductoService from '@/entities/linea-producto/linea-producto.service';
import router from '@/router';

const localVue = createLocalVue();
localVue.use(VueRouter);

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('router-link', {});

describe('Component Tests', () => {
  describe('LineaProducto Management Detail Component', () => {
    let wrapper: Wrapper<LineaProductoClass>;
    let comp: LineaProductoClass;
    let lineaProductoServiceStub: SinonStubbedInstance<LineaProductoService>;

    beforeEach(() => {
      lineaProductoServiceStub = sinon.createStubInstance<LineaProductoService>(LineaProductoService);

      wrapper = shallowMount<LineaProductoClass>(LineaProductoDetailComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: { lineaProductoService: () => lineaProductoServiceStub },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundLineaProducto = { id: 123 };
        lineaProductoServiceStub.find.resolves(foundLineaProducto);

        // WHEN
        comp.retrieveLineaProducto(123);
        await comp.$nextTick();

        // THEN
        expect(comp.lineaProducto).toBe(foundLineaProducto);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundLineaProducto = { id: 123 };
        lineaProductoServiceStub.find.resolves(foundLineaProducto);

        // WHEN
        comp.beforeRouteEnter({ params: { lineaProductoId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.lineaProducto).toBe(foundLineaProducto);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        comp.previousState();
        await comp.$nextTick();

        expect(comp.$router.currentRoute.fullPath).toContain('/');
      });
    });
  });
});
