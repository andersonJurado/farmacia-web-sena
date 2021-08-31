/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import LaboratorioDetailComponent from '@/entities/laboratorio/laboratorio-details.vue';
import LaboratorioClass from '@/entities/laboratorio/laboratorio-details.component';
import LaboratorioService from '@/entities/laboratorio/laboratorio.service';
import router from '@/router';

const localVue = createLocalVue();
localVue.use(VueRouter);

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('router-link', {});

describe('Component Tests', () => {
  describe('Laboratorio Management Detail Component', () => {
    let wrapper: Wrapper<LaboratorioClass>;
    let comp: LaboratorioClass;
    let laboratorioServiceStub: SinonStubbedInstance<LaboratorioService>;

    beforeEach(() => {
      laboratorioServiceStub = sinon.createStubInstance<LaboratorioService>(LaboratorioService);

      wrapper = shallowMount<LaboratorioClass>(LaboratorioDetailComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: { laboratorioService: () => laboratorioServiceStub },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundLaboratorio = { id: 123 };
        laboratorioServiceStub.find.resolves(foundLaboratorio);

        // WHEN
        comp.retrieveLaboratorio(123);
        await comp.$nextTick();

        // THEN
        expect(comp.laboratorio).toBe(foundLaboratorio);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundLaboratorio = { id: 123 };
        laboratorioServiceStub.find.resolves(foundLaboratorio);

        // WHEN
        comp.beforeRouteEnter({ params: { laboratorioId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.laboratorio).toBe(foundLaboratorio);
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
