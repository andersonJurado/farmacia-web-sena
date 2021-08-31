/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';

import * as config from '@/shared/config/config';
import CompraProductoComponent from '@/entities/compra-producto/compra-producto.vue';
import CompraProductoClass from '@/entities/compra-producto/compra-producto.component';
import CompraProductoService from '@/entities/compra-producto/compra-producto.service';

const localVue = createLocalVue();

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('b-badge', {});
localVue.component('jhi-sort-indicator', {});
localVue.directive('b-modal', {});
localVue.component('b-button', {});
localVue.component('router-link', {});

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  describe('CompraProducto Management Component', () => {
    let wrapper: Wrapper<CompraProductoClass>;
    let comp: CompraProductoClass;
    let compraProductoServiceStub: SinonStubbedInstance<CompraProductoService>;

    beforeEach(() => {
      compraProductoServiceStub = sinon.createStubInstance<CompraProductoService>(CompraProductoService);
      compraProductoServiceStub.retrieve.resolves({ headers: {} });

      wrapper = shallowMount<CompraProductoClass>(CompraProductoComponent, {
        store,
        i18n,
        localVue,
        stubs: { jhiItemCount: true, bPagination: true, bModal: bModalStub as any },
        provide: {
          compraProductoService: () => compraProductoServiceStub,
        },
      });
      comp = wrapper.vm;
    });

    it('Should call load all on init', async () => {
      // GIVEN
      compraProductoServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

      // WHEN
      comp.retrieveAllCompraProductos();
      await comp.$nextTick();

      // THEN
      expect(compraProductoServiceStub.retrieve.called).toBeTruthy();
      expect(comp.compraProductos[0]).toEqual(expect.objectContaining({ id: 123 }));
    });

    it('should load a page', async () => {
      // GIVEN
      compraProductoServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });
      comp.previousPage = 1;

      // WHEN
      comp.loadPage(2);
      await comp.$nextTick();

      // THEN
      expect(compraProductoServiceStub.retrieve.called).toBeTruthy();
      expect(comp.compraProductos[0]).toEqual(expect.objectContaining({ id: 123 }));
    });

    it('should not load a page if the page is the same as the previous page', () => {
      // GIVEN
      compraProductoServiceStub.retrieve.reset();
      comp.previousPage = 1;

      // WHEN
      comp.loadPage(1);

      // THEN
      expect(compraProductoServiceStub.retrieve.called).toBeFalsy();
    });

    it('should re-initialize the page', async () => {
      // GIVEN
      compraProductoServiceStub.retrieve.reset();
      compraProductoServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

      // WHEN
      comp.loadPage(2);
      await comp.$nextTick();
      comp.clear();
      await comp.$nextTick();

      // THEN
      expect(compraProductoServiceStub.retrieve.callCount).toEqual(3);
      expect(comp.page).toEqual(1);
      expect(comp.compraProductos[0]).toEqual(expect.objectContaining({ id: 123 }));
    });

    it('should calculate the sort attribute for an id', () => {
      // WHEN
      const result = comp.sort();

      // THEN
      expect(result).toEqual(['id,asc']);
    });

    it('should calculate the sort attribute for a non-id attribute', () => {
      // GIVEN
      comp.propOrder = 'name';

      // WHEN
      const result = comp.sort();

      // THEN
      expect(result).toEqual(['name,asc', 'id']);
    });
    it('Should call delete service on confirmDelete', async () => {
      // GIVEN
      compraProductoServiceStub.delete.resolves({});

      // WHEN
      comp.prepareRemove({ id: 123 });
      comp.removeCompraProducto();
      await comp.$nextTick();

      // THEN
      expect(compraProductoServiceStub.delete.called).toBeTruthy();
      expect(compraProductoServiceStub.retrieve.callCount).toEqual(1);
    });
  });
});
