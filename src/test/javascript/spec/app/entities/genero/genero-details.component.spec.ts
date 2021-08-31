/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import GeneroDetailComponent from '@/entities/genero/genero-details.vue';
import GeneroClass from '@/entities/genero/genero-details.component';
import GeneroService from '@/entities/genero/genero.service';
import router from '@/router';

const localVue = createLocalVue();
localVue.use(VueRouter);

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('router-link', {});

describe('Component Tests', () => {
  describe('Genero Management Detail Component', () => {
    let wrapper: Wrapper<GeneroClass>;
    let comp: GeneroClass;
    let generoServiceStub: SinonStubbedInstance<GeneroService>;

    beforeEach(() => {
      generoServiceStub = sinon.createStubInstance<GeneroService>(GeneroService);

      wrapper = shallowMount<GeneroClass>(GeneroDetailComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: { generoService: () => generoServiceStub },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundGenero = { id: 123 };
        generoServiceStub.find.resolves(foundGenero);

        // WHEN
        comp.retrieveGenero(123);
        await comp.$nextTick();

        // THEN
        expect(comp.genero).toBe(foundGenero);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundGenero = { id: 123 };
        generoServiceStub.find.resolves(foundGenero);

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
