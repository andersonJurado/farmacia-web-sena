/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';

import * as config from '@/shared/config/config';
import ProveedorUpdateComponent from '@/entities/proveedor/proveedor-update.vue';
import ProveedorClass from '@/entities/proveedor/proveedor-update.component';
import ProveedorService from '@/entities/proveedor/proveedor.service';

import MunicipioService from '@/entities/municipio/municipio.service';

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
  describe('Proveedor Management Update Component', () => {
    let wrapper: Wrapper<ProveedorClass>;
    let comp: ProveedorClass;
    let proveedorServiceStub: SinonStubbedInstance<ProveedorService>;

    beforeEach(() => {
      proveedorServiceStub = sinon.createStubInstance<ProveedorService>(ProveedorService);

      wrapper = shallowMount<ProveedorClass>(ProveedorUpdateComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: {
          proveedorService: () => proveedorServiceStub,

          municipioService: () => new MunicipioService(),

          compraService: () => new CompraService(),
        },
      });
      comp = wrapper.vm;
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const entity = { id: 123 };
        comp.proveedor = entity;
        proveedorServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(proveedorServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.proveedor = entity;
        proveedorServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(proveedorServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundProveedor = { id: 123 };
        proveedorServiceStub.find.resolves(foundProveedor);
        proveedorServiceStub.retrieve.resolves([foundProveedor]);

        // WHEN
        comp.beforeRouteEnter({ params: { proveedorId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.proveedor).toBe(foundProveedor);
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
