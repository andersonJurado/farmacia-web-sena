/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';

import * as config from '@/shared/config/config';
import PresentacionUpdateComponent from '@/entities/presentacion/presentacion-update.vue';
import PresentacionClass from '@/entities/presentacion/presentacion-update.component';
import PresentacionService from '@/entities/presentacion/presentacion.service';

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
  describe('Presentacion Management Update Component', () => {
    let wrapper: Wrapper<PresentacionClass>;
    let comp: PresentacionClass;
    let presentacionServiceStub: SinonStubbedInstance<PresentacionService>;

    beforeEach(() => {
      presentacionServiceStub = sinon.createStubInstance<PresentacionService>(PresentacionService);

      wrapper = shallowMount<PresentacionClass>(PresentacionUpdateComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: {
          presentacionService: () => presentacionServiceStub,

          productoService: () => new ProductoService(),
        },
      });
      comp = wrapper.vm;
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const entity = { id: 123 };
        comp.presentacion = entity;
        presentacionServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(presentacionServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.presentacion = entity;
        presentacionServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(presentacionServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundPresentacion = { id: 123 };
        presentacionServiceStub.find.resolves(foundPresentacion);
        presentacionServiceStub.retrieve.resolves([foundPresentacion]);

        // WHEN
        comp.beforeRouteEnter({ params: { presentacionId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.presentacion).toBe(foundPresentacion);
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
