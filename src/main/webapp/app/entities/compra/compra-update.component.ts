import { Component, Vue, Inject } from 'vue-property-decorator';

import { required } from 'vuelidate/lib/validators';
import dayjs from 'dayjs';
import { DATE_TIME_LONG_FORMAT } from '@/shared/date/filters';

import CompraProductoService from '@/entities/compra-producto/compra-producto.service';
import { ICompraProducto } from '@/shared/model/compra-producto.model';

import ProveedorService from '@/entities/proveedor/proveedor.service';
import { IProveedor } from '@/shared/model/proveedor.model';

import { ICompra, Compra } from '@/shared/model/compra.model';
import CompraService from './compra.service';

const validations: any = {
  compra: {
    nroFactura: {
      required,
    },
    fecha: {},
  },
};

@Component({
  validations,
})
export default class CompraUpdate extends Vue {
  @Inject('compraService') private compraService: () => CompraService;
  public compra: ICompra = new Compra();

  @Inject('compraProductoService') private compraProductoService: () => CompraProductoService;

  public compraProductos: ICompraProducto[] = [];

  @Inject('proveedorService') private proveedorService: () => ProveedorService;

  public proveedors: IProveedor[] = [];
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.compraId) {
        vm.retrieveCompra(to.params.compraId);
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
    if (this.compra.id) {
      this.compraService()
        .update(this.compra)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('farmaciaApp.compra.updated', { param: param.id });
          return this.$root.$bvToast.toast(message.toString(), {
            toaster: 'b-toaster-top-center',
            title: 'Info',
            variant: 'info',
            solid: true,
            autoHideDelay: 5000,
          });
        });
    } else {
      this.compraService()
        .create(this.compra)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('farmaciaApp.compra.created', { param: param.id });
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

  public convertDateTimeFromServer(date: Date): string {
    if (date && dayjs(date).isValid()) {
      return dayjs(date).format(DATE_TIME_LONG_FORMAT);
    }
    return null;
  }

  public updateInstantField(field, event) {
    if (event.target.value) {
      this.compra[field] = dayjs(event.target.value, DATE_TIME_LONG_FORMAT);
    } else {
      this.compra[field] = null;
    }
  }

  public updateZonedDateTimeField(field, event) {
    if (event.target.value) {
      this.compra[field] = dayjs(event.target.value, DATE_TIME_LONG_FORMAT);
    } else {
      this.compra[field] = null;
    }
  }

  public retrieveCompra(compraId): void {
    this.compraService()
      .find(compraId)
      .then(res => {
        res.fecha = new Date(res.fecha);
        this.compra = res;
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {
    this.compraProductoService()
      .retrieve()
      .then(res => {
        this.compraProductos = res.data;
      });
    this.proveedorService()
      .retrieve()
      .then(res => {
        this.proveedors = res.data;
      });
  }
}
