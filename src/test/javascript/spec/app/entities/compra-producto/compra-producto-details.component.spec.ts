/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import CompraProductoDetailComponent from '@/entities/compra-producto/compra-producto-details.vue';
import CompraProductoClass from '@/entities/compra-producto/compra-producto-details.component';
import CompraProductoService from '@/entities/compra-producto/compra-producto.service';
import router from '@/router';

const localVue = createLocalVue();
localVue.use(VueRouter);

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('router-link', {});

describe('Component Tests', () => {
  describe('CompraProducto Management Detail Component', () => {
    let wrapper: Wrapper<CompraProductoClass>;
    let comp: CompraProductoClass;
    let compraProductoServiceStub: SinonStubbedInstance<CompraProductoService>;

    beforeEach(() => {
      compraProductoServiceStub = sinon.createStubInstance<CompraProductoService>(CompraProductoService);

      wrapper = shallowMount<CompraProductoClass>(CompraProductoDetailComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: { compraProductoService: () => compraProductoServiceStub },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundCompraProducto = { id: 123 };
        compraProductoServiceStub.find.resolves(foundCompraProducto);

        // WHEN
        comp.retrieveCompraProducto(123);
        await comp.$nextTick();

        // THEN
        expect(comp.compraProducto).toBe(foundCompraProducto);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundCompraProducto = { id: 123 };
        compraProductoServiceStub.find.resolves(foundCompraProducto);

        // WHEN
        comp.beforeRouteEnter({ params: { compraProductoId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.compraProducto).toBe(foundCompraProducto);
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
