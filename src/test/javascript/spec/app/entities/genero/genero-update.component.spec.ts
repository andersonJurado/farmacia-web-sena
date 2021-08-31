/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';

import * as config from '@/shared/config/config';
import GeneroUpdateComponent from '@/entities/genero/genero-update.vue';
import GeneroClass from '@/entities/genero/genero-update.component';
import GeneroService from '@/entities/genero/genero.service';

import ClienteService from '@/entities/cliente/cliente.service';

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
  describe('Genero Management Update Component', () => {
    let wrapper: Wrapper<GeneroClass>;
    let comp: GeneroClass;
    let generoServiceStub: SinonStubbedInstance<GeneroService>;

    beforeEach(() => {
      generoServiceStub = sinon.createStubInstance<GeneroService>(GeneroService);

      wrapper = shallowMount<GeneroClass>(GeneroUpdateComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: {
          generoService: () => generoServiceStub,

          clienteService: () => new ClienteService(),
        },
      });
      comp = wrapper.vm;
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const entity = { id: 123 };
        comp.genero = entity;
        generoServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(generoServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.genero = entity;
        generoServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(generoServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundGenero = { id: 123 };
        generoServiceStub.find.resolves(foundGenero);
        generoServiceStub.retrieve.resolves([foundGenero]);

        // WHEN
        comp.beforeRouteEnter({ params: { generoId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.genero).toBe(foundGenero);
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
