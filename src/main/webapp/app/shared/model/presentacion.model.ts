import { IProducto } from '@/shared/model/producto.model';

export interface IPresentacion {
  id?: number;
  nombre?: string;
  productos?: IProducto[] | null;
}

export class Presentacion implements IPresentacion {
  constructor(public id?: number, public nombre?: string, public productos?: IProducto[] | null) {}
}
