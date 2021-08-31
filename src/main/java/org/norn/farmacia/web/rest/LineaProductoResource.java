package org.norn.farmacia.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.norn.farmacia.repository.LineaProductoRepository;
import org.norn.farmacia.service.LineaProductoQueryService;
import org.norn.farmacia.service.LineaProductoService;
import org.norn.farmacia.service.criteria.LineaProductoCriteria;
import org.norn.farmacia.service.dto.LineaProductoDTO;
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
 * REST controller for managing {@link org.norn.farmacia.domain.LineaProducto}.
 */
@RestController
@RequestMapping("/api")
public class LineaProductoResource {

    private final Logger log = LoggerFactory.getLogger(LineaProductoResource.class);

    private static final String ENTITY_NAME = "lineaProducto";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LineaProductoService lineaProductoService;

    private final LineaProductoRepository lineaProductoRepository;

    private final LineaProductoQueryService lineaProductoQueryService;

    public LineaProductoResource(
        LineaProductoService lineaProductoService,
        LineaProductoRepository lineaProductoRepository,
        LineaProductoQueryService lineaProductoQueryService
    ) {
        this.lineaProductoService = lineaProductoService;
        this.lineaProductoRepository = lineaProductoRepository;
        this.lineaProductoQueryService = lineaProductoQueryService;
    }

    /**
     * {@code POST  /linea-productos} : Create a new lineaProducto.
     *
     * @param lineaProductoDTO the lineaProductoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new lineaProductoDTO, or with status {@code 400 (Bad Request)} if the lineaProducto has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/linea-productos")
    public ResponseEntity<LineaProductoDTO> createLineaProducto(@Valid @RequestBody LineaProductoDTO lineaProductoDTO)
        throws URISyntaxException {
        log.debug("REST request to save LineaProducto : {}", lineaProductoDTO);
        if (lineaProductoDTO.getId() != null) {
            throw new BadRequestAlertException("A new lineaProducto cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LineaProductoDTO result = lineaProductoService.save(lineaProductoDTO);
        return ResponseEntity
            .created(new URI("/api/linea-productos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /linea-productos/:id} : Updates an existing lineaProducto.
     *
     * @param id the id of the lineaProductoDTO to save.
     * @param lineaProductoDTO the lineaProductoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated lineaProductoDTO,
     * or with status {@code 400 (Bad Request)} if the lineaProductoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the lineaProductoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/linea-productos/{id}")
    public ResponseEntity<LineaProductoDTO> updateLineaProducto(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody LineaProductoDTO lineaProductoDTO
    ) throws URISyntaxException {
        log.debug("REST request to update LineaProducto : {}, {}", id, lineaProductoDTO);
        if (lineaProductoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, lineaProductoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!lineaProductoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        LineaProductoDTO result = lineaProductoService.save(lineaProductoDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, lineaProductoDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /linea-productos/:id} : Partial updates given fields of an existing lineaProducto, field will ignore if it is null
     *
     * @param id the id of the lineaProductoDTO to save.
     * @param lineaProductoDTO the lineaProductoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated lineaProductoDTO,
     * or with status {@code 400 (Bad Request)} if the lineaProductoDTO is not valid,
     * or with status {@code 404 (Not Found)} if the lineaProductoDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the lineaProductoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/linea-productos/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<LineaProductoDTO> partialUpdateLineaProducto(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody LineaProductoDTO lineaProductoDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update LineaProducto partially : {}, {}", id, lineaProductoDTO);
        if (lineaProductoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, lineaProductoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!lineaProductoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LineaProductoDTO> result = lineaProductoService.partialUpdate(lineaProductoDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, lineaProductoDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /linea-productos} : get all the lineaProductos.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of lineaProductos in body.
     */
    @GetMapping("/linea-productos")
    public ResponseEntity<List<LineaProductoDTO>> getAllLineaProductos(LineaProductoCriteria criteria, Pageable pageable) {
        log.debug("REST request to get LineaProductos by criteria: {}", criteria);
        Page<LineaProductoDTO> page = lineaProductoQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /linea-productos/count} : count all the lineaProductos.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/linea-productos/count")
    public ResponseEntity<Long> countLineaProductos(LineaProductoCriteria criteria) {
        log.debug("REST request to count LineaProductos by criteria: {}", criteria);
        return ResponseEntity.ok().body(lineaProductoQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /linea-productos/:id} : get the "id" lineaProducto.
     *
     * @param id the id of the lineaProductoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the lineaProductoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/linea-productos/{id}")
    public ResponseEntity<LineaProductoDTO> getLineaProducto(@PathVariable Long id) {
        log.debug("REST request to get LineaProducto : {}", id);
        Optional<LineaProductoDTO> lineaProductoDTO = lineaProductoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(lineaProductoDTO);
    }

    /**
     * {@code DELETE  /linea-productos/:id} : delete the "id" lineaProducto.
     *
     * @param id the id of the lineaProductoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/linea-productos/{id}")
    public ResponseEntity<Void> deleteLineaProducto(@PathVariable Long id) {
        log.debug("REST request to delete LineaProducto : {}", id);
        lineaProductoService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
