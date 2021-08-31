package org.norn.farmacia.service.mapper;

import org.mapstruct.*;
import org.norn.farmacia.domain.*;
import org.norn.farmacia.service.dto.LineaProductoDTO;

/**
 * Mapper for the entity {@link LineaProducto} and its DTO {@link LineaProductoDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface LineaProductoMapper extends EntityMapper<LineaProductoDTO, LineaProducto> {
    @Named("nombre")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nombre", source = "nombre")
    LineaProductoDTO toDtoNombre(LineaProducto lineaProducto);
}
