/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import ProveedorDetailComponent from '@/entities/proveedor/proveedor-details.vue';
import ProveedorClass from '@/entities/proveedor/proveedor-details.component';
import ProveedorService from '@/entities/proveedor/proveedor.service';
import router from '@/router';

const localVue = createLocalVue();
localVue.use(VueRouter);

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('router-link', {});

describe('Component Tests', () => {
  describe('Proveedor Management Detail Component', () => {
    let wrapper: Wrapper<ProveedorClass>;
    let comp: ProveedorClass;
    let proveedorServiceStub: SinonStubbedInstance<ProveedorService>;

    beforeEach(() => {
      proveedorServiceStub = sinon.createStubInstance<ProveedorService>(ProveedorService);

      wrapper = shallowMount<ProveedorClass>(ProveedorDetailComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: { proveedorService: () => proveedorServiceStub },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundProveedor = { id: 123 };
        proveedorServiceStub.find.resolves(foundProveedor);

        // WHEN
        comp.retrieveProveedor(123);
        await comp.$nextTick();

        // THEN
        expect(comp.proveedor).toBe(foundProveedor);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundProveedor = { id: 123 };
        proveedorServiceStub.find.resolves(foundProveedor);

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
