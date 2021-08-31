import { IDepartamento } from '@/shared/model/departamento.model';
import { ICliente } from '@/shared/model/cliente.model';
import { IProveedor } from '@/shared/model/proveedor.model';

export interface IMunicipio {
  id?: number;
  nombre?: string;
  departamento?: IDepartamento | null;
  clientes?: ICliente[] | null;
  proveedors?: IProveedor[] | null;
}

export class Municipio implements IMunicipio {
  constructor(
    public id?: number,
    public nombre?: string,
    public departamento?: IDepartamento | null,
    public clientes?: ICliente[] | null,
    public proveedors?: IProveedor[] | null
  ) {}
}
