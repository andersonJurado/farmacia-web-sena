package org.norn.farmacia.repository;

import org.norn.farmacia.domain.VentaProducto;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the VentaProducto entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VentaProductoRepository extends JpaRepository<VentaProducto, Long>, JpaSpecificationExecutor<VentaProducto> {}
