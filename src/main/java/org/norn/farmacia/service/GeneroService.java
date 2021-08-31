package org.norn.farmacia.service;

import java.util.Optional;
import org.norn.farmacia.service.dto.GeneroDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link org.norn.farmacia.domain.Genero}.
 */
public interface GeneroService {
    /**
     * Save a genero.
     *
     * @param generoDTO the entity to save.
     * @return the persisted entity.
     */
    GeneroDTO save(GeneroDTO generoDTO);

    /**
     * Partially updates a genero.
     *
     * @param generoDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<GeneroDTO> partialUpdate(GeneroDTO generoDTO);

    /**
     * Get all the generos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<GeneroDTO> findAll(Pageable pageable);

    /**
     * Get the "id" genero.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<GeneroDTO> findOne(Long id);

    /**
     * Delete the "id" genero.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
