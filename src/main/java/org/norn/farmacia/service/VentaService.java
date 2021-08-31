package org.norn.farmacia.service;

import java.util.Optional;
import org.norn.farmacia.service.dto.VentaDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link org.norn.farmacia.domain.Venta}.
 */
public interface VentaService {
    /**
     * Save a venta.
     *
     * @param ventaDTO the entity to save.
     * @return the persisted entity.
     */
    VentaDTO save(VentaDTO ventaDTO);

    /**
     * Partially updates a venta.
     *
     * @param ventaDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<VentaDTO> partialUpdate(VentaDTO ventaDTO);

    /**
     * Get all the ventas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<VentaDTO> findAll(Pageable pageable);

    /**
     * Get the "id" venta.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<VentaDTO> findOne(Long id);

    /**
     * Delete the "id" venta.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
