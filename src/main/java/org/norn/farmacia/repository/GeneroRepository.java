package org.norn.farmacia.repository;

import org.norn.farmacia.domain.Genero;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Genero entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GeneroRepository extends JpaRepository<Genero, Long>, JpaSpecificationExecutor<Genero> {}
