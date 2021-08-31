package org.norn.farmacia.service.impl;

import java.util.Optional;
import org.norn.farmacia.domain.Municipio;
import org.norn.farmacia.repository.MunicipioRepository;
import org.norn.farmacia.service.MunicipioService;
import org.norn.farmacia.service.dto.MunicipioDTO;
import org.norn.farmacia.service.mapper.MunicipioMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Municipio}.
 */
@Service
@Transactional
public class MunicipioServiceImpl implements MunicipioService {

    private final Logger log = LoggerFactory.getLogger(MunicipioServiceImpl.class);

    private final MunicipioRepository municipioRepository;

    private final MunicipioMapper municipioMapper;

    public MunicipioServiceImpl(MunicipioRepository municipioRepository, MunicipioMapper municipioMapper) {
        this.municipioRepository = municipioRepository;
        this.municipioMapper = municipioMapper;
    }

    @Override
    public MunicipioDTO save(MunicipioDTO municipioDTO) {
        log.debug("Request to save Municipio : {}", municipioDTO);
        Municipio municipio = municipioMapper.toEntity(municipioDTO);
        municipio = municipioRepository.save(municipio);
        return municipioMapper.toDto(municipio);
    }

    @Override
    public Optional<MunicipioDTO> partialUpdate(MunicipioDTO municipioDTO) {
        log.debug("Request to partially update Municipio : {}", municipioDTO);

        return municipioRepository
            .findById(municipioDTO.getId())
            .map(
                existingMunicipio -> {
                    municipioMapper.partialUpdate(existingMunicipio, municipioDTO);

                    return existingMunicipio;
                }
            )
            .map(municipioRepository::save)
            .map(municipioMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MunicipioDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Municipios");
        return municipioRepository.findAll(pageable).map(municipioMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MunicipioDTO> findOne(Long id) {
        log.debug("Request to get Municipio : {}", id);
        return municipioRepository.findById(id).map(municipioMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Municipio : {}", id);
        municipioRepository.deleteById(id);
    }
}
