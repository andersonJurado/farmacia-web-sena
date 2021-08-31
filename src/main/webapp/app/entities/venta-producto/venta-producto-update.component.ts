import { Component, Vue, Inject } from 'vue-property-decorator';

import ProductoService from '@/entities/producto/producto.service';
import { IProducto } from '@/shared/model/producto.model';

import VentaService from '@/entities/venta/venta.service';
import { IVenta } from '@/shared/model/venta.model';

import { IVentaProducto, VentaProducto } from '@/shared/model/venta-producto.model';
import VentaProductoService from './venta-producto.service';

const validations: any = {
  ventaProducto: {
    cantidad: {},
    total: {},
  },
};

@Component({
  validations,
})
export default class VentaProductoUpdate extends Vue {
  @Inject('ventaProductoService') private ventaProductoService: () => VentaProductoService;
  public ventaProducto: IVentaProducto = new VentaProducto();

  @Inject('productoService') private productoService: () => ProductoService;

  public productos: IProducto[] = [];

  @Inject('ventaService') private ventaService: () => VentaService;

  public ventas: IVenta[] = [];
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.ventaProductoId) {
        vm.retrieveVentaProducto(to.params.ventaProductoId);
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
    if (this.ventaProducto.id) {
      this.ventaProductoService()
        .update(this.ventaProducto)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('farmaciaApp.ventaProducto.updated', { param: param.id });
          return this.$root.$bvToast.toast(message.toString(), {
            toaster: 'b-toaster-top-center',
            title: 'Info',
            variant: 'info',
            solid: true,
            autoHideDelay: 5000,
          });
        });
    } else {
      this.ventaProductoService()
        .create(this.ventaProducto)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('farmaciaApp.ventaProducto.created', { param: param.id });
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

  public retrieveVentaProducto(ventaProductoId): void {
    this.ventaProductoService()
      .find(ventaProductoId)
      .then(res => {
        this.ventaProducto = res;
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {
    this.productoService()
      .retrieve()
      .then(res => {
        this.productos = res.data;
      });
    this.ventaService()
      .retrieve()
      .then(res => {
        this.ventas = res.data;
      });
  }
}
