import { IMunicipio } from '@/shared/model/municipio.model';

export interface IDepartamento {
  id?: number;
  nombre?: string;
  municipios?: IMunicipio[] | null;
}

export class Departamento implements IDepartamento {
  constructor(public id?: number, public nombre?: string, public municipios?: IMunicipio[] | null) {}
}
