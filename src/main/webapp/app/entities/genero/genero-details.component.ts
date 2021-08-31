import { Component, Vue, Inject } from 'vue-property-decorator';

import { IGenero } from '@/shared/model/genero.model';
import GeneroService from './genero.service';

@Component
export default class GeneroDetails extends Vue {
  @Inject('generoService') private generoService: () => GeneroService;
  public genero: IGenero = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.generoId) {
        vm.retrieveGenero(to.params.generoId);
      }
    });
  }

  public retrieveGenero(generoId) {
    this.generoService()
      .find(generoId)
      .then(res => {
        this.genero = res;
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
