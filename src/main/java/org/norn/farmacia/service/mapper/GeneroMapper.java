package org.norn.farmacia.service.mapper;

import org.mapstruct.*;
import org.norn.farmacia.domain.*;
import org.norn.farmacia.service.dto.GeneroDTO;

/**
 * Mapper for the entity {@link Genero} and its DTO {@link GeneroDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface GeneroMapper extends EntityMapper<GeneroDTO, Genero> {
    @Named("nombre")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nombre", source = "nombre")
    GeneroDTO toDtoNombre(Genero genero);
}
