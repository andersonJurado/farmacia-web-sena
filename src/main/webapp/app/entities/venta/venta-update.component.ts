import { Component, Vue, Inject } from 'vue-property-decorator';

import { required } from 'vuelidate/lib/validators';
import dayjs from 'dayjs';
import { DATE_TIME_LONG_FORMAT } from '@/shared/date/filters';

import VentaProductoService from '@/entities/venta-producto/venta-producto.service';
import { IVentaProducto } from '@/shared/model/venta-producto.model';

import ClienteService from '@/entities/cliente/cliente.service';
import { ICliente } from '@/shared/model/cliente.model';

import { IVenta, Venta } from '@/shared/model/venta.model';
import VentaService from './venta.service';

const validations: any = {
  venta: {
    nroFactura: {
      required,
    },
    fecha: {},
  },
};

@Component({
  validations,
})
export default class VentaUpdate extends Vue {
  @Inject('ventaService') private ventaService: () => VentaService;
  public venta: IVenta = new Venta();

  @Inject('ventaProductoService') private ventaProductoService: () => VentaProductoService;

  public ventaProductos: IVentaProducto[] = [];

  @Inject('clienteService') private clienteService: () => ClienteService;

  public clientes: ICliente[] = [];
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.ventaId) {
        vm.retrieveVenta(to.params.ventaId);
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
    if (this.venta.id) {
      this.ventaService()
        .update(this.venta)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('farmaciaApp.venta.updated', { param: param.id });
          return this.$root.$bvToast.toast(message.toString(), {
            toaster: 'b-toaster-top-center',
            title: 'Info',
            variant: 'info',
            solid: true,
            autoHideDelay: 5000,
          });
        });
    } else {
      this.ventaService()
        .create(this.venta)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('farmaciaApp.venta.created', { param: param.id });
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
      this.venta[field] = dayjs(event.target.value, DATE_TIME_LONG_FORMAT);
    } else {
      this.venta[field] = null;
    }
  }

  public updateZonedDateTimeField(field, event) {
    if (event.target.value) {
      this.venta[field] = dayjs(event.target.value, DATE_TIME_LONG_FORMAT);
    } else {
      this.venta[field] = null;
    }
  }

  public retrieveVenta(ventaId): void {
    this.ventaService()
      .find(ventaId)
      .then(res => {
        res.fecha = new Date(res.fecha);
        this.venta = res;
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {
    this.ventaProductoService()
      .retrieve()
      .then(res => {
        this.ventaProductos = res.data;
      });
    this.clienteService()
      .retrieve()
      .then(res => {
        this.clientes = res.data;
      });
  }
}
