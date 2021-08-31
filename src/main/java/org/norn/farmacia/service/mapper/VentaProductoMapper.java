package org.norn.farmacia.service.mapper;

import org.mapstruct.*;
import org.norn.farmacia.domain.*;
import org.norn.farmacia.service.dto.VentaProductoDTO;

/**
 * Mapper for the entity {@link VentaProducto} and its DTO {@link VentaProductoDTO}.
 */
@Mapper(componentModel = "spring", uses = { ProductoMapper.class, VentaMapper.class })
public interface VentaProductoMapper extends EntityMapper<VentaProductoDTO, VentaProducto> {
    @Mapping(target = "producto", source = "producto", qualifiedByName = "nombreProducto")
    @Mapping(target = "venta", source = "venta", qualifiedByName = "nroFactura")
    VentaProductoDTO toDto(VentaProducto s);
}
