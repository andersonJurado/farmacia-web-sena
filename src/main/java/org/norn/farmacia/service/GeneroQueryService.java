package org.norn.farmacia.service;

import java.util.List;
import javax.persistence.criteria.JoinType;
import org.norn.farmacia.domain.*; // for static metamodels
import org.norn.farmacia.domain.Genero;
import org.norn.farmacia.repository.GeneroRepository;
import org.norn.farmacia.service.criteria.GeneroCriteria;
import org.norn.farmacia.service.dto.GeneroDTO;
import org.norn.farmacia.service.mapper.GeneroMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Genero} entities in the database.
 * The main input is a {@link GeneroCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link GeneroDTO} or a {@link Page} of {@link GeneroDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class GeneroQueryService extends QueryService<Genero> {

    private final Logger log = LoggerFactory.getLogger(GeneroQueryService.class);

    private final GeneroRepository generoRepository;

    private final GeneroMapper generoMapper;

    public GeneroQueryService(GeneroRepository generoRepository, GeneroMapper generoMapper) {
        this.generoRepository = generoRepository;
        this.generoMapper = generoMapper;
    }

    /**
     * Return a {@link List} of {@link GeneroDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<GeneroDTO> findByCriteria(GeneroCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Genero> specification = createSpecification(criteria);
        return generoMapper.toDto(generoRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link GeneroDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<GeneroDTO> findByCriteria(GeneroCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Genero> specification = createSpecification(criteria);
        return generoRepository.findAll(specification, page).map(generoMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(GeneroCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Genero> specification = createSpecification(criteria);
        return generoRepository.count(specification);
    }

    /**
     * Function to convert {@link GeneroCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Genero> createSpecification(GeneroCriteria criteria) {
        Specification<Genero> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Genero_.id));
            }
            if (criteria.getNombre() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNombre(), Genero_.nombre));
            }
            if (criteria.getClienteId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getClienteId(), root -> root.join(Genero_.clientes, JoinType.LEFT).get(Cliente_.id))
                    );
            }
        }
        return specification;
    }
}
