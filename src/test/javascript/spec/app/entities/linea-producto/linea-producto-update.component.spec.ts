/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';

import * as config from '@/shared/config/config';
import LineaProductoUpdateComponent from '@/entities/linea-producto/linea-producto-update.vue';
import LineaProductoClass from '@/entities/linea-producto/linea-producto-update.component';
import LineaProductoService from '@/entities/linea-producto/linea-producto.service';

import ProductoService from '@/entities/producto/producto.service';

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
  describe('LineaProducto Management Update Component', () => {
    let wrapper: Wrapper<LineaProductoClass>;
    let comp: LineaProductoClass;
    let lineaProductoServiceStub: SinonStubbedInstance<LineaProductoService>;

    beforeEach(() => {
      lineaProductoServiceStub = sinon.createStubInstance<LineaProductoService>(LineaProductoService);

      wrapper = shallowMount<LineaProductoClass>(LineaProductoUpdateComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: {
          lineaProductoService: () => lineaProductoServiceStub,

          productoService: () => new ProductoService(),
        },
      });
      comp = wrapper.vm;
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const entity = { id: 123 };
        comp.lineaProducto = entity;
        lineaProductoServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(lineaProductoServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.lineaProducto = entity;
        lineaProductoServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(lineaProductoServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundLineaProducto = { id: 123 };
        lineaProductoServiceStub.find.resolves(foundLineaProducto);
        lineaProductoServiceStub.retrieve.resolves([foundLineaProducto]);

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
