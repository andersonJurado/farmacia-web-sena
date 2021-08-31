package org.norn.farmacia.service.mapper;

import org.mapstruct.*;
import org.norn.farmacia.domain.*;
import org.norn.farmacia.service.dto.PresentacionDTO;

/**
 * Mapper for the entity {@link Presentacion} and its DTO {@link PresentacionDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PresentacionMapper extends EntityMapper<PresentacionDTO, Presentacion> {
    @Named("nombre")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nombre", source = "nombre")
    PresentacionDTO toDtoNombre(Presentacion presentacion);
}
