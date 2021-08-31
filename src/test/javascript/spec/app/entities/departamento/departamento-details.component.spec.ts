/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import DepartamentoDetailComponent from '@/entities/departamento/departamento-details.vue';
import DepartamentoClass from '@/entities/departamento/departamento-details.component';
import DepartamentoService from '@/entities/departamento/departamento.service';
import router from '@/router';

const localVue = createLocalVue();
localVue.use(VueRouter);

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('router-link', {});

describe('Component Tests', () => {
  describe('Departamento Management Detail Component', () => {
    let wrapper: Wrapper<DepartamentoClass>;
    let comp: DepartamentoClass;
    let departamentoServiceStub: SinonStubbedInstance<DepartamentoService>;

    beforeEach(() => {
      departamentoServiceStub = sinon.createStubInstance<DepartamentoService>(DepartamentoService);

      wrapper = shallowMount<DepartamentoClass>(DepartamentoDetailComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: { departamentoService: () => departamentoServiceStub },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundDepartamento = { id: 123 };
        departamentoServiceStub.find.resolves(foundDepartamento);

        // WHEN
        comp.retrieveDepartamento(123);
        await comp.$nextTick();

        // THEN
        expect(comp.departamento).toBe(foundDepartamento);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundDepartamento = { id: 123 };
        departamentoServiceStub.find.resolves(foundDepartamento);

        // WHEN
        comp.beforeRouteEnter({ params: { departamentoId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.departamento).toBe(foundDepartamento);
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
