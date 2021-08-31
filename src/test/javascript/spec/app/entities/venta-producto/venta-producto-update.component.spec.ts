/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';

import * as config from '@/shared/config/config';
import VentaProductoUpdateComponent from '@/entities/venta-producto/venta-producto-update.vue';
import VentaProductoClass from '@/entities/venta-producto/venta-producto-update.component';
import VentaProductoService from '@/entities/venta-producto/venta-producto.service';

import ProductoService from '@/entities/producto/producto.service';

import VentaService from '@/entities/venta/venta.service';

const localVue = createLocalVue();

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
const router = new Router();
localVue.use(Router);
localVue.component('font-awesome-icon', {});
localVue.component('b-input-group', {});
localVue.component('b-input-group-prepend', {});
localVue.component('b-form-datepicker', {});
localVue.component('b-form-input', {});

describe('Component Tests', () => {
  describe('VentaProducto Management Update Component', () => {
    let wrapper: Wrapper<VentaProductoClass>;
    let comp: VentaProductoClass;
    let ventaProductoServiceStub: SinonStubbedInstance<VentaProductoService>;

    beforeEach(() => {
      ventaProductoServiceStub = sinon.createStubInstance<VentaProductoService>(VentaProductoService);

      wrapper = shallowMount<VentaProductoClass>(VentaProductoUpdateComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: {
          ventaProductoService: () => ventaProductoServiceStub,

          productoService: () => new ProductoService(),

          ventaService: () => new VentaService(),
        },
      });
      comp = wrapper.vm;
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const entity = { id: 123 };
        comp.ventaProducto = entity;
        ventaProductoServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(ventaProductoServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.ventaProducto = entity;
        ventaProductoServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(ventaProductoServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundVentaProducto = { id: 123 };
        ventaProductoServiceStub.find.resolves(foundVentaProducto);
        ventaProductoServiceStub.retrieve.resolves([foundVentaProducto]);

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
