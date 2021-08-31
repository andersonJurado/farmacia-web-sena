import { Component, Vue, Inject } from 'vue-property-decorator';

import { ICompraProducto } from '@/shared/model/compra-producto.model';
import CompraProductoService from './compra-producto.service';

@Component
export default class CompraProductoDetails extends Vue {
  @Inject('compraProductoService') private compraProductoService: () => CompraProductoService;
  public compraProducto: ICompraProducto = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.compraProductoId) {
        vm.retrieveCompraProducto(to.params.compraProductoId);
      }
    });
  }

  public retrieveCompraProducto(compraProductoId) {
    this.compraProductoService()
      .find(compraProductoId)
      .then(res => {
        this.compraProducto = res;
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
