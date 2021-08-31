package org.norn.farmacia.service;

import java.util.Optional;
import org.norn.farmacia.service.dto.LineaProductoDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link org.norn.farmacia.domain.LineaProducto}.
 */
public interface LineaProductoService {
    /**
     * Save a lineaProducto.
     *
     * @param lineaProductoDTO the entity to save.
     * @return the persisted entity.
     */
    LineaProductoDTO save(LineaProductoDTO lineaProductoDTO);

    /**
     * Partially updates a lineaProducto.
     *
     * @param lineaProductoDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<LineaProductoDTO> partialUpdate(LineaProductoDTO lineaProductoDTO);

    /**
     * Get all the lineaProductos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<LineaProductoDTO> findAll(Pageable pageable);

    /**
     * Get the "id" lineaProducto.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<LineaProductoDTO> findOne(Long id);

    /**
     * Delete the "id" lineaProducto.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
