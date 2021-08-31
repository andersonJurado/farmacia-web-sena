package org.norn.farmacia.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.norn.farmacia.repository.VentaProductoRepository;
import org.norn.farmacia.service.VentaProductoQueryService;
import org.norn.farmacia.service.VentaProductoService;
import org.norn.farmacia.service.criteria.VentaProductoCriteria;
import org.norn.farmacia.service.dto.VentaProductoDTO;
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
 * REST controller for managing {@link org.norn.farmacia.domain.VentaProducto}.
 */
@RestController
@RequestMapping("/api")
public class VentaProductoResource {

    private final Logger log = LoggerFactory.getLogger(VentaProductoResource.class);

    private static final String ENTITY_NAME = "ventaProducto";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VentaProductoService ventaProductoService;

    private final VentaProductoRepository ventaProductoRepository;

    private final VentaProductoQueryService ventaProductoQueryService;

    public VentaProductoResource(
        VentaProductoService ventaProductoService,
        VentaProductoRepository ventaProductoRepository,
        VentaProductoQueryService ventaProductoQueryService
    ) {
        this.ventaProductoService = ventaProductoService;
        this.ventaProductoRepository = ventaProductoRepository;
        this.ventaProductoQueryService = ventaProductoQueryService;
    }

    /**
     * {@code POST  /venta-productos} : Create a new ventaProducto.
     *
     * @param ventaProductoDTO the ventaProductoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ventaProductoDTO, or with status {@code 400 (Bad Request)} if the ventaProducto has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/venta-productos")
    public ResponseEntity<VentaProductoDTO> createVentaProducto(@RequestBody VentaProductoDTO ventaProductoDTO) throws URISyntaxException {
        log.debug("REST request to save VentaProducto : {}", ventaProductoDTO);
        if (ventaProductoDTO.getId() != null) {
            throw new BadRequestAlertException("A new ventaProducto cannot already have an ID", ENTITY_NAME, "idexists");
        }
        VentaProductoDTO result = ventaProductoService.save(ventaProductoDTO);
        return ResponseEntity
            .created(new URI("/api/venta-productos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /venta-productos/:id} : Updates an existing ventaProducto.
     *
     * @param id the id of the ventaProductoDTO to save.
     * @param ventaProductoDTO the ventaProductoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ventaProductoDTO,
     * or with status {@code 400 (Bad Request)} if the ventaProductoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ventaProductoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/venta-productos/{id}")
    public ResponseEntity<VentaProductoDTO> updateVentaProducto(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody VentaProductoDTO ventaProductoDTO
    ) throws URISyntaxException {
        log.debug("REST request to update VentaProducto : {}, {}", id, ventaProductoDTO);
        if (ventaProductoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ventaProductoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ventaProductoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        VentaProductoDTO result = ventaProductoService.save(ventaProductoDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ventaProductoDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /venta-productos/:id} : Partial updates given fields of an existing ventaProducto, field will ignore if it is null
     *
     * @param id the id of the ventaProductoDTO to save.
     * @param ventaProductoDTO the ventaProductoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ventaProductoDTO,
     * or with status {@code 400 (Bad Request)} if the ventaProductoDTO is not valid,
     * or with status {@code 404 (Not Found)} if the ventaProductoDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the ventaProductoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/venta-productos/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<VentaProductoDTO> partialUpdateVentaProducto(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody VentaProductoDTO ventaProductoDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update VentaProducto partially : {}, {}", id, ventaProductoDTO);
        if (ventaProductoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ventaProductoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ventaProductoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<VentaProductoDTO> result = ventaProductoService.partialUpdate(ventaProductoDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ventaProductoDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /venta-productos} : get all the ventaProductos.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ventaProductos in body.
     */
    @GetMapping("/venta-productos")
    public ResponseEntity<List<VentaProductoDTO>> getAllVentaProductos(VentaProductoCriteria criteria, Pageable pageable) {
        log.debug("REST request to get VentaProductos by criteria: {}", criteria);
        Page<VentaProductoDTO> page = ventaProductoQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /venta-productos/count} : count all the ventaProductos.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/venta-productos/count")
    public ResponseEntity<Long> countVentaProductos(VentaProductoCriteria criteria) {
        log.debug("REST request to count VentaProductos by criteria: {}", criteria);
        return ResponseEntity.ok().body(ventaProductoQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /venta-productos/:id} : get the "id" ventaProducto.
     *
     * @param id the id of the ventaProductoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ventaProductoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/venta-productos/{id}")
    public ResponseEntity<VentaProductoDTO> getVentaProducto(@PathVariable Long id) {
        log.debug("REST request to get VentaProducto : {}", id);
        Optional<VentaProductoDTO> ventaProductoDTO = ventaProductoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(ventaProductoDTO);
    }

    /**
     * {@code DELETE  /venta-productos/:id} : delete the "id" ventaProducto.
     *
     * @param id the id of the ventaProductoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/venta-productos/{id}")
    public ResponseEntity<Void> deleteVentaProducto(@PathVariable Long id) {
        log.debug("REST request to delete VentaProducto : {}", id);
        ventaProductoService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
