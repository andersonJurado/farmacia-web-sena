import { Component, Vue, Inject } from 'vue-property-decorator';

import { IVenta } from '@/shared/model/venta.model';
import VentaService from './venta.service';

@Component
export default class VentaDetails extends Vue {
  @Inject('ventaService') private ventaService: () => VentaService;
  public venta: IVenta = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.ventaId) {
        vm.retrieveVenta(to.params.ventaId);
      }
    });
  }

  public retrieveVenta(ventaId) {
    this.ventaService()
      .find(ventaId)
      .then(res => {
        this.venta = res;
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
