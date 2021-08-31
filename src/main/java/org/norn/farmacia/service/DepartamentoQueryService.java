package org.norn.farmacia.service;

import java.util.List;
import javax.persistence.criteria.JoinType;
import org.norn.farmacia.domain.*; // for static metamodels
import org.norn.farmacia.domain.Departamento;
import org.norn.farmacia.repository.DepartamentoRepository;
import org.norn.farmacia.service.criteria.DepartamentoCriteria;
import org.norn.farmacia.service.dto.DepartamentoDTO;
import org.norn.farmacia.service.mapper.DepartamentoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Departamento} entities in the database.
 * The main input is a {@link DepartamentoCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link DepartamentoDTO} or a {@link Page} of {@link DepartamentoDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DepartamentoQueryService extends QueryService<Departamento> {

    private final Logger log = LoggerFactory.getLogger(DepartamentoQueryService.class);

    private final DepartamentoRepository departamentoRepository;

    private final DepartamentoMapper departamentoMapper;

    public DepartamentoQueryService(DepartamentoRepository departamentoRepository, DepartamentoMapper departamentoMapper) {
        this.departamentoRepository = departamentoRepository;
        this.departamentoMapper = departamentoMapper;
    }

    /**
     * Return a {@link List} of {@link DepartamentoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<DepartamentoDTO> findByCriteria(DepartamentoCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Departamento> specification = createSpecification(criteria);
        return departamentoMapper.toDto(departamentoRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link DepartamentoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DepartamentoDTO> findByCriteria(DepartamentoCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Departamento> specification = createSpecification(criteria);
        return departamentoRepository.findAll(specification, page).map(departamentoMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DepartamentoCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Departamento> specification = createSpecification(criteria);
        return departamentoRepository.count(specification);
    }

    /**
     * Function to convert {@link DepartamentoCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Departamento> createSpecification(DepartamentoCriteria criteria) {
        Specification<Departamento> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Departamento_.id));
            }
            if (criteria.getNombre() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNombre(), Departamento_.nombre));
            }
            if (criteria.getMunicipioId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getMunicipioId(),
                            root -> root.join(Departamento_.municipios, JoinType.LEFT).get(Municipio_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
