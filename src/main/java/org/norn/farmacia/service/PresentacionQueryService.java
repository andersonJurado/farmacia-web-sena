package org.norn.farmacia.service;

import java.util.List;
import javax.persistence.criteria.JoinType;
import org.norn.farmacia.domain.*; // for static metamodels
import org.norn.farmacia.domain.Presentacion;
import org.norn.farmacia.repository.PresentacionRepository;
import org.norn.farmacia.service.criteria.PresentacionCriteria;
import org.norn.farmacia.service.dto.PresentacionDTO;
import org.norn.farmacia.service.mapper.PresentacionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Presentacion} entities in the database.
 * The main input is a {@link PresentacionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PresentacionDTO} or a {@link Page} of {@link PresentacionDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PresentacionQueryService extends QueryService<Presentacion> {

    private final Logger log = LoggerFactory.getLogger(PresentacionQueryService.class);

    private final PresentacionRepository presentacionRepository;

    private final PresentacionMapper presentacionMapper;

    public PresentacionQueryService(PresentacionRepository presentacionRepository, PresentacionMapper presentacionMapper) {
        this.presentacionRepository = presentacionRepository;
        this.presentacionMapper = presentacionMapper;
    }

    /**
     * Return a {@link List} of {@link PresentacionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PresentacionDTO> findByCriteria(PresentacionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Presentacion> specification = createSpecification(criteria);
        return presentacionMapper.toDto(presentacionRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link PresentacionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PresentacionDTO> findByCriteria(PresentacionCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Presentacion> specification = createSpecification(criteria);
        return presentacionRepository.findAll(specification, page).map(presentacionMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PresentacionCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Presentacion> specification = createSpecification(criteria);
        return presentacionRepository.count(specification);
    }

    /**
     * Function to convert {@link PresentacionCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Presentacion> createSpecification(PresentacionCriteria criteria) {
        Specification<Presentacion> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Presentacion_.id));
            }
            if (criteria.getNombre() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNombre(), Presentacion_.nombre));
            }
            if (criteria.getProductoId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getProductoId(),
                            root -> root.join(Presentacion_.productos, JoinType.LEFT).get(Producto_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
