package org.norn.farmacia.service.mapper;

import org.mapstruct.*;
import org.norn.farmacia.domain.*;
import org.norn.farmacia.service.dto.ClienteDTO;

/**
 * Mapper for the entity {@link Cliente} and its DTO {@link ClienteDTO}.
 */
@Mapper(componentModel = "spring", uses = { MunicipioMapper.class, GeneroMapper.class })
public interface ClienteMapper extends EntityMapper<ClienteDTO, Cliente> {
    @Mapping(target = "municipio", source = "municipio", qualifiedByName = "nombre")
    @Mapping(target = "genero", source = "genero", qualifiedByName = "nombre")
    ClienteDTO toDto(Cliente s);

    @Named("primerNombre")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "primerNombre", source = "primerNombre")
    ClienteDTO toDtoPrimerNombre(Cliente cliente);
}
