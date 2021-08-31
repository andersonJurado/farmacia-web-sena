import { Component, Vue, Inject } from 'vue-property-decorator';

import { required } from 'vuelidate/lib/validators';

import ClienteService from '@/entities/cliente/cliente.service';
import { ICliente } from '@/shared/model/cliente.model';

import { IGenero, Genero } from '@/shared/model/genero.model';
import GeneroService from './genero.service';

const validations: any = {
  genero: {
    nombre: {
      required,
    },
  },
};

@Component({
  validations,
})
export default class GeneroUpdate extends Vue {
  @Inject('generoService') private generoService: () => GeneroService;
  public genero: IGenero = new Genero();

  @Inject('clienteService') private clienteService: () => ClienteService;

  public clientes: ICliente[] = [];
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.generoId) {
        vm.retrieveGenero(to.params.generoId);
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
    if (this.genero.id) {
      this.generoService()
        .update(this.genero)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('farmaciaApp.genero.updated', { param: param.id });
          return this.$root.$bvToast.toast(message.toString(), {
            toaster: 'b-toaster-top-center',
            title: 'Info',
            variant: 'info',
            solid: true,
            autoHideDelay: 5000,
          });
        });
    } else {
      this.generoService()
        .create(this.genero)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('farmaciaApp.genero.created', { param: param.id });
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

  public retrieveGenero(generoId): void {
    this.generoService()
      .find(generoId)
      .then(res => {
        this.genero = res;
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {
    this.clienteService()
      .retrieve()
      .then(res => {
        this.clientes = res.data;
      });
  }
}
