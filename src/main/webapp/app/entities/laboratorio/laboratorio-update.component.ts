import { Component, Vue, Inject } from 'vue-property-decorator';

import { required } from 'vuelidate/lib/validators';

import ProductoService from '@/entities/producto/producto.service';
import { IProducto } from '@/shared/model/producto.model';

import { ILaboratorio, Laboratorio } from '@/shared/model/laboratorio.model';
import LaboratorioService from './laboratorio.service';

const validations: any = {
  laboratorio: {
    nombre: {
      required,
    },
  },
};

@Component({
  validations,
})
export default class LaboratorioUpdate extends Vue {
  @Inject('laboratorioService') private laboratorioService: () => LaboratorioService;
  public laboratorio: ILaboratorio = new Laboratorio();

  @Inject('productoService') private productoService: () => ProductoService;

  public productos: IProducto[] = [];
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.laboratorioId) {
        vm.retrieveLaboratorio(to.params.laboratorioId);
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
    if (this.laboratorio.id) {
      this.laboratorioService()
        .update(this.laboratorio)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('farmaciaApp.laboratorio.updated', { param: param.id });
          return this.$root.$bvToast.toast(message.toString(), {
            toaster: 'b-toaster-top-center',
            title: 'Info',
            variant: 'info',
            solid: true,
            autoHideDelay: 5000,
          });
        });
    } else {
      this.laboratorioService()
        .create(this.laboratorio)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('farmaciaApp.laboratorio.created', { param: param.id });
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

  public retrieveLaboratorio(laboratorioId): void {
    this.laboratorioService()
      .find(laboratorioId)
      .then(res => {
        this.laboratorio = res;
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
