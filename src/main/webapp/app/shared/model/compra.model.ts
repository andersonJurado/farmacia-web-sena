import { ICompraProducto } from '@/shared/model/compra-producto.model';
import { IProveedor } from '@/shared/model/proveedor.model';

export interface ICompra {
  id?: number;
  nroFactura?: string;
  fecha?: Date | null;
  compraProductos?: ICompraProducto[] | null;
  proveedor?: IProveedor | null;
}

export class Compra implements ICompra {
  constructor(
    public id?: number,
    public nroFactura?: string,
    public fecha?: Date | null,
    public compraProductos?: ICompraProducto[] | null,
    public proveedor?: IProveedor | null
  ) {}
}
