package org.norn.farmacia.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.norn.farmacia.IntegrationTest;
import org.norn.farmacia.domain.Producto;
import org.norn.farmacia.domain.Venta;
import org.norn.farmacia.domain.VentaProducto;
import org.norn.farmacia.repository.VentaProductoRepository;
import org.norn.farmacia.service.criteria.VentaProductoCriteria;
import org.norn.farmacia.service.dto.VentaProductoDTO;
import org.norn.farmacia.service.mapper.VentaProductoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link VentaProductoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class VentaProductoResourceIT {

    private static final Integer DEFAULT_CANTIDAD = 1;
    private static final Integer UPDATED_CANTIDAD = 2;
    private static final Integer SMALLER_CANTIDAD = 1 - 1;

    private static final Double DEFAULT_TOTAL = 1D;
    private static final Double UPDATED_TOTAL = 2D;
    private static final Double SMALLER_TOTAL = 1D - 1D;

    private static final String ENTITY_API_URL = "/api/venta-productos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private VentaProductoRepository ventaProductoRepository;

    @Autowired
    private VentaProductoMapper ventaProductoMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVentaProductoMockMvc;

    private VentaProducto ventaProducto;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VentaProducto createEntity(EntityManager em) {
        VentaProducto ventaProducto = new VentaProducto().cantidad(DEFAULT_CANTIDAD).total(DEFAULT_TOTAL);
        return ventaProducto;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VentaProducto createUpdatedEntity(EntityManager em) {
        VentaProducto ventaProducto = new VentaProducto().cantidad(UPDATED_CANTIDAD).total(UPDATED_TOTAL);
        return ventaProducto;
    }

    @BeforeEach
    public void initTest() {
        ventaProducto = createEntity(em);
    }

    @Test
    @Transactional
    void createVentaProducto() throws Exception {
        int databaseSizeBeforeCreate = ventaProductoRepository.findAll().size();
        // Create the VentaProducto
        VentaProductoDTO ventaProductoDTO = ventaProductoMapper.toDto(ventaProducto);
        restVentaProductoMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ventaProductoDTO))
            )
            .andExpect(status().isCreated());

        // Validate the VentaProducto in the database
        List<VentaProducto> ventaProductoList = ventaProductoRepository.findAll();
        assertThat(ventaProductoList).hasSize(databaseSizeBeforeCreate + 1);
        VentaProducto testVentaProducto = ventaProductoList.get(ventaProductoList.size() - 1);
        assertThat(testVentaProducto.getCantidad()).isEqualTo(DEFAULT_CANTIDAD);
        assertThat(testVentaProducto.getTotal()).isEqualTo(DEFAULT_TOTAL);
    }

    @Test
    @Transactional
    void createVentaProductoWithExistingId() throws Exception {
        // Create the VentaProducto with an existing ID
        ventaProducto.setId(1L);
        VentaProductoDTO ventaProductoDTO = ventaProductoMapper.toDto(ventaProducto);

        int databaseSizeBeforeCreate = ventaProductoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVentaProductoMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ventaProductoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VentaProducto in the database
        List<VentaProducto> ventaProductoList = ventaProductoRepository.findAll();
        assertThat(ventaProductoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllVentaProductos() throws Exception {
        // Initialize the database
        ventaProductoRepository.saveAndFlush(ventaProducto);

        // Get all the ventaProductoList
        restVentaProductoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ventaProducto.getId().intValue())))
            .andExpect(jsonPath("$.[*].cantidad").value(hasItem(DEFAULT_CANTIDAD)))
            .andExpect(jsonPath("$.[*].total").value(hasItem(DEFAULT_TOTAL.doubleValue())));
    }

    @Test
    @Transactional
    void getVentaProducto() throws Exception {
        // Initialize the database
        ventaProductoRepository.saveAndFlush(ventaProducto);

        // Get the ventaProducto
        restVentaProductoMockMvc
            .perform(get(ENTITY_API_URL_ID, ventaProducto.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ventaProducto.getId().intValue()))
            .andExpect(jsonPath("$.cantidad").value(DEFAULT_CANTIDAD))
            .andExpect(jsonPath("$.total").value(DEFAULT_TOTAL.doubleValue()));
    }

    @Test
    @Transactional
    void getVentaProductosByIdFiltering() throws Exception {
        // Initialize the database
        ventaProductoRepository.saveAndFlush(ventaProducto);

        Long id = ventaProducto.getId();

        defaultVentaProductoShouldBeFound("id.equals=" + id);
        defaultVentaProductoShouldNotBeFound("id.notEquals=" + id);

        defaultVentaProductoShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultVentaProductoShouldNotBeFound("id.greaterThan=" + id);

        defaultVentaProductoShouldBeFound("id.lessThanOrEqual=" + id);
        defaultVentaProductoShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllVentaProductosByCantidadIsEqualToSomething() throws Exception {
        // Initialize the database
        ventaProductoRepository.saveAndFlush(ventaProducto);

        // Get all the ventaProductoList where cantidad equals to DEFAULT_CANTIDAD
        defaultVentaProductoShouldBeFound("cantidad.equals=" + DEFAULT_CANTIDAD);

        // Get all the ventaProductoList where cantidad equals to UPDATED_CANTIDAD
        defaultVentaProductoShouldNotBeFound("cantidad.equals=" + UPDATED_CANTIDAD);
    }

    @Test
    @Transactional
    void getAllVentaProductosByCantidadIsNotEqualToSomething() throws Exception {
        // Initialize the database
        ventaProductoRepository.saveAndFlush(ventaProducto);

        // Get all the ventaProductoList where cantidad not equals to DEFAULT_CANTIDAD
        defaultVentaProductoShouldNotBeFound("cantidad.notEquals=" + DEFAULT_CANTIDAD);

        // Get all the ventaProductoList where cantidad not equals to UPDATED_CANTIDAD
        defaultVentaProductoShouldBeFound("cantidad.notEquals=" + UPDATED_CANTIDAD);
    }

    @Test
    @Transactional
    void getAllVentaProductosByCantidadIsInShouldWork() throws Exception {
        // Initialize the database
        ventaProductoRepository.saveAndFlush(ventaProducto);

        // Get all the ventaProductoList where cantidad in DEFAULT_CANTIDAD or UPDATED_CANTIDAD
        defaultVentaProductoShouldBeFound("cantidad.in=" + DEFAULT_CANTIDAD + "," + UPDATED_CANTIDAD);

        // Get all the ventaProductoList where cantidad equals to UPDATED_CANTIDAD
        defaultVentaProductoShouldNotBeFound("cantidad.in=" + UPDATED_CANTIDAD);
    }

    @Test
    @Transactional
    void getAllVentaProductosByCantidadIsNullOrNotNull() throws Exception {
        // Initialize the database
        ventaProductoRepository.saveAndFlush(ventaProducto);

        // Get all the ventaProductoList where cantidad is not null
        defaultVentaProductoShouldBeFound("cantidad.specified=true");

        // Get all the ventaProductoList where cantidad is null
        defaultVentaProductoShouldNotBeFound("cantidad.specified=false");
    }

    @Test
    @Transactional
    void getAllVentaProductosByCantidadIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        ventaProductoRepository.saveAndFlush(ventaProducto);

        // Get all the ventaProductoList where cantidad is greater than or equal to DEFAULT_CANTIDAD
        defaultVentaProductoShouldBeFound("cantidad.greaterThanOrEqual=" + DEFAULT_CANTIDAD);

        // Get all the ventaProductoList where cantidad is greater than or equal to UPDATED_CANTIDAD
        defaultVentaProductoShouldNotBeFound("cantidad.greaterThanOrEqual=" + UPDATED_CANTIDAD);
    }

    @Test
    @Transactional
    void getAllVentaProductosByCantidadIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        ventaProductoRepository.saveAndFlush(ventaProducto);

        // Get all the ventaProductoList where cantidad is less than or equal to DEFAULT_CANTIDAD
        defaultVentaProductoShouldBeFound("cantidad.lessThanOrEqual=" + DEFAULT_CANTIDAD);

        // Get all the ventaProductoList where cantidad is less than or equal to SMALLER_CANTIDAD
        defaultVentaProductoShouldNotBeFound("cantidad.lessThanOrEqual=" + SMALLER_CANTIDAD);
    }

    @Test
    @Transactional
    void getAllVentaProductosByCantidadIsLessThanSomething() throws Exception {
        // Initialize the database
        ventaProductoRepository.saveAndFlush(ventaProducto);

        // Get all the ventaProductoList where cantidad is less than DEFAULT_CANTIDAD
        defaultVentaProductoShouldNotBeFound("cantidad.lessThan=" + DEFAULT_CANTIDAD);

        // Get all the ventaProductoList where cantidad is less than UPDATED_CANTIDAD
        defaultVentaProductoShouldBeFound("cantidad.lessThan=" + UPDATED_CANTIDAD);
    }

    @Test
    @Transactional
    void getAllVentaProductosByCantidadIsGreaterThanSomething() throws Exception {
        // Initialize the database
        ventaProductoRepository.saveAndFlush(ventaProducto);

        // Get all the ventaProductoList where cantidad is greater than DEFAULT_CANTIDAD
        defaultVentaProductoShouldNotBeFound("cantidad.greaterThan=" + DEFAULT_CANTIDAD);

        // Get all the ventaProductoList where cantidad is greater than SMALLER_CANTIDAD
        defaultVentaProductoShouldBeFound("cantidad.greaterThan=" + SMALLER_CANTIDAD);
    }

    @Test
    @Transactional
    void getAllVentaProductosByTotalIsEqualToSomething() throws Exception {
        // Initialize the database
        ventaProductoRepository.saveAndFlush(ventaProducto);

        // Get all the ventaProductoList where total equals to DEFAULT_TOTAL
        defaultVentaProductoShouldBeFound("total.equals=" + DEFAULT_TOTAL);

        // Get all the ventaProductoList where total equals to UPDATED_TOTAL
        defaultVentaProductoShouldNotBeFound("total.equals=" + UPDATED_TOTAL);
    }

    @Test
    @Transactional
    void getAllVentaProductosByTotalIsNotEqualToSomething() throws Exception {
        // Initialize the database
        ventaProductoRepository.saveAndFlush(ventaProducto);

        // Get all the ventaProductoList where total not equals to DEFAULT_TOTAL
        defaultVentaProductoShouldNotBeFound("total.notEquals=" + DEFAULT_TOTAL);

        // Get all the ventaProductoList where total not equals to UPDATED_TOTAL
        defaultVentaProductoShouldBeFound("total.notEquals=" + UPDATED_TOTAL);
    }

    @Test
    @Transactional
    void getAllVentaProductosByTotalIsInShouldWork() throws Exception {
        // Initialize the database
        ventaProductoRepository.saveAndFlush(ventaProducto);

        // Get all the ventaProductoList where total in DEFAULT_TOTAL or UPDATED_TOTAL
        defaultVentaProductoShouldBeFound("total.in=" + DEFAULT_TOTAL + "," + UPDATED_TOTAL);

        // Get all the ventaProductoList where total equals to UPDATED_TOTAL
        defaultVentaProductoShouldNotBeFound("total.in=" + UPDATED_TOTAL);
    }

    @Test
    @Transactional
    void getAllVentaProductosByTotalIsNullOrNotNull() throws Exception {
        // Initialize the database
        ventaProductoRepository.saveAndFlush(ventaProducto);

        // Get all the ventaProductoList where total is not null
        defaultVentaProductoShouldBeFound("total.specified=true");

        // Get all the ventaProductoList where total is null
        defaultVentaProductoShouldNotBeFound("total.specified=false");
    }

    @Test
    @Transactional
    void getAllVentaProductosByTotalIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        ventaProductoRepository.saveAndFlush(ventaProducto);

        // Get all the ventaProductoList where total is greater than or equal to DEFAULT_TOTAL
        defaultVentaProductoShouldBeFound("total.greaterThanOrEqual=" + DEFAULT_TOTAL);

        // Get all the ventaProductoList where total is greater than or equal to UPDATED_TOTAL
        defaultVentaProductoShouldNotBeFound("total.greaterThanOrEqual=" + UPDATED_TOTAL);
    }

    @Test
    @Transactional
    void getAllVentaProductosByTotalIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        ventaProductoRepository.saveAndFlush(ventaProducto);

        // Get all the ventaProductoList where total is less than or equal to DEFAULT_TOTAL
        defaultVentaProductoShouldBeFound("total.lessThanOrEqual=" + DEFAULT_TOTAL);

        // Get all the ventaProductoList where total is less than or equal to SMALLER_TOTAL
        defaultVentaProductoShouldNotBeFound("total.lessThanOrEqual=" + SMALLER_TOTAL);
    }

    @Test
    @Transactional
    void getAllVentaProductosByTotalIsLessThanSomething() throws Exception {
        // Initialize the database
        ventaProductoRepository.saveAndFlush(ventaProducto);

        // Get all the ventaProductoList where total is less than DEFAULT_TOTAL
        defaultVentaProductoShouldNotBeFound("total.lessThan=" + DEFAULT_TOTAL);

        // Get all the ventaProductoList where total is less than UPDATED_TOTAL
        defaultVentaProductoShouldBeFound("total.lessThan=" + UPDATED_TOTAL);
    }

    @Test
    @Transactional
    void getAllVentaProductosByTotalIsGreaterThanSomething() throws Exception {
        // Initialize the database
        ventaProductoRepository.saveAndFlush(ventaProducto);

        // Get all the ventaProductoList where total is greater than DEFAULT_TOTAL
        defaultVentaProductoShouldNotBeFound("total.greaterThan=" + DEFAULT_TOTAL);

        // Get all the ventaProductoList where total is greater than SMALLER_TOTAL
        defaultVentaProductoShouldBeFound("total.greaterThan=" + SMALLER_TOTAL);
    }

    @Test
    @Transactional
    void getAllVentaProductosByProductoIsEqualToSomething() throws Exception {
        // Initialize the database
        ventaProductoRepository.saveAndFlush(ventaProducto);
        Producto producto = ProductoResourceIT.createEntity(em);
        em.persist(producto);
        em.flush();
        ventaProducto.setProducto(producto);
        ventaProductoRepository.saveAndFlush(ventaProducto);
        Long productoId = producto.getId();

        // Get all the ventaProductoList where producto equals to productoId
        defaultVentaProductoShouldBeFound("productoId.equals=" + productoId);

        // Get all the ventaProductoList where producto equals to (productoId + 1)
        defaultVentaProductoShouldNotBeFound("productoId.equals=" + (productoId + 1));
    }

    @Test
    @Transactional
    void getAllVentaProductosByVentaIsEqualToSomething() throws Exception {
        // Initialize the database
        ventaProductoRepository.saveAndFlush(ventaProducto);
        Venta venta = VentaResourceIT.createEntity(em);
        em.persist(venta);
        em.flush();
        ventaProducto.setVenta(venta);
        ventaProductoRepository.saveAndFlush(ventaProducto);
        Long ventaId = venta.getId();

        // Get all the ventaProductoList where venta equals to ventaId
        defaultVentaProductoShouldBeFound("ventaId.equals=" + ventaId);

        // Get all the ventaProductoList where venta equals to (ventaId + 1)
        defaultVentaProductoShouldNotBeFound("ventaId.equals=" + (ventaId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultVentaProductoShouldBeFound(String filter) throws Exception {
        restVentaProductoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ventaProducto.getId().intValue())))
            .andExpect(jsonPath("$.[*].cantidad").value(hasItem(DEFAULT_CANTIDAD)))
            .andExpect(jsonPath("$.[*].total").value(hasItem(DEFAULT_TOTAL.doubleValue())));

        // Check, that the count call also returns 1
        restVentaProductoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultVentaProductoShouldNotBeFound(String filter) throws Exception {
        restVentaProductoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restVentaProductoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingVentaProducto() throws Exception {
        // Get the ventaProducto
        restVentaProductoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewVentaProducto() throws Exception {
        // Initialize the database
        ventaProductoRepository.saveAndFlush(ventaProducto);

        int databaseSizeBeforeUpdate = ventaProductoRepository.findAll().size();

        // Update the ventaProducto
        VentaProducto updatedVentaProducto = ventaProductoRepository.findById(ventaProducto.getId()).get();
        // Disconnect from session so that the updates on updatedVentaProducto are not directly saved in db
        em.detach(updatedVentaProducto);
        updatedVentaProducto.cantidad(UPDATED_CANTIDAD).total(UPDATED_TOTAL);
        VentaProductoDTO ventaProductoDTO = ventaProductoMapper.toDto(updatedVentaProducto);

        restVentaProductoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ventaProductoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ventaProductoDTO))
            )
            .andExpect(status().isOk());

        // Validate the VentaProducto in the database
        List<VentaProducto> ventaProductoList = ventaProductoRepository.findAll();
        assertThat(ventaProductoList).hasSize(databaseSizeBeforeUpdate);
        VentaProducto testVentaProducto = ventaProductoList.get(ventaProductoList.size() - 1);
        assertThat(testVentaProducto.getCantidad()).isEqualTo(UPDATED_CANTIDAD);
        assertThat(testVentaProducto.getTotal()).isEqualTo(UPDATED_TOTAL);
    }

    @Test
    @Transactional
    void putNonExistingVentaProducto() throws Exception {
        int databaseSizeBeforeUpdate = ventaProductoRepository.findAll().size();
        ventaProducto.setId(count.incrementAndGet());

        // Create the VentaProducto
        VentaProductoDTO ventaProductoDTO = ventaProductoMapper.toDto(ventaProducto);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVentaProductoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ventaProductoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ventaProductoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VentaProducto in the database
        List<VentaProducto> ventaProductoList = ventaProductoRepository.findAll();
        assertThat(ventaProductoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchVentaProducto() throws Exception {
        int databaseSizeBeforeUpdate = ventaProductoRepository.findAll().size();
        ventaProducto.setId(count.incrementAndGet());

        // Create the VentaProducto
        VentaProductoDTO ventaProductoDTO = ventaProductoMapper.toDto(ventaProducto);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVentaProductoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ventaProductoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VentaProducto in the database
        List<VentaProducto> ventaProductoList = ventaProductoRepository.findAll();
        assertThat(ventaProductoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVentaProducto() throws Exception {
        int databaseSizeBeforeUpdate = ventaProductoRepository.findAll().size();
        ventaProducto.setId(count.incrementAndGet());

        // Create the VentaProducto
        VentaProductoDTO ventaProductoDTO = ventaProductoMapper.toDto(ventaProducto);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVentaProductoMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ventaProductoDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the VentaProducto in the database
        List<VentaProducto> ventaProductoList = ventaProductoRepository.findAll();
        assertThat(ventaProductoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateVentaProductoWithPatch() throws Exception {
        // Initialize the database
        ventaProductoRepository.saveAndFlush(ventaProducto);

        int databaseSizeBeforeUpdate = ventaProductoRepository.findAll().size();

        // Update the ventaProducto using partial update
        VentaProducto partialUpdatedVentaProducto = new VentaProducto();
        partialUpdatedVentaProducto.setId(ventaProducto.getId());

        restVentaProductoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVentaProducto.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVentaProducto))
            )
            .andExpect(status().isOk());

        // Validate the VentaProducto in the database
        List<VentaProducto> ventaProductoList = ventaProductoRepository.findAll();
        assertThat(ventaProductoList).hasSize(databaseSizeBeforeUpdate);
        VentaProducto testVentaProducto = ventaProductoList.get(ventaProductoList.size() - 1);
        assertThat(testVentaProducto.getCantidad()).isEqualTo(DEFAULT_CANTIDAD);
        assertThat(testVentaProducto.getTotal()).isEqualTo(DEFAULT_TOTAL);
    }

    @Test
    @Transactional
    void fullUpdateVentaProductoWithPatch() throws Exception {
        // Initialize the database
        ventaProductoRepository.saveAndFlush(ventaProducto);

        int databaseSizeBeforeUpdate = ventaProductoRepository.findAll().size();

        // Update the ventaProducto using partial update
        VentaProducto partialUpdatedVentaProducto = new VentaProducto();
        partialUpdatedVentaProducto.setId(ventaProducto.getId());

        partialUpdatedVentaProducto.cantidad(UPDATED_CANTIDAD).total(UPDATED_TOTAL);

        restVentaProductoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVentaProducto.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVentaProducto))
            )
            .andExpect(status().isOk());

        // Validate the VentaProducto in the database
        List<VentaProducto> ventaProductoList = ventaProductoRepository.findAll();
        assertThat(ventaProductoList).hasSize(databaseSizeBeforeUpdate);
        VentaProducto testVentaProducto = ventaProductoList.get(ventaProductoList.size() - 1);
        assertThat(testVentaProducto.getCantidad()).isEqualTo(UPDATED_CANTIDAD);
        assertThat(testVentaProducto.getTotal()).isEqualTo(UPDATED_TOTAL);
    }

    @Test
    @Transactional
    void patchNonExistingVentaProducto() throws Exception {
        int databaseSizeBeforeUpdate = ventaProductoRepository.findAll().size();
        ventaProducto.setId(count.incrementAndGet());

        // Create the VentaProducto
        VentaProductoDTO ventaProductoDTO = ventaProductoMapper.toDto(ventaProducto);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVentaProductoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ventaProductoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ventaProductoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VentaProducto in the database
        List<VentaProducto> ventaProductoList = ventaProductoRepository.findAll();
        assertThat(ventaProductoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVentaProducto() throws Exception {
        int databaseSizeBeforeUpdate = ventaProductoRepository.findAll().size();
        ventaProducto.setId(count.incrementAndGet());

        // Create the VentaProducto
        VentaProductoDTO ventaProductoDTO = ventaProductoMapper.toDto(ventaProducto);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVentaProductoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ventaProductoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VentaProducto in the database
        List<VentaProducto> ventaProductoList = ventaProductoRepository.findAll();
        assertThat(ventaProductoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVentaProducto() throws Exception {
        int databaseSizeBeforeUpdate = ventaProductoRepository.findAll().size();
        ventaProducto.setId(count.incrementAndGet());

        // Create the VentaProducto
        VentaProductoDTO ventaProductoDTO = ventaProductoMapper.toDto(ventaProducto);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVentaProductoMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ventaProductoDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the VentaProducto in the database
        List<VentaProducto> ventaProductoList = ventaProductoRepository.findAll();
        assertThat(ventaProductoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteVentaProducto() throws Exception {
        // Initialize the database
        ventaProductoRepository.saveAndFlush(ventaProducto);

        int databaseSizeBeforeDelete = ventaProductoRepository.findAll().size();

        // Delete the ventaProducto
        restVentaProductoMockMvc
            .perform(delete(ENTITY_API_URL_ID, ventaProducto.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<VentaProducto> ventaProductoList = ventaProductoRepository.findAll();
        assertThat(ventaProductoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
