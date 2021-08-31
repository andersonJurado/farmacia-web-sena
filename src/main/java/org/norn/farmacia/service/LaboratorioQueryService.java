package org.norn.farmacia.service;

import java.util.List;
import javax.persistence.criteria.JoinType;
import org.norn.farmacia.domain.*; // for static metamodels
import org.norn.farmacia.domain.Laboratorio;
import org.norn.farmacia.repository.LaboratorioRepository;
import org.norn.farmacia.service.criteria.LaboratorioCriteria;
import org.norn.farmacia.service.dto.LaboratorioDTO;
import org.norn.farmacia.service.mapper.LaboratorioMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Laboratorio} entities in the database.
 * The main input is a {@link LaboratorioCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link LaboratorioDTO} or a {@link Page} of {@link LaboratorioDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class LaboratorioQueryService extends QueryService<Laboratorio> {

    private final Logger log = LoggerFactory.getLogger(LaboratorioQueryService.class);

    private final LaboratorioRepository laboratorioRepository;

    private final LaboratorioMapper laboratorioMapper;

    public LaboratorioQueryService(LaboratorioRepository laboratorioRepository, LaboratorioMapper laboratorioMapper) {
        this.laboratorioRepository = laboratorioRepository;
        this.laboratorioMapper = laboratorioMapper;
    }

    /**
     * Return a {@link List} of {@link LaboratorioDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<LaboratorioDTO> findByCriteria(LaboratorioCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Laboratorio> specification = createSpecification(criteria);
        return laboratorioMapper.toDto(laboratorioRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link LaboratorioDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<LaboratorioDTO> findByCriteria(LaboratorioCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Laboratorio> specification = createSpecification(criteria);
        return laboratorioRepository.findAll(specification, page).map(laboratorioMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(LaboratorioCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Laboratorio> specification = createSpecification(criteria);
        return laboratorioRepository.count(specification);
    }

    /**
     * Function to convert {@link LaboratorioCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Laboratorio> createSpecification(LaboratorioCriteria criteria) {
        Specification<Laboratorio> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Laboratorio_.id));
            }
            if (criteria.getNombre() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNombre(), Laboratorio_.nombre));
            }
            if (criteria.getProductoId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getProductoId(),
                            root -> root.join(Laboratorio_.productos, JoinType.LEFT).get(Producto_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
