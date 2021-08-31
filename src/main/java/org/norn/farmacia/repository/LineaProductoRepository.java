package org.norn.farmacia.repository;

import org.norn.farmacia.domain.LineaProducto;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the LineaProducto entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LineaProductoRepository extends JpaRepository<LineaProducto, Long>, JpaSpecificationExecutor<LineaProducto> {}
