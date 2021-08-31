import { Component, Vue, Inject } from 'vue-property-decorator';

import { IDepartamento } from '@/shared/model/departamento.model';
import DepartamentoService from './departamento.service';

@Component
export default class DepartamentoDetails extends Vue {
  @Inject('departamentoService') private departamentoService: () => DepartamentoService;
  public departamento: IDepartamento = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.departamentoId) {
        vm.retrieveDepartamento(to.params.departamentoId);
      }
    });
  }

  public retrieveDepartamento(departamentoId) {
    this.departamentoService()
      .find(departamentoId)
      .then(res => {
        this.departamento = res;
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
