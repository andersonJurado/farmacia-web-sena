package org.norn.farmacia.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.norn.farmacia.IntegrationTest;
import org.norn.farmacia.domain.Compra;
import org.norn.farmacia.domain.CompraProducto;
import org.norn.farmacia.domain.Producto;
import org.norn.farmacia.repository.CompraProductoRepository;
import org.norn.farmacia.service.criteria.CompraProductoCriteria;
import org.norn.farmacia.service.dto.CompraProductoDTO;
import org.norn.farmacia.service.mapper.CompraProductoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link CompraProductoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CompraProductoResourceIT {

    private static final Double DEFAULT_CANTIDAD_UDS = 1D;
    private static final Double UPDATED_CANTIDAD_UDS = 2D;
    private static final Double SMALLER_CANTIDAD_UDS = 1D - 1D;

    private static final Double DEFAULT_PRECIO_UDS_COMPRA = 1D;
    private static final Double UPDATED_PRECIO_UDS_COMPRA = 2D;
    private static final Double SMALLER_PRECIO_UDS_COMPRA = 1D - 1D;

    private static final Double DEFAULT_SUB_TOTAL = 1D;
    private static final Double UPDATED_SUB_TOTAL = 2D;
    private static final Double SMALLER_SUB_TOTAL = 1D - 1D;

    private static final Double DEFAULT_IVA = 1D;
    private static final Double UPDATED_IVA = 2D;
    private static final Double SMALLER_IVA = 1D - 1D;

    private static final Double DEFAULT_TOTAL = 1D;
    private static final Double UPDATED_TOTAL = 2D;
    private static final Double SMALLER_TOTAL = 1D - 1D;

    private static final LocalDate DEFAULT_FECHA_VENCIMIENTO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_VENCIMIENTO = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_FECHA_VENCIMIENTO = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_LOTE = "AAAAAAAAAA";
    private static final String UPDATED_LOTE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/compra-productos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CompraProductoRepository compraProductoRepository;

    @Autowired
    private CompraProductoMapper compraProductoMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCompraProductoMockMvc;

    private CompraProducto compraProducto;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CompraProducto createEntity(EntityManager em) {
        CompraProducto compraProducto = new CompraProducto()
            .cantidadUds(DEFAULT_CANTIDAD_UDS)
            .precioUdsCompra(DEFAULT_PRECIO_UDS_COMPRA)
            .subTotal(DEFAULT_SUB_TOTAL)
            .iva(DEFAULT_IVA)
            .total(DEFAULT_TOTAL)
            .fechaVencimiento(DEFAULT_FECHA_VENCIMIENTO)
            .lote(DEFAULT_LOTE);
        return compraProducto;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CompraProducto createUpdatedEntity(EntityManager em) {
        CompraProducto compraProducto = new CompraProducto()
            .cantidadUds(UPDATED_CANTIDAD_UDS)
            .precioUdsCompra(UPDATED_PRECIO_UDS_COMPRA)
            .subTotal(UPDATED_SUB_TOTAL)
            .iva(UPDATED_IVA)
            .total(UPDATED_TOTAL)
            .fechaVencimiento(UPDATED_FECHA_VENCIMIENTO)
            .lote(UPDATED_LOTE);
        return compraProducto;
    }

    @BeforeEach
    public void initTest() {
        compraProducto = createEntity(em);
    }

    @Test
    @Transactional
    void createCompraProducto() throws Exception {
        int databaseSizeBeforeCreate = compraProductoRepository.findAll().size();
        // Create the CompraProducto
        CompraProductoDTO compraProductoDTO = compraProductoMapper.toDto(compraProducto);
        restCompraProductoMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(compraProductoDTO))
            )
            .andExpect(status().isCreated());

        // Validate the CompraProducto in the database
        List<CompraProducto> compraProductoList = compraProductoRepository.findAll();
        assertThat(compraProductoList).hasSize(databaseSizeBeforeCreate + 1);
        CompraProducto testCompraProducto = compraProductoList.get(compraProductoList.size() - 1);
        assertThat(testCompraProducto.getCantidadUds()).isEqualTo(DEFAULT_CANTIDAD_UDS);
        assertThat(testCompraProducto.getPrecioUdsCompra()).isEqualTo(DEFAULT_PRECIO_UDS_COMPRA);
        assertThat(testCompraProducto.getSubTotal()).isEqualTo(DEFAULT_SUB_TOTAL);
        assertThat(testCompraProducto.getIva()).isEqualTo(DEFAULT_IVA);
        assertThat(testCompraProducto.getTotal()).isEqualTo(DEFAULT_TOTAL);
        assertThat(testCompraProducto.getFechaVencimiento()).isEqualTo(DEFAULT_FECHA_VENCIMIENTO);
        assertThat(testCompraProducto.getLote()).isEqualTo(DEFAULT_LOTE);
    }

    @Test
    @Transactional
    void createCompraProductoWithExistingId() throws Exception {
        // Create the CompraProducto with an existing ID
        compraProducto.setId(1L);
        CompraProductoDTO compraProductoDTO = compraProductoMapper.toDto(compraProducto);

        int databaseSizeBeforeCreate = compraProductoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCompraProductoMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(compraProductoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CompraProducto in the database
        List<CompraProducto> compraProductoList = compraProductoRepository.findAll();
        assertThat(compraProductoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCompraProductos() throws Exception {
        // Initialize the database
        compraProductoRepository.saveAndFlush(compraProducto);

        // Get all the compraProductoList
        restCompraProductoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(compraProducto.getId().intValue())))
            .andExpect(jsonPath("$.[*].cantidadUds").value(hasItem(DEFAULT_CANTIDAD_UDS.doubleValue())))
            .andExpect(jsonPath("$.[*].precioUdsCompra").value(hasItem(DEFAULT_PRECIO_UDS_COMPRA.doubleValue())))
            .andExpect(jsonPath("$.[*].subTotal").value(hasItem(DEFAULT_SUB_TOTAL.doubleValue())))
            .andExpect(jsonPath("$.[*].iva").value(hasItem(DEFAULT_IVA.doubleValue())))
            .andExpect(jsonPath("$.[*].total").value(hasItem(DEFAULT_TOTAL.doubleValue())))
            .andExpect(jsonPath("$.[*].fechaVencimiento").value(hasItem(DEFAULT_FECHA_VENCIMIENTO.toString())))
            .andExpect(jsonPath("$.[*].lote").value(hasItem(DEFAULT_LOTE)));
    }

    @Test
    @Transactional
    void getCompraProducto() throws Exception {
        // Initialize the database
        compraProductoRepository.saveAndFlush(compraProducto);

        // Get the compraProducto
        restCompraProductoMockMvc
            .perform(get(ENTITY_API_URL_ID, compraProducto.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(compraProducto.getId().intValue()))
            .andExpect(jsonPath("$.cantidadUds").value(DEFAULT_CANTIDAD_UDS.doubleValue()))
            .andExpect(jsonPath("$.precioUdsCompra").value(DEFAULT_PRECIO_UDS_COMPRA.doubleValue()))
            .andExpect(jsonPath("$.subTotal").value(DEFAULT_SUB_TOTAL.doubleValue()))
            .andExpect(jsonPath("$.iva").value(DEFAULT_IVA.doubleValue()))
            .andExpect(jsonPath("$.total").value(DEFAULT_TOTAL.doubleValue()))
            .andExpect(jsonPath("$.fechaVencimiento").value(DEFAULT_FECHA_VENCIMIENTO.toString()))
            .andExpect(jsonPath("$.lote").value(DEFAULT_LOTE));
    }

    @Test
    @Transactional
    void getCompraProductosByIdFiltering() throws Exception {
        // Initialize the database
        compraProductoRepository.saveAndFlush(compraProducto);

        Long id = compraProducto.getId();

        defaultCompraProductoShouldBeFound("id.equals=" + id);
        defaultCompraProductoShouldNotBeFound("id.notEquals=" + id);

        defaultCompraProductoShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCompraProductoShouldNotBeFound("id.greaterThan=" + id);

        defaultCompraProductoShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCompraProductoShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCompraProductosByCantidadUdsIsEqualToSomething() throws Exception {
        // Initialize the database
        compraProductoRepository.saveAndFlush(compraProducto);

        // Get all the compraProductoList where cantidadUds equals to DEFAULT_CANTIDAD_UDS
        defaultCompraProductoShouldBeFound("cantidadUds.equals=" + DEFAULT_CANTIDAD_UDS);

        // Get all the compraProductoList where cantidadUds equals to UPDATED_CANTIDAD_UDS
        defaultCompraProductoShouldNotBeFound("cantidadUds.equals=" + UPDATED_CANTIDAD_UDS);
    }

    @Test
    @Transactional
    void getAllCompraProductosByCantidadUdsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        compraProductoRepository.saveAndFlush(compraProducto);

        // Get all the compraProductoList where cantidadUds not equals to DEFAULT_CANTIDAD_UDS
        defaultCompraProductoShouldNotBeFound("cantidadUds.notEquals=" + DEFAULT_CANTIDAD_UDS);

        // Get all the compraProductoList where cantidadUds not equals to UPDATED_CANTIDAD_UDS
        defaultCompraProductoShouldBeFound("cantidadUds.notEquals=" + UPDATED_CANTIDAD_UDS);
    }

    @Test
    @Transactional
    void getAllCompraProductosByCantidadUdsIsInShouldWork() throws Exception {
        // Initialize the database
        compraProductoRepository.saveAndFlush(compraProducto);

        // Get all the compraProductoList where cantidadUds in DEFAULT_CANTIDAD_UDS or UPDATED_CANTIDAD_UDS
        defaultCompraProductoShouldBeFound("cantidadUds.in=" + DEFAULT_CANTIDAD_UDS + "," + UPDATED_CANTIDAD_UDS);

        // Get all the compraProductoList where cantidadUds equals to UPDATED_CANTIDAD_UDS
        defaultCompraProductoShouldNotBeFound("cantidadUds.in=" + UPDATED_CANTIDAD_UDS);
    }

    @Test
    @Transactional
    void getAllCompraProductosByCantidadUdsIsNullOrNotNull() throws Exception {
        // Initialize the database
        compraProductoRepository.saveAndFlush(compraProducto);

        // Get all the compraProductoList where cantidadUds is not null
        defaultCompraProductoShouldBeFound("cantidadUds.specified=true");

        // Get all the compraProductoList where cantidadUds is null
        defaultCompraProductoShouldNotBeFound("cantidadUds.specified=false");
    }

    @Test
    @Transactional
    void getAllCompraProductosByCantidadUdsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        compraProductoRepository.saveAndFlush(compraProducto);

        // Get all the compraProductoList where cantidadUds is greater than or equal to DEFAULT_CANTIDAD_UDS
        defaultCompraProductoShouldBeFound("cantidadUds.greaterThanOrEqual=" + DEFAULT_CANTIDAD_UDS);

        // Get all the compraProductoList where cantidadUds is greater than or equal to UPDATED_CANTIDAD_UDS
        defaultCompraProductoShouldNotBeFound("cantidadUds.greaterThanOrEqual=" + UPDATED_CANTIDAD_UDS);
    }

    @Test
    @Transactional
    void getAllCompraProductosByCantidadUdsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        compraProductoRepository.saveAndFlush(compraProducto);

        // Get all the compraProductoList where cantidadUds is less than or equal to DEFAULT_CANTIDAD_UDS
        defaultCompraProductoShouldBeFound("cantidadUds.lessThanOrEqual=" + DEFAULT_CANTIDAD_UDS);

        // Get all the compraProductoList where cantidadUds is less than or equal to SMALLER_CANTIDAD_UDS
        defaultCompraProductoShouldNotBeFound("cantidadUds.lessThanOrEqual=" + SMALLER_CANTIDAD_UDS);
    }

    @Test
    @Transactional
    void getAllCompraProductosByCantidadUdsIsLessThanSomething() throws Exception {
        // Initialize the database
        compraProductoRepository.saveAndFlush(compraProducto);

        // Get all the compraProductoList where cantidadUds is less than DEFAULT_CANTIDAD_UDS
        defaultCompraProductoShouldNotBeFound("cantidadUds.lessThan=" + DEFAULT_CANTIDAD_UDS);

        // Get all the compraProductoList where cantidadUds is less than UPDATED_CANTIDAD_UDS
        defaultCompraProductoShouldBeFound("cantidadUds.lessThan=" + UPDATED_CANTIDAD_UDS);
    }

    @Test
    @Transactional
    void getAllCompraProductosByCantidadUdsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        compraProductoRepository.saveAndFlush(compraProducto);

        // Get all the compraProductoList where cantidadUds is greater than DEFAULT_CANTIDAD_UDS
        defaultCompraProductoShouldNotBeFound("cantidadUds.greaterThan=" + DEFAULT_CANTIDAD_UDS);

        // Get all the compraProductoList where cantidadUds is greater than SMALLER_CANTIDAD_UDS
        defaultCompraProductoShouldBeFound("cantidadUds.greaterThan=" + SMALLER_CANTIDAD_UDS);
    }

    @Test
    @Transactional
    void getAllCompraProductosByPrecioUdsCompraIsEqualToSomething() throws Exception {
        // Initialize the database
        compraProductoRepository.saveAndFlush(compraProducto);

        // Get all the compraProductoList where precioUdsCompra equals to DEFAULT_PRECIO_UDS_COMPRA
        defaultCompraProductoShouldBeFound("precioUdsCompra.equals=" + DEFAULT_PRECIO_UDS_COMPRA);

        // Get all the compraProductoList where precioUdsCompra equals to UPDATED_PRECIO_UDS_COMPRA
        defaultCompraProductoShouldNotBeFound("precioUdsCompra.equals=" + UPDATED_PRECIO_UDS_COMPRA);
    }

    @Test
    @Transactional
    void getAllCompraProductosByPrecioUdsCompraIsNotEqualToSomething() throws Exception {
        // Initialize the database
        compraProductoRepository.saveAndFlush(compraProducto);

        // Get all the compraProductoList where precioUdsCompra not equals to DEFAULT_PRECIO_UDS_COMPRA
        defaultCompraProductoShouldNotBeFound("precioUdsCompra.notEquals=" + DEFAULT_PRECIO_UDS_COMPRA);

        // Get all the compraProductoList where precioUdsCompra not equals to UPDATED_PRECIO_UDS_COMPRA
        defaultCompraProductoShouldBeFound("precioUdsCompra.notEquals=" + UPDATED_PRECIO_UDS_COMPRA);
    }

    @Test
    @Transactional
    void getAllCompraProductosByPrecioUdsCompraIsInShouldWork() throws Exception {
        // Initialize the database
        compraProductoRepository.saveAndFlush(compraProducto);

        // Get all the compraProductoList where precioUdsCompra in DEFAULT_PRECIO_UDS_COMPRA or UPDATED_PRECIO_UDS_COMPRA
        defaultCompraProductoShouldBeFound("precioUdsCompra.in=" + DEFAULT_PRECIO_UDS_COMPRA + "," + UPDATED_PRECIO_UDS_COMPRA);

        // Get all the compraProductoList where precioUdsCompra equals to UPDATED_PRECIO_UDS_COMPRA
        defaultCompraProductoShouldNotBeFound("precioUdsCompra.in=" + UPDATED_PRECIO_UDS_COMPRA);
    }

    @Test
    @Transactional
    void getAllCompraProductosByPrecioUdsCompraIsNullOrNotNull() throws Exception {
        // Initialize the database
        compraProductoRepository.saveAndFlush(compraProducto);

        // Get all the compraProductoList where precioUdsCompra is not null
        defaultCompraProductoShouldBeFound("precioUdsCompra.specified=true");

        // Get all the compraProductoList where precioUdsCompra is null
        defaultCompraProductoShouldNotBeFound("precioUdsCompra.specified=false");
    }

    @Test
    @Transactional
    void getAllCompraProductosByPrecioUdsCompraIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        compraProductoRepository.saveAndFlush(compraProducto);

        // Get all the compraProductoList where precioUdsCompra is greater than or equal to DEFAULT_PRECIO_UDS_COMPRA
        defaultCompraProductoShouldBeFound("precioUdsCompra.greaterThanOrEqual=" + DEFAULT_PRECIO_UDS_COMPRA);

        // Get all the compraProductoList where precioUdsCompra is greater than or equal to UPDATED_PRECIO_UDS_COMPRA
        defaultCompraProductoShouldNotBeFound("precioUdsCompra.greaterThanOrEqual=" + UPDATED_PRECIO_UDS_COMPRA);
    }

    @Test
    @Transactional
    void getAllCompraProductosByPrecioUdsCompraIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        compraProductoRepository.saveAndFlush(compraProducto);

        // Get all the compraProductoList where precioUdsCompra is less than or equal to DEFAULT_PRECIO_UDS_COMPRA
        defaultCompraProductoShouldBeFound("precioUdsCompra.lessThanOrEqual=" + DEFAULT_PRECIO_UDS_COMPRA);

        // Get all the compraProductoList where precioUdsCompra is less than or equal to SMALLER_PRECIO_UDS_COMPRA
        defaultCompraProductoShouldNotBeFound("precioUdsCompra.lessThanOrEqual=" + SMALLER_PRECIO_UDS_COMPRA);
    }

    @Test
    @Transactional
    void getAllCompraProductosByPrecioUdsCompraIsLessThanSomething() throws Exception {
        // Initialize the database
        compraProductoRepository.saveAndFlush(compraProducto);

        // Get all the compraProductoList where precioUdsCompra is less than DEFAULT_PRECIO_UDS_COMPRA
        defaultCompraProductoShouldNotBeFound("precioUdsCompra.lessThan=" + DEFAULT_PRECIO_UDS_COMPRA);

        // Get all the compraProductoList where precioUdsCompra is less than UPDATED_PRECIO_UDS_COMPRA
        defaultCompraProductoShouldBeFound("precioUdsCompra.lessThan=" + UPDATED_PRECIO_UDS_COMPRA);
    }

    @Test
    @Transactional
    void getAllCompraProductosByPrecioUdsCompraIsGreaterThanSomething() throws Exception {
        // Initialize the database
        compraProductoRepository.saveAndFlush(compraProducto);

        // Get all the compraProductoList where precioUdsCompra is greater than DEFAULT_PRECIO_UDS_COMPRA
        defaultCompraProductoShouldNotBeFound("precioUdsCompra.greaterThan=" + DEFAULT_PRECIO_UDS_COMPRA);

        // Get all the compraProductoList where precioUdsCompra is greater than SMALLER_PRECIO_UDS_COMPRA
        defaultCompraProductoShouldBeFound("precioUdsCompra.greaterThan=" + SMALLER_PRECIO_UDS_COMPRA);
    }

    @Test
    @Transactional
    void getAllCompraProductosBySubTotalIsEqualToSomething() throws Exception {
        // Initialize the database
        compraProductoRepository.saveAndFlush(compraProducto);

        // Get all the compraProductoList where subTotal equals to DEFAULT_SUB_TOTAL
        defaultCompraProductoShouldBeFound("subTotal.equals=" + DEFAULT_SUB_TOTAL);

        // Get all the compraProductoList where subTotal equals to UPDATED_SUB_TOTAL
        defaultCompraProductoShouldNotBeFound("subTotal.equals=" + UPDATED_SUB_TOTAL);
    }

    @Test
    @Transactional
    void getAllCompraProductosBySubTotalIsNotEqualToSomething() throws Exception {
        // Initialize the database
        compraProductoRepository.saveAndFlush(compraProducto);

        // Get all the compraProductoList where subTotal not equals to DEFAULT_SUB_TOTAL
        defaultCompraProductoShouldNotBeFound("subTotal.notEquals=" + DEFAULT_SUB_TOTAL);

        // Get all the compraProductoList where subTotal not equals to UPDATED_SUB_TOTAL
        defaultCompraProductoShouldBeFound("subTotal.notEquals=" + UPDATED_SUB_TOTAL);
    }

    @Test
    @Transactional
    void getAllCompraProductosBySubTotalIsInShouldWork() throws Exception {
        // Initialize the database
        compraProductoRepository.saveAndFlush(compraProducto);

        // Get all the compraProductoList where subTotal in DEFAULT_SUB_TOTAL or UPDATED_SUB_TOTAL
        defaultCompraProductoShouldBeFound("subTotal.in=" + DEFAULT_SUB_TOTAL + "," + UPDATED_SUB_TOTAL);

        // Get all the compraProductoList where subTotal equals to UPDATED_SUB_TOTAL
        defaultCompraProductoShouldNotBeFound("subTotal.in=" + UPDATED_SUB_TOTAL);
    }

    @Test
    @Transactional
    void getAllCompraProductosBySubTotalIsNullOrNotNull() throws Exception {
        // Initialize the database
        compraProductoRepository.saveAndFlush(compraProducto);

        // Get all the compraProductoList where subTotal is not null
        defaultCompraProductoShouldBeFound("subTotal.specified=true");

        // Get all the compraProductoList where subTotal is null
        defaultCompraProductoShouldNotBeFound("subTotal.specified=false");
    }

    @Test
    @Transactional
    void getAllCompraProductosBySubTotalIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        compraProductoRepository.saveAndFlush(compraProducto);

        // Get all the compraProductoList where subTotal is greater than or equal to DEFAULT_SUB_TOTAL
        defaultCompraProductoShouldBeFound("subTotal.greaterThanOrEqual=" + DEFAULT_SUB_TOTAL);

        // Get all the compraProductoList where subTotal is greater than or equal to UPDATED_SUB_TOTAL
        defaultCompraProductoShouldNotBeFound("subTotal.greaterThanOrEqual=" + UPDATED_SUB_TOTAL);
    }

    @Test
    @Transactional
    void getAllCompraProductosBySubTotalIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        compraProductoRepository.saveAndFlush(compraProducto);

        // Get all the compraProductoList where subTotal is less than or equal to DEFAULT_SUB_TOTAL
        defaultCompraProductoShouldBeFound("subTotal.lessThanOrEqual=" + DEFAULT_SUB_TOTAL);

        // Get all the compraProductoList where subTotal is less than or equal to SMALLER_SUB_TOTAL
        defaultCompraProductoShouldNotBeFound("subTotal.lessThanOrEqual=" + SMALLER_SUB_TOTAL);
    }

    @Test
    @Transactional
    void getAllCompraProductosBySubTotalIsLessThanSomething() throws Exception {
        // Initialize the database
        compraProductoRepository.saveAndFlush(compraProducto);

        // Get all the compraProductoList where subTotal is less than DEFAULT_SUB_TOTAL
        defaultCompraProductoShouldNotBeFound("subTotal.lessThan=" + DEFAULT_SUB_TOTAL);

        // Get all the compraProductoList where subTotal is less than UPDATED_SUB_TOTAL
        defaultCompraProductoShouldBeFound("subTotal.lessThan=" + UPDATED_SUB_TOTAL);
    }

    @Test
    @Transactional
    void getAllCompraProductosBySubTotalIsGreaterThanSomething() throws Exception {
        // Initialize the database
        compraProductoRepository.saveAndFlush(compraProducto);

        // Get all the compraProductoList where subTotal is greater than DEFAULT_SUB_TOTAL
        defaultCompraProductoShouldNotBeFound("subTotal.greaterThan=" + DEFAULT_SUB_TOTAL);

        // Get all the compraProductoList where subTotal is greater than SMALLER_SUB_TOTAL
        defaultCompraProductoShouldBeFound("subTotal.greaterThan=" + SMALLER_SUB_TOTAL);
    }

    @Test
    @Transactional
    void getAllCompraProductosByIvaIsEqualToSomething() throws Exception {
        // Initialize the database
        compraProductoRepository.saveAndFlush(compraProducto);

        // Get all the compraProductoList where iva equals to DEFAULT_IVA
        defaultCompraProductoShouldBeFound("iva.equals=" + DEFAULT_IVA);

        // Get all the compraProductoList where iva equals to UPDATED_IVA
        defaultCompraProductoShouldNotBeFound("iva.equals=" + UPDATED_IVA);
    }

    @Test
    @Transactional
    void getAllCompraProductosByIvaIsNotEqualToSomething() throws Exception {
        // Initialize the database
        compraProductoRepository.saveAndFlush(compraProducto);

        // Get all the compraProductoList where iva not equals to DEFAULT_IVA
        defaultCompraProductoShouldNotBeFound("iva.notEquals=" + DEFAULT_IVA);

        // Get all the compraProductoList where iva not equals to UPDATED_IVA
        defaultCompraProductoShouldBeFound("iva.notEquals=" + UPDATED_IVA);
    }

    @Test
    @Transactional
    void getAllCompraProductosByIvaIsInShouldWork() throws Exception {
        // Initialize the database
        compraProductoRepository.saveAndFlush(compraProducto);

        // Get all the compraProductoList where iva in DEFAULT_IVA or UPDATED_IVA
        defaultCompraProductoShouldBeFound("iva.in=" + DEFAULT_IVA + "," + UPDATED_IVA);

        // Get all the compraProductoList where iva equals to UPDATED_IVA
        defaultCompraProductoShouldNotBeFound("iva.in=" + UPDATED_IVA);
    }

    @Test
    @Transactional
    void getAllCompraProductosByIvaIsNullOrNotNull() throws Exception {
        // Initialize the database
        compraProductoRepository.saveAndFlush(compraProducto);

        // Get all the compraProductoList where iva is not null
        defaultCompraProductoShouldBeFound("iva.specified=true");

        // Get all the compraProductoList where iva is null
        defaultCompraProductoShouldNotBeFound("iva.specified=false");
    }

    @Test
    @Transactional
    void getAllCompraProductosByIvaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        compraProductoRepository.saveAndFlush(compraProducto);

        // Get all the compraProductoList where iva is greater than or equal to DEFAULT_IVA
        defaultCompraProductoShouldBeFound("iva.greaterThanOrEqual=" + DEFAULT_IVA);

        // Get all the compraProductoList where iva is greater than or equal to UPDATED_IVA
        defaultCompraProductoShouldNotBeFound("iva.greaterThanOrEqual=" + UPDATED_IVA);
    }

    @Test
    @Transactional
    void getAllCompraProductosByIvaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        compraProductoRepository.saveAndFlush(compraProducto);

        // Get all the compraProductoList where iva is less than or equal to DEFAULT_IVA
        defaultCompraProductoShouldBeFound("iva.lessThanOrEqual=" + DEFAULT_IVA);

        // Get all the compraProductoList where iva is less than or equal to SMALLER_IVA
        defaultCompraProductoShouldNotBeFound("iva.lessThanOrEqual=" + SMALLER_IVA);
    }

    @Test
    @Transactional
    void getAllCompraProductosByIvaIsLessThanSomething() throws Exception {
        // Initialize the database
        compraProductoRepository.saveAndFlush(compraProducto);

        // Get all the compraProductoList where iva is less than DEFAULT_IVA
        defaultCompraProductoShouldNotBeFound("iva.lessThan=" + DEFAULT_IVA);

        // Get all the compraProductoList where iva is less than UPDATED_IVA
        defaultCompraProductoShouldBeFound("iva.lessThan=" + UPDATED_IVA);
    }

    @Test
    @Transactional
    void getAllCompraProductosByIvaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        compraProductoRepository.saveAndFlush(compraProducto);

        // Get all the compraProductoList where iva is greater than DEFAULT_IVA
        defaultCompraProductoShouldNotBeFound("iva.greaterThan=" + DEFAULT_IVA);

        // Get all the compraProductoList where iva is greater than SMALLER_IVA
        defaultCompraProductoShouldBeFound("iva.greaterThan=" + SMALLER_IVA);
    }

    @Test
    @Transactional
    void getAllCompraProductosByTotalIsEqualToSomething() throws Exception {
        // Initialize the database
        compraProductoRepository.saveAndFlush(compraProducto);

        // Get all the compraProductoList where total equals to DEFAULT_TOTAL
        defaultCompraProductoShouldBeFound("total.equals=" + DEFAULT_TOTAL);

        // Get all the compraProductoList where total equals to UPDATED_TOTAL
        defaultCompraProductoShouldNotBeFound("total.equals=" + UPDATED_TOTAL);
    }

    @Test
    @Transactional
    void getAllCompraProductosByTotalIsNotEqualToSomething() throws Exception {
        // Initialize the database
        compraProductoRepository.saveAndFlush(compraProducto);

        // Get all the compraProductoList where total not equals to DEFAULT_TOTAL
        defaultCompraProductoShouldNotBeFound("total.notEquals=" + DEFAULT_TOTAL);

        // Get all the compraProductoList where total not equals to UPDATED_TOTAL
        defaultCompraProductoShouldBeFound("total.notEquals=" + UPDATED_TOTAL);
    }

    @Test
    @Transactional
    void getAllCompraProductosByTotalIsInShouldWork() throws Exception {
        // Initialize the database
        compraProductoRepository.saveAndFlush(compraProducto);

        // Get all the compraProductoList where total in DEFAULT_TOTAL or UPDATED_TOTAL
        defaultCompraProductoShouldBeFound("total.in=" + DEFAULT_TOTAL + "," + UPDATED_TOTAL);

        // Get all the compraProductoList where total equals to UPDATED_TOTAL
        defaultCompraProductoShouldNotBeFound("total.in=" + UPDATED_TOTAL);
    }

    @Test
    @Transactional
    void getAllCompraProductosByTotalIsNullOrNotNull() throws Exception {
        // Initialize the database
        compraProductoRepository.saveAndFlush(compraProducto);

        // Get all the compraProductoList where total is not null
        defaultCompraProductoShouldBeFound("total.specified=true");

        // Get all the compraProductoList where total is null
        defaultCompraProductoShouldNotBeFound("total.specified=false");
    }

    @Test
    @Transactional
    void getAllCompraProductosByTotalIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        compraProductoRepository.saveAndFlush(compraProducto);

        // Get all the compraProductoList where total is greater than or equal to DEFAULT_TOTAL
        defaultCompraProductoShouldBeFound("total.greaterThanOrEqual=" + DEFAULT_TOTAL);

        // Get all the compraProductoList where total is greater than or equal to UPDATED_TOTAL
        defaultCompraProductoShouldNotBeFound("total.greaterThanOrEqual=" + UPDATED_TOTAL);
    }

    @Test
    @Transactional
    void getAllCompraProductosByTotalIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        compraProductoRepository.saveAndFlush(compraProducto);

        // Get all the compraProductoList where total is less than or equal to DEFAULT_TOTAL
        defaultCompraProductoShouldBeFound("total.lessThanOrEqual=" + DEFAULT_TOTAL);

        // Get all the compraProductoList where total is less than or equal to SMALLER_TOTAL
        defaultCompraProductoShouldNotBeFound("total.lessThanOrEqual=" + SMALLER_TOTAL);
    }

    @Test
    @Transactional
    void getAllCompraProductosByTotalIsLessThanSomething() throws Exception {
        // Initialize the database
        compraProductoRepository.saveAndFlush(compraProducto);

        // Get all the compraProductoList where total is less than DEFAULT_TOTAL
        defaultCompraProductoShouldNotBeFound("total.lessThan=" + DEFAULT_TOTAL);

        // Get all the compraProductoList where total is less than UPDATED_TOTAL
        defaultCompraProductoShouldBeFound("total.lessThan=" + UPDATED_TOTAL);
    }

    @Test
    @Transactional
    void getAllCompraProductosByTotalIsGreaterThanSomething() throws Exception {
        // Initialize the database
        compraProductoRepository.saveAndFlush(compraProducto);

        // Get all the compraProductoList where total is greater than DEFAULT_TOTAL
        defaultCompraProductoShouldNotBeFound("total.greaterThan=" + DEFAULT_TOTAL);

        // Get all the compraProductoList where total is greater than SMALLER_TOTAL
        defaultCompraProductoShouldBeFound("total.greaterThan=" + SMALLER_TOTAL);
    }

    @Test
    @Transactional
    void getAllCompraProductosByFechaVencimientoIsEqualToSomething() throws Exception {
        // Initialize the database
        compraProductoRepository.saveAndFlush(compraProducto);

        // Get all the compraProductoList where fechaVencimiento equals to DEFAULT_FECHA_VENCIMIENTO
        defaultCompraProductoShouldBeFound("fechaVencimiento.equals=" + DEFAULT_FECHA_VENCIMIENTO);

        // Get all the compraProductoList where fechaVencimiento equals to UPDATED_FECHA_VENCIMIENTO
        defaultCompraProductoShouldNotBeFound("fechaVencimiento.equals=" + UPDATED_FECHA_VENCIMIENTO);
    }

    @Test
    @Transactional
    void getAllCompraProductosByFechaVencimientoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        compraProductoRepository.saveAndFlush(compraProducto);

        // Get all the compraProductoList where fechaVencimiento not equals to DEFAULT_FECHA_VENCIMIENTO
        defaultCompraProductoShouldNotBeFound("fechaVencimiento.notEquals=" + DEFAULT_FECHA_VENCIMIENTO);

        // Get all the compraProductoList where fechaVencimiento not equals to UPDATED_FECHA_VENCIMIENTO
        defaultCompraProductoShouldBeFound("fechaVencimiento.notEquals=" + UPDATED_FECHA_VENCIMIENTO);
    }

    @Test
    @Transactional
    void getAllCompraProductosByFechaVencimientoIsInShouldWork() throws Exception {
        // Initialize the database
        compraProductoRepository.saveAndFlush(compraProducto);

        // Get all the compraProductoList where fechaVencimiento in DEFAULT_FECHA_VENCIMIENTO or UPDATED_FECHA_VENCIMIENTO
        defaultCompraProductoShouldBeFound("fechaVencimiento.in=" + DEFAULT_FECHA_VENCIMIENTO + "," + UPDATED_FECHA_VENCIMIENTO);

        // Get all the compraProductoList where fechaVencimiento equals to UPDATED_FECHA_VENCIMIENTO
        defaultCompraProductoShouldNotBeFound("fechaVencimiento.in=" + UPDATED_FECHA_VENCIMIENTO);
    }

    @Test
    @Transactional
    void getAllCompraProductosByFechaVencimientoIsNullOrNotNull() throws Exception {
        // Initialize the database
        compraProductoRepository.saveAndFlush(compraProducto);

        // Get all the compraProductoList where fechaVencimiento is not null
        defaultCompraProductoShouldBeFound("fechaVencimiento.specified=true");

        // Get all the compraProductoList where fechaVencimiento is null
        defaultCompraProductoShouldNotBeFound("fechaVencimiento.specified=false");
    }

    @Test
    @Transactional
    void getAllCompraProductosByFechaVencimientoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        compraProductoRepository.saveAndFlush(compraProducto);

        // Get all the compraProductoList where fechaVencimiento is greater than or equal to DEFAULT_FECHA_VENCIMIENTO
        defaultCompraProductoShouldBeFound("fechaVencimiento.greaterThanOrEqual=" + DEFAULT_FECHA_VENCIMIENTO);

        // Get all the compraProductoList where fechaVencimiento is greater than or equal to UPDATED_FECHA_VENCIMIENTO
        defaultCompraProductoShouldNotBeFound("fechaVencimiento.greaterThanOrEqual=" + UPDATED_FECHA_VENCIMIENTO);
    }

    @Test
    @Transactional
    void getAllCompraProductosByFechaVencimientoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        compraProductoRepository.saveAndFlush(compraProducto);

        // Get all the compraProductoList where fechaVencimiento is less than or equal to DEFAULT_FECHA_VENCIMIENTO
        defaultCompraProductoShouldBeFound("fechaVencimiento.lessThanOrEqual=" + DEFAULT_FECHA_VENCIMIENTO);

        // Get all the compraProductoList where fechaVencimiento is less than or equal to SMALLER_FECHA_VENCIMIENTO
        defaultCompraProductoShouldNotBeFound("fechaVencimiento.lessThanOrEqual=" + SMALLER_FECHA_VENCIMIENTO);
    }

    @Test
    @Transactional
    void getAllCompraProductosByFechaVencimientoIsLessThanSomething() throws Exception {
        // Initialize the database
        compraProductoRepository.saveAndFlush(compraProducto);

        // Get all the compraProductoList where fechaVencimiento is less than DEFAULT_FECHA_VENCIMIENTO
        defaultCompraProductoShouldNotBeFound("fechaVencimiento.lessThan=" + DEFAULT_FECHA_VENCIMIENTO);

        // Get all the compraProductoList where fechaVencimiento is less than UPDATED_FECHA_VENCIMIENTO
        defaultCompraProductoShouldBeFound("fechaVencimiento.lessThan=" + UPDATED_FECHA_VENCIMIENTO);
    }

    @Test
    @Transactional
    void getAllCompraProductosByFechaVencimientoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        compraProductoRepository.saveAndFlush(compraProducto);

        // Get all the compraProductoList where fechaVencimiento is greater than DEFAULT_FECHA_VENCIMIENTO
        defaultCompraProductoShouldNotBeFound("fechaVencimiento.greaterThan=" + DEFAULT_FECHA_VENCIMIENTO);

        // Get all the compraProductoList where fechaVencimiento is greater than SMALLER_FECHA_VENCIMIENTO
        defaultCompraProductoShouldBeFound("fechaVencimiento.greaterThan=" + SMALLER_FECHA_VENCIMIENTO);
    }

    @Test
    @Transactional
    void getAllCompraProductosByLoteIsEqualToSomething() throws Exception {
        // Initialize the database
        compraProductoRepository.saveAndFlush(compraProducto);

        // Get all the compraProductoList where lote equals to DEFAULT_LOTE
        defaultCompraProductoShouldBeFound("lote.equals=" + DEFAULT_LOTE);

        // Get all the compraProductoList where lote equals to UPDATED_LOTE
        defaultCompraProductoShouldNotBeFound("lote.equals=" + UPDATED_LOTE);
    }

    @Test
    @Transactional
    void getAllCompraProductosByLoteIsNotEqualToSomething() throws Exception {
        // Initialize the database
        compraProductoRepository.saveAndFlush(compraProducto);

        // Get all the compraProductoList where lote not equals to DEFAULT_LOTE
        defaultCompraProductoShouldNotBeFound("lote.notEquals=" + DEFAULT_LOTE);

        // Get all the compraProductoList where lote not equals to UPDATED_LOTE
        defaultCompraProductoShouldBeFound("lote.notEquals=" + UPDATED_LOTE);
    }

    @Test
    @Transactional
    void getAllCompraProductosByLoteIsInShouldWork() throws Exception {
        // Initialize the database
        compraProductoRepository.saveAndFlush(compraProducto);

        // Get all the compraProductoList where lote in DEFAULT_LOTE or UPDATED_LOTE
        defaultCompraProductoShouldBeFound("lote.in=" + DEFAULT_LOTE + "," + UPDATED_LOTE);

        // Get all the compraProductoList where lote equals to UPDATED_LOTE
        defaultCompraProductoShouldNotBeFound("lote.in=" + UPDATED_LOTE);
    }

    @Test
    @Transactional
    void getAllCompraProductosByLoteIsNullOrNotNull() throws Exception {
        // Initialize the database
        compraProductoRepository.saveAndFlush(compraProducto);

        // Get all the compraProductoList where lote is not null
        defaultCompraProductoShouldBeFound("lote.specified=true");

        // Get all the compraProductoList where lote is null
        defaultCompraProductoShouldNotBeFound("lote.specified=false");
    }

    @Test
    @Transactional
    void getAllCompraProductosByLoteContainsSomething() throws Exception {
        // Initialize the database
        compraProductoRepository.saveAndFlush(compraProducto);

        // Get all the compraProductoList where lote contains DEFAULT_LOTE
        defaultCompraProductoShouldBeFound("lote.contains=" + DEFAULT_LOTE);

        // Get all the compraProductoList where lote contains UPDATED_LOTE
        defaultCompraProductoShouldNotBeFound("lote.contains=" + UPDATED_LOTE);
    }

    @Test
    @Transactional
    void getAllCompraProductosByLoteNotContainsSomething() throws Exception {
        // Initialize the database
        compraProductoRepository.saveAndFlush(compraProducto);

        // Get all the compraProductoList where lote does not contain DEFAULT_LOTE
        defaultCompraProductoShouldNotBeFound("lote.doesNotContain=" + DEFAULT_LOTE);

        // Get all the compraProductoList where lote does not contain UPDATED_LOTE
        defaultCompraProductoShouldBeFound("lote.doesNotContain=" + UPDATED_LOTE);
    }

    @Test
    @Transactional
    void getAllCompraProductosByProductoIsEqualToSomething() throws Exception {
        // Initialize the database
        compraProductoRepository.saveAndFlush(compraProducto);
        Producto producto = ProductoResourceIT.createEntity(em);
        em.persist(producto);
        em.flush();
        compraProducto.setProducto(producto);
        compraProductoRepository.saveAndFlush(compraProducto);
        Long productoId = producto.getId();

        // Get all the compraProductoList where producto equals to productoId
        defaultCompraProductoShouldBeFound("productoId.equals=" + productoId);

        // Get all the compraProductoList where producto equals to (productoId + 1)
        defaultCompraProductoShouldNotBeFound("productoId.equals=" + (productoId + 1));
    }

    @Test
    @Transactional
    void getAllCompraProductosByCompraIsEqualToSomething() throws Exception {
        // Initialize the database
        compraProductoRepository.saveAndFlush(compraProducto);
        Compra compra = CompraResourceIT.createEntity(em);
        em.persist(compra);
        em.flush();
        compraProducto.setCompra(compra);
        compraProductoRepository.saveAndFlush(compraProducto);
        Long compraId = compra.getId();

        // Get all the compraProductoList where compra equals to compraId
        defaultCompraProductoShouldBeFound("compraId.equals=" + compraId);

        // Get all the compraProductoList where compra equals to (compraId + 1)
        defaultCompraProductoShouldNotBeFound("compraId.equals=" + (compraId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCompraProductoShouldBeFound(String filter) throws Exception {
        restCompraProductoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(compraProducto.getId().intValue())))
            .andExpect(jsonPath("$.[*].cantidadUds").value(hasItem(DEFAULT_CANTIDAD_UDS.doubleValue())))
            .andExpect(jsonPath("$.[*].precioUdsCompra").value(hasItem(DEFAULT_PRECIO_UDS_COMPRA.doubleValue())))
            .andExpect(jsonPath("$.[*].subTotal").value(hasItem(DEFAULT_SUB_TOTAL.doubleValue())))
            .andExpect(jsonPath("$.[*].iva").value(hasItem(DEFAULT_IVA.doubleValue())))
            .andExpect(jsonPath("$.[*].total").value(hasItem(DEFAULT_TOTAL.doubleValue())))
            .andExpect(jsonPath("$.[*].fechaVencimiento").value(hasItem(DEFAULT_FECHA_VENCIMIENTO.toString())))
            .andExpect(jsonPath("$.[*].lote").value(hasItem(DEFAULT_LOTE)));

        // Check, that the count call also returns 1
        restCompraProductoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCompraProductoShouldNotBeFound(String filter) throws Exception {
        restCompraProductoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCompraProductoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCompraProducto() throws Exception {
        // Get the compraProducto
        restCompraProductoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCompraProducto() throws Exception {
        // Initialize the database
        compraProductoRepository.saveAndFlush(compraProducto);

        int databaseSizeBeforeUpdate = compraProductoRepository.findAll().size();

        // Update the compraProducto
        CompraProducto updatedCompraProducto = compraProductoRepository.findById(compraProducto.getId()).get();
        // Disconnect from session so that the updates on updatedCompraProducto are not directly saved in db
        em.detach(updatedCompraProducto);
        updatedCompraProducto
            .cantidadUds(UPDATED_CANTIDAD_UDS)
            .precioUdsCompra(UPDATED_PRECIO_UDS_COMPRA)
            .subTotal(UPDATED_SUB_TOTAL)
            .iva(UPDATED_IVA)
            .total(UPDATED_TOTAL)
            .fechaVencimiento(UPDATED_FECHA_VENCIMIENTO)
            .lote(UPDATED_LOTE);
        CompraProductoDTO compraProductoDTO = compraProductoMapper.toDto(updatedCompraProducto);

        restCompraProductoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, compraProductoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(compraProductoDTO))
            )
            .andExpect(status().isOk());

        // Validate the CompraProducto in the database
        List<CompraProducto> compraProductoList = compraProductoRepository.findAll();
        assertThat(compraProductoList).hasSize(databaseSizeBeforeUpdate);
        CompraProducto testCompraProducto = compraProductoList.get(compraProductoList.size() - 1);
        assertThat(testCompraProducto.getCantidadUds()).isEqualTo(UPDATED_CANTIDAD_UDS);
        assertThat(testCompraProducto.getPrecioUdsCompra()).isEqualTo(UPDATED_PRECIO_UDS_COMPRA);
        assertThat(testCompraProducto.getSubTotal()).isEqualTo(UPDATED_SUB_TOTAL);
        assertThat(testCompraProducto.getIva()).isEqualTo(UPDATED_IVA);
        assertThat(testCompraProducto.getTotal()).isEqualTo(UPDATED_TOTAL);
        assertThat(testCompraProducto.getFechaVencimiento()).isEqualTo(UPDATED_FECHA_VENCIMIENTO);
        assertThat(testCompraProducto.getLote()).isEqualTo(UPDATED_LOTE);
    }

    @Test
    @Transactional
    void putNonExistingCompraProducto() throws Exception {
        int databaseSizeBeforeUpdate = compraProductoRepository.findAll().size();
        compraProducto.setId(count.incrementAndGet());

        // Create the CompraProducto
        CompraProductoDTO compraProductoDTO = compraProductoMapper.toDto(compraProducto);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCompraProductoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, compraProductoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(compraProductoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CompraProducto in the database
        List<CompraProducto> compraProductoList = compraProductoRepository.findAll();
        assertThat(compraProductoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCompraProducto() throws Exception {
        int databaseSizeBeforeUpdate = compraProductoRepository.findAll().size();
        compraProducto.setId(count.incrementAndGet());

        // Create the CompraProducto
        CompraProductoDTO compraProductoDTO = compraProductoMapper.toDto(compraProducto);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompraProductoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(compraProductoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CompraProducto in the database
        List<CompraProducto> compraProductoList = compraProductoRepository.findAll();
        assertThat(compraProductoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCompraProducto() throws Exception {
        int databaseSizeBeforeUpdate = compraProductoRepository.findAll().size();
        compraProducto.setId(count.incrementAndGet());

        // Create the CompraProducto
        CompraProductoDTO compraProductoDTO = compraProductoMapper.toDto(compraProducto);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompraProductoMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(compraProductoDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CompraProducto in the database
        List<CompraProducto> compraProductoList = compraProductoRepository.findAll();
        assertThat(compraProductoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCompraProductoWithPatch() throws Exception {
        // Initialize the database
        compraProductoRepository.saveAndFlush(compraProducto);

        int databaseSizeBeforeUpdate = compraProductoRepository.findAll().size();

        // Update the compraProducto using partial update
        CompraProducto partialUpdatedCompraProducto = new CompraProducto();
        partialUpdatedCompraProducto.setId(compraProducto.getId());

        partialUpdatedCompraProducto.cantidadUds(UPDATED_CANTIDAD_UDS).iva(UPDATED_IVA).lote(UPDATED_LOTE);

        restCompraProductoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCompraProducto.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCompraProducto))
            )
            .andExpect(status().isOk());

        // Validate the CompraProducto in the database
        List<CompraProducto> compraProductoList = compraProductoRepository.findAll();
        assertThat(compraProductoList).hasSize(databaseSizeBeforeUpdate);
        CompraProducto testCompraProducto = compraProductoList.get(compraProductoList.size() - 1);
        assertThat(testCompraProducto.getCantidadUds()).isEqualTo(UPDATED_CANTIDAD_UDS);
        assertThat(testCompraProducto.getPrecioUdsCompra()).isEqualTo(DEFAULT_PRECIO_UDS_COMPRA);
        assertThat(testCompraProducto.getSubTotal()).isEqualTo(DEFAULT_SUB_TOTAL);
        assertThat(testCompraProducto.getIva()).isEqualTo(UPDATED_IVA);
        assertThat(testCompraProducto.getTotal()).isEqualTo(DEFAULT_TOTAL);
        assertThat(testCompraProducto.getFechaVencimiento()).isEqualTo(DEFAULT_FECHA_VENCIMIENTO);
        assertThat(testCompraProducto.getLote()).isEqualTo(UPDATED_LOTE);
    }

    @Test
    @Transactional
    void fullUpdateCompraProductoWithPatch() throws Exception {
        // Initialize the database
        compraProductoRepository.saveAndFlush(compraProducto);

        int databaseSizeBeforeUpdate = compraProductoRepository.findAll().size();

        // Update the compraProducto using partial update
        CompraProducto partialUpdatedCompraProducto = new CompraProducto();
        partialUpdatedCompraProducto.setId(compraProducto.getId());

        partialUpdatedCompraProducto
            .cantidadUds(UPDATED_CANTIDAD_UDS)
            .precioUdsCompra(UPDATED_PRECIO_UDS_COMPRA)
            .subTotal(UPDATED_SUB_TOTAL)
            .iva(UPDATED_IVA)
            .total(UPDATED_TOTAL)
            .fechaVencimiento(UPDATED_FECHA_VENCIMIENTO)
            .lote(UPDATED_LOTE);

        restCompraProductoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCompraProducto.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCompraProducto))
            )
            .andExpect(status().isOk());

        // Validate the CompraProducto in the database
        List<CompraProducto> compraProductoList = compraProductoRepository.findAll();
        assertThat(compraProductoList).hasSize(databaseSizeBeforeUpdate);
        CompraProducto testCompraProducto = compraProductoList.get(compraProductoList.size() - 1);
        assertThat(testCompraProducto.getCantidadUds()).isEqualTo(UPDATED_CANTIDAD_UDS);
        assertThat(testCompraProducto.getPrecioUdsCompra()).isEqualTo(UPDATED_PRECIO_UDS_COMPRA);
        assertThat(testCompraProducto.getSubTotal()).isEqualTo(UPDATED_SUB_TOTAL);
        assertThat(testCompraProducto.getIva()).isEqualTo(UPDATED_IVA);
        assertThat(testCompraProducto.getTotal()).isEqualTo(UPDATED_TOTAL);
        assertThat(testCompraProducto.getFechaVencimiento()).isEqualTo(UPDATED_FECHA_VENCIMIENTO);
        assertThat(testCompraProducto.getLote()).isEqualTo(UPDATED_LOTE);
    }

    @Test
    @Transactional
    void patchNonExistingCompraProducto() throws Exception {
        int databaseSizeBeforeUpdate = compraProductoRepository.findAll().size();
        compraProducto.setId(count.incrementAndGet());

        // Create the CompraProducto
        CompraProductoDTO compraProductoDTO = compraProductoMapper.toDto(compraProducto);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCompraProductoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, compraProductoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(compraProductoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CompraProducto in the database
        List<CompraProducto> compraProductoList = compraProductoRepository.findAll();
        assertThat(compraProductoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCompraProducto() throws Exception {
        int databaseSizeBeforeUpdate = compraProductoRepository.findAll().size();
        compraProducto.setId(count.incrementAndGet());

        // Create the CompraProducto
        CompraProductoDTO compraProductoDTO = compraProductoMapper.toDto(compraProducto);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompraProductoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(compraProductoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CompraProducto in the database
        List<CompraProducto> compraProductoList = compraProductoRepository.findAll();
        assertThat(compraProductoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCompraProducto() throws Exception {
        int databaseSizeBeforeUpdate = compraProductoRepository.findAll().size();
        compraProducto.setId(count.incrementAndGet());

        // Create the CompraProducto
        CompraProductoDTO compraProductoDTO = compraProductoMapper.toDto(compraProducto);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompraProductoMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(compraProductoDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CompraProducto in the database
        List<CompraProducto> compraProductoList = compraProductoRepository.findAll();
        assertThat(compraProductoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCompraProducto() throws Exception {
        // Initialize the database
        compraProductoRepository.saveAndFlush(compraProducto);

        int databaseSizeBeforeDelete = compraProductoRepository.findAll().size();

        // Delete the compraProducto
        restCompraProductoMockMvc
            .perform(delete(ENTITY_API_URL_ID, compraProducto.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CompraProducto> compraProductoList = compraProductoRepository.findAll();
        assertThat(compraProductoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
