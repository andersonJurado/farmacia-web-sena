import { Component, Vue, Inject } from 'vue-property-decorator';

import { ILineaProducto } from '@/shared/model/linea-producto.model';
import LineaProductoService from './linea-producto.service';

@Component
export default class LineaProductoDetails extends Vue {
  @Inject('lineaProductoService') private lineaProductoService: () => LineaProductoService;
  public lineaProducto: ILineaProducto = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.lineaProductoId) {
        vm.retrieveLineaProducto(to.params.lineaProductoId);
      }
    });
  }

  public retrieveLineaProducto(lineaProductoId) {
    this.lineaProductoService()
      .find(lineaProductoId)
      .then(res => {
        this.lineaProducto = res;
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
