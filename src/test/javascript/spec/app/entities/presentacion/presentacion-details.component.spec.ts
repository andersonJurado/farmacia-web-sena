/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import PresentacionDetailComponent from '@/entities/presentacion/presentacion-details.vue';
import PresentacionClass from '@/entities/presentacion/presentacion-details.component';
import PresentacionService from '@/entities/presentacion/presentacion.service';
import router from '@/router';

const localVue = createLocalVue();
localVue.use(VueRouter);

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('router-link', {});

describe('Component Tests', () => {
  describe('Presentacion Management Detail Component', () => {
    let wrapper: Wrapper<PresentacionClass>;
    let comp: PresentacionClass;
    let presentacionServiceStub: SinonStubbedInstance<PresentacionService>;

    beforeEach(() => {
      presentacionServiceStub = sinon.createStubInstance<PresentacionService>(PresentacionService);

      wrapper = shallowMount<PresentacionClass>(PresentacionDetailComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: { presentacionService: () => presentacionServiceStub },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundPresentacion = { id: 123 };
        presentacionServiceStub.find.resolves(foundPresentacion);

        // WHEN
        comp.retrievePresentacion(123);
        await comp.$nextTick();

        // THEN
        expect(comp.presentacion).toBe(foundPresentacion);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundPresentacion = { id: 123 };
        presentacionServiceStub.find.resolves(foundPresentacion);

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
