import { Component, Vue, Inject } from 'vue-property-decorator';

import { IPresentacion } from '@/shared/model/presentacion.model';
import PresentacionService from './presentacion.service';

@Component
export default class PresentacionDetails extends Vue {
  @Inject('presentacionService') private presentacionService: () => PresentacionService;
  public presentacion: IPresentacion = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.presentacionId) {
        vm.retrievePresentacion(to.params.presentacionId);
      }
    });
  }

  public retrievePresentacion(presentacionId) {
    this.presentacionService()
      .find(presentacionId)
      .then(res => {
        this.presentacion = res;
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
