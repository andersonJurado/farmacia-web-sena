package org.norn.farmacia.service;

import java.util.Optional;
import org.norn.farmacia.service.dto.CompraProductoDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link org.norn.farmacia.domain.CompraProducto}.
 */
public interface CompraProductoService {
    /**
     * Save a compraProducto.
     *
     * @param compraProductoDTO the entity to save.
     * @return the persisted entity.
     */
    CompraProductoDTO save(CompraProductoDTO compraProductoDTO);

    /**
     * Partially updates a compraProducto.
     *
     * @param compraProductoDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CompraProductoDTO> partialUpdate(CompraProductoDTO compraProductoDTO);

    /**
     * Get all the compraProductos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CompraProductoDTO> findAll(Pageable pageable);

    /**
     * Get the "id" compraProducto.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CompraProductoDTO> findOne(Long id);

    /**
     * Delete the "id" compraProducto.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
