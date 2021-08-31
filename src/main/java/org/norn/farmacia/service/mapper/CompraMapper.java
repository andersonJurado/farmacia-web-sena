package org.norn.farmacia.service.mapper;

import org.mapstruct.*;
import org.norn.farmacia.domain.*;
import org.norn.farmacia.service.dto.CompraDTO;

/**
 * Mapper for the entity {@link Compra} and its DTO {@link CompraDTO}.
 */
@Mapper(componentModel = "spring", uses = { ProveedorMapper.class })
public interface CompraMapper extends EntityMapper<CompraDTO, Compra> {
    @Mapping(target = "proveedor", source = "proveedor", qualifiedByName = "nombre")
    CompraDTO toDto(Compra s);

    @Named("nroFactura")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nroFactura", source = "nroFactura")
    CompraDTO toDtoNroFactura(Compra compra);
}
