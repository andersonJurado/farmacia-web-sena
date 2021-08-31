package org.norn.farmacia.service.mapper;

import org.mapstruct.*;
import org.norn.farmacia.domain.*;
import org.norn.farmacia.service.dto.LaboratorioDTO;

/**
 * Mapper for the entity {@link Laboratorio} and its DTO {@link LaboratorioDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface LaboratorioMapper extends EntityMapper<LaboratorioDTO, Laboratorio> {
    @Named("nombre")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nombre", source = "nombre")
    LaboratorioDTO toDtoNombre(Laboratorio laboratorio);
}
