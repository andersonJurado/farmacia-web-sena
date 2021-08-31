import { Component, Vue, Inject } from 'vue-property-decorator';

import { required } from 'vuelidate/lib/validators';

import MunicipioService from '@/entities/municipio/municipio.service';
import { IMunicipio } from '@/shared/model/municipio.model';

import { IDepartamento, Departamento } from '@/shared/model/departamento.model';
import DepartamentoService from './departamento.service';

const validations: any = {
  departamento: {
    nombre: {
      required,
    },
  },
};

@Component({
  validations,
})
export default class DepartamentoUpdate extends Vue {
  @Inject('departamentoService') private departamentoService: () => DepartamentoService;
  public departamento: IDepartamento = new Departamento();

  @Inject('municipioService') private municipioService: () => MunicipioService;

  public municipios: IMunicipio[] = [];
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.departamentoId) {
        vm.retrieveDepartamento(to.params.departamentoId);
      }
      vm.initRelationships();
    });
  }

  created(): void {
    this.currentLanguage = this.$store.getters.currentLanguage;
    this.$store.watch(
      () => this.$store.getters.currentLanguage,
      () => {
        this.currentLanguage = this.$store.getters.currentLanguage;
      }
    );
  }

  public save(): void {
    this.isSaving = true;
    if (this.departamento.id) {
      this.departamentoService()
        .update(this.departamento)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('farmaciaApp.departamento.updated', { param: param.id });
          return this.$root.$bvToast.toast(message.toString(), {
            toaster: 'b-toaster-top-center',
            title: 'Info',
            variant: 'info',
            solid: true,
            autoHideDelay: 5000,
          });
        });
    } else {
      this.departamentoService()
        .create(this.departamento)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('farmaciaApp.departamento.created', { param: param.id });
          this.$root.$bvToast.toast(message.toString(), {
            toaster: 'b-toaster-top-center',
            title: 'Success',
            variant: 'success',
            solid: true,
            autoHideDelay: 5000,
          });
        });
    }
  }

  public retrieveDepartamento(departamentoId): void {
    this.departamentoService()
      .find(departamentoId)
      .then(res => {
        this.departamento = res;
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {
    this.municipioService()
      .retrieve()
      .then(res => {
        this.municipios = res.data;
      });
  }
}
