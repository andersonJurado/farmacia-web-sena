package org.norn.farmacia.repository;

import org.norn.farmacia.domain.Presentacion;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Presentacion entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PresentacionRepository extends JpaRepository<Presentacion, Long>, JpaSpecificationExecutor<Presentacion> {}
