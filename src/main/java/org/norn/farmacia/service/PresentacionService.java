package org.norn.farmacia.service;

import java.util.Optional;
import org.norn.farmacia.service.dto.PresentacionDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link org.norn.farmacia.domain.Presentacion}.
 */
public interface PresentacionService {
    /**
     * Save a presentacion.
     *
     * @param presentacionDTO the entity to save.
     * @return the persisted entity.
     */
    PresentacionDTO save(PresentacionDTO presentacionDTO);

    /**
     * Partially updates a presentacion.
     *
     * @param presentacionDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PresentacionDTO> partialUpdate(PresentacionDTO presentacionDTO);

    /**
     * Get all the presentacions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PresentacionDTO> findAll(Pageable pageable);

    /**
     * Get the "id" presentacion.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PresentacionDTO> findOne(Long id);

    /**
     * Delete the "id" presentacion.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
