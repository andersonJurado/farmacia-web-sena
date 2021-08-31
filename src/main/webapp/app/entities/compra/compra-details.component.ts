import { Component, Vue, Inject } from 'vue-property-decorator';

import { ICompra } from '@/shared/model/compra.model';
import CompraService from './compra.service';

@Component
export default class CompraDetails extends Vue {
  @Inject('compraService') private compraService: () => CompraService;
  public compra: ICompra = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.compraId) {
        vm.retrieveCompra(to.params.compraId);
      }
    });
  }

  public retrieveCompra(compraId) {
    this.compraService()
      .find(compraId)
      .then(res => {
        this.compra = res;
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
