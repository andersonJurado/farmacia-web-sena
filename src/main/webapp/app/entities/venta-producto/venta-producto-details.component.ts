import { Component, Vue, Inject } from 'vue-property-decorator';

import { IVentaProducto } from '@/shared/model/venta-producto.model';
import VentaProductoService from './venta-producto.service';

@Component
export default class VentaProductoDetails extends Vue {
  @Inject('ventaProductoService') private ventaProductoService: () => VentaProductoService;
  public ventaProducto: IVentaProducto = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.ventaProductoId) {
        vm.retrieveVentaProducto(to.params.ventaProductoId);
      }
    });
  }

  public retrieveVentaProducto(ventaProductoId) {
    this.ventaProductoService()
      .find(ventaProductoId)
      .then(res => {
        this.ventaProducto = res;
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
