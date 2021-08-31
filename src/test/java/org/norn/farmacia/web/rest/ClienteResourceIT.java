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
import org.norn.farmacia.domain.Cliente;
import org.norn.farmacia.domain.Genero;
import org.norn.farmacia.domain.Municipio;
import org.norn.farmacia.domain.Venta;
import org.norn.farmacia.repository.ClienteRepository;
import org.norn.farmacia.service.criteria.ClienteCriteria;
import org.norn.farmacia.service.dto.ClienteDTO;
import org.norn.farmacia.service.mapper.ClienteMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ClienteResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ClienteResourceIT {

    private static final String DEFAULT_PRIMER_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_PRIMER_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_SEGUNDO_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_SEGUNDO_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_PRIMER_APELLIDO = "AAAAAAAAAA";
    private static final String UPDATED_PRIMER_APELLIDO = "BBBBBBBBBB";

    private static final String DEFAULT_SEGUNDO_APELLIDO = "AAAAAAAAAA";
    private static final String UPDATED_SEGUNDO_APELLIDO = "BBBBBBBBBB";

    private static final String DEFAULT_PRIMER_TELEFONO = "AAAAAAAAAA";
    private static final String UPDATED_PRIMER_TELEFONO = "BBBBBBBBBB";

    private static final String DEFAULT_SEGUNDO_TELEFONO = "AAAAAAAAAA";
    private static final String UPDATED_SEGUNDO_TELEFONO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/clientes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ClienteMapper clienteMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restClienteMockMvc;

    private Cliente cliente;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cliente createEntity(EntityManager em) {
        Cliente cliente = new Cliente()
            .primerNombre(DEFAULT_PRIMER_NOMBRE)
            .segundoNombre(DEFAULT_SEGUNDO_NOMBRE)
            .primerApellido(DEFAULT_PRIMER_APELLIDO)
            .segundoApellido(DEFAULT_SEGUNDO_APELLIDO)
            .primerTelefono(DEFAULT_PRIMER_TELEFONO)
            .segundoTelefono(DEFAULT_SEGUNDO_TELEFONO);
        return cliente;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cliente createUpdatedEntity(EntityManager em) {
        Cliente cliente = new Cliente()
            .primerNombre(UPDATED_PRIMER_NOMBRE)
            .segundoNombre(UPDATED_SEGUNDO_NOMBRE)
            .primerApellido(UPDATED_PRIMER_APELLIDO)
            .segundoApellido(UPDATED_SEGUNDO_APELLIDO)
            .primerTelefono(UPDATED_PRIMER_TELEFONO)
            .segundoTelefono(UPDATED_SEGUNDO_TELEFONO);
        return cliente;
    }

    @BeforeEach
    public void initTest() {
        cliente = createEntity(em);
    }

    @Test
    @Transactional
    void createCliente() throws Exception {
        int databaseSizeBeforeCreate = clienteRepository.findAll().size();
        // Create the Cliente
        ClienteDTO clienteDTO = clienteMapper.toDto(cliente);
        restClienteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(clienteDTO)))
            .andExpect(status().isCreated());

        // Validate the Cliente in the database
        List<Cliente> clienteList = clienteRepository.findAll();
        assertThat(clienteList).hasSize(databaseSizeBeforeCreate + 1);
        Cliente testCliente = clienteList.get(clienteList.size() - 1);
        assertThat(testCliente.getPrimerNombre()).isEqualTo(DEFAULT_PRIMER_NOMBRE);
        assertThat(testCliente.getSegundoNombre()).isEqualTo(DEFAULT_SEGUNDO_NOMBRE);
        assertThat(testCliente.getPrimerApellido()).isEqualTo(DEFAULT_PRIMER_APELLIDO);
        assertThat(testCliente.getSegundoApellido()).isEqualTo(DEFAULT_SEGUNDO_APELLIDO);
        assertThat(testCliente.getPrimerTelefono()).isEqualTo(DEFAULT_PRIMER_TELEFONO);
        assertThat(testCliente.getSegundoTelefono()).isEqualTo(DEFAULT_SEGUNDO_TELEFONO);
    }

    @Test
    @Transactional
    void createClienteWithExistingId() throws Exception {
        // Create the Cliente with an existing ID
        cliente.setId(1L);
        ClienteDTO clienteDTO = clienteMapper.toDto(cliente);

        int databaseSizeBeforeCreate = clienteRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restClienteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(clienteDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Cliente in the database
        List<Cliente> clienteList = clienteRepository.findAll();
        assertThat(clienteList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkPrimerNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = clienteRepository.findAll().size();
        // set the field null
        cliente.setPrimerNombre(null);

        // Create the Cliente, which fails.
        ClienteDTO clienteDTO = clienteMapper.toDto(cliente);

        restClienteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(clienteDTO)))
            .andExpect(status().isBadRequest());

        List<Cliente> clienteList = clienteRepository.findAll();
        assertThat(clienteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllClientes() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList
        restClienteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cliente.getId().intValue())))
            .andExpect(jsonPath("$.[*].primerNombre").value(hasItem(DEFAULT_PRIMER_NOMBRE)))
            .andExpect(jsonPath("$.[*].segundoNombre").value(hasItem(DEFAULT_SEGUNDO_NOMBRE)))
            .andExpect(jsonPath("$.[*].primerApellido").value(hasItem(DEFAULT_PRIMER_APELLIDO)))
            .andExpect(jsonPath("$.[*].segundoApellido").value(hasItem(DEFAULT_SEGUNDO_APELLIDO)))
            .andExpect(jsonPath("$.[*].primerTelefono").value(hasItem(DEFAULT_PRIMER_TELEFONO)))
            .andExpect(jsonPath("$.[*].segundoTelefono").value(hasItem(DEFAULT_SEGUNDO_TELEFONO)));
    }

    @Test
    @Transactional
    void getCliente() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        // Get the cliente
        restClienteMockMvc
            .perform(get(ENTITY_API_URL_ID, cliente.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cliente.getId().intValue()))
            .andExpect(jsonPath("$.primerNombre").value(DEFAULT_PRIMER_NOMBRE))
            .andExpect(jsonPath("$.segundoNombre").value(DEFAULT_SEGUNDO_NOMBRE))
            .andExpect(jsonPath("$.primerApellido").value(DEFAULT_PRIMER_APELLIDO))
            .andExpect(jsonPath("$.segundoApellido").value(DEFAULT_SEGUNDO_APELLIDO))
            .andExpect(jsonPath("$.primerTelefono").value(DEFAULT_PRIMER_TELEFONO))
            .andExpect(jsonPath("$.segundoTelefono").value(DEFAULT_SEGUNDO_TELEFONO));
    }

    @Test
    @Transactional
    void getClientesByIdFiltering() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        Long id = cliente.getId();

        defaultClienteShouldBeFound("id.equals=" + id);
        defaultClienteShouldNotBeFound("id.notEquals=" + id);

        defaultClienteShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultClienteShouldNotBeFound("id.greaterThan=" + id);

        defaultClienteShouldBeFound("id.lessThanOrEqual=" + id);
        defaultClienteShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllClientesByPrimerNombreIsEqualToSomething() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where primerNombre equals to DEFAULT_PRIMER_NOMBRE
        defaultClienteShouldBeFound("primerNombre.equals=" + DEFAULT_PRIMER_NOMBRE);

        // Get all the clienteList where primerNombre equals to UPDATED_PRIMER_NOMBRE
        defaultClienteShouldNotBeFound("primerNombre.equals=" + UPDATED_PRIMER_NOMBRE);
    }

    @Test
    @Transactional
    void getAllClientesByPrimerNombreIsNotEqualToSomething() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where primerNombre not equals to DEFAULT_PRIMER_NOMBRE
        defaultClienteShouldNotBeFound("primerNombre.notEquals=" + DEFAULT_PRIMER_NOMBRE);

        // Get all the clienteList where primerNombre not equals to UPDATED_PRIMER_NOMBRE
        defaultClienteShouldBeFound("primerNombre.notEquals=" + UPDATED_PRIMER_NOMBRE);
    }

    @Test
    @Transactional
    void getAllClientesByPrimerNombreIsInShouldWork() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where primerNombre in DEFAULT_PRIMER_NOMBRE or UPDATED_PRIMER_NOMBRE
        defaultClienteShouldBeFound("primerNombre.in=" + DEFAULT_PRIMER_NOMBRE + "," + UPDATED_PRIMER_NOMBRE);

        // Get all the clienteList where primerNombre equals to UPDATED_PRIMER_NOMBRE
        defaultClienteShouldNotBeFound("primerNombre.in=" + UPDATED_PRIMER_NOMBRE);
    }

    @Test
    @Transactional
    void getAllClientesByPrimerNombreIsNullOrNotNull() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where primerNombre is not null
        defaultClienteShouldBeFound("primerNombre.specified=true");

        // Get all the clienteList where primerNombre is null
        defaultClienteShouldNotBeFound("primerNombre.specified=false");
    }

    @Test
    @Transactional
    void getAllClientesByPrimerNombreContainsSomething() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where primerNombre contains DEFAULT_PRIMER_NOMBRE
        defaultClienteShouldBeFound("primerNombre.contains=" + DEFAULT_PRIMER_NOMBRE);

        // Get all the clienteList where primerNombre contains UPDATED_PRIMER_NOMBRE
        defaultClienteShouldNotBeFound("primerNombre.contains=" + UPDATED_PRIMER_NOMBRE);
    }

    @Test
    @Transactional
    void getAllClientesByPrimerNombreNotContainsSomething() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where primerNombre does not contain DEFAULT_PRIMER_NOMBRE
        defaultClienteShouldNotBeFound("primerNombre.doesNotContain=" + DEFAULT_PRIMER_NOMBRE);

        // Get all the clienteList where primerNombre does not contain UPDATED_PRIMER_NOMBRE
        defaultClienteShouldBeFound("primerNombre.doesNotContain=" + UPDATED_PRIMER_NOMBRE);
    }

    @Test
    @Transactional
    void getAllClientesBySegundoNombreIsEqualToSomething() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where segundoNombre equals to DEFAULT_SEGUNDO_NOMBRE
        defaultClienteShouldBeFound("segundoNombre.equals=" + DEFAULT_SEGUNDO_NOMBRE);

        // Get all the clienteList where segundoNombre equals to UPDATED_SEGUNDO_NOMBRE
        defaultClienteShouldNotBeFound("segundoNombre.equals=" + UPDATED_SEGUNDO_NOMBRE);
    }

    @Test
    @Transactional
    void getAllClientesBySegundoNombreIsNotEqualToSomething() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where segundoNombre not equals to DEFAULT_SEGUNDO_NOMBRE
        defaultClienteShouldNotBeFound("segundoNombre.notEquals=" + DEFAULT_SEGUNDO_NOMBRE);

        // Get all the clienteList where segundoNombre not equals to UPDATED_SEGUNDO_NOMBRE
        defaultClienteShouldBeFound("segundoNombre.notEquals=" + UPDATED_SEGUNDO_NOMBRE);
    }

    @Test
    @Transactional
    void getAllClientesBySegundoNombreIsInShouldWork() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where segundoNombre in DEFAULT_SEGUNDO_NOMBRE or UPDATED_SEGUNDO_NOMBRE
        defaultClienteShouldBeFound("segundoNombre.in=" + DEFAULT_SEGUNDO_NOMBRE + "," + UPDATED_SEGUNDO_NOMBRE);

        // Get all the clienteList where segundoNombre equals to UPDATED_SEGUNDO_NOMBRE
        defaultClienteShouldNotBeFound("segundoNombre.in=" + UPDATED_SEGUNDO_NOMBRE);
    }

    @Test
    @Transactional
    void getAllClientesBySegundoNombreIsNullOrNotNull() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where segundoNombre is not null
        defaultClienteShouldBeFound("segundoNombre.specified=true");

        // Get all the clienteList where segundoNombre is null
        defaultClienteShouldNotBeFound("segundoNombre.specified=false");
    }

    @Test
    @Transactional
    void getAllClientesBySegundoNombreContainsSomething() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where segundoNombre contains DEFAULT_SEGUNDO_NOMBRE
        defaultClienteShouldBeFound("segundoNombre.contains=" + DEFAULT_SEGUNDO_NOMBRE);

        // Get all the clienteList where segundoNombre contains UPDATED_SEGUNDO_NOMBRE
        defaultClienteShouldNotBeFound("segundoNombre.contains=" + UPDATED_SEGUNDO_NOMBRE);
    }

    @Test
    @Transactional
    void getAllClientesBySegundoNombreNotContainsSomething() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where segundoNombre does not contain DEFAULT_SEGUNDO_NOMBRE
        defaultClienteShouldNotBeFound("segundoNombre.doesNotContain=" + DEFAULT_SEGUNDO_NOMBRE);

        // Get all the clienteList where segundoNombre does not contain UPDATED_SEGUNDO_NOMBRE
        defaultClienteShouldBeFound("segundoNombre.doesNotContain=" + UPDATED_SEGUNDO_NOMBRE);
    }

    @Test
    @Transactional
    void getAllClientesByPrimerApellidoIsEqualToSomething() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where primerApellido equals to DEFAULT_PRIMER_APELLIDO
        defaultClienteShouldBeFound("primerApellido.equals=" + DEFAULT_PRIMER_APELLIDO);

        // Get all the clienteList where primerApellido equals to UPDATED_PRIMER_APELLIDO
        defaultClienteShouldNotBeFound("primerApellido.equals=" + UPDATED_PRIMER_APELLIDO);
    }

    @Test
    @Transactional
    void getAllClientesByPrimerApellidoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where primerApellido not equals to DEFAULT_PRIMER_APELLIDO
        defaultClienteShouldNotBeFound("primerApellido.notEquals=" + DEFAULT_PRIMER_APELLIDO);

        // Get all the clienteList where primerApellido not equals to UPDATED_PRIMER_APELLIDO
        defaultClienteShouldBeFound("primerApellido.notEquals=" + UPDATED_PRIMER_APELLIDO);
    }

    @Test
    @Transactional
    void getAllClientesByPrimerApellidoIsInShouldWork() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where primerApellido in DEFAULT_PRIMER_APELLIDO or UPDATED_PRIMER_APELLIDO
        defaultClienteShouldBeFound("primerApellido.in=" + DEFAULT_PRIMER_APELLIDO + "," + UPDATED_PRIMER_APELLIDO);

        // Get all the clienteList where primerApellido equals to UPDATED_PRIMER_APELLIDO
        defaultClienteShouldNotBeFound("primerApellido.in=" + UPDATED_PRIMER_APELLIDO);
    }

    @Test
    @Transactional
    void getAllClientesByPrimerApellidoIsNullOrNotNull() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where primerApellido is not null
        defaultClienteShouldBeFound("primerApellido.specified=true");

        // Get all the clienteList where primerApellido is null
        defaultClienteShouldNotBeFound("primerApellido.specified=false");
    }

    @Test
    @Transactional
    void getAllClientesByPrimerApellidoContainsSomething() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where primerApellido contains DEFAULT_PRIMER_APELLIDO
        defaultClienteShouldBeFound("primerApellido.contains=" + DEFAULT_PRIMER_APELLIDO);

        // Get all the clienteList where primerApellido contains UPDATED_PRIMER_APELLIDO
        defaultClienteShouldNotBeFound("primerApellido.contains=" + UPDATED_PRIMER_APELLIDO);
    }

    @Test
    @Transactional
    void getAllClientesByPrimerApellidoNotContainsSomething() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where primerApellido does not contain DEFAULT_PRIMER_APELLIDO
        defaultClienteShouldNotBeFound("primerApellido.doesNotContain=" + DEFAULT_PRIMER_APELLIDO);

        // Get all the clienteList where primerApellido does not contain UPDATED_PRIMER_APELLIDO
        defaultClienteShouldBeFound("primerApellido.doesNotContain=" + UPDATED_PRIMER_APELLIDO);
    }

    @Test
    @Transactional
    void getAllClientesBySegundoApellidoIsEqualToSomething() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where segundoApellido equals to DEFAULT_SEGUNDO_APELLIDO
        defaultClienteShouldBeFound("segundoApellido.equals=" + DEFAULT_SEGUNDO_APELLIDO);

        // Get all the clienteList where segundoApellido equals to UPDATED_SEGUNDO_APELLIDO
        defaultClienteShouldNotBeFound("segundoApellido.equals=" + UPDATED_SEGUNDO_APELLIDO);
    }

    @Test
    @Transactional
    void getAllClientesBySegundoApellidoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where segundoApellido not equals to DEFAULT_SEGUNDO_APELLIDO
        defaultClienteShouldNotBeFound("segundoApellido.notEquals=" + DEFAULT_SEGUNDO_APELLIDO);

        // Get all the clienteList where segundoApellido not equals to UPDATED_SEGUNDO_APELLIDO
        defaultClienteShouldBeFound("segundoApellido.notEquals=" + UPDATED_SEGUNDO_APELLIDO);
    }

    @Test
    @Transactional
    void getAllClientesBySegundoApellidoIsInShouldWork() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where segundoApellido in DEFAULT_SEGUNDO_APELLIDO or UPDATED_SEGUNDO_APELLIDO
        defaultClienteShouldBeFound("segundoApellido.in=" + DEFAULT_SEGUNDO_APELLIDO + "," + UPDATED_SEGUNDO_APELLIDO);

        // Get all the clienteList where segundoApellido equals to UPDATED_SEGUNDO_APELLIDO
        defaultClienteShouldNotBeFound("segundoApellido.in=" + UPDATED_SEGUNDO_APELLIDO);
    }

    @Test
    @Transactional
    void getAllClientesBySegundoApellidoIsNullOrNotNull() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where segundoApellido is not null
        defaultClienteShouldBeFound("segundoApellido.specified=true");

        // Get all the clienteList where segundoApellido is null
        defaultClienteShouldNotBeFound("segundoApellido.specified=false");
    }

    @Test
    @Transactional
    void getAllClientesBySegundoApellidoContainsSomething() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where segundoApellido contains DEFAULT_SEGUNDO_APELLIDO
        defaultClienteShouldBeFound("segundoApellido.contains=" + DEFAULT_SEGUNDO_APELLIDO);

        // Get all the clienteList where segundoApellido contains UPDATED_SEGUNDO_APELLIDO
        defaultClienteShouldNotBeFound("segundoApellido.contains=" + UPDATED_SEGUNDO_APELLIDO);
    }

    @Test
    @Transactional
    void getAllClientesBySegundoApellidoNotContainsSomething() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where segundoApellido does not contain DEFAULT_SEGUNDO_APELLIDO
        defaultClienteShouldNotBeFound("segundoApellido.doesNotContain=" + DEFAULT_SEGUNDO_APELLIDO);

        // Get all the clienteList where segundoApellido does not contain UPDATED_SEGUNDO_APELLIDO
        defaultClienteShouldBeFound("segundoApellido.doesNotContain=" + UPDATED_SEGUNDO_APELLIDO);
    }

    @Test
    @Transactional
    void getAllClientesByPrimerTelefonoIsEqualToSomething() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where primerTelefono equals to DEFAULT_PRIMER_TELEFONO
        defaultClienteShouldBeFound("primerTelefono.equals=" + DEFAULT_PRIMER_TELEFONO);

        // Get all the clienteList where primerTelefono equals to UPDATED_PRIMER_TELEFONO
        defaultClienteShouldNotBeFound("primerTelefono.equals=" + UPDATED_PRIMER_TELEFONO);
    }

    @Test
    @Transactional
    void getAllClientesByPrimerTelefonoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where primerTelefono not equals to DEFAULT_PRIMER_TELEFONO
        defaultClienteShouldNotBeFound("primerTelefono.notEquals=" + DEFAULT_PRIMER_TELEFONO);

        // Get all the clienteList where primerTelefono not equals to UPDATED_PRIMER_TELEFONO
        defaultClienteShouldBeFound("primerTelefono.notEquals=" + UPDATED_PRIMER_TELEFONO);
    }

    @Test
    @Transactional
    void getAllClientesByPrimerTelefonoIsInShouldWork() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where primerTelefono in DEFAULT_PRIMER_TELEFONO or UPDATED_PRIMER_TELEFONO
        defaultClienteShouldBeFound("primerTelefono.in=" + DEFAULT_PRIMER_TELEFONO + "," + UPDATED_PRIMER_TELEFONO);

        // Get all the clienteList where primerTelefono equals to UPDATED_PRIMER_TELEFONO
        defaultClienteShouldNotBeFound("primerTelefono.in=" + UPDATED_PRIMER_TELEFONO);
    }

    @Test
    @Transactional
    void getAllClientesByPrimerTelefonoIsNullOrNotNull() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where primerTelefono is not null
        defaultClienteShouldBeFound("primerTelefono.specified=true");

        // Get all the clienteList where primerTelefono is null
        defaultClienteShouldNotBeFound("primerTelefono.specified=false");
    }

    @Test
    @Transactional
    void getAllClientesByPrimerTelefonoContainsSomething() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where primerTelefono contains DEFAULT_PRIMER_TELEFONO
        defaultClienteShouldBeFound("primerTelefono.contains=" + DEFAULT_PRIMER_TELEFONO);

        // Get all the clienteList where primerTelefono contains UPDATED_PRIMER_TELEFONO
        defaultClienteShouldNotBeFound("primerTelefono.contains=" + UPDATED_PRIMER_TELEFONO);
    }

    @Test
    @Transactional
    void getAllClientesByPrimerTelefonoNotContainsSomething() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where primerTelefono does not contain DEFAULT_PRIMER_TELEFONO
        defaultClienteShouldNotBeFound("primerTelefono.doesNotContain=" + DEFAULT_PRIMER_TELEFONO);

        // Get all the clienteList where primerTelefono does not contain UPDATED_PRIMER_TELEFONO
        defaultClienteShouldBeFound("primerTelefono.doesNotContain=" + UPDATED_PRIMER_TELEFONO);
    }

    @Test
    @Transactional
    void getAllClientesBySegundoTelefonoIsEqualToSomething() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where segundoTelefono equals to DEFAULT_SEGUNDO_TELEFONO
        defaultClienteShouldBeFound("segundoTelefono.equals=" + DEFAULT_SEGUNDO_TELEFONO);

        // Get all the clienteList where segundoTelefono equals to UPDATED_SEGUNDO_TELEFONO
        defaultClienteShouldNotBeFound("segundoTelefono.equals=" + UPDATED_SEGUNDO_TELEFONO);
    }

    @Test
    @Transactional
    void getAllClientesBySegundoTelefonoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where segundoTelefono not equals to DEFAULT_SEGUNDO_TELEFONO
        defaultClienteShouldNotBeFound("segundoTelefono.notEquals=" + DEFAULT_SEGUNDO_TELEFONO);

        // Get all the clienteList where segundoTelefono not equals to UPDATED_SEGUNDO_TELEFONO
        defaultClienteShouldBeFound("segundoTelefono.notEquals=" + UPDATED_SEGUNDO_TELEFONO);
    }

    @Test
    @Transactional
    void getAllClientesBySegundoTelefonoIsInShouldWork() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where segundoTelefono in DEFAULT_SEGUNDO_TELEFONO or UPDATED_SEGUNDO_TELEFONO
        defaultClienteShouldBeFound("segundoTelefono.in=" + DEFAULT_SEGUNDO_TELEFONO + "," + UPDATED_SEGUNDO_TELEFONO);

        // Get all the clienteList where segundoTelefono equals to UPDATED_SEGUNDO_TELEFONO
        defaultClienteShouldNotBeFound("segundoTelefono.in=" + UPDATED_SEGUNDO_TELEFONO);
    }

    @Test
    @Transactional
    void getAllClientesBySegundoTelefonoIsNullOrNotNull() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where segundoTelefono is not null
        defaultClienteShouldBeFound("segundoTelefono.specified=true");

        // Get all the clienteList where segundoTelefono is null
        defaultClienteShouldNotBeFound("segundoTelefono.specified=false");
    }

    @Test
    @Transactional
    void getAllClientesBySegundoTelefonoContainsSomething() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where segundoTelefono contains DEFAULT_SEGUNDO_TELEFONO
        defaultClienteShouldBeFound("segundoTelefono.contains=" + DEFAULT_SEGUNDO_TELEFONO);

        // Get all the clienteList where segundoTelefono contains UPDATED_SEGUNDO_TELEFONO
        defaultClienteShouldNotBeFound("segundoTelefono.contains=" + UPDATED_SEGUNDO_TELEFONO);
    }

    @Test
    @Transactional
    void getAllClientesBySegundoTelefonoNotContainsSomething() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where segundoTelefono does not contain DEFAULT_SEGUNDO_TELEFONO
        defaultClienteShouldNotBeFound("segundoTelefono.doesNotContain=" + DEFAULT_SEGUNDO_TELEFONO);

        // Get all the clienteList where segundoTelefono does not contain UPDATED_SEGUNDO_TELEFONO
        defaultClienteShouldBeFound("segundoTelefono.doesNotContain=" + UPDATED_SEGUNDO_TELEFONO);
    }

    @Test
    @Transactional
    void getAllClientesByMunicipioIsEqualToSomething() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);
        Municipio municipio = MunicipioResourceIT.createEntity(em);
        em.persist(municipio);
        em.flush();
        cliente.setMunicipio(municipio);
        clienteRepository.saveAndFlush(cliente);
        Long municipioId = municipio.getId();

        // Get all the clienteList where municipio equals to municipioId
        defaultClienteShouldBeFound("municipioId.equals=" + municipioId);

        // Get all the clienteList where municipio equals to (municipioId + 1)
        defaultClienteShouldNotBeFound("municipioId.equals=" + (municipioId + 1));
    }

    @Test
    @Transactional
    void getAllClientesByGeneroIsEqualToSomething() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);
        Genero genero = GeneroResourceIT.createEntity(em);
        em.persist(genero);
        em.flush();
        cliente.setGenero(genero);
        clienteRepository.saveAndFlush(cliente);
        Long generoId = genero.getId();

        // Get all the clienteList where genero equals to generoId
        defaultClienteShouldBeFound("generoId.equals=" + generoId);

        // Get all the clienteList where genero equals to (generoId + 1)
        defaultClienteShouldNotBeFound("generoId.equals=" + (generoId + 1));
    }

    @Test
    @Transactional
    void getAllClientesByVentaIsEqualToSomething() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);
        Venta venta = VentaResourceIT.createEntity(em);
        em.persist(venta);
        em.flush();
        cliente.addVenta(venta);
        clienteRepository.saveAndFlush(cliente);
        Long ventaId = venta.getId();

        // Get all the clienteList where venta equals to ventaId
        defaultClienteShouldBeFound("ventaId.equals=" + ventaId);

        // Get all the clienteList where venta equals to (ventaId + 1)
        defaultClienteShouldNotBeFound("ventaId.equals=" + (ventaId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultClienteShouldBeFound(String filter) throws Exception {
        restClienteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cliente.getId().intValue())))
            .andExpect(jsonPath("$.[*].primerNombre").value(hasItem(DEFAULT_PRIMER_NOMBRE)))
            .andExpect(jsonPath("$.[*].segundoNombre").value(hasItem(DEFAULT_SEGUNDO_NOMBRE)))
            .andExpect(jsonPath("$.[*].primerApellido").value(hasItem(DEFAULT_PRIMER_APELLIDO)))
            .andExpect(jsonPath("$.[*].segundoApellido").value(hasItem(DEFAULT_SEGUNDO_APELLIDO)))
            .andExpect(jsonPath("$.[*].primerTelefono").value(hasItem(DEFAULT_PRIMER_TELEFONO)))
            .andExpect(jsonPath("$.[*].segundoTelefono").value(hasItem(DEFAULT_SEGUNDO_TELEFONO)));

        // Check, that the count call also returns 1
        restClienteMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultClienteShouldNotBeFound(String filter) throws Exception {
        restClienteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restClienteMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCliente() throws Exception {
        // Get the cliente
        restClienteMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCliente() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        int databaseSizeBeforeUpdate = clienteRepository.findAll().size();

        // Update the cliente
        Cliente updatedCliente = clienteRepository.findById(cliente.getId()).get();
        // Disconnect from session so that the updates on updatedCliente are not directly saved in db
        em.detach(updatedCliente);
        updatedCliente
            .primerNombre(UPDATED_PRIMER_NOMBRE)
            .segundoNombre(UPDATED_SEGUNDO_NOMBRE)
            .primerApellido(UPDATED_PRIMER_APELLIDO)
            .segundoApellido(UPDATED_SEGUNDO_APELLIDO)
            .primerTelefono(UPDATED_PRIMER_TELEFONO)
            .segundoTelefono(UPDATED_SEGUNDO_TELEFONO);
        ClienteDTO clienteDTO = clienteMapper.toDto(updatedCliente);

        restClienteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, clienteDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(clienteDTO))
            )
            .andExpect(status().isOk());

        // Validate the Cliente in the database
        List<Cliente> clienteList = clienteRepository.findAll();
        assertThat(clienteList).hasSize(databaseSizeBeforeUpdate);
        Cliente testCliente = clienteList.get(clienteList.size() - 1);
        assertThat(testCliente.getPrimerNombre()).isEqualTo(UPDATED_PRIMER_NOMBRE);
        assertThat(testCliente.getSegundoNombre()).isEqualTo(UPDATED_SEGUNDO_NOMBRE);
        assertThat(testCliente.getPrimerApellido()).isEqualTo(UPDATED_PRIMER_APELLIDO);
        assertThat(testCliente.getSegundoApellido()).isEqualTo(UPDATED_SEGUNDO_APELLIDO);
        assertThat(testCliente.getPrimerTelefono()).isEqualTo(UPDATED_PRIMER_TELEFONO);
        assertThat(testCliente.getSegundoTelefono()).isEqualTo(UPDATED_SEGUNDO_TELEFONO);
    }

    @Test
    @Transactional
    void putNonExistingCliente() throws Exception {
        int databaseSizeBeforeUpdate = clienteRepository.findAll().size();
        cliente.setId(count.incrementAndGet());

        // Create the Cliente
        ClienteDTO clienteDTO = clienteMapper.toDto(cliente);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClienteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, clienteDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(clienteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cliente in the database
        List<Cliente> clienteList = clienteRepository.findAll();
        assertThat(clienteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCliente() throws Exception {
        int databaseSizeBeforeUpdate = clienteRepository.findAll().size();
        cliente.setId(count.incrementAndGet());

        // Create the Cliente
        ClienteDTO clienteDTO = clienteMapper.toDto(cliente);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClienteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(clienteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cliente in the database
        List<Cliente> clienteList = clienteRepository.findAll();
        assertThat(clienteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCliente() throws Exception {
        int databaseSizeBeforeUpdate = clienteRepository.findAll().size();
        cliente.setId(count.incrementAndGet());

        // Create the Cliente
        ClienteDTO clienteDTO = clienteMapper.toDto(cliente);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClienteMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(clienteDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Cliente in the database
        List<Cliente> clienteList = clienteRepository.findAll();
        assertThat(clienteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateClienteWithPatch() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        int databaseSizeBeforeUpdate = clienteRepository.findAll().size();

        // Update the cliente using partial update
        Cliente partialUpdatedCliente = new Cliente();
        partialUpdatedCliente.setId(cliente.getId());

        partialUpdatedCliente.primerApellido(UPDATED_PRIMER_APELLIDO);

        restClienteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCliente.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCliente))
            )
            .andExpect(status().isOk());

        // Validate the Cliente in the database
        List<Cliente> clienteList = clienteRepository.findAll();
        assertThat(clienteList).hasSize(databaseSizeBeforeUpdate);
        Cliente testCliente = clienteList.get(clienteList.size() - 1);
        assertThat(testCliente.getPrimerNombre()).isEqualTo(DEFAULT_PRIMER_NOMBRE);
        assertThat(testCliente.getSegundoNombre()).isEqualTo(DEFAULT_SEGUNDO_NOMBRE);
        assertThat(testCliente.getPrimerApellido()).isEqualTo(UPDATED_PRIMER_APELLIDO);
        assertThat(testCliente.getSegundoApellido()).isEqualTo(DEFAULT_SEGUNDO_APELLIDO);
        assertThat(testCliente.getPrimerTelefono()).isEqualTo(DEFAULT_PRIMER_TELEFONO);
        assertThat(testCliente.getSegundoTelefono()).isEqualTo(DEFAULT_SEGUNDO_TELEFONO);
    }

    @Test
    @Transactional
    void fullUpdateClienteWithPatch() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        int databaseSizeBeforeUpdate = clienteRepository.findAll().size();

        // Update the cliente using partial update
        Cliente partialUpdatedCliente = new Cliente();
        partialUpdatedCliente.setId(cliente.getId());

        partialUpdatedCliente
            .primerNombre(UPDATED_PRIMER_NOMBRE)
            .segundoNombre(UPDATED_SEGUNDO_NOMBRE)
            .primerApellido(UPDATED_PRIMER_APELLIDO)
            .segundoApellido(UPDATED_SEGUNDO_APELLIDO)
            .primerTelefono(UPDATED_PRIMER_TELEFONO)
            .segundoTelefono(UPDATED_SEGUNDO_TELEFONO);

        restClienteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCliente.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCliente))
            )
            .andExpect(status().isOk());

        // Validate the Cliente in the database
        List<Cliente> clienteList = clienteRepository.findAll();
        assertThat(clienteList).hasSize(databaseSizeBeforeUpdate);
        Cliente testCliente = clienteList.get(clienteList.size() - 1);
        assertThat(testCliente.getPrimerNombre()).isEqualTo(UPDATED_PRIMER_NOMBRE);
        assertThat(testCliente.getSegundoNombre()).isEqualTo(UPDATED_SEGUNDO_NOMBRE);
        assertThat(testCliente.getPrimerApellido()).isEqualTo(UPDATED_PRIMER_APELLIDO);
        assertThat(testCliente.getSegundoApellido()).isEqualTo(UPDATED_SEGUNDO_APELLIDO);
        assertThat(testCliente.getPrimerTelefono()).isEqualTo(UPDATED_PRIMER_TELEFONO);
        assertThat(testCliente.getSegundoTelefono()).isEqualTo(UPDATED_SEGUNDO_TELEFONO);
    }

    @Test
    @Transactional
    void patchNonExistingCliente() throws Exception {
        int databaseSizeBeforeUpdate = clienteRepository.findAll().size();
        cliente.setId(count.incrementAndGet());

        // Create the Cliente
        ClienteDTO clienteDTO = clienteMapper.toDto(cliente);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClienteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, clienteDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(clienteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cliente in the database
        List<Cliente> clienteList = clienteRepository.findAll();
        assertThat(clienteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCliente() throws Exception {
        int databaseSizeBeforeUpdate = clienteRepository.findAll().size();
        cliente.setId(count.incrementAndGet());

        // Create the Cliente
        ClienteDTO clienteDTO = clienteMapper.toDto(cliente);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClienteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(clienteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cliente in the database
        List<Cliente> clienteList = clienteRepository.findAll();
        assertThat(clienteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCliente() throws Exception {
        int databaseSizeBeforeUpdate = clienteRepository.findAll().size();
        cliente.setId(count.incrementAndGet());

        // Create the Cliente
        ClienteDTO clienteDTO = clienteMapper.toDto(cliente);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClienteMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(clienteDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Cliente in the database
        List<Cliente> clienteList = clienteRepository.findAll();
        assertThat(clienteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCliente() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        int databaseSizeBeforeDelete = clienteRepository.findAll().size();

        // Delete the cliente
        restClienteMockMvc
            .perform(delete(ENTITY_API_URL_ID, cliente.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Cliente> clienteList = clienteRepository.findAll();
        assertThat(clienteList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
