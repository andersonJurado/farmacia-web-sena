import { Component, Vue, Inject } from 'vue-property-decorator';

import { required } from 'vuelidate/lib/validators';

import PresentacionService from '@/entities/presentacion/presentacion.service';
import { IPresentacion } from '@/shared/model/presentacion.model';

import LaboratorioService from '@/entities/laboratorio/laboratorio.service';
import { ILaboratorio } from '@/shared/model/laboratorio.model';

import LineaProductoService from '@/entities/linea-producto/linea-producto.service';
import { ILineaProducto } from '@/shared/model/linea-producto.model';

import CompraProductoService from '@/entities/compra-producto/compra-producto.service';
import { ICompraProducto } from '@/shared/model/compra-producto.model';

import VentaProductoService from '@/entities/venta-producto/venta-producto.service';
import { IVentaProducto } from '@/shared/model/venta-producto.model';

import { IProducto, Producto } from '@/shared/model/producto.model';
import ProductoService from './producto.service';

const validations: any = {
  producto: {
    nombreProducto: {
      required,
    },
    cantidad: {},
    iva: {},
    precioUdsVenta: {},
    margenDeGanancia: {},
    invima: {},
  },
};

@Component({
  validations,
})
export default class ProductoUpdate extends Vue {
  @Inject('productoService') private productoService: () => ProductoService;
  public producto: IProducto = new Producto();

  @Inject('presentacionService') private presentacionService: () => PresentacionService;

  public presentacions: IPresentacion[] = [];

  @Inject('laboratorioService') private laboratorioService: () => LaboratorioService;

  public laboratorios: ILaboratorio[] = [];

  @Inject('lineaProductoService') private lineaProductoService: () => LineaProductoService;

  public lineaProductos: ILineaProducto[] = [];

  @Inject('compraProductoService') private compraProductoService: () => CompraProductoService;

  public compraProductos: ICompraProducto[] = [];

  @Inject('ventaProductoService') private ventaProductoService: () => VentaProductoService;

  public ventaProductos: IVentaProducto[] = [];
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.productoId) {
        vm.retrieveProducto(to.params.productoId);
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
    if (this.producto.id) {
      this.productoService()
        .update(this.producto)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('farmaciaApp.producto.updated', { param: param.id });
          return this.$root.$bvToast.toast(message.toString(), {
            toaster: 'b-toaster-top-center',
            title: 'Info',
            variant: 'info',
            solid: true,
            autoHideDelay: 5000,
          });
        });
    } else {
      this.productoService()
        .create(this.producto)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('farmaciaApp.producto.created', { param: param.id });
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

  public retrieveProducto(productoId): void {
    this.productoService()
      .find(productoId)
      .then(res => {
        this.producto = res;
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {
    this.presentacionService()
      .retrieve()
      .then(res => {
        this.presentacions = res.data;
      });
    this.laboratorioService()
      .retrieve()
      .then(res => {
        this.laboratorios = res.data;
      });
    this.lineaProductoService()
      .retrieve()
      .then(res => {
        this.lineaProductos = res.data;
      });
    this.compraProductoService()
      .retrieve()
      .then(res => {
        this.compraProductos = res.data;
      });
    this.ventaProductoService()
      .retrieve()
      .then(res => {
        this.ventaProductos = res.data;
      });
  }
}
