import { IProducto } from '@/shared/model/producto.model';

export interface ILineaProducto {
  id?: number;
  nombre?: string;
  productos?: IProducto[] | null;
}

export class LineaProducto implements ILineaProducto {
  constructor(public id?: number, public nombre?: string, public productos?: IProducto[] | null) {}
}
