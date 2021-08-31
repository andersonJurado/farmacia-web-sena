package org.norn.farmacia.service;

import java.util.Optional;
import org.norn.farmacia.service.dto.VentaProductoDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link org.norn.farmacia.domain.VentaProducto}.
 */
public interface VentaProductoService {
    /**
     * Save a ventaProducto.
     *
     * @param ventaProductoDTO the entity to save.
     * @return the persisted entity.
     */
    VentaProductoDTO save(VentaProductoDTO ventaProductoDTO);

    /**
     * Partially updates a ventaProducto.
     *
     * @param ventaProductoDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<VentaProductoDTO> partialUpdate(VentaProductoDTO ventaProductoDTO);

    /**
     * Get all the ventaProductos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<VentaProductoDTO> findAll(Pageable pageable);

    /**
     * Get the "id" ventaProducto.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<VentaProductoDTO> findOne(Long id);

    /**
     * Delete the "id" ventaProducto.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
