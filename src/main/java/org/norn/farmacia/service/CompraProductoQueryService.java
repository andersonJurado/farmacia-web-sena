package org.norn.farmacia.service;

import java.util.List;
import javax.persistence.criteria.JoinType;
import org.norn.farmacia.domain.*; // for static metamodels
import org.norn.farmacia.domain.CompraProducto;
import org.norn.farmacia.repository.CompraProductoRepository;
import org.norn.farmacia.service.criteria.CompraProductoCriteria;
import org.norn.farmacia.service.dto.CompraProductoDTO;
import org.norn.farmacia.service.mapper.CompraProductoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link CompraProducto} entities in the database.
 * The main input is a {@link CompraProductoCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CompraProductoDTO} or a {@link Page} of {@link CompraProductoDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CompraProductoQueryService extends QueryService<CompraProducto> {

    private final Logger log = LoggerFactory.getLogger(CompraProductoQueryService.class);

    private final CompraProductoRepository compraProductoRepository;

    private final CompraProductoMapper compraProductoMapper;

    public CompraProductoQueryService(CompraProductoRepository compraProductoRepository, CompraProductoMapper compraProductoMapper) {
        this.compraProductoRepository = compraProductoRepository;
        this.compraProductoMapper = compraProductoMapper;
    }

    /**
     * Return a {@link List} of {@link CompraProductoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CompraProductoDTO> findByCriteria(CompraProductoCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<CompraProducto> specification = createSpecification(criteria);
        return compraProductoMapper.toDto(compraProductoRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CompraProductoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CompraProductoDTO> findByCriteria(CompraProductoCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CompraProducto> specification = createSpecification(criteria);
        return compraProductoRepository.findAll(specification, page).map(compraProductoMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CompraProductoCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<CompraProducto> specification = createSpecification(criteria);
        return compraProductoRepository.count(specification);
    }

    /**
     * Function to convert {@link CompraProductoCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CompraProducto> createSpecification(CompraProductoCriteria criteria) {
        Specification<CompraProducto> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), CompraProducto_.id));
            }
            if (criteria.getCantidadUds() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCantidadUds(), CompraProducto_.cantidadUds));
            }
            if (criteria.getPrecioUdsCompra() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPrecioUdsCompra(), CompraProducto_.precioUdsCompra));
            }
            if (criteria.getSubTotal() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSubTotal(), CompraProducto_.subTotal));
            }
            if (criteria.getIva() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getIva(), CompraProducto_.iva));
            }
            if (criteria.getTotal() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTotal(), CompraProducto_.total));
            }
            if (criteria.getFechaVencimiento() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getFechaVencimiento(), CompraProducto_.fechaVencimiento));
            }
            if (criteria.getLote() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLote(), CompraProducto_.lote));
            }
            if (criteria.getProductoId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getProductoId(),
                            root -> root.join(CompraProducto_.producto, JoinType.LEFT).get(Producto_.id)
                        )
                    );
            }
            if (criteria.getCompraId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getCompraId(), root -> root.join(CompraProducto_.compra, JoinType.LEFT).get(Compra_.id))
                    );
            }
        }
        return specification;
    }
}
