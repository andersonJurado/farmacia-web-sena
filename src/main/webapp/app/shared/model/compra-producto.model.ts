import { IProducto } from '@/shared/model/producto.model';
import { ICompra } from '@/shared/model/compra.model';

export interface ICompraProducto {
  id?: number;
  cantidadUds?: number | null;
  precioUdsCompra?: number | null;
  subTotal?: number | null;
  iva?: number | null;
  total?: number | null;
  fechaVencimiento?: Date | null;
  lote?: string | null;
  producto?: IProducto | null;
  compra?: ICompra | null;
}

export class CompraProducto implements ICompraProducto {
  constructor(
    public id?: number,
    public cantidadUds?: number | null,
    public precioUdsCompra?: number | null,
    public subTotal?: number | null,
    public iva?: number | null,
    public total?: number | null,
    public fechaVencimiento?: Date | null,
    public lote?: string | null,
    public producto?: IProducto | null,
    public compra?: ICompra | null
  ) {}
}
