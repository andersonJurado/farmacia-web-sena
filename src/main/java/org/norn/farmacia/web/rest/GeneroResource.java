package org.norn.farmacia.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.norn.farmacia.repository.GeneroRepository;
import org.norn.farmacia.service.GeneroQueryService;
import org.norn.farmacia.service.GeneroService;
import org.norn.farmacia.service.criteria.GeneroCriteria;
import org.norn.farmacia.service.dto.GeneroDTO;
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
 * REST controller for managing {@link org.norn.farmacia.domain.Genero}.
 */
@RestController
@RequestMapping("/api")
public class GeneroResource {

    private final Logger log = LoggerFactory.getLogger(GeneroResource.class);

    private static final String ENTITY_NAME = "genero";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GeneroService generoService;

    private final GeneroRepository generoRepository;

    private final GeneroQueryService generoQueryService;

    public GeneroResource(GeneroService generoService, GeneroRepository generoRepository, GeneroQueryService generoQueryService) {
        this.generoService = generoService;
        this.generoRepository = generoRepository;
        this.generoQueryService = generoQueryService;
    }

    /**
     * {@code POST  /generos} : Create a new genero.
     *
     * @param generoDTO the generoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new generoDTO, or with status {@code 400 (Bad Request)} if the genero has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/generos")
    public ResponseEntity<GeneroDTO> createGenero(@Valid @RequestBody GeneroDTO generoDTO) throws URISyntaxException {
        log.debug("REST request to save Genero : {}", generoDTO);
        if (generoDTO.getId() != null) {
            throw new BadRequestAlertException("A new genero cannot already have an ID", ENTITY_NAME, "idexists");
        }
        GeneroDTO result = generoService.save(generoDTO);
        return ResponseEntity
            .created(new URI("/api/generos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /generos/:id} : Updates an existing genero.
     *
     * @param id the id of the generoDTO to save.
     * @param generoDTO the generoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated generoDTO,
     * or with status {@code 400 (Bad Request)} if the generoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the generoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/generos/{id}")
    public ResponseEntity<GeneroDTO> updateGenero(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody GeneroDTO generoDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Genero : {}, {}", id, generoDTO);
        if (generoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, generoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!generoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        GeneroDTO result = generoService.save(generoDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, generoDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /generos/:id} : Partial updates given fields of an existing genero, field will ignore if it is null
     *
     * @param id the id of the generoDTO to save.
     * @param generoDTO the generoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated generoDTO,
     * or with status {@code 400 (Bad Request)} if the generoDTO is not valid,
     * or with status {@code 404 (Not Found)} if the generoDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the generoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/generos/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<GeneroDTO> partialUpdateGenero(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody GeneroDTO generoDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Genero partially : {}, {}", id, generoDTO);
        if (generoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, generoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!generoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<GeneroDTO> result = generoService.partialUpdate(generoDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, generoDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /generos} : get all the generos.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of generos in body.
     */
    @GetMapping("/generos")
    public ResponseEntity<List<GeneroDTO>> getAllGeneros(GeneroCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Generos by criteria: {}", criteria);
        Page<GeneroDTO> page = generoQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /generos/count} : count all the generos.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/generos/count")
    public ResponseEntity<Long> countGeneros(GeneroCriteria criteria) {
        log.debug("REST request to count Generos by criteria: {}", criteria);
        return ResponseEntity.ok().body(generoQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /generos/:id} : get the "id" genero.
     *
     * @param id the id of the generoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the generoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/generos/{id}")
    public ResponseEntity<GeneroDTO> getGenero(@PathVariable Long id) {
        log.debug("REST request to get Genero : {}", id);
        Optional<GeneroDTO> generoDTO = generoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(generoDTO);
    }

    /**
     * {@code DELETE  /generos/:id} : delete the "id" genero.
     *
     * @param id the id of the generoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/generos/{id}")
    public ResponseEntity<Void> deleteGenero(@PathVariable Long id) {
        log.debug("REST request to delete Genero : {}", id);
        generoService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
