package org.norn.farmacia.service.mapper;

import org.mapstruct.*;
import org.norn.farmacia.domain.*;
import org.norn.farmacia.service.dto.ProveedorDTO;

/**
 * Mapper for the entity {@link Proveedor} and its DTO {@link ProveedorDTO}.
 */
@Mapper(componentModel = "spring", uses = { MunicipioMapper.class })
public interface ProveedorMapper extends EntityMapper<ProveedorDTO, Proveedor> {
    @Mapping(target = "municpio", source = "municpio", qualifiedByName = "nombre")
    ProveedorDTO toDto(Proveedor s);

    @Named("nombre")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nombre", source = "nombre")
    ProveedorDTO toDtoNombre(Proveedor proveedor);
}
