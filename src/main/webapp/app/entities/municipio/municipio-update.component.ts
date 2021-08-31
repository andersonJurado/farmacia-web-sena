import { Component, Vue, Inject } from 'vue-property-decorator';

import { required } from 'vuelidate/lib/validators';

import DepartamentoService from '@/entities/departamento/departamento.service';
import { IDepartamento } from '@/shared/model/departamento.model';

import ClienteService from '@/entities/cliente/cliente.service';
import { ICliente } from '@/shared/model/cliente.model';

import ProveedorService from '@/entities/proveedor/proveedor.service';
import { IProveedor } from '@/shared/model/proveedor.model';

import { IMunicipio, Municipio } from '@/shared/model/municipio.model';
import MunicipioService from './municipio.service';

const validations: any = {
  municipio: {
    nombre: {
      required,
    },
  },
};

@Component({
  validations,
})
export default class MunicipioUpdate extends Vue {
  @Inject('municipioService') private municipioService: () => MunicipioService;
  public municipio: IMunicipio = new Municipio();

  @Inject('departamentoService') private departamentoService: () => DepartamentoService;

  public departamentos: IDepartamento[] = [];

  @Inject('clienteService') private clienteService: () => ClienteService;

  public clientes: ICliente[] = [];

  @Inject('proveedorService') private proveedorService: () => ProveedorService;

  public proveedors: IProveedor[] = [];
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.municipioId) {
        vm.retrieveMunicipio(to.params.municipioId);
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
    if (this.municipio.id) {
      this.municipioService()
        .update(this.municipio)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('farmaciaApp.municipio.updated', { param: param.id });
          return this.$root.$bvToast.toast(message.toString(), {
            toaster: 'b-toaster-top-center',
            title: 'Info',
            variant: 'info',
            solid: true,
            autoHideDelay: 5000,
          });
        });
    } else {
      this.municipioService()
        .create(this.municipio)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('farmaciaApp.municipio.created', { param: param.id });
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

  public retrieveMunicipio(municipioId): void {
    this.municipioService()
      .find(municipioId)
      .then(res => {
        this.municipio = res;
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {
    this.departamentoService()
      .retrieve()
      .then(res => {
        this.departamentos = res.data;
      });
    this.clienteService()
      .retrieve()
      .then(res => {
        this.clientes = res.data;
      });
    this.proveedorService()
      .retrieve()
      .then(res => {
        this.proveedors = res.data;
      });
  }
}
