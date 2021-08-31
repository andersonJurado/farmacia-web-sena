package org.norn.farmacia.repository;

import org.norn.farmacia.domain.CompraProducto;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the CompraProducto entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CompraProductoRepository extends JpaRepository<CompraProducto, Long>, JpaSpecificationExecutor<CompraProducto> {}
