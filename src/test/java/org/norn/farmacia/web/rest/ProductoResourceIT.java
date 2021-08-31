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
import org.norn.farmacia.domain.CompraProducto;
import org.norn.farmacia.domain.Laboratorio;
import org.norn.farmacia.domain.LineaProducto;
import org.norn.farmacia.domain.Presentacion;
import org.norn.farmacia.domain.Producto;
import org.norn.farmacia.domain.VentaProducto;
import org.norn.farmacia.repository.ProductoRepository;
import org.norn.farmacia.service.criteria.ProductoCriteria;
import org.norn.farmacia.service.dto.ProductoDTO;
import org.norn.farmacia.service.mapper.ProductoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ProductoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProductoResourceIT {

    private static final String DEFAULT_NOMBRE_PRODUCTO = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE_PRODUCTO = "BBBBBBBBBB";

    private static final Integer DEFAULT_CANTIDAD = 1;
    private static final Integer UPDATED_CANTIDAD = 2;
    private static final Integer SMALLER_CANTIDAD = 1 - 1;

    private static final Double DEFAULT_IVA = 1D;
    private static final Double UPDATED_IVA = 2D;
    private static final Double SMALLER_IVA = 1D - 1D;

    private static final Double DEFAULT_PRECIO_UDS_VENTA = 1D;
    private static final Double UPDATED_PRECIO_UDS_VENTA = 2D;
    private static final Double SMALLER_PRECIO_UDS_VENTA = 1D - 1D;

    private static final Double DEFAULT_MARGEN_DE_GANANCIA = 1D;
    private static final Double UPDATED_MARGEN_DE_GANANCIA = 2D;
    private static final Double SMALLER_MARGEN_DE_GANANCIA = 1D - 1D;

    private static final String DEFAULT_INVIMA = "AAAAAAAAAA";
    private static final String UPDATED_INVIMA = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/productos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private ProductoMapper productoMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductoMockMvc;

    private Producto producto;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Producto createEntity(EntityManager em) {
        Producto producto = new Producto()
            .nombreProducto(DEFAULT_NOMBRE_PRODUCTO)
            .cantidad(DEFAULT_CANTIDAD)
            .iva(DEFAULT_IVA)
            .precioUdsVenta(DEFAULT_PRECIO_UDS_VENTA)
            .margenDeGanancia(DEFAULT_MARGEN_DE_GANANCIA)
            .invima(DEFAULT_INVIMA);
        return producto;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Producto createUpdatedEntity(EntityManager em) {
        Producto producto = new Producto()
            .nombreProducto(UPDATED_NOMBRE_PRODUCTO)
            .cantidad(UPDATED_CANTIDAD)
            .iva(UPDATED_IVA)
            .precioUdsVenta(UPDATED_PRECIO_UDS_VENTA)
            .margenDeGanancia(UPDATED_MARGEN_DE_GANANCIA)
            .invima(UPDATED_INVIMA);
        return producto;
    }

    @BeforeEach
    public void initTest() {
        producto = createEntity(em);
    }

    @Test
    @Transactional
    void createProducto() throws Exception {
        int databaseSizeBeforeCreate = productoRepository.findAll().size();
        // Create the Producto
        ProductoDTO productoDTO = productoMapper.toDto(producto);
        restProductoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productoDTO)))
            .andExpect(status().isCreated());

        // Validate the Producto in the database
        List<Producto> productoList = productoRepository.findAll();
        assertThat(productoList).hasSize(databaseSizeBeforeCreate + 1);
        Producto testProducto = productoList.get(productoList.size() - 1);
        assertThat(testProducto.getNombreProducto()).isEqualTo(DEFAULT_NOMBRE_PRODUCTO);
        assertThat(testProducto.getCantidad()).isEqualTo(DEFAULT_CANTIDAD);
        assertThat(testProducto.getIva()).isEqualTo(DEFAULT_IVA);
        assertThat(testProducto.getPrecioUdsVenta()).isEqualTo(DEFAULT_PRECIO_UDS_VENTA);
        assertThat(testProducto.getMargenDeGanancia()).isEqualTo(DEFAULT_MARGEN_DE_GANANCIA);
        assertThat(testProducto.getInvima()).isEqualTo(DEFAULT_INVIMA);
    }

    @Test
    @Transactional
    void createProductoWithExistingId() throws Exception {
        // Create the Producto with an existing ID
        producto.setId(1L);
        ProductoDTO productoDTO = productoMapper.toDto(producto);

        int databaseSizeBeforeCreate = productoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Producto in the database
        List<Producto> productoList = productoRepository.findAll();
        assertThat(productoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNombreProductoIsRequired() throws Exception {
        int databaseSizeBeforeTest = productoRepository.findAll().size();
        // set the field null
        producto.setNombreProducto(null);

        // Create the Producto, which fails.
        ProductoDTO productoDTO = productoMapper.toDto(producto);

        restProductoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productoDTO)))
            .andExpect(status().isBadRequest());

        List<Producto> productoList = productoRepository.findAll();
        assertThat(productoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProductos() throws Exception {
        // Initialize the database
        productoRepository.saveAndFlush(producto);

        // Get all the productoList
        restProductoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(producto.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombreProducto").value(hasItem(DEFAULT_NOMBRE_PRODUCTO)))
            .andExpect(jsonPath("$.[*].cantidad").value(hasItem(DEFAULT_CANTIDAD)))
            .andExpect(jsonPath("$.[*].iva").value(hasItem(DEFAULT_IVA.doubleValue())))
            .andExpect(jsonPath("$.[*].precioUdsVenta").value(hasItem(DEFAULT_PRECIO_UDS_VENTA.doubleValue())))
            .andExpect(jsonPath("$.[*].margenDeGanancia").value(hasItem(DEFAULT_MARGEN_DE_GANANCIA.doubleValue())))
            .andExpect(jsonPath("$.[*].invima").value(hasItem(DEFAULT_INVIMA)));
    }

    @Test
    @Transactional
    void getProducto() throws Exception {
        // Initialize the database
        productoRepository.saveAndFlush(producto);

        // Get the producto
        restProductoMockMvc
            .perform(get(ENTITY_API_URL_ID, producto.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(producto.getId().intValue()))
            .andExpect(jsonPath("$.nombreProducto").value(DEFAULT_NOMBRE_PRODUCTO))
            .andExpect(jsonPath("$.cantidad").value(DEFAULT_CANTIDAD))
            .andExpect(jsonPath("$.iva").value(DEFAULT_IVA.doubleValue()))
            .andExpect(jsonPath("$.precioUdsVenta").value(DEFAULT_PRECIO_UDS_VENTA.doubleValue()))
            .andExpect(jsonPath("$.margenDeGanancia").value(DEFAULT_MARGEN_DE_GANANCIA.doubleValue()))
            .andExpect(jsonPath("$.invima").value(DEFAULT_INVIMA));
    }

    @Test
    @Transactional
    void getProductosByIdFiltering() throws Exception {
        // Initialize the database
        productoRepository.saveAndFlush(producto);

        Long id = producto.getId();

        defaultProductoShouldBeFound("id.equals=" + id);
        defaultProductoShouldNotBeFound("id.notEquals=" + id);

        defaultProductoShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultProductoShouldNotBeFound("id.greaterThan=" + id);

        defaultProductoShouldBeFound("id.lessThanOrEqual=" + id);
        defaultProductoShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllProductosByNombreProductoIsEqualToSomething() throws Exception {
        // Initialize the database
        productoRepository.saveAndFlush(producto);

        // Get all the productoList where nombreProducto equals to DEFAULT_NOMBRE_PRODUCTO
        defaultProductoShouldBeFound("nombreProducto.equals=" + DEFAULT_NOMBRE_PRODUCTO);

        // Get all the productoList where nombreProducto equals to UPDATED_NOMBRE_PRODUCTO
        defaultProductoShouldNotBeFound("nombreProducto.equals=" + UPDATED_NOMBRE_PRODUCTO);
    }

    @Test
    @Transactional
    void getAllProductosByNombreProductoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        productoRepository.saveAndFlush(producto);

        // Get all the productoList where nombreProducto not equals to DEFAULT_NOMBRE_PRODUCTO
        defaultProductoShouldNotBeFound("nombreProducto.notEquals=" + DEFAULT_NOMBRE_PRODUCTO);

        // Get all the productoList where nombreProducto not equals to UPDATED_NOMBRE_PRODUCTO
        defaultProductoShouldBeFound("nombreProducto.notEquals=" + UPDATED_NOMBRE_PRODUCTO);
    }

    @Test
    @Transactional
    void getAllProductosByNombreProductoIsInShouldWork() throws Exception {
        // Initialize the database
        productoRepository.saveAndFlush(producto);

        // Get all the productoList where nombreProducto in DEFAULT_NOMBRE_PRODUCTO or UPDATED_NOMBRE_PRODUCTO
        defaultProductoShouldBeFound("nombreProducto.in=" + DEFAULT_NOMBRE_PRODUCTO + "," + UPDATED_NOMBRE_PRODUCTO);

        // Get all the productoList where nombreProducto equals to UPDATED_NOMBRE_PRODUCTO
        defaultProductoShouldNotBeFound("nombreProducto.in=" + UPDATED_NOMBRE_PRODUCTO);
    }

    @Test
    @Transactional
    void getAllProductosByNombreProductoIsNullOrNotNull() throws Exception {
        // Initialize the database
        productoRepository.saveAndFlush(producto);

        // Get all the productoList where nombreProducto is not null
        defaultProductoShouldBeFound("nombreProducto.specified=true");

        // Get all the productoList where nombreProducto is null
        defaultProductoShouldNotBeFound("nombreProducto.specified=false");
    }

    @Test
    @Transactional
    void getAllProductosByNombreProductoContainsSomething() throws Exception {
        // Initialize the database
        productoRepository.saveAndFlush(producto);

        // Get all the productoList where nombreProducto contains DEFAULT_NOMBRE_PRODUCTO
        defaultProductoShouldBeFound("nombreProducto.contains=" + DEFAULT_NOMBRE_PRODUCTO);

        // Get all the productoList where nombreProducto contains UPDATED_NOMBRE_PRODUCTO
        defaultProductoShouldNotBeFound("nombreProducto.contains=" + UPDATED_NOMBRE_PRODUCTO);
    }

    @Test
    @Transactional
    void getAllProductosByNombreProductoNotContainsSomething() throws Exception {
        // Initialize the database
        productoRepository.saveAndFlush(producto);

        // Get all the productoList where nombreProducto does not contain DEFAULT_NOMBRE_PRODUCTO
        defaultProductoShouldNotBeFound("nombreProducto.doesNotContain=" + DEFAULT_NOMBRE_PRODUCTO);

        // Get all the productoList where nombreProducto does not contain UPDATED_NOMBRE_PRODUCTO
        defaultProductoShouldBeFound("nombreProducto.doesNotContain=" + UPDATED_NOMBRE_PRODUCTO);
    }

    @Test
    @Transactional
    void getAllProductosByCantidadIsEqualToSomething() throws Exception {
        // Initialize the database
        productoRepository.saveAndFlush(producto);

        // Get all the productoList where cantidad equals to DEFAULT_CANTIDAD
        defaultProductoShouldBeFound("cantidad.equals=" + DEFAULT_CANTIDAD);

        // Get all the productoList where cantidad equals to UPDATED_CANTIDAD
        defaultProductoShouldNotBeFound("cantidad.equals=" + UPDATED_CANTIDAD);
    }

    @Test
    @Transactional
    void getAllProductosByCantidadIsNotEqualToSomething() throws Exception {
        // Initialize the database
        productoRepository.saveAndFlush(producto);

        // Get all the productoList where cantidad not equals to DEFAULT_CANTIDAD
        defaultProductoShouldNotBeFound("cantidad.notEquals=" + DEFAULT_CANTIDAD);

        // Get all the productoList where cantidad not equals to UPDATED_CANTIDAD
        defaultProductoShouldBeFound("cantidad.notEquals=" + UPDATED_CANTIDAD);
    }

    @Test
    @Transactional
    void getAllProductosByCantidadIsInShouldWork() throws Exception {
        // Initialize the database
        productoRepository.saveAndFlush(producto);

        // Get all the productoList where cantidad in DEFAULT_CANTIDAD or UPDATED_CANTIDAD
        defaultProductoShouldBeFound("cantidad.in=" + DEFAULT_CANTIDAD + "," + UPDATED_CANTIDAD);

        // Get all the productoList where cantidad equals to UPDATED_CANTIDAD
        defaultProductoShouldNotBeFound("cantidad.in=" + UPDATED_CANTIDAD);
    }

    @Test
    @Transactional
    void getAllProductosByCantidadIsNullOrNotNull() throws Exception {
        // Initialize the database
        productoRepository.saveAndFlush(producto);

        // Get all the productoList where cantidad is not null
        defaultProductoShouldBeFound("cantidad.specified=true");

        // Get all the productoList where cantidad is null
        defaultProductoShouldNotBeFound("cantidad.specified=false");
    }

    @Test
    @Transactional
    void getAllProductosByCantidadIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        productoRepository.saveAndFlush(producto);

        // Get all the productoList where cantidad is greater than or equal to DEFAULT_CANTIDAD
        defaultProductoShouldBeFound("cantidad.greaterThanOrEqual=" + DEFAULT_CANTIDAD);

        // Get all the productoList where cantidad is greater than or equal to UPDATED_CANTIDAD
        defaultProductoShouldNotBeFound("cantidad.greaterThanOrEqual=" + UPDATED_CANTIDAD);
    }

    @Test
    @Transactional
    void getAllProductosByCantidadIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        productoRepository.saveAndFlush(producto);

        // Get all the productoList where cantidad is less than or equal to DEFAULT_CANTIDAD
        defaultProductoShouldBeFound("cantidad.lessThanOrEqual=" + DEFAULT_CANTIDAD);

        // Get all the productoList where cantidad is less than or equal to SMALLER_CANTIDAD
        defaultProductoShouldNotBeFound("cantidad.lessThanOrEqual=" + SMALLER_CANTIDAD);
    }

    @Test
    @Transactional
    void getAllProductosByCantidadIsLessThanSomething() throws Exception {
        // Initialize the database
        productoRepository.saveAndFlush(producto);

        // Get all the productoList where cantidad is less than DEFAULT_CANTIDAD
        defaultProductoShouldNotBeFound("cantidad.lessThan=" + DEFAULT_CANTIDAD);

        // Get all the productoList where cantidad is less than UPDATED_CANTIDAD
        defaultProductoShouldBeFound("cantidad.lessThan=" + UPDATED_CANTIDAD);
    }

    @Test
    @Transactional
    void getAllProductosByCantidadIsGreaterThanSomething() throws Exception {
        // Initialize the database
        productoRepository.saveAndFlush(producto);

        // Get all the productoList where cantidad is greater than DEFAULT_CANTIDAD
        defaultProductoShouldNotBeFound("cantidad.greaterThan=" + DEFAULT_CANTIDAD);

        // Get all the productoList where cantidad is greater than SMALLER_CANTIDAD
        defaultProductoShouldBeFound("cantidad.greaterThan=" + SMALLER_CANTIDAD);
    }

    @Test
    @Transactional
    void getAllProductosByIvaIsEqualToSomething() throws Exception {
        // Initialize the database
        productoRepository.saveAndFlush(producto);

        // Get all the productoList where iva equals to DEFAULT_IVA
        defaultProductoShouldBeFound("iva.equals=" + DEFAULT_IVA);

        // Get all the productoList where iva equals to UPDATED_IVA
        defaultProductoShouldNotBeFound("iva.equals=" + UPDATED_IVA);
    }

    @Test
    @Transactional
    void getAllProductosByIvaIsNotEqualToSomething() throws Exception {
        // Initialize the database
        productoRepository.saveAndFlush(producto);

        // Get all the productoList where iva not equals to DEFAULT_IVA
        defaultProductoShouldNotBeFound("iva.notEquals=" + DEFAULT_IVA);

        // Get all the productoList where iva not equals to UPDATED_IVA
        defaultProductoShouldBeFound("iva.notEquals=" + UPDATED_IVA);
    }

    @Test
    @Transactional
    void getAllProductosByIvaIsInShouldWork() throws Exception {
        // Initialize the database
        productoRepository.saveAndFlush(producto);

        // Get all the productoList where iva in DEFAULT_IVA or UPDATED_IVA
        defaultProductoShouldBeFound("iva.in=" + DEFAULT_IVA + "," + UPDATED_IVA);

        // Get all the productoList where iva equals to UPDATED_IVA
        defaultProductoShouldNotBeFound("iva.in=" + UPDATED_IVA);
    }

    @Test
    @Transactional
    void getAllProductosByIvaIsNullOrNotNull() throws Exception {
        // Initialize the database
        productoRepository.saveAndFlush(producto);

        // Get all the productoList where iva is not null
        defaultProductoShouldBeFound("iva.specified=true");

        // Get all the productoList where iva is null
        defaultProductoShouldNotBeFound("iva.specified=false");
    }

    @Test
    @Transactional
    void getAllProductosByIvaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        productoRepository.saveAndFlush(producto);

        // Get all the productoList where iva is greater than or equal to DEFAULT_IVA
        defaultProductoShouldBeFound("iva.greaterThanOrEqual=" + DEFAULT_IVA);

        // Get all the productoList where iva is greater than or equal to UPDATED_IVA
        defaultProductoShouldNotBeFound("iva.greaterThanOrEqual=" + UPDATED_IVA);
    }

    @Test
    @Transactional
    void getAllProductosByIvaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        productoRepository.saveAndFlush(producto);

        // Get all the productoList where iva is less than or equal to DEFAULT_IVA
        defaultProductoShouldBeFound("iva.lessThanOrEqual=" + DEFAULT_IVA);

        // Get all the productoList where iva is less than or equal to SMALLER_IVA
        defaultProductoShouldNotBeFound("iva.lessThanOrEqual=" + SMALLER_IVA);
    }

    @Test
    @Transactional
    void getAllProductosByIvaIsLessThanSomething() throws Exception {
        // Initialize the database
        productoRepository.saveAndFlush(producto);

        // Get all the productoList where iva is less than DEFAULT_IVA
        defaultProductoShouldNotBeFound("iva.lessThan=" + DEFAULT_IVA);

        // Get all the productoList where iva is less than UPDATED_IVA
        defaultProductoShouldBeFound("iva.lessThan=" + UPDATED_IVA);
    }

    @Test
    @Transactional
    void getAllProductosByIvaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        productoRepository.saveAndFlush(producto);

        // Get all the productoList where iva is greater than DEFAULT_IVA
        defaultProductoShouldNotBeFound("iva.greaterThan=" + DEFAULT_IVA);

        // Get all the productoList where iva is greater than SMALLER_IVA
        defaultProductoShouldBeFound("iva.greaterThan=" + SMALLER_IVA);
    }

    @Test
    @Transactional
    void getAllProductosByPrecioUdsVentaIsEqualToSomething() throws Exception {
        // Initialize the database
        productoRepository.saveAndFlush(producto);

        // Get all the productoList where precioUdsVenta equals to DEFAULT_PRECIO_UDS_VENTA
        defaultProductoShouldBeFound("precioUdsVenta.equals=" + DEFAULT_PRECIO_UDS_VENTA);

        // Get all the productoList where precioUdsVenta equals to UPDATED_PRECIO_UDS_VENTA
        defaultProductoShouldNotBeFound("precioUdsVenta.equals=" + UPDATED_PRECIO_UDS_VENTA);
    }

    @Test
    @Transactional
    void getAllProductosByPrecioUdsVentaIsNotEqualToSomething() throws Exception {
        // Initialize the database
        productoRepository.saveAndFlush(producto);

        // Get all the productoList where precioUdsVenta not equals to DEFAULT_PRECIO_UDS_VENTA
        defaultProductoShouldNotBeFound("precioUdsVenta.notEquals=" + DEFAULT_PRECIO_UDS_VENTA);

        // Get all the productoList where precioUdsVenta not equals to UPDATED_PRECIO_UDS_VENTA
        defaultProductoShouldBeFound("precioUdsVenta.notEquals=" + UPDATED_PRECIO_UDS_VENTA);
    }

    @Test
    @Transactional
    void getAllProductosByPrecioUdsVentaIsInShouldWork() throws Exception {
        // Initialize the database
        productoRepository.saveAndFlush(producto);

        // Get all the productoList where precioUdsVenta in DEFAULT_PRECIO_UDS_VENTA or UPDATED_PRECIO_UDS_VENTA
        defaultProductoShouldBeFound("precioUdsVenta.in=" + DEFAULT_PRECIO_UDS_VENTA + "," + UPDATED_PRECIO_UDS_VENTA);

        // Get all the productoList where precioUdsVenta equals to UPDATED_PRECIO_UDS_VENTA
        defaultProductoShouldNotBeFound("precioUdsVenta.in=" + UPDATED_PRECIO_UDS_VENTA);
    }

    @Test
    @Transactional
    void getAllProductosByPrecioUdsVentaIsNullOrNotNull() throws Exception {
        // Initialize the database
        productoRepository.saveAndFlush(producto);

        // Get all the productoList where precioUdsVenta is not null
        defaultProductoShouldBeFound("precioUdsVenta.specified=true");

        // Get all the productoList where precioUdsVenta is null
        defaultProductoShouldNotBeFound("precioUdsVenta.specified=false");
    }

    @Test
    @Transactional
    void getAllProductosByPrecioUdsVentaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        productoRepository.saveAndFlush(producto);

        // Get all the productoList where precioUdsVenta is greater than or equal to DEFAULT_PRECIO_UDS_VENTA
        defaultProductoShouldBeFound("precioUdsVenta.greaterThanOrEqual=" + DEFAULT_PRECIO_UDS_VENTA);

        // Get all the productoList where precioUdsVenta is greater than or equal to UPDATED_PRECIO_UDS_VENTA
        defaultProductoShouldNotBeFound("precioUdsVenta.greaterThanOrEqual=" + UPDATED_PRECIO_UDS_VENTA);
    }

    @Test
    @Transactional
    void getAllProductosByPrecioUdsVentaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        productoRepository.saveAndFlush(producto);

        // Get all the productoList where precioUdsVenta is less than or equal to DEFAULT_PRECIO_UDS_VENTA
        defaultProductoShouldBeFound("precioUdsVenta.lessThanOrEqual=" + DEFAULT_PRECIO_UDS_VENTA);

        // Get all the productoList where precioUdsVenta is less than or equal to SMALLER_PRECIO_UDS_VENTA
        defaultProductoShouldNotBeFound("precioUdsVenta.lessThanOrEqual=" + SMALLER_PRECIO_UDS_VENTA);
    }

    @Test
    @Transactional
    void getAllProductosByPrecioUdsVentaIsLessThanSomething() throws Exception {
        // Initialize the database
        productoRepository.saveAndFlush(producto);

        // Get all the productoList where precioUdsVenta is less than DEFAULT_PRECIO_UDS_VENTA
        defaultProductoShouldNotBeFound("precioUdsVenta.lessThan=" + DEFAULT_PRECIO_UDS_VENTA);

        // Get all the productoList where precioUdsVenta is less than UPDATED_PRECIO_UDS_VENTA
        defaultProductoShouldBeFound("precioUdsVenta.lessThan=" + UPDATED_PRECIO_UDS_VENTA);
    }

    @Test
    @Transactional
    void getAllProductosByPrecioUdsVentaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        productoRepository.saveAndFlush(producto);

        // Get all the productoList where precioUdsVenta is greater than DEFAULT_PRECIO_UDS_VENTA
        defaultProductoShouldNotBeFound("precioUdsVenta.greaterThan=" + DEFAULT_PRECIO_UDS_VENTA);

        // Get all the productoList where precioUdsVenta is greater than SMALLER_PRECIO_UDS_VENTA
        defaultProductoShouldBeFound("precioUdsVenta.greaterThan=" + SMALLER_PRECIO_UDS_VENTA);
    }

    @Test
    @Transactional
    void getAllProductosByMargenDeGananciaIsEqualToSomething() throws Exception {
        // Initialize the database
        productoRepository.saveAndFlush(producto);

        // Get all the productoList where margenDeGanancia equals to DEFAULT_MARGEN_DE_GANANCIA
        defaultProductoShouldBeFound("margenDeGanancia.equals=" + DEFAULT_MARGEN_DE_GANANCIA);

        // Get all the productoList where margenDeGanancia equals to UPDATED_MARGEN_DE_GANANCIA
        defaultProductoShouldNotBeFound("margenDeGanancia.equals=" + UPDATED_MARGEN_DE_GANANCIA);
    }

    @Test
    @Transactional
    void getAllProductosByMargenDeGananciaIsNotEqualToSomething() throws Exception {
        // Initialize the database
        productoRepository.saveAndFlush(producto);

        // Get all the productoList where margenDeGanancia not equals to DEFAULT_MARGEN_DE_GANANCIA
        defaultProductoShouldNotBeFound("margenDeGanancia.notEquals=" + DEFAULT_MARGEN_DE_GANANCIA);

        // Get all the productoList where margenDeGanancia not equals to UPDATED_MARGEN_DE_GANANCIA
        defaultProductoShouldBeFound("margenDeGanancia.notEquals=" + UPDATED_MARGEN_DE_GANANCIA);
    }

    @Test
    @Transactional
    void getAllProductosByMargenDeGananciaIsInShouldWork() throws Exception {
        // Initialize the database
        productoRepository.saveAndFlush(producto);

        // Get all the productoList where margenDeGanancia in DEFAULT_MARGEN_DE_GANANCIA or UPDATED_MARGEN_DE_GANANCIA
        defaultProductoShouldBeFound("margenDeGanancia.in=" + DEFAULT_MARGEN_DE_GANANCIA + "," + UPDATED_MARGEN_DE_GANANCIA);

        // Get all the productoList where margenDeGanancia equals to UPDATED_MARGEN_DE_GANANCIA
        defaultProductoShouldNotBeFound("margenDeGanancia.in=" + UPDATED_MARGEN_DE_GANANCIA);
    }

    @Test
    @Transactional
    void getAllProductosByMargenDeGananciaIsNullOrNotNull() throws Exception {
        // Initialize the database
        productoRepository.saveAndFlush(producto);

        // Get all the productoList where margenDeGanancia is not null
        defaultProductoShouldBeFound("margenDeGanancia.specified=true");

        // Get all the productoList where margenDeGanancia is null
        defaultProductoShouldNotBeFound("margenDeGanancia.specified=false");
    }

    @Test
    @Transactional
    void getAllProductosByMargenDeGananciaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        productoRepository.saveAndFlush(producto);

        // Get all the productoList where margenDeGanancia is greater than or equal to DEFAULT_MARGEN_DE_GANANCIA
        defaultProductoShouldBeFound("margenDeGanancia.greaterThanOrEqual=" + DEFAULT_MARGEN_DE_GANANCIA);

        // Get all the productoList where margenDeGanancia is greater than or equal to UPDATED_MARGEN_DE_GANANCIA
        defaultProductoShouldNotBeFound("margenDeGanancia.greaterThanOrEqual=" + UPDATED_MARGEN_DE_GANANCIA);
    }

    @Test
    @Transactional
    void getAllProductosByMargenDeGananciaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        productoRepository.saveAndFlush(producto);

        // Get all the productoList where margenDeGanancia is less than or equal to DEFAULT_MARGEN_DE_GANANCIA
        defaultProductoShouldBeFound("margenDeGanancia.lessThanOrEqual=" + DEFAULT_MARGEN_DE_GANANCIA);

        // Get all the productoList where margenDeGanancia is less than or equal to SMALLER_MARGEN_DE_GANANCIA
        defaultProductoShouldNotBeFound("margenDeGanancia.lessThanOrEqual=" + SMALLER_MARGEN_DE_GANANCIA);
    }

    @Test
    @Transactional
    void getAllProductosByMargenDeGananciaIsLessThanSomething() throws Exception {
        // Initialize the database
        productoRepository.saveAndFlush(producto);

        // Get all the productoList where margenDeGanancia is less than DEFAULT_MARGEN_DE_GANANCIA
        defaultProductoShouldNotBeFound("margenDeGanancia.lessThan=" + DEFAULT_MARGEN_DE_GANANCIA);

        // Get all the productoList where margenDeGanancia is less than UPDATED_MARGEN_DE_GANANCIA
        defaultProductoShouldBeFound("margenDeGanancia.lessThan=" + UPDATED_MARGEN_DE_GANANCIA);
    }

    @Test
    @Transactional
    void getAllProductosByMargenDeGananciaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        productoRepository.saveAndFlush(producto);

        // Get all the productoList where margenDeGanancia is greater than DEFAULT_MARGEN_DE_GANANCIA
        defaultProductoShouldNotBeFound("margenDeGanancia.greaterThan=" + DEFAULT_MARGEN_DE_GANANCIA);

        // Get all the productoList where margenDeGanancia is greater than SMALLER_MARGEN_DE_GANANCIA
        defaultProductoShouldBeFound("margenDeGanancia.greaterThan=" + SMALLER_MARGEN_DE_GANANCIA);
    }

    @Test
    @Transactional
    void getAllProductosByInvimaIsEqualToSomething() throws Exception {
        // Initialize the database
        productoRepository.saveAndFlush(producto);

        // Get all the productoList where invima equals to DEFAULT_INVIMA
        defaultProductoShouldBeFound("invima.equals=" + DEFAULT_INVIMA);

        // Get all the productoList where invima equals to UPDATED_INVIMA
        defaultProductoShouldNotBeFound("invima.equals=" + UPDATED_INVIMA);
    }

    @Test
    @Transactional
    void getAllProductosByInvimaIsNotEqualToSomething() throws Exception {
        // Initialize the database
        productoRepository.saveAndFlush(producto);

        // Get all the productoList where invima not equals to DEFAULT_INVIMA
        defaultProductoShouldNotBeFound("invima.notEquals=" + DEFAULT_INVIMA);

        // Get all the productoList where invima not equals to UPDATED_INVIMA
        defaultProductoShouldBeFound("invima.notEquals=" + UPDATED_INVIMA);
    }

    @Test
    @Transactional
    void getAllProductosByInvimaIsInShouldWork() throws Exception {
        // Initialize the database
        productoRepository.saveAndFlush(producto);

        // Get all the productoList where invima in DEFAULT_INVIMA or UPDATED_INVIMA
        defaultProductoShouldBeFound("invima.in=" + DEFAULT_INVIMA + "," + UPDATED_INVIMA);

        // Get all the productoList where invima equals to UPDATED_INVIMA
        defaultProductoShouldNotBeFound("invima.in=" + UPDATED_INVIMA);
    }

    @Test
    @Transactional
    void getAllProductosByInvimaIsNullOrNotNull() throws Exception {
        // Initialize the database
        productoRepository.saveAndFlush(producto);

        // Get all the productoList where invima is not null
        defaultProductoShouldBeFound("invima.specified=true");

        // Get all the productoList where invima is null
        defaultProductoShouldNotBeFound("invima.specified=false");
    }

    @Test
    @Transactional
    void getAllProductosByInvimaContainsSomething() throws Exception {
        // Initialize the database
        productoRepository.saveAndFlush(producto);

        // Get all the productoList where invima contains DEFAULT_INVIMA
        defaultProductoShouldBeFound("invima.contains=" + DEFAULT_INVIMA);

        // Get all the productoList where invima contains UPDATED_INVIMA
        defaultProductoShouldNotBeFound("invima.contains=" + UPDATED_INVIMA);
    }

    @Test
    @Transactional
    void getAllProductosByInvimaNotContainsSomething() throws Exception {
        // Initialize the database
        productoRepository.saveAndFlush(producto);

        // Get all the productoList where invima does not contain DEFAULT_INVIMA
        defaultProductoShouldNotBeFound("invima.doesNotContain=" + DEFAULT_INVIMA);

        // Get all the productoList where invima does not contain UPDATED_INVIMA
        defaultProductoShouldBeFound("invima.doesNotContain=" + UPDATED_INVIMA);
    }

    @Test
    @Transactional
    void getAllProductosByPresentacionIsEqualToSomething() throws Exception {
        // Initialize the database
        productoRepository.saveAndFlush(producto);
        Presentacion presentacion = PresentacionResourceIT.createEntity(em);
        em.persist(presentacion);
        em.flush();
        producto.setPresentacion(presentacion);
        productoRepository.saveAndFlush(producto);
        Long presentacionId = presentacion.getId();

        // Get all the productoList where presentacion equals to presentacionId
        defaultProductoShouldBeFound("presentacionId.equals=" + presentacionId);

        // Get all the productoList where presentacion equals to (presentacionId + 1)
        defaultProductoShouldNotBeFound("presentacionId.equals=" + (presentacionId + 1));
    }

    @Test
    @Transactional
    void getAllProductosByLaboratorioIsEqualToSomething() throws Exception {
        // Initialize the database
        productoRepository.saveAndFlush(producto);
        Laboratorio laboratorio = LaboratorioResourceIT.createEntity(em);
        em.persist(laboratorio);
        em.flush();
        producto.setLaboratorio(laboratorio);
        productoRepository.saveAndFlush(producto);
        Long laboratorioId = laboratorio.getId();

        // Get all the productoList where laboratorio equals to laboratorioId
        defaultProductoShouldBeFound("laboratorioId.equals=" + laboratorioId);

        // Get all the productoList where laboratorio equals to (laboratorioId + 1)
        defaultProductoShouldNotBeFound("laboratorioId.equals=" + (laboratorioId + 1));
    }

    @Test
    @Transactional
    void getAllProductosByLineaProductoIsEqualToSomething() throws Exception {
        // Initialize the database
        productoRepository.saveAndFlush(producto);
        LineaProducto lineaProducto = LineaProductoResourceIT.createEntity(em);
        em.persist(lineaProducto);
        em.flush();
        producto.setLineaProducto(lineaProducto);
        productoRepository.saveAndFlush(producto);
        Long lineaProductoId = lineaProducto.getId();

        // Get all the productoList where lineaProducto equals to lineaProductoId
        defaultProductoShouldBeFound("lineaProductoId.equals=" + lineaProductoId);

        // Get all the productoList where lineaProducto equals to (lineaProductoId + 1)
        defaultProductoShouldNotBeFound("lineaProductoId.equals=" + (lineaProductoId + 1));
    }

    @Test
    @Transactional
    void getAllProductosByCompraProductoIsEqualToSomething() throws Exception {
        // Initialize the database
        productoRepository.saveAndFlush(producto);
        CompraProducto compraProducto = CompraProductoResourceIT.createEntity(em);
        em.persist(compraProducto);
        em.flush();
        producto.addCompraProducto(compraProducto);
        productoRepository.saveAndFlush(producto);
        Long compraProductoId = compraProducto.getId();

        // Get all the productoList where compraProducto equals to compraProductoId
        defaultProductoShouldBeFound("compraProductoId.equals=" + compraProductoId);

        // Get all the productoList where compraProducto equals to (compraProductoId + 1)
        defaultProductoShouldNotBeFound("compraProductoId.equals=" + (compraProductoId + 1));
    }

    @Test
    @Transactional
    void getAllProductosByVentaProductoIsEqualToSomething() throws Exception {
        // Initialize the database
        productoRepository.saveAndFlush(producto);
        VentaProducto ventaProducto = VentaProductoResourceIT.createEntity(em);
        em.persist(ventaProducto);
        em.flush();
        producto.addVentaProducto(ventaProducto);
        productoRepository.saveAndFlush(producto);
        Long ventaProductoId = ventaProducto.getId();

        // Get all the productoList where ventaProducto equals to ventaProductoId
        defaultProductoShouldBeFound("ventaProductoId.equals=" + ventaProductoId);

        // Get all the productoList where ventaProducto equals to (ventaProductoId + 1)
        defaultProductoShouldNotBeFound("ventaProductoId.equals=" + (ventaProductoId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultProductoShouldBeFound(String filter) throws Exception {
        restProductoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(producto.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombreProducto").value(hasItem(DEFAULT_NOMBRE_PRODUCTO)))
            .andExpect(jsonPath("$.[*].cantidad").value(hasItem(DEFAULT_CANTIDAD)))
            .andExpect(jsonPath("$.[*].iva").value(hasItem(DEFAULT_IVA.doubleValue())))
            .andExpect(jsonPath("$.[*].precioUdsVenta").value(hasItem(DEFAULT_PRECIO_UDS_VENTA.doubleValue())))
            .andExpect(jsonPath("$.[*].margenDeGanancia").value(hasItem(DEFAULT_MARGEN_DE_GANANCIA.doubleValue())))
            .andExpect(jsonPath("$.[*].invima").value(hasItem(DEFAULT_INVIMA)));

        // Check, that the count call also returns 1
        restProductoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultProductoShouldNotBeFound(String filter) throws Exception {
        restProductoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProductoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingProducto() throws Exception {
        // Get the producto
        restProductoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewProducto() throws Exception {
        // Initialize the database
        productoRepository.saveAndFlush(producto);

        int databaseSizeBeforeUpdate = productoRepository.findAll().size();

        // Update the producto
        Producto updatedProducto = productoRepository.findById(producto.getId()).get();
        // Disconnect from session so that the updates on updatedProducto are not directly saved in db
        em.detach(updatedProducto);
        updatedProducto
            .nombreProducto(UPDATED_NOMBRE_PRODUCTO)
            .cantidad(UPDATED_CANTIDAD)
            .iva(UPDATED_IVA)
            .precioUdsVenta(UPDATED_PRECIO_UDS_VENTA)
            .margenDeGanancia(UPDATED_MARGEN_DE_GANANCIA)
            .invima(UPDATED_INVIMA);
        ProductoDTO productoDTO = productoMapper.toDto(updatedProducto);

        restProductoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productoDTO))
            )
            .andExpect(status().isOk());

        // Validate the Producto in the database
        List<Producto> productoList = productoRepository.findAll();
        assertThat(productoList).hasSize(databaseSizeBeforeUpdate);
        Producto testProducto = productoList.get(productoList.size() - 1);
        assertThat(testProducto.getNombreProducto()).isEqualTo(UPDATED_NOMBRE_PRODUCTO);
        assertThat(testProducto.getCantidad()).isEqualTo(UPDATED_CANTIDAD);
        assertThat(testProducto.getIva()).isEqualTo(UPDATED_IVA);
        assertThat(testProducto.getPrecioUdsVenta()).isEqualTo(UPDATED_PRECIO_UDS_VENTA);
        assertThat(testProducto.getMargenDeGanancia()).isEqualTo(UPDATED_MARGEN_DE_GANANCIA);
        assertThat(testProducto.getInvima()).isEqualTo(UPDATED_INVIMA);
    }

    @Test
    @Transactional
    void putNonExistingProducto() throws Exception {
        int databaseSizeBeforeUpdate = productoRepository.findAll().size();
        producto.setId(count.incrementAndGet());

        // Create the Producto
        ProductoDTO productoDTO = productoMapper.toDto(producto);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Producto in the database
        List<Producto> productoList = productoRepository.findAll();
        assertThat(productoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProducto() throws Exception {
        int databaseSizeBeforeUpdate = productoRepository.findAll().size();
        producto.setId(count.incrementAndGet());

        // Create the Producto
        ProductoDTO productoDTO = productoMapper.toDto(producto);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Producto in the database
        List<Producto> productoList = productoRepository.findAll();
        assertThat(productoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProducto() throws Exception {
        int databaseSizeBeforeUpdate = productoRepository.findAll().size();
        producto.setId(count.incrementAndGet());

        // Create the Producto
        ProductoDTO productoDTO = productoMapper.toDto(producto);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Producto in the database
        List<Producto> productoList = productoRepository.findAll();
        assertThat(productoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProductoWithPatch() throws Exception {
        // Initialize the database
        productoRepository.saveAndFlush(producto);

        int databaseSizeBeforeUpdate = productoRepository.findAll().size();

        // Update the producto using partial update
        Producto partialUpdatedProducto = new Producto();
        partialUpdatedProducto.setId(producto.getId());

        partialUpdatedProducto
            .nombreProducto(UPDATED_NOMBRE_PRODUCTO)
            .cantidad(UPDATED_CANTIDAD)
            .margenDeGanancia(UPDATED_MARGEN_DE_GANANCIA);

        restProductoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProducto.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProducto))
            )
            .andExpect(status().isOk());

        // Validate the Producto in the database
        List<Producto> productoList = productoRepository.findAll();
        assertThat(productoList).hasSize(databaseSizeBeforeUpdate);
        Producto testProducto = productoList.get(productoList.size() - 1);
        assertThat(testProducto.getNombreProducto()).isEqualTo(UPDATED_NOMBRE_PRODUCTO);
        assertThat(testProducto.getCantidad()).isEqualTo(UPDATED_CANTIDAD);
        assertThat(testProducto.getIva()).isEqualTo(DEFAULT_IVA);
        assertThat(testProducto.getPrecioUdsVenta()).isEqualTo(DEFAULT_PRECIO_UDS_VENTA);
        assertThat(testProducto.getMargenDeGanancia()).isEqualTo(UPDATED_MARGEN_DE_GANANCIA);
        assertThat(testProducto.getInvima()).isEqualTo(DEFAULT_INVIMA);
    }

    @Test
    @Transactional
    void fullUpdateProductoWithPatch() throws Exception {
        // Initialize the database
        productoRepository.saveAndFlush(producto);

        int databaseSizeBeforeUpdate = productoRepository.findAll().size();

        // Update the producto using partial update
        Producto partialUpdatedProducto = new Producto();
        partialUpdatedProducto.setId(producto.getId());

        partialUpdatedProducto
            .nombreProducto(UPDATED_NOMBRE_PRODUCTO)
            .cantidad(UPDATED_CANTIDAD)
            .iva(UPDATED_IVA)
            .precioUdsVenta(UPDATED_PRECIO_UDS_VENTA)
            .margenDeGanancia(UPDATED_MARGEN_DE_GANANCIA)
            .invima(UPDATED_INVIMA);

        restProductoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProducto.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProducto))
            )
            .andExpect(status().isOk());

        // Validate the Producto in the database
        List<Producto> productoList = productoRepository.findAll();
        assertThat(productoList).hasSize(databaseSizeBeforeUpdate);
        Producto testProducto = productoList.get(productoList.size() - 1);
        assertThat(testProducto.getNombreProducto()).isEqualTo(UPDATED_NOMBRE_PRODUCTO);
        assertThat(testProducto.getCantidad()).isEqualTo(UPDATED_CANTIDAD);
        assertThat(testProducto.getIva()).isEqualTo(UPDATED_IVA);
        assertThat(testProducto.getPrecioUdsVenta()).isEqualTo(UPDATED_PRECIO_UDS_VENTA);
        assertThat(testProducto.getMargenDeGanancia()).isEqualTo(UPDATED_MARGEN_DE_GANANCIA);
        assertThat(testProducto.getInvima()).isEqualTo(UPDATED_INVIMA);
    }

    @Test
    @Transactional
    void patchNonExistingProducto() throws Exception {
        int databaseSizeBeforeUpdate = productoRepository.findAll().size();
        producto.setId(count.incrementAndGet());

        // Create the Producto
        ProductoDTO productoDTO = productoMapper.toDto(producto);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, productoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Producto in the database
        List<Producto> productoList = productoRepository.findAll();
        assertThat(productoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProducto() throws Exception {
        int databaseSizeBeforeUpdate = productoRepository.findAll().size();
        producto.setId(count.incrementAndGet());

        // Create the Producto
        ProductoDTO productoDTO = productoMapper.toDto(producto);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Producto in the database
        List<Producto> productoList = productoRepository.findAll();
        assertThat(productoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProducto() throws Exception {
        int databaseSizeBeforeUpdate = productoRepository.findAll().size();
        producto.setId(count.incrementAndGet());

        // Create the Producto
        ProductoDTO productoDTO = productoMapper.toDto(producto);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductoMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(productoDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Producto in the database
        List<Producto> productoList = productoRepository.findAll();
        assertThat(productoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProducto() throws Exception {
        // Initialize the database
        productoRepository.saveAndFlush(producto);

        int databaseSizeBeforeDelete = productoRepository.findAll().size();

        // Delete the producto
        restProductoMockMvc
            .perform(delete(ENTITY_API_URL_ID, producto.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Producto> productoList = productoRepository.findAll();
        assertThat(productoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
