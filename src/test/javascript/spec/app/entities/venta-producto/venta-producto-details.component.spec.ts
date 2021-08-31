/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import VentaProductoDetailComponent from '@/entities/venta-producto/venta-producto-details.vue';
import VentaProductoClass from '@/entities/venta-producto/venta-producto-details.component';
import VentaProductoService from '@/entities/venta-producto/venta-producto.service';
import router from '@/router';

const localVue = createLocalVue();
localVue.use(VueRouter);

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('router-link', {});

describe('Component Tests', () => {
  describe('VentaProducto Management Detail Component', () => {
    let wrapper: Wrapper<VentaProductoClass>;
    let comp: VentaProductoClass;
    let ventaProductoServiceStub: SinonStubbedInstance<VentaProductoService>;

    beforeEach(() => {
      ventaProductoServiceStub = sinon.createStubInstance<VentaProductoService>(VentaProductoService);

      wrapper = shallowMount<VentaProductoClass>(VentaProductoDetailComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: { ventaProductoService: () => ventaProductoServiceStub },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundVentaProducto = { id: 123 };
        ventaProductoServiceStub.find.resolves(foundVentaProducto);

        // WHEN
        comp.retrieveVentaProducto(123);
        await comp.$nextTick();

        // THEN
        expect(comp.ventaProducto).toBe(foundVentaProducto);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundVentaProducto = { id: 123 };
        ventaProductoServiceStub.find.resolves(foundVentaProducto);

        // WHEN
        comp.beforeRouteEnter({ params: { ventaProductoId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.ventaProducto).toBe(foundVentaProducto);
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
