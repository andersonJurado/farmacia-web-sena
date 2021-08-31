import { Component, Vue, Inject } from 'vue-property-decorator';

import { required } from 'vuelidate/lib/validators';

import MunicipioService from '@/entities/municipio/municipio.service';
import { IMunicipio } from '@/shared/model/municipio.model';

import CompraService from '@/entities/compra/compra.service';
import { ICompra } from '@/shared/model/compra.model';

import { IProveedor, Proveedor } from '@/shared/model/proveedor.model';
import ProveedorService from './proveedor.service';

const validations: any = {
  proveedor: {
    nombre: {
      required,
    },
    primerTelefono: {},
    segundoTelefono: {},
  },
};

@Component({
  validations,
})
export default class ProveedorUpdate extends Vue {
  @Inject('proveedorService') private proveedorService: () => ProveedorService;
  public proveedor: IProveedor = new Proveedor();

  @Inject('municipioService') private municipioService: () => MunicipioService;

  public municipios: IMunicipio[] = [];

  @Inject('compraService') private compraService: () => CompraService;

  public compras: ICompra[] = [];
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.proveedorId) {
        vm.retrieveProveedor(to.params.proveedorId);
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
    if (this.proveedor.id) {
      this.proveedorService()
        .update(this.proveedor)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('farmaciaApp.proveedor.updated', { param: param.id });
          return this.$root.$bvToast.toast(message.toString(), {
            toaster: 'b-toaster-top-center',
            title: 'Info',
            variant: 'info',
            solid: true,
            autoHideDelay: 5000,
          });
        });
    } else {
      this.proveedorService()
        .create(this.proveedor)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('farmaciaApp.proveedor.created', { param: param.id });
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

  public retrieveProveedor(proveedorId): void {
    this.proveedorService()
      .find(proveedorId)
      .then(res => {
        this.proveedor = res;
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
    this.compraService()
      .retrieve()
      .then(res => {
        this.compras = res.data;
      });
  }
}
