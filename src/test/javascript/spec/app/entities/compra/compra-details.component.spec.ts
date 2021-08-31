/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import CompraDetailComponent from '@/entities/compra/compra-details.vue';
import CompraClass from '@/entities/compra/compra-details.component';
import CompraService from '@/entities/compra/compra.service';
import router from '@/router';

const localVue = createLocalVue();
localVue.use(VueRouter);

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('router-link', {});

describe('Component Tests', () => {
  describe('Compra Management Detail Component', () => {
    let wrapper: Wrapper<CompraClass>;
    let comp: CompraClass;
    let compraServiceStub: SinonStubbedInstance<CompraService>;

    beforeEach(() => {
      compraServiceStub = sinon.createStubInstance<CompraService>(CompraService);

      wrapper = shallowMount<CompraClass>(CompraDetailComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: { compraService: () => compraServiceStub },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundCompra = { id: 123 };
        compraServiceStub.find.resolves(foundCompra);

        // WHEN
        comp.retrieveCompra(123);
        await comp.$nextTick();

        // THEN
        expect(comp.compra).toBe(foundCompra);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundCompra = { id: 123 };
        compraServiceStub.find.resolves(foundCompra);

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
