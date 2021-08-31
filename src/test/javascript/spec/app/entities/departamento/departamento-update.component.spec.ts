/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';

import * as config from '@/shared/config/config';
import DepartamentoUpdateComponent from '@/entities/departamento/departamento-update.vue';
import DepartamentoClass from '@/entities/departamento/departamento-update.component';
import DepartamentoService from '@/entities/departamento/departamento.service';

import MunicipioService from '@/entities/municipio/municipio.service';

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
  describe('Departamento Management Update Component', () => {
    let wrapper: Wrapper<DepartamentoClass>;
    let comp: DepartamentoClass;
    let departamentoServiceStub: SinonStubbedInstance<DepartamentoService>;

    beforeEach(() => {
      departamentoServiceStub = sinon.createStubInstance<DepartamentoService>(DepartamentoService);

      wrapper = shallowMount<DepartamentoClass>(DepartamentoUpdateComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: {
          departamentoService: () => departamentoServiceStub,

          municipioService: () => new MunicipioService(),
        },
      });
      comp = wrapper.vm;
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const entity = { id: 123 };
        comp.departamento = entity;
        departamentoServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(departamentoServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.departamento = entity;
        departamentoServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(departamentoServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundDepartamento = { id: 123 };
        departamentoServiceStub.find.resolves(foundDepartamento);
        departamentoServiceStub.retrieve.resolves([foundDepartamento]);

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
