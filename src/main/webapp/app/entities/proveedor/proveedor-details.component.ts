import { Component, Vue, Inject } from 'vue-property-decorator';

import { IProveedor } from '@/shared/model/proveedor.model';
import ProveedorService from './proveedor.service';

@Component
export default class ProveedorDetails extends Vue {
  @Inject('proveedorService') private proveedorService: () => ProveedorService;
  public proveedor: IProveedor = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.proveedorId) {
        vm.retrieveProveedor(to.params.proveedorId);
      }
    });
  }

  public retrieveProveedor(proveedorId) {
    this.proveedorService()
      .find(proveedorId)
      .then(res => {
        this.proveedor = res;
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
