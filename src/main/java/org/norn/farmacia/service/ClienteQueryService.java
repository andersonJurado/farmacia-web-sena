package org.norn.farmacia.service;

import java.util.List;
import javax.persistence.criteria.JoinType;
import org.norn.farmacia.domain.*; // for static metamodels
import org.norn.farmacia.domain.Cliente;
import org.norn.farmacia.repository.ClienteRepository;
import org.norn.farmacia.service.criteria.ClienteCriteria;
import org.norn.farmacia.service.dto.ClienteDTO;
import org.norn.farmacia.service.mapper.ClienteMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Cliente} entities in the database.
 * The main input is a {@link ClienteCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ClienteDTO} or a {@link Page} of {@link ClienteDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ClienteQueryService extends QueryService<Cliente> {

    private final Logger log = LoggerFactory.getLogger(ClienteQueryService.class);

    private final ClienteRepository clienteRepository;

    private final ClienteMapper clienteMapper;

    public ClienteQueryService(ClienteRepository clienteRepository, ClienteMapper clienteMapper) {
        this.clienteRepository = clienteRepository;
        this.clienteMapper = clienteMapper;
    }

    /**
     * Return a {@link List} of {@link ClienteDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ClienteDTO> findByCriteria(ClienteCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Cliente> specification = createSpecification(criteria);
        return clienteMapper.toDto(clienteRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ClienteDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ClienteDTO> findByCriteria(ClienteCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Cliente> specification = createSpecification(criteria);
        return clienteRepository.findAll(specification, page).map(clienteMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ClienteCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Cliente> specification = createSpecification(criteria);
        return clienteRepository.count(specification);
    }

    /**
     * Function to convert {@link ClienteCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Cliente> createSpecification(ClienteCriteria criteria) {
        Specification<Cliente> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Cliente_.id));
            }
            if (criteria.getPrimerNombre() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPrimerNombre(), Cliente_.primerNombre));
            }
            if (criteria.getSegundoNombre() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSegundoNombre(), Cliente_.segundoNombre));
            }
            if (criteria.getPrimerApellido() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPrimerApellido(), Cliente_.primerApellido));
            }
            if (criteria.getSegundoApellido() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSegundoApellido(), Cliente_.segundoApellido));
            }
            if (criteria.getPrimerTelefono() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPrimerTelefono(), Cliente_.primerTelefono));
            }
            if (criteria.getSegundoTelefono() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSegundoTelefono(), Cliente_.segundoTelefono));
            }
            if (criteria.getMunicipioId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getMunicipioId(),
                            root -> root.join(Cliente_.municipio, JoinType.LEFT).get(Municipio_.id)
                        )
                    );
            }
            if (criteria.getGeneroId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getGeneroId(), root -> root.join(Cliente_.genero, JoinType.LEFT).get(Genero_.id))
                    );
            }
            if (criteria.getVentaId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getVentaId(), root -> root.join(Cliente_.ventas, JoinType.LEFT).get(Venta_.id))
                    );
            }
        }
        return specification;
    }
}
