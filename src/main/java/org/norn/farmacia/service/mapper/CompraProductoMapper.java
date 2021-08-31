package org.norn.farmacia.service.mapper;

import org.mapstruct.*;
import org.norn.farmacia.domain.*;
import org.norn.farmacia.service.dto.CompraProductoDTO;

/**
 * Mapper for the entity {@link CompraProducto} and its DTO {@link CompraProductoDTO}.
 */
@Mapper(componentModel = "spring", uses = { ProductoMapper.class, CompraMapper.class })
public interface CompraProductoMapper extends EntityMapper<CompraProductoDTO, CompraProducto> {
    @Mapping(target = "producto", source = "producto", qualifiedByName = "nombreProducto")
    @Mapping(target = "compra", source = "compra", qualifiedByName = "nroFactura")
    CompraProductoDTO toDto(CompraProducto s);
}
