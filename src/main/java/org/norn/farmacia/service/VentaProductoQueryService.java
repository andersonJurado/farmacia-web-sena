package org.norn.farmacia.service;

import java.util.List;
import javax.persistence.criteria.JoinType;
import org.norn.farmacia.domain.*; // for static metamodels
import org.norn.farmacia.domain.VentaProducto;
import org.norn.farmacia.repository.VentaProductoRepository;
import org.norn.farmacia.service.criteria.VentaProductoCriteria;
import org.norn.farmacia.service.dto.VentaProductoDTO;
import org.norn.farmacia.service.mapper.VentaProductoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link VentaProducto} entities in the database.
 * The main input is a {@link VentaProductoCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link VentaProductoDTO} or a {@link Page} of {@link VentaProductoDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class VentaProductoQueryService extends QueryService<VentaProducto> {

    private final Logger log = LoggerFactory.getLogger(VentaProductoQueryService.class);

    private final VentaProductoRepository ventaProductoRepository;

    private final VentaProductoMapper ventaProductoMapper;

    public VentaProductoQueryService(VentaProductoRepository ventaProductoRepository, VentaProductoMapper ventaProductoMapper) {
        this.ventaProductoRepository = ventaProductoRepository;
        this.ventaProductoMapper = ventaProductoMapper;
    }

    /**
     * Return a {@link List} of {@link VentaProductoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<VentaProductoDTO> findByCriteria(VentaProductoCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<VentaProducto> specification = createSpecification(criteria);
        return ventaProductoMapper.toDto(ventaProductoRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link VentaProductoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<VentaProductoDTO> findByCriteria(VentaProductoCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<VentaProducto> specification = createSpecification(criteria);
        return ventaProductoRepository.findAll(specification, page).map(ventaProductoMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(VentaProductoCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<VentaProducto> specification = createSpecification(criteria);
        return ventaProductoRepository.count(specification);
    }

    /**
     * Function to convert {@link VentaProductoCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<VentaProducto> createSpecification(VentaProductoCriteria criteria) {
        Specification<VentaProducto> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), VentaProducto_.id));
            }
            if (criteria.getCantidad() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCantidad(), VentaProducto_.cantidad));
            }
            if (criteria.getTotal() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTotal(), VentaProducto_.total));
            }
            if (criteria.getProductoId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getProductoId(),
                            root -> root.join(VentaProducto_.producto, JoinType.LEFT).get(Producto_.id)
                        )
                    );
            }
            if (criteria.getVentaId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getVentaId(), root -> root.join(VentaProducto_.venta, JoinType.LEFT).get(Venta_.id))
                    );
            }
        }
        return specification;
    }
}
