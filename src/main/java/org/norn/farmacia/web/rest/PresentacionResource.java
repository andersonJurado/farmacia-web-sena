package org.norn.farmacia.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.norn.farmacia.repository.PresentacionRepository;
import org.norn.farmacia.service.PresentacionQueryService;
import org.norn.farmacia.service.PresentacionService;
import org.norn.farmacia.service.criteria.PresentacionCriteria;
import org.norn.farmacia.service.dto.PresentacionDTO;
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
 * REST controller for managing {@link org.norn.farmacia.domain.Presentacion}.
 */
@RestController
@RequestMapping("/api")
public class PresentacionResource {

    private final Logger log = LoggerFactory.getLogger(PresentacionResource.class);

    private static final String ENTITY_NAME = "presentacion";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PresentacionService presentacionService;

    private final PresentacionRepository presentacionRepository;

    private final PresentacionQueryService presentacionQueryService;

    public PresentacionResource(
        PresentacionService presentacionService,
        PresentacionRepository presentacionRepository,
        PresentacionQueryService presentacionQueryService
    ) {
        this.presentacionService = presentacionService;
        this.presentacionRepository = presentacionRepository;
        this.presentacionQueryService = presentacionQueryService;
    }

    /**
     * {@code POST  /presentacions} : Create a new presentacion.
     *
     * @param presentacionDTO the presentacionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new presentacionDTO, or with status {@code 400 (Bad Request)} if the presentacion has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/presentacions")
    public ResponseEntity<PresentacionDTO> createPresentacion(@Valid @RequestBody PresentacionDTO presentacionDTO)
        throws URISyntaxException {
        log.debug("REST request to save Presentacion : {}", presentacionDTO);
        if (presentacionDTO.getId() != null) {
            throw new BadRequestAlertException("A new presentacion cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PresentacionDTO result = presentacionService.save(presentacionDTO);
        return ResponseEntity
            .created(new URI("/api/presentacions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /presentacions/:id} : Updates an existing presentacion.
     *
     * @param id the id of the presentacionDTO to save.
     * @param presentacionDTO the presentacionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated presentacionDTO,
     * or with status {@code 400 (Bad Request)} if the presentacionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the presentacionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/presentacions/{id}")
    public ResponseEntity<PresentacionDTO> updatePresentacion(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PresentacionDTO presentacionDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Presentacion : {}, {}", id, presentacionDTO);
        if (presentacionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, presentacionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!presentacionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PresentacionDTO result = presentacionService.save(presentacionDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, presentacionDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /presentacions/:id} : Partial updates given fields of an existing presentacion, field will ignore if it is null
     *
     * @param id the id of the presentacionDTO to save.
     * @param presentacionDTO the presentacionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated presentacionDTO,
     * or with status {@code 400 (Bad Request)} if the presentacionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the presentacionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the presentacionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/presentacions/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<PresentacionDTO> partialUpdatePresentacion(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PresentacionDTO presentacionDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Presentacion partially : {}, {}", id, presentacionDTO);
        if (presentacionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, presentacionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!presentacionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PresentacionDTO> result = presentacionService.partialUpdate(presentacionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, presentacionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /presentacions} : get all the presentacions.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of presentacions in body.
     */
    @GetMapping("/presentacions")
    public ResponseEntity<List<PresentacionDTO>> getAllPresentacions(PresentacionCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Presentacions by criteria: {}", criteria);
        Page<PresentacionDTO> page = presentacionQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /presentacions/count} : count all the presentacions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/presentacions/count")
    public ResponseEntity<Long> countPresentacions(PresentacionCriteria criteria) {
        log.debug("REST request to count Presentacions by criteria: {}", criteria);
        return ResponseEntity.ok().body(presentacionQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /presentacions/:id} : get the "id" presentacion.
     *
     * @param id the id of the presentacionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the presentacionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/presentacions/{id}")
    public ResponseEntity<PresentacionDTO> getPresentacion(@PathVariable Long id) {
        log.debug("REST request to get Presentacion : {}", id);
        Optional<PresentacionDTO> presentacionDTO = presentacionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(presentacionDTO);
    }

    /**
     * {@code DELETE  /presentacions/:id} : delete the "id" presentacion.
     *
     * @param id the id of the presentacionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/presentacions/{id}")
    public ResponseEntity<Void> deletePresentacion(@PathVariable Long id) {
        log.debug("REST request to delete Presentacion : {}", id);
        presentacionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
