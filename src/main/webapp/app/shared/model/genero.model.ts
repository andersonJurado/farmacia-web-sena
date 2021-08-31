import { ICliente } from '@/shared/model/cliente.model';

export interface IGenero {
  id?: number;
  nombre?: string;
  clientes?: ICliente[] | null;
}

export class Genero implements IGenero {
  constructor(public id?: number, public nombre?: string, public clientes?: ICliente[] | null) {}
}
