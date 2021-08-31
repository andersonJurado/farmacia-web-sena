/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';

import * as config from '@/shared/config/config';
import CompraProductoUpdateComponent from '@/entities/compra-producto/compra-producto-update.vue';
import CompraProductoClass from '@/entities/compra-producto/compra-producto-update.component';
import CompraProductoService from '@/entities/compra-producto/compra-producto.service';

import ProductoService from '@/entities/producto/producto.service';

import CompraService from '@/entities/compra/compra.service';

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
  describe('CompraProducto Management Update Component', () => {
    let wrapper: Wrapper<CompraProductoClass>;
    let comp: CompraProductoClass;
    let compraProductoServiceStub: SinonStubbedInstance<CompraProductoService>;

    beforeEach(() => {
      compraProductoServiceStub = sinon.createStubInstance<CompraProductoService>(CompraProductoService);

      wrapper = shallowMount<CompraProductoClass>(CompraProductoUpdateComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: {
          compraProductoService: () => compraProductoServiceStub,

          productoService: () => new ProductoService(),

          compraService: () => new CompraService(),
        },
      });
      comp = wrapper.vm;
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const entity = { id: 123 };
        comp.compraProducto = entity;
        compraProductoServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(compraProductoServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.compraProducto = entity;
        compraProductoServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(compraProductoServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundCompraProducto = { id: 123 };
        compraProductoServiceStub.find.resolves(foundCompraProducto);
        compraProductoServiceStub.retrieve.resolves([foundCompraProducto]);

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
