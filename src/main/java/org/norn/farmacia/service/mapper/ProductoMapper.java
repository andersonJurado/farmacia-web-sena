package org.norn.farmacia.service.mapper;

import org.mapstruct.*;
import org.norn.farmacia.domain.*;
import org.norn.farmacia.service.dto.ProductoDTO;

/**
 * Mapper for the entity {@link Producto} and its DTO {@link ProductoDTO}.
 */
@Mapper(componentModel = "spring", uses = { PresentacionMapper.class, LaboratorioMapper.class, LineaProductoMapper.class })
public interface ProductoMapper extends EntityMapper<ProductoDTO, Producto> {
    @Mapping(target = "presentacion", source = "presentacion", qualifiedByName = "nombre")
    @Mapping(target = "laboratorio", source = "laboratorio", qualifiedByName = "nombre")
    @Mapping(target = "lineaProducto", source = "lineaProducto", qualifiedByName = "nombre")
    ProductoDTO toDto(Producto s);

    @Named("nombreProducto")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nombreProducto", source = "nombreProducto")
    ProductoDTO toDtoNombreProducto(Producto producto);
}
