package org.norn.farmacia.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.norn.farmacia.repository.ProveedorRepository;
import org.norn.farmacia.service.ProveedorQueryService;
import org.norn.farmacia.service.ProveedorService;
import org.norn.farmacia.service.criteria.ProveedorCriteria;
import org.norn.farmacia.service.dto.ProveedorDTO;
import org.norn.farmacia.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link org.norn.farmacia.domain.Proveedor}.
 */
@RestController
@RequestMapping("/api")
public class ProveedorResource {

    private final Logger log = LoggerFactory.getLogger(ProveedorResource.class);

    private static final String ENTITY_NAME = "proveedor";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProveedorService proveedorService;

    private final ProveedorRepository proveedorRepository;

    private final ProveedorQueryService proveedorQueryService;

    public ProveedorResource(
        ProveedorService proveedorService,
        ProveedorRepository proveedorRepository,
        ProveedorQueryService proveedorQueryService
    ) {
        this.proveedorService = proveedorService;
        this.proveedorRepository = proveedorRepository;
        this.proveedorQueryService = proveedorQueryService;
    }

    /**
     * {@code POST  /proveedors} : Create a new proveedor.
     *
     * @param proveedorDTO the proveedorDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new proveedorDTO, or with status {@code 400 (Bad Request)} if the proveedor has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/proveedors")
    public ResponseEntity<ProveedorDTO> createProveedor(@Valid @RequestBody ProveedorDTO proveedorDTO) throws URISyntaxException {
        log.debug("REST request to save Proveedor : {}", proveedorDTO);
        if (proveedorDTO.getId() != null) {
            throw new BadRequestAlertException("A new proveedor cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProveedorDTO result = proveedorService.save(proveedorDTO);
        return ResponseEntity
            .created(new URI("/api/proveedors/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /proveedors/:id} : Updates an existing proveedor.
     *
     * @param id the id of the proveedorDTO to save.
     * @param proveedorDTO the proveedorDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated proveedorDTO,
     * or with status {@code 400 (Bad Request)} if the proveedorDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the proveedorDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/proveedors/{id}")
    public ResponseEntity<ProveedorDTO> updateProveedor(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ProveedorDTO proveedorDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Proveedor : {}, {}", id, proveedorDTO);
        if (proveedorDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, proveedorDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!proveedorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ProveedorDTO result = proveedorService.save(proveedorDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, proveedorDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /proveedors/:id} : Partial updates given fields of an existing proveedor, field will ignore if it is null
     *
     * @param id the id of the proveedorDTO to save.
     * @param proveedorDTO the proveedorDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated proveedorDTO,
     * or with status {@code 400 (Bad Request)} if the proveedorDTO is not valid,
     * or with status {@code 404 (Not Found)} if the proveedorDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the proveedorDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/proveedors/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<ProveedorDTO> partialUpdateProveedor(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ProveedorDTO proveedorDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Proveedor partially : {}, {}", id, proveedorDTO);
        if (proveedorDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, proveedorDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!proveedorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProveedorDTO> result = proveedorService.partialUpdate(proveedorDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, proveedorDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /proveedors} : get all the proveedors.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of proveedors in body.
     */
    @GetMapping("/proveedors")
    public ResponseEntity<List<ProveedorDTO>> getAllProveedors(ProveedorCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Proveedors by criteria: {}", criteria);
        Page<ProveedorDTO> page = proveedorQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /proveedors/count} : count all the proveedors.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/proveedors/count")
    public ResponseEntity<Long> countProveedors(ProveedorCriteria criteria) {
        log.debug("REST request to count Proveedors by criteria: {}", criteria);
        return ResponseEntity.ok().body(proveedorQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /proveedors/:id} : get the "id" proveedor.
     *
     * @param id the id of the proveedorDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the proveedorDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/proveedors/{id}")
    public ResponseEntity<ProveedorDTO> getProveedor(@PathVariable Long id) {
        log.debug("REST request to get Proveedor : {}", id);
        Optional<ProveedorDTO> proveedorDTO = proveedorService.findOne(id);
        return ResponseUtil.wrapOrNotFound(proveedorDTO);
    }

    /**
     * {@code DELETE  /proveedors/:id} : delete the "id" proveedor.
     *
     * @param id the id of the proveedorDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/proveedors/{id}")
    public ResponseEntity<Void> deleteProveedor(@PathVariable Long id) {
        log.debug("REST request to delete Proveedor : {}", id);
        proveedorService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
