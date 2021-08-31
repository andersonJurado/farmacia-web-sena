import { Component, Vue, Inject } from 'vue-property-decorator';

import { required } from 'vuelidate/lib/validators';

import ProductoService from '@/entities/producto/producto.service';
import { IProducto } from '@/shared/model/producto.model';

import { ILineaProducto, LineaProducto } from '@/shared/model/linea-producto.model';
import LineaProductoService from './linea-producto.service';

const validations: any = {
  lineaProducto: {
    nombre: {
      required,
    },
  },
};

@Component({
  validations,
})
export default class LineaProductoUpdate extends Vue {
  @Inject('lineaProductoService') private lineaProductoService: () => LineaProductoService;
  public lineaProducto: ILineaProducto = new LineaProducto();

  @Inject('productoService') private productoService: () => ProductoService;

  public productos: IProducto[] = [];
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.lineaProductoId) {
        vm.retrieveLineaProducto(to.params.lineaProductoId);
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
    if (this.lineaProducto.id) {
      this.lineaProductoService()
        .update(this.lineaProducto)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('farmaciaApp.lineaProducto.updated', { param: param.id });
          return this.$root.$bvToast.toast(message.toString(), {
            toaster: 'b-toaster-top-center',
            title: 'Info',
            variant: 'info',
            solid: true,
            autoHideDelay: 5000,
          });
        });
    } else {
      this.lineaProductoService()
        .create(this.lineaProducto)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('farmaciaApp.lineaProducto.created', { param: param.id });
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

  public retrieveLineaProducto(lineaProductoId): void {
    this.lineaProductoService()
      .find(lineaProductoId)
      .then(res => {
        this.lineaProducto = res;
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
  }
}
