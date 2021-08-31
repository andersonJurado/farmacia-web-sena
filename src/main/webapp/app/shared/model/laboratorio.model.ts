import { IProducto } from '@/shared/model/producto.model';

export interface ILaboratorio {
  id?: number;
  nombre?: string;
  productos?: IProducto[] | null;
}

export class Laboratorio implements ILaboratorio {
  constructor(public id?: number, public nombre?: string, public productos?: IProducto[] | null) {}
}
