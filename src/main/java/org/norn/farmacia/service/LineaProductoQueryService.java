package org.norn.farmacia.service;

import java.util.List;
import javax.persistence.criteria.JoinType;
import org.norn.farmacia.domain.*; // for static metamodels
import org.norn.farmacia.domain.LineaProducto;
import org.norn.farmacia.repository.LineaProductoRepository;
import org.norn.farmacia.service.criteria.LineaProductoCriteria;
import org.norn.farmacia.service.dto.LineaProductoDTO;
import org.norn.farmacia.service.mapper.LineaProductoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link LineaProducto} entities in the database.
 * The main input is a {@link LineaProductoCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link LineaProductoDTO} or a {@link Page} of {@link LineaProductoDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class LineaProductoQueryService extends QueryService<LineaProducto> {

    private final Logger log = LoggerFactory.getLogger(LineaProductoQueryService.class);

    private final LineaProductoRepository lineaProductoRepository;

    private final LineaProductoMapper lineaProductoMapper;

    public LineaProductoQueryService(LineaProductoRepository lineaProductoRepository, LineaProductoMapper lineaProductoMapper) {
        this.lineaProductoRepository = lineaProductoRepository;
        this.lineaProductoMapper = lineaProductoMapper;
    }

    /**
     * Return a {@link List} of {@link LineaProductoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<LineaProductoDTO> findByCriteria(LineaProductoCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<LineaProducto> specification = createSpecification(criteria);
        return lineaProductoMapper.toDto(lineaProductoRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link LineaProductoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<LineaProductoDTO> findByCriteria(LineaProductoCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<LineaProducto> specification = createSpecification(criteria);
        return lineaProductoRepository.findAll(specification, page).map(lineaProductoMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(LineaProductoCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<LineaProducto> specification = createSpecification(criteria);
        return lineaProductoRepository.count(specification);
    }

    /**
     * Function to convert {@link LineaProductoCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<LineaProducto> createSpecification(LineaProductoCriteria criteria) {
        Specification<LineaProducto> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), LineaProducto_.id));
            }
            if (criteria.getNombre() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNombre(), LineaProducto_.nombre));
            }
            if (criteria.getProductoId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getProductoId(),
                            root -> root.join(LineaProducto_.productos, JoinType.LEFT).get(Producto_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
