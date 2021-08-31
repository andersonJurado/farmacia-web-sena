package org.norn.farmacia.service;

import java.util.Optional;
import org.norn.farmacia.service.dto.LaboratorioDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link org.norn.farmacia.domain.Laboratorio}.
 */
public interface LaboratorioService {
    /**
     * Save a laboratorio.
     *
     * @param laboratorioDTO the entity to save.
     * @return the persisted entity.
     */
    LaboratorioDTO save(LaboratorioDTO laboratorioDTO);

    /**
     * Partially updates a laboratorio.
     *
     * @param laboratorioDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<LaboratorioDTO> partialUpdate(LaboratorioDTO laboratorioDTO);

    /**
     * Get all the laboratorios.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<LaboratorioDTO> findAll(Pageable pageable);

    /**
     * Get the "id" laboratorio.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<LaboratorioDTO> findOne(Long id);

    /**
     * Delete the "id" laboratorio.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
