import { IVentaProducto } from '@/shared/model/venta-producto.model';
import { ICliente } from '@/shared/model/cliente.model';

export interface IVenta {
  id?: number;
  nroFactura?: string;
  fecha?: Date | null;
  ventaProductos?: IVentaProducto[] | null;
  cliente?: ICliente | null;
}

export class Venta implements IVenta {
  constructor(
    public id?: number,
    public nroFactura?: string,
    public fecha?: Date | null,
    public ventaProductos?: IVentaProducto[] | null,
    public cliente?: ICliente | null
  ) {}
}
