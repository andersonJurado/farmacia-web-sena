/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';

import dayjs from 'dayjs';
import { DATE_TIME_LONG_FORMAT } from '@/shared/date/filters';

import * as config from '@/shared/config/config';
import CompraUpdateComponent from '@/entities/compra/compra-update.vue';
import CompraClass from '@/entities/compra/compra-update.component';
import CompraService from '@/entities/compra/compra.service';

import CompraProductoService from '@/entities/compra-producto/compra-producto.service';

import ProveedorService from '@/entities/proveedor/proveedor.service';

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
  describe('Compra Management Update Component', () => {
    let wrapper: Wrapper<CompraClass>;
    let comp: CompraClass;
    let compraServiceStub: SinonStubbedInstance<CompraService>;

    beforeEach(() => {
      compraServiceStub = sinon.createStubInstance<CompraService>(CompraService);

      wrapper = shallowMount<CompraClass>(CompraUpdateComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: {
          compraService: () => compraServiceStub,

          compraProductoService: () => new CompraProductoService(),

          proveedorService: () => new ProveedorService(),
        },
      });
      comp = wrapper.vm;
    });

    describe('load', () => {
      it('Should convert date from string', () => {
        // GIVEN
        const date = new Date('2019-10-15T11:42:02Z');

        // WHEN
        const convertedDate = comp.convertDateTimeFromServer(date);

        // THEN
        expect(convertedDate).toEqual(dayjs(date).format(DATE_TIME_LONG_FORMAT));
      });

      it('Should not convert date if date is not present', () => {
        expect(comp.convertDateTimeFromServer(null)).toBeNull();
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const entity = { id: 123 };
        comp.compra = entity;
        compraServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(compraServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.compra = entity;
        compraServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(compraServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundCompra = { id: 123 };
        compraServiceStub.find.resolves(foundCompra);
        compraServiceStub.retrieve.resolves([foundCompra]);

        // WHEN
        comp.beforeRouteEnter({ params: { compraId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.compra).toBe(foundCompra);
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
