import { IMunicipio } from '@/shared/model/municipio.model';
import { IGenero } from '@/shared/model/genero.model';
import { IVenta } from '@/shared/model/venta.model';

export interface ICliente {
  id?: number;
  primerNombre?: string;
  segundoNombre?: string | null;
  primerApellido?: string | null;
  segundoApellido?: string | null;
  primerTelefono?: string | null;
  segundoTelefono?: string | null;
  municipio?: IMunicipio | null;
  genero?: IGenero | null;
  ventas?: IVenta[] | null;
}

export class Cliente implements ICliente {
  constructor(
    public id?: number,
    public primerNombre?: string,
    public segundoNombre?: string | null,
    public primerApellido?: string | null,
    public segundoApellido?: string | null,
    public primerTelefono?: string | null,
    public segundoTelefono?: string | null,
    public municipio?: IMunicipio | null,
    public genero?: IGenero | null,
    public ventas?: IVenta[] | null
  ) {}
}
