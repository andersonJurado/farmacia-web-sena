import { Component, Vue, Inject } from 'vue-property-decorator';

import { required } from 'vuelidate/lib/validators';

import ProductoService from '@/entities/producto/producto.service';
import { IProducto } from '@/shared/model/producto.model';

import { IPresentacion, Presentacion } from '@/shared/model/presentacion.model';
import PresentacionService from './presentacion.service';

const validations: any = {
  presentacion: {
    nombre: {
      required,
    },
  },
};

@Component({
  validations,
})
export default class PresentacionUpdate extends Vue {
  @Inject('presentacionService') private presentacionService: () => PresentacionService;
  public presentacion: IPresentacion = new Presentacion();

  @Inject('productoService') private productoService: () => ProductoService;

  public productos: IProducto[] = [];
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.presentacionId) {
        vm.retrievePresentacion(to.params.presentacionId);
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
    if (this.presentacion.id) {
      this.presentacionService()
        .update(this.presentacion)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('farmaciaApp.presentacion.updated', { param: param.id });
          return this.$root.$bvToast.toast(message.toString(), {
            toaster: 'b-toaster-top-center',
            title: 'Info',
            variant: 'info',
            solid: true,
            autoHideDelay: 5000,
          });
        });
    } else {
      this.presentacionService()
        .create(this.presentacion)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('farmaciaApp.presentacion.created', { param: param.id });
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

  public retrievePresentacion(presentacionId): void {
    this.presentacionService()
      .find(presentacionId)
      .then(res => {
        this.presentacion = res;
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
