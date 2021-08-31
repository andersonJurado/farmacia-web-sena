import { IProducto } from '@/shared/model/producto.model';
import { IVenta } from '@/shared/model/venta.model';

export interface IVentaProducto {
  id?: number;
  cantidad?: number | null;
  total?: number | null;
  producto?: IProducto | null;
  venta?: IVenta | null;
}

export class VentaProducto implements IVentaProducto {
  constructor(
    public id?: number,
    public cantidad?: number | null,
    public total?: number | null,
    public producto?: IProducto | null,
    public venta?: IVenta | null
  ) {}
}
