package org.norn.farmacia.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.norn.farmacia.repository.CompraProductoRepository;
import org.norn.farmacia.service.CompraProductoQueryService;
import org.norn.farmacia.service.CompraProductoService;
import org.norn.farmacia.service.criteria.CompraProductoCriteria;
import org.norn.farmacia.service.dto.CompraProductoDTO;
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
 * REST controller for managing {@link org.norn.farmacia.domain.CompraProducto}.
 */
@RestController
@RequestMapping("/api")
public class CompraProductoResource {

    private final Logger log = LoggerFactory.getLogger(CompraProductoResource.class);

    private static final String ENTITY_NAME = "compraProducto";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CompraProductoService compraProductoService;

    private final CompraProductoRepository compraProductoRepository;

    private final CompraProductoQueryService compraProductoQueryService;

    public CompraProductoResource(
        CompraProductoService compraProductoService,
        CompraProductoRepository compraProductoRepository,
        CompraProductoQueryService compraProductoQueryService
    ) {
        this.compraProductoService = compraProductoService;
        this.compraProductoRepository = compraProductoRepository;
        this.compraProductoQueryService = compraProductoQueryService;
    }

    /**
     * {@code POST  /compra-productos} : Create a new compraProducto.
     *
     * @param compraProductoDTO the compraProductoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new compraProductoDTO, or with status {@code 400 (Bad Request)} if the compraProducto has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/compra-productos")
    public ResponseEntity<CompraProductoDTO> createCompraProducto(@RequestBody CompraProductoDTO compraProductoDTO)
        throws URISyntaxException {
        log.debug("REST request to save CompraProducto : {}", compraProductoDTO);
        if (compraProductoDTO.getId() != null) {
            throw new BadRequestAlertException("A new compraProducto cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CompraProductoDTO result = compraProductoService.save(compraProductoDTO);
        return ResponseEntity
            .created(new URI("/api/compra-productos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /compra-productos/:id} : Updates an existing compraProducto.
     *
     * @param id the id of the compraProductoDTO to save.
     * @param compraProductoDTO the compraProductoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated compraProductoDTO,
     * or with status {@code 400 (Bad Request)} if the compraProductoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the compraProductoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/compra-productos/{id}")
    public ResponseEntity<CompraProductoDTO> updateCompraProducto(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CompraProductoDTO compraProductoDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CompraProducto : {}, {}", id, compraProductoDTO);
        if (compraProductoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, compraProductoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!compraProductoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CompraProductoDTO result = compraProductoService.save(compraProductoDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, compraProductoDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /compra-productos/:id} : Partial updates given fields of an existing compraProducto, field will ignore if it is null
     *
     * @param id the id of the compraProductoDTO to save.
     * @param compraProductoDTO the compraProductoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated compraProductoDTO,
     * or with status {@code 400 (Bad Request)} if the compraProductoDTO is not valid,
     * or with status {@code 404 (Not Found)} if the compraProductoDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the compraProductoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/compra-productos/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<CompraProductoDTO> partialUpdateCompraProducto(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CompraProductoDTO compraProductoDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CompraProducto partially : {}, {}", id, compraProductoDTO);
        if (compraProductoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, compraProductoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!compraProductoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CompraProductoDTO> result = compraProductoService.partialUpdate(compraProductoDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, compraProductoDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /compra-productos} : get all the compraProductos.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of compraProductos in body.
     */
    @GetMapping("/compra-productos")
    public ResponseEntity<List<CompraProductoDTO>> getAllCompraProductos(CompraProductoCriteria criteria, Pageable pageable) {
        log.debug("REST request to get CompraProductos by criteria: {}", criteria);
        Page<CompraProductoDTO> page = compraProductoQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /compra-productos/count} : count all the compraProductos.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/compra-productos/count")
    public ResponseEntity<Long> countCompraProductos(CompraProductoCriteria criteria) {
        log.debug("REST request to count CompraProductos by criteria: {}", criteria);
        return ResponseEntity.ok().body(compraProductoQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /compra-productos/:id} : get the "id" compraProducto.
     *
     * @param id the id of the compraProductoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the compraProductoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/compra-productos/{id}")
    public ResponseEntity<CompraProductoDTO> getCompraProducto(@PathVariable Long id) {
        log.debug("REST request to get CompraProducto : {}", id);
        Optional<CompraProductoDTO> compraProductoDTO = compraProductoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(compraProductoDTO);
    }

    /**
     * {@code DELETE  /compra-productos/:id} : delete the "id" compraProducto.
     *
     * @param id the id of the compraProductoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/compra-productos/{id}")
    public ResponseEntity<Void> deleteCompraProducto(@PathVariable Long id) {
        log.debug("REST request to delete CompraProducto : {}", id);
        compraProductoService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
