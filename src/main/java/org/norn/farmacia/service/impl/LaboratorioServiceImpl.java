package org.norn.farmacia.service.impl;

import java.util.Optional;
import org.norn.farmacia.domain.Laboratorio;
import org.norn.farmacia.repository.LaboratorioRepository;
import org.norn.farmacia.service.LaboratorioService;
import org.norn.farmacia.service.dto.LaboratorioDTO;
import org.norn.farmacia.service.mapper.LaboratorioMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Laboratorio}.
 */
@Service
@Transactional
public class LaboratorioServiceImpl implements LaboratorioService {

    private final Logger log = LoggerFactory.getLogger(LaboratorioServiceImpl.class);

    private final LaboratorioRepository laboratorioRepository;

    private final LaboratorioMapper laboratorioMapper;

    public LaboratorioServiceImpl(LaboratorioRepository laboratorioRepository, LaboratorioMapper laboratorioMapper) {
        this.laboratorioRepository = laboratorioRepository;
        this.laboratorioMapper = laboratorioMapper;
    }

    @Override
    public LaboratorioDTO save(LaboratorioDTO laboratorioDTO) {
        log.debug("Request to save Laboratorio : {}", laboratorioDTO);
        Laboratorio laboratorio = laboratorioMapper.toEntity(laboratorioDTO);
        laboratorio = laboratorioRepository.save(laboratorio);
        return laboratorioMapper.toDto(laboratorio);
    }

    @Override
    public Optional<LaboratorioDTO> partialUpdate(LaboratorioDTO laboratorioDTO) {
        log.debug("Request to partially update Laboratorio : {}", laboratorioDTO);

        return laboratorioRepository
            .findById(laboratorioDTO.getId())
            .map(
                existingLaboratorio -> {
                    laboratorioMapper.partialUpdate(existingLaboratorio, laboratorioDTO);

                    return existingLaboratorio;
                }
            )
            .map(laboratorioRepository::save)
            .map(laboratorioMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LaboratorioDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Laboratorios");
        return laboratorioRepository.findAll(pageable).map(laboratorioMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LaboratorioDTO> findOne(Long id) {
        log.debug("Request to get Laboratorio : {}", id);
        return laboratorioRepository.findById(id).map(laboratorioMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Laboratorio : {}", id);
        laboratorioRepository.deleteById(id);
    }
}
