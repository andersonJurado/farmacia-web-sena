package org.norn.farmacia.service.impl;

import java.util.Optional;
import org.norn.farmacia.domain.Genero;
import org.norn.farmacia.repository.GeneroRepository;
import org.norn.farmacia.service.GeneroService;
import org.norn.farmacia.service.dto.GeneroDTO;
import org.norn.farmacia.service.mapper.GeneroMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Genero}.
 */
@Service
@Transactional
public class GeneroServiceImpl implements GeneroService {

    private final Logger log = LoggerFactory.getLogger(GeneroServiceImpl.class);

    private final GeneroRepository generoRepository;

    private final GeneroMapper generoMapper;

    public GeneroServiceImpl(GeneroRepository generoRepository, GeneroMapper generoMapper) {
        this.generoRepository = generoRepository;
        this.generoMapper = generoMapper;
    }

    @Override
    public GeneroDTO save(GeneroDTO generoDTO) {
        log.debug("Request to save Genero : {}", generoDTO);
        Genero genero = generoMapper.toEntity(generoDTO);
        genero = generoRepository.save(genero);
        return generoMapper.toDto(genero);
    }

    @Override
    public Optional<GeneroDTO> partialUpdate(GeneroDTO generoDTO) {
        log.debug("Request to partially update Genero : {}", generoDTO);

        return generoRepository
            .findById(generoDTO.getId())
            .map(
                existingGenero -> {
                    generoMapper.partialUpdate(existingGenero, generoDTO);

                    return existingGenero;
                }
            )
            .map(generoRepository::save)
            .map(generoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<GeneroDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Generos");
        return generoRepository.findAll(pageable).map(generoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<GeneroDTO> findOne(Long id) {
        log.debug("Request to get Genero : {}", id);
        return generoRepository.findById(id).map(generoMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Genero : {}", id);
        generoRepository.deleteById(id);
    }
}
