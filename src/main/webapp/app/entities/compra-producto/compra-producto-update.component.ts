import { Component, Vue, Inject } from 'vue-property-decorator';

import ProductoService from '@/entities/producto/producto.service';
import { IProducto } from '@/shared/model/producto.model';

import CompraService from '@/entities/compra/compra.service';
import { ICompra } from '@/shared/model/compra.model';

import { ICompraProducto, CompraProducto } from '@/shared/model/compra-producto.model';
import CompraProductoService from './compra-producto.service';

const validations: any = {
  compraProducto: {
    cantidadUds: {},
    precioUdsCompra: {},
    subTotal: {},
    iva: {},
    total: {},
    fechaVencimiento: {},
    lote: {},
  },
};

@Component({
  validations,
})
export default class CompraProductoUpdate extends Vue {
  @Inject('compraProductoService') private compraProductoService: () => CompraProductoService;
  public compraProducto: ICompraProducto = new CompraProducto();

  @Inject('productoService') private productoService: () => ProductoService;

  public productos: IProducto[] = [];

  @Inject('compraService') private compraService: () => CompraService;

  public compras: ICompra[] = [];
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.compraProductoId) {
        vm.retrieveCompraProducto(to.params.compraProductoId);
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
    if (this.compraProducto.id) {
      this.compraProductoService()
        .update(this.compraProducto)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('farmaciaApp.compraProducto.updated', { param: param.id });
          return this.$root.$bvToast.toast(message.toString(), {
            toaster: 'b-toaster-top-center',
            title: 'Info',
            variant: 'info',
            solid: true,
            autoHideDelay: 5000,
          });
        });
    } else {
      this.compraProductoService()
        .create(this.compraProducto)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('farmaciaApp.compraProducto.created', { param: param.id });
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

  public retrieveCompraProducto(compraProductoId): void {
    this.compraProductoService()
      .find(compraProductoId)
      .then(res => {
        this.compraProducto = res;
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
    this.compraService()
      .retrieve()
      .then(res => {
        this.compras = res.data;
      });
  }
}
