package org.norn.farmacia.service.mapper;

import org.mapstruct.*;
import org.norn.farmacia.domain.*;
import org.norn.farmacia.service.dto.MunicipioDTO;

/**
 * Mapper for the entity {@link Municipio} and its DTO {@link MunicipioDTO}.
 */
@Mapper(componentModel = "spring", uses = { DepartamentoMapper.class })
public interface MunicipioMapper extends EntityMapper<MunicipioDTO, Municipio> {
    @Mapping(target = "departamento", source = "departamento", qualifiedByName = "nombre")
    MunicipioDTO toDto(Municipio s);

    @Named("nombre")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nombre", source = "nombre")
    MunicipioDTO toDtoNombre(Municipio municipio);
}
