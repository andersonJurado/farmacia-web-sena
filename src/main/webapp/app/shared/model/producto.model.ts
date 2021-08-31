import { IPresentacion } from '@/shared/model/presentacion.model';
import { ILaboratorio } from '@/shared/model/laboratorio.model';
import { ILineaProducto } from '@/shared/model/linea-producto.model';
import { ICompraProducto } from '@/shared/model/compra-producto.model';
import { IVentaProducto } from '@/shared/model/venta-producto.model';

export interface IProducto {
  id?: number;
  nombreProducto?: string;
  cantidad?: number | null;
  iva?: number | null;
  precioUdsVenta?: number | null;
  margenDeGanancia?: number | null;
  invima?: string | null;
  presentacion?: IPresentacion | null;
  laboratorio?: ILaboratorio | null;
  lineaProducto?: ILineaProducto | null;
  compraProductos?: ICompraProducto[] | null;
  ventaProductos?: IVentaProducto[] | null;
}

export class Producto implements IProducto {
  constructor(
    public id?: number,
    public nombreProducto?: string,
    public cantidad?: number | null,
    public iva?: number | null,
    public precioUdsVenta?: number | null,
    public margenDeGanancia?: number | null,
    public invima?: string | null,
    public presentacion?: IPresentacion | null,
    public laboratorio?: ILaboratorio | null,
    public lineaProducto?: ILineaProducto | null,
    public compraProductos?: ICompraProducto[] | null,
    public ventaProductos?: IVentaProducto[] | null
  ) {}
}
