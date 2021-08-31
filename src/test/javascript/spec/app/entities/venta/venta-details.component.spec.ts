/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import VentaDetailComponent from '@/entities/venta/venta-details.vue';
import VentaClass from '@/entities/venta/venta-details.component';
import VentaService from '@/entities/venta/venta.service';
import router from '@/router';

const localVue = createLocalVue();
localVue.use(VueRouter);

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('router-link', {});

describe('Component Tests', () => {
  describe('Venta Management Detail Component', () => {
    let wrapper: Wrapper<VentaClass>;
    let comp: VentaClass;
    let ventaServiceStub: SinonStubbedInstance<VentaService>;

    beforeEach(() => {
      ventaServiceStub = sinon.createStubInstance<VentaService>(VentaService);

      wrapper = shallowMount<VentaClass>(VentaDetailComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: { ventaService: () => ventaServiceStub },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundVenta = { id: 123 };
        ventaServiceStub.find.resolves(foundVenta);

        // WHEN
        comp.retrieveVenta(123);
        await comp.$nextTick();

        // THEN
        expect(comp.venta).toBe(foundVenta);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundVenta = { id: 123 };
        ventaServiceStub.find.resolves(foundVenta);

        // WHEN
        comp.beforeRouteEnter({ params: { ventaId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.venta).toBe(foundVenta);
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
