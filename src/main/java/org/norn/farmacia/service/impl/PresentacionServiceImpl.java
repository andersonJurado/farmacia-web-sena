package org.norn.farmacia.service.impl;

import java.util.Optional;
import org.norn.farmacia.domain.Presentacion;
import org.norn.farmacia.repository.PresentacionRepository;
import org.norn.farmacia.service.PresentacionService;
import org.norn.farmacia.service.dto.PresentacionDTO;
import org.norn.farmacia.service.mapper.PresentacionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Presentacion}.
 */
@Service
@Transactional
public class PresentacionServiceImpl implements PresentacionService {

    private final Logger log = LoggerFactory.getLogger(PresentacionServiceImpl.class);

    private final PresentacionRepository presentacionRepository;

    private final PresentacionMapper presentacionMapper;

    public PresentacionServiceImpl(PresentacionRepository presentacionRepository, PresentacionMapper presentacionMapper) {
        this.presentacionRepository = presentacionRepository;
        this.presentacionMapper = presentacionMapper;
    }

    @Override
    public PresentacionDTO save(PresentacionDTO presentacionDTO) {
        log.debug("Request to save Presentacion : {}", presentacionDTO);
        Presentacion presentacion = presentacionMapper.toEntity(presentacionDTO);
        presentacion = presentacionRepository.save(presentacion);
        return presentacionMapper.toDto(presentacion);
    }

    @Override
    public Optional<PresentacionDTO> partialUpdate(PresentacionDTO presentacionDTO) {
        log.debug("Request to partially update Presentacion : {}", presentacionDTO);

        return presentacionRepository
            .findById(presentacionDTO.getId())
            .map(
                existingPresentacion -> {
                    presentacionMapper.partialUpdate(existingPresentacion, presentacionDTO);

                    return existingPresentacion;
                }
            )
            .map(presentacionRepository::save)
            .map(presentacionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PresentacionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Presentacions");
        return presentacionRepository.findAll(pageable).map(presentacionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PresentacionDTO> findOne(Long id) {
        log.debug("Request to get Presentacion : {}", id);
        return presentacionRepository.findById(id).map(presentacionMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Presentacion : {}", id);
        presentacionRepository.deleteById(id);
    }
}
