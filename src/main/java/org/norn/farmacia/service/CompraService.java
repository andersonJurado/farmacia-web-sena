package org.norn.farmacia.service;

import java.util.Optional;
import org.norn.farmacia.service.dto.CompraDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link org.norn.farmacia.domain.Compra}.
 */
public interface CompraService {
    /**
     * Save a compra.
     *
     * @param compraDTO the entity to save.
     * @return the persisted entity.
     */
    CompraDTO save(CompraDTO compraDTO);

    /**
     * Partially updates a compra.
     *
     * @param compraDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CompraDTO> partialUpdate(CompraDTO compraDTO);

    /**
     * Get all the compras.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CompraDTO> findAll(Pageable pageable);

    /**
     * Get the "id" compra.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CompraDTO> findOne(Long id);

    /**
     * Delete the "id" compra.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
