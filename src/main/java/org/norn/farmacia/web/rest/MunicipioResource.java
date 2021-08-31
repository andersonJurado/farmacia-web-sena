package org.norn.farmacia.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.norn.farmacia.repository.MunicipioRepository;
import org.norn.farmacia.service.MunicipioQueryService;
import org.norn.farmacia.service.MunicipioService;
import org.norn.farmacia.service.criteria.MunicipioCriteria;
import org.norn.farmacia.service.dto.MunicipioDTO;
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
 * REST controller for managing {@link org.norn.farmacia.domain.Municipio}.
 */
@RestController
@RequestMapping("/api")
public class MunicipioResource {

    private final Logger log = LoggerFactory.getLogger(MunicipioResource.class);

    private static final String ENTITY_NAME = "municipio";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MunicipioService municipioService;

    private final MunicipioRepository municipioRepository;

    private final MunicipioQueryService municipioQueryService;

    public MunicipioResource(
        MunicipioService municipioService,
        MunicipioRepository municipioRepository,
        MunicipioQueryService municipioQueryService
    ) {
        this.municipioService = municipioService;
        this.municipioRepository = municipioRepository;
        this.municipioQueryService = municipioQueryService;
    }

    /**
     * {@code POST  /municipios} : Create a new municipio.
     *
     * @param municipioDTO the municipioDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new municipioDTO, or with status {@code 400 (Bad Request)} if the municipio has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/municipios")
    public ResponseEntity<MunicipioDTO> createMunicipio(@Valid @RequestBody MunicipioDTO municipioDTO) throws URISyntaxException {
        log.debug("REST request to save Municipio : {}", municipioDTO);
        if (municipioDTO.getId() != null) {
            throw new BadRequestAlertException("A new municipio cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MunicipioDTO result = municipioService.save(municipioDTO);
        return ResponseEntity
            .created(new URI("/api/municipios/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /municipios/:id} : Updates an existing municipio.
     *
     * @param id the id of the municipioDTO to save.
     * @param municipioDTO the municipioDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated municipioDTO,
     * or with status {@code 400 (Bad Request)} if the municipioDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the municipioDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/municipios/{id}")
    public ResponseEntity<MunicipioDTO> updateMunicipio(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MunicipioDTO municipioDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Municipio : {}, {}", id, municipioDTO);
        if (municipioDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, municipioDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!municipioRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        MunicipioDTO result = municipioService.save(municipioDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, municipioDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /municipios/:id} : Partial updates given fields of an existing municipio, field will ignore if it is null
     *
     * @param id the id of the municipioDTO to save.
     * @param municipioDTO the municipioDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated municipioDTO,
     * or with status {@code 400 (Bad Request)} if the municipioDTO is not valid,
     * or with status {@code 404 (Not Found)} if the municipioDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the municipioDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/municipios/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<MunicipioDTO> partialUpdateMunicipio(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MunicipioDTO municipioDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Municipio partially : {}, {}", id, municipioDTO);
        if (municipioDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, municipioDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!municipioRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MunicipioDTO> result = municipioService.partialUpdate(municipioDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, municipioDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /municipios} : get all the municipios.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of municipios in body.
     */
    @GetMapping("/municipios")
    public ResponseEntity<List<MunicipioDTO>> getAllMunicipios(MunicipioCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Municipios by criteria: {}", criteria);
        Page<MunicipioDTO> page = municipioQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /municipios/count} : count all the municipios.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/municipios/count")
    public ResponseEntity<Long> countMunicipios(MunicipioCriteria criteria) {
        log.debug("REST request to count Municipios by criteria: {}", criteria);
        return ResponseEntity.ok().body(municipioQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /municipios/:id} : get the "id" municipio.
     *
     * @param id the id of the municipioDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the municipioDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/municipios/{id}")
    public ResponseEntity<MunicipioDTO> getMunicipio(@PathVariable Long id) {
        log.debug("REST request to get Municipio : {}", id);
        Optional<MunicipioDTO> municipioDTO = municipioService.findOne(id);
        return ResponseUtil.wrapOrNotFound(municipioDTO);
    }

    /**
     * {@code DELETE  /municipios/:id} : delete the "id" municipio.
     *
     * @param id the id of the municipioDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/municipios/{id}")
    public ResponseEntity<Void> deleteMunicipio(@PathVariable Long id) {
        log.debug("REST request to delete Municipio : {}", id);
        municipioService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
