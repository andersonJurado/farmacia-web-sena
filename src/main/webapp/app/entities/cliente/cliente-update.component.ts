import { Component, Vue, Inject } from 'vue-property-decorator';

import { required } from 'vuelidate/lib/validators';

import MunicipioService from '@/entities/municipio/municipio.service';
import { IMunicipio } from '@/shared/model/municipio.model';

import GeneroService from '@/entities/genero/genero.service';
import { IGenero } from '@/shared/model/genero.model';

import VentaService from '@/entities/venta/venta.service';
import { IVenta } from '@/shared/model/venta.model';

import { ICliente, Cliente } from '@/shared/model/cliente.model';
import ClienteService from './cliente.service';

const validations: any = {
  cliente: {
    primerNombre: {
      required,
    },
    segundoNombre: {},
    primerApellido: {},
    segundoApellido: {},
    primerTelefono: {},
    segundoTelefono: {},
  },
};

@Component({
  validations,
})
export default class ClienteUpdate extends Vue {
  @Inject('clienteService') private clienteService: () => ClienteService;
  public cliente: ICliente = new Cliente();

  @Inject('municipioService') private municipioService: () => MunicipioService;

  public municipios: IMunicipio[] = [];

  @Inject('generoService') private generoService: () => GeneroService;

  public generos: IGenero[] = [];

  @Inject('ventaService') private ventaService: () => VentaService;

  public ventas: IVenta[] = [];
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.clienteId) {
        vm.retrieveCliente(to.params.clienteId);
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
    if (this.cliente.id) {
      this.clienteService()
        .update(this.cliente)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('farmaciaApp.cliente.updated', { param: param.id });
          return this.$root.$bvToast.toast(message.toString(), {
            toaster: 'b-toaster-top-center',
            title: 'Info',
            variant: 'info',
            solid: true,
            autoHideDelay: 5000,
          });
        });
    } else {
      this.clienteService()
        .create(this.cliente)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('farmaciaApp.cliente.created', { param: param.id });
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

  public retrieveCliente(clienteId): void {
    this.clienteService()
      .find(clienteId)
      .then(res => {
        this.cliente = res;
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
    this.generoService()
      .retrieve()
      .then(res => {
        this.generos = res.data;
      });
    this.ventaService()
      .retrieve()
      .then(res => {
        this.ventas = res.data;
      });
  }
}
