package org.norn.farmacia.service;

import java.util.List;
import javax.persistence.criteria.JoinType;
import org.norn.farmacia.domain.*; // for static metamodels
import org.norn.farmacia.domain.Producto;
import org.norn.farmacia.repository.ProductoRepository;
import org.norn.farmacia.service.criteria.ProductoCriteria;
import org.norn.farmacia.service.dto.ProductoDTO;
import org.norn.farmacia.service.mapper.ProductoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Producto} entities in the database.
 * The main input is a {@link ProductoCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ProductoDTO} or a {@link Page} of {@link ProductoDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ProductoQueryService extends QueryService<Producto> {

    private final Logger log = LoggerFactory.getLogger(ProductoQueryService.class);

    private final ProductoRepository productoRepository;

    private final ProductoMapper productoMapper;

    public ProductoQueryService(ProductoRepository productoRepository, ProductoMapper productoMapper) {
        this.productoRepository = productoRepository;
        this.productoMapper = productoMapper;
    }

    /**
     * Return a {@link List} of {@link ProductoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ProductoDTO> findByCriteria(ProductoCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Producto> specification = createSpecification(criteria);
        return productoMapper.toDto(productoRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ProductoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ProductoDTO> findByCriteria(ProductoCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Producto> specification = createSpecification(criteria);
        return productoRepository.findAll(specification, page).map(productoMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ProductoCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Producto> specification = createSpecification(criteria);
        return productoRepository.count(specification);
    }

    /**
     * Function to convert {@link ProductoCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Producto> createSpecification(ProductoCriteria criteria) {
        Specification<Producto> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Producto_.id));
            }
            if (criteria.getNombreProducto() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNombreProducto(), Producto_.nombreProducto));
            }
            if (criteria.getCantidad() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCantidad(), Producto_.cantidad));
            }
            if (criteria.getIva() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getIva(), Producto_.iva));
            }
            if (criteria.getPrecioUdsVenta() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPrecioUdsVenta(), Producto_.precioUdsVenta));
            }
            if (criteria.getMargenDeGanancia() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMargenDeGanancia(), Producto_.margenDeGanancia));
            }
            if (criteria.getInvima() != null) {
                specification = specification.and(buildStringSpecification(criteria.getInvima(), Producto_.invima));
            }
            if (criteria.getPresentacionId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPresentacionId(),
                            root -> root.join(Producto_.presentacion, JoinType.LEFT).get(Presentacion_.id)
                        )
                    );
            }
            if (criteria.getLaboratorioId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getLaboratorioId(),
                            root -> root.join(Producto_.laboratorio, JoinType.LEFT).get(Laboratorio_.id)
                        )
                    );
            }
            if (criteria.getLineaProductoId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getLineaProductoId(),
                            root -> root.join(Producto_.lineaProducto, JoinType.LEFT).get(LineaProducto_.id)
                        )
                    );
            }
            if (criteria.getCompraProductoId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCompraProductoId(),
                            root -> root.join(Producto_.compraProductos, JoinType.LEFT).get(CompraProducto_.id)
                        )
                    );
            }
            if (criteria.getVentaProductoId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getVentaProductoId(),
                            root -> root.join(Producto_.ventaProductos, JoinType.LEFT).get(VentaProducto_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
