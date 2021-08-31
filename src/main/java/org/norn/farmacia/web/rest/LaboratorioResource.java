package org.norn.farmacia.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.norn.farmacia.repository.LaboratorioRepository;
import org.norn.farmacia.service.LaboratorioQueryService;
import org.norn.farmacia.service.LaboratorioService;
import org.norn.farmacia.service.criteria.LaboratorioCriteria;
import org.norn.farmacia.service.dto.LaboratorioDTO;
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
 * REST controller for managing {@link org.norn.farmacia.domain.Laboratorio}.
 */
@RestController
@RequestMapping("/api")
public class LaboratorioResource {

    private final Logger log = LoggerFactory.getLogger(LaboratorioResource.class);

    private static final String ENTITY_NAME = "laboratorio";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LaboratorioService laboratorioService;

    private final LaboratorioRepository laboratorioRepository;

    private final LaboratorioQueryService laboratorioQueryService;

    public LaboratorioResource(
        LaboratorioService laboratorioService,
        LaboratorioRepository laboratorioRepository,
        LaboratorioQueryService laboratorioQueryService
    ) {
        this.laboratorioService = laboratorioService;
        this.laboratorioRepository = laboratorioRepository;
        this.laboratorioQueryService = laboratorioQueryService;
    }

    /**
     * {@code POST  /laboratorios} : Create a new laboratorio.
     *
     * @param laboratorioDTO the laboratorioDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new laboratorioDTO, or with status {@code 400 (Bad Request)} if the laboratorio has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/laboratorios")
    public ResponseEntity<LaboratorioDTO> createLaboratorio(@Valid @RequestBody LaboratorioDTO laboratorioDTO) throws URISyntaxException {
        log.debug("REST request to save Laboratorio : {}", laboratorioDTO);
        if (laboratorioDTO.getId() != null) {
            throw new BadRequestAlertException("A new laboratorio cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LaboratorioDTO result = laboratorioService.save(laboratorioDTO);
        return ResponseEntity
            .created(new URI("/api/laboratorios/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /laboratorios/:id} : Updates an existing laboratorio.
     *
     * @param id the id of the laboratorioDTO to save.
     * @param laboratorioDTO the laboratorioDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated laboratorioDTO,
     * or with status {@code 400 (Bad Request)} if the laboratorioDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the laboratorioDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/laboratorios/{id}")
    public ResponseEntity<LaboratorioDTO> updateLaboratorio(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody LaboratorioDTO laboratorioDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Laboratorio : {}, {}", id, laboratorioDTO);
        if (laboratorioDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, laboratorioDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!laboratorioRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        LaboratorioDTO result = laboratorioService.save(laboratorioDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, laboratorioDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /laboratorios/:id} : Partial updates given fields of an existing laboratorio, field will ignore if it is null
     *
     * @param id the id of the laboratorioDTO to save.
     * @param laboratorioDTO the laboratorioDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated laboratorioDTO,
     * or with status {@code 400 (Bad Request)} if the laboratorioDTO is not valid,
     * or with status {@code 404 (Not Found)} if the laboratorioDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the laboratorioDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/laboratorios/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<LaboratorioDTO> partialUpdateLaboratorio(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody LaboratorioDTO laboratorioDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Laboratorio partially : {}, {}", id, laboratorioDTO);
        if (laboratorioDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, laboratorioDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!laboratorioRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LaboratorioDTO> result = laboratorioService.partialUpdate(laboratorioDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, laboratorioDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /laboratorios} : get all the laboratorios.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of laboratorios in body.
     */
    @GetMapping("/laboratorios")
    public ResponseEntity<List<LaboratorioDTO>> getAllLaboratorios(LaboratorioCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Laboratorios by criteria: {}", criteria);
        Page<LaboratorioDTO> page = laboratorioQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /laboratorios/count} : count all the laboratorios.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/laboratorios/count")
    public ResponseEntity<Long> countLaboratorios(LaboratorioCriteria criteria) {
        log.debug("REST request to count Laboratorios by criteria: {}", criteria);
        return ResponseEntity.ok().body(laboratorioQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /laboratorios/:id} : get the "id" laboratorio.
     *
     * @param id the id of the laboratorioDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the laboratorioDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/laboratorios/{id}")
    public ResponseEntity<LaboratorioDTO> getLaboratorio(@PathVariable Long id) {
        log.debug("REST request to get Laboratorio : {}", id);
        Optional<LaboratorioDTO> laboratorioDTO = laboratorioService.findOne(id);
        return ResponseUtil.wrapOrNotFound(laboratorioDTO);
    }

    /**
     * {@code DELETE  /laboratorios/:id} : delete the "id" laboratorio.
     *
     * @param id the id of the laboratorioDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/laboratorios/{id}")
    public ResponseEntity<Void> deleteLaboratorio(@PathVariable Long id) {
        log.debug("REST request to delete Laboratorio : {}", id);
        laboratorioService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
