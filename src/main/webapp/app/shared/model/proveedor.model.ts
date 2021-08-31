import { IMunicipio } from '@/shared/model/municipio.model';
import { ICompra } from '@/shared/model/compra.model';

export interface IProveedor {
  id?: number;
  nombre?: string;
  primerTelefono?: string | null;
  segundoTelefono?: string | null;
  municpio?: IMunicipio | null;
  compras?: ICompra[] | null;
}

export class Proveedor implements IProveedor {
  constructor(
    public id?: number,
    public nombre?: string,
    public primerTelefono?: string | null,
    public segundoTelefono?: string | null,
    public municpio?: IMunicipio | null,
    public compras?: ICompra[] | null
  ) {}
}
