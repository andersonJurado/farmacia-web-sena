/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';

import * as config from '@/shared/config/config';
import LineaProductoComponent from '@/entities/linea-producto/linea-producto.vue';
import LineaProductoClass from '@/entities/linea-producto/linea-producto.component';
import LineaProductoService from '@/entities/linea-producto/linea-producto.service';

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
  describe('LineaProducto Management Component', () => {
    let wrapper: Wrapper<LineaProductoClass>;
    let comp: LineaProductoClass;
    let lineaProductoServiceStub: SinonStubbedInstance<LineaProductoService>;

    beforeEach(() => {
      lineaProductoServiceStub = sinon.createStubInstance<LineaProductoService>(LineaProductoService);
      lineaProductoServiceStub.retrieve.resolves({ headers: {} });

      wrapper = shallowMount<LineaProductoClass>(LineaProductoComponent, {
        store,
        i18n,
        localVue,
        stubs: { jhiItemCount: true, bPagination: true, bModal: bModalStub as any },
        provide: {
          lineaProductoService: () => lineaProductoServiceStub,
        },
      });
      comp = wrapper.vm;
    });

    it('Should call load all on init', async () => {
      // GIVEN
      lineaProductoServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

      // WHEN
      comp.retrieveAllLineaProductos();
      await comp.$nextTick();

      // THEN
      expect(lineaProductoServiceStub.retrieve.called).toBeTruthy();
      expect(comp.lineaProductos[0]).toEqual(expect.objectContaining({ id: 123 }));
    });

    it('should load a page', async () => {
      // GIVEN
      lineaProductoServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });
      comp.previousPage = 1;

      // WHEN
      comp.loadPage(2);
      await comp.$nextTick();

      // THEN
      expect(lineaProductoServiceStub.retrieve.called).toBeTruthy();
      expect(comp.lineaProductos[0]).toEqual(expect.objectContaining({ id: 123 }));
    });

    it('should not load a page if the page is the same as the previous page', () => {
      // GIVEN
      lineaProductoServiceStub.retrieve.reset();
      comp.previousPage = 1;

      // WHEN
      comp.loadPage(1);

      // THEN
      expect(lineaProductoServiceStub.retrieve.called).toBeFalsy();
    });

    it('should re-initialize the page', async () => {
      // GIVEN
      lineaProductoServiceStub.retrieve.reset();
      lineaProductoServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

      // WHEN
      comp.loadPage(2);
      await comp.$nextTick();
      comp.clear();
      await comp.$nextTick();

      // THEN
      expect(lineaProductoServiceStub.retrieve.callCount).toEqual(3);
      expect(comp.page).toEqual(1);
      expect(comp.lineaProductos[0]).toEqual(expect.objectContaining({ id: 123 }));
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
      lineaProductoServiceStub.delete.resolves({});

      // WHEN
      comp.prepareRemove({ id: 123 });
      comp.removeLineaProducto();
      await comp.$nextTick();

      // THEN
      expect(lineaProductoServiceStub.delete.called).toBeTruthy();
      expect(lineaProductoServiceStub.retrieve.callCount).toEqual(1);
    });
  });
});
