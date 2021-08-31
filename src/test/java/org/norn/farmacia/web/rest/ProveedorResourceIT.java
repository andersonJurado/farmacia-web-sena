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
import org.norn.farmacia.domain.Compra;
import org.norn.farmacia.domain.Municipio;
import org.norn.farmacia.domain.Proveedor;
import org.norn.farmacia.repository.ProveedorRepository;
import org.norn.farmacia.service.criteria.ProveedorCriteria;
import org.norn.farmacia.service.dto.ProveedorDTO;
import org.norn.farmacia.service.mapper.ProveedorMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ProveedorResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProveedorResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_PRIMER_TELEFONO = "AAAAAAAAAA";
    private static final String UPDATED_PRIMER_TELEFONO = "BBBBBBBBBB";

    private static final String DEFAULT_SEGUNDO_TELEFONO = "AAAAAAAAAA";
    private static final String UPDATED_SEGUNDO_TELEFONO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/proveedors";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProveedorRepository proveedorRepository;

    @Autowired
    private ProveedorMapper proveedorMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProveedorMockMvc;

    private Proveedor proveedor;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Proveedor createEntity(EntityManager em) {
        Proveedor proveedor = new Proveedor()
            .nombre(DEFAULT_NOMBRE)
            .primerTelefono(DEFAULT_PRIMER_TELEFONO)
            .segundoTelefono(DEFAULT_SEGUNDO_TELEFONO);
        return proveedor;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Proveedor createUpdatedEntity(EntityManager em) {
        Proveedor proveedor = new Proveedor()
            .nombre(UPDATED_NOMBRE)
            .primerTelefono(UPDATED_PRIMER_TELEFONO)
            .segundoTelefono(UPDATED_SEGUNDO_TELEFONO);
        return proveedor;
    }

    @BeforeEach
    public void initTest() {
        proveedor = createEntity(em);
    }

    @Test
    @Transactional
    void createProveedor() throws Exception {
        int databaseSizeBeforeCreate = proveedorRepository.findAll().size();
        // Create the Proveedor
        ProveedorDTO proveedorDTO = proveedorMapper.toDto(proveedor);
        restProveedorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(proveedorDTO)))
            .andExpect(status().isCreated());

        // Validate the Proveedor in the database
        List<Proveedor> proveedorList = proveedorRepository.findAll();
        assertThat(proveedorList).hasSize(databaseSizeBeforeCreate + 1);
        Proveedor testProveedor = proveedorList.get(proveedorList.size() - 1);
        assertThat(testProveedor.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testProveedor.getPrimerTelefono()).isEqualTo(DEFAULT_PRIMER_TELEFONO);
        assertThat(testProveedor.getSegundoTelefono()).isEqualTo(DEFAULT_SEGUNDO_TELEFONO);
    }

    @Test
    @Transactional
    void createProveedorWithExistingId() throws Exception {
        // Create the Proveedor with an existing ID
        proveedor.setId(1L);
        ProveedorDTO proveedorDTO = proveedorMapper.toDto(proveedor);

        int databaseSizeBeforeCreate = proveedorRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProveedorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(proveedorDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Proveedor in the database
        List<Proveedor> proveedorList = proveedorRepository.findAll();
        assertThat(proveedorList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = proveedorRepository.findAll().size();
        // set the field null
        proveedor.setNombre(null);

        // Create the Proveedor, which fails.
        ProveedorDTO proveedorDTO = proveedorMapper.toDto(proveedor);

        restProveedorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(proveedorDTO)))
            .andExpect(status().isBadRequest());

        List<Proveedor> proveedorList = proveedorRepository.findAll();
        assertThat(proveedorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProveedors() throws Exception {
        // Initialize the database
        proveedorRepository.saveAndFlush(proveedor);

        // Get all the proveedorList
        restProveedorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(proveedor.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].primerTelefono").value(hasItem(DEFAULT_PRIMER_TELEFONO)))
            .andExpect(jsonPath("$.[*].segundoTelefono").value(hasItem(DEFAULT_SEGUNDO_TELEFONO)));
    }

    @Test
    @Transactional
    void getProveedor() throws Exception {
        // Initialize the database
        proveedorRepository.saveAndFlush(proveedor);

        // Get the proveedor
        restProveedorMockMvc
            .perform(get(ENTITY_API_URL_ID, proveedor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(proveedor.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.primerTelefono").value(DEFAULT_PRIMER_TELEFONO))
            .andExpect(jsonPath("$.segundoTelefono").value(DEFAULT_SEGUNDO_TELEFONO));
    }

    @Test
    @Transactional
    void getProveedorsByIdFiltering() throws Exception {
        // Initialize the database
        proveedorRepository.saveAndFlush(proveedor);

        Long id = proveedor.getId();

        defaultProveedorShouldBeFound("id.equals=" + id);
        defaultProveedorShouldNotBeFound("id.notEquals=" + id);

        defaultProveedorShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultProveedorShouldNotBeFound("id.greaterThan=" + id);

        defaultProveedorShouldBeFound("id.lessThanOrEqual=" + id);
        defaultProveedorShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllProveedorsByNombreIsEqualToSomething() throws Exception {
        // Initialize the database
        proveedorRepository.saveAndFlush(proveedor);

        // Get all the proveedorList where nombre equals to DEFAULT_NOMBRE
        defaultProveedorShouldBeFound("nombre.equals=" + DEFAULT_NOMBRE);

        // Get all the proveedorList where nombre equals to UPDATED_NOMBRE
        defaultProveedorShouldNotBeFound("nombre.equals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllProveedorsByNombreIsNotEqualToSomething() throws Exception {
        // Initialize the database
        proveedorRepository.saveAndFlush(proveedor);

        // Get all the proveedorList where nombre not equals to DEFAULT_NOMBRE
        defaultProveedorShouldNotBeFound("nombre.notEquals=" + DEFAULT_NOMBRE);

        // Get all the proveedorList where nombre not equals to UPDATED_NOMBRE
        defaultProveedorShouldBeFound("nombre.notEquals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllProveedorsByNombreIsInShouldWork() throws Exception {
        // Initialize the database
        proveedorRepository.saveAndFlush(proveedor);

        // Get all the proveedorList where nombre in DEFAULT_NOMBRE or UPDATED_NOMBRE
        defaultProveedorShouldBeFound("nombre.in=" + DEFAULT_NOMBRE + "," + UPDATED_NOMBRE);

        // Get all the proveedorList where nombre equals to UPDATED_NOMBRE
        defaultProveedorShouldNotBeFound("nombre.in=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllProveedorsByNombreIsNullOrNotNull() throws Exception {
        // Initialize the database
        proveedorRepository.saveAndFlush(proveedor);

        // Get all the proveedorList where nombre is not null
        defaultProveedorShouldBeFound("nombre.specified=true");

        // Get all the proveedorList where nombre is null
        defaultProveedorShouldNotBeFound("nombre.specified=false");
    }

    @Test
    @Transactional
    void getAllProveedorsByNombreContainsSomething() throws Exception {
        // Initialize the database
        proveedorRepository.saveAndFlush(proveedor);

        // Get all the proveedorList where nombre contains DEFAULT_NOMBRE
        defaultProveedorShouldBeFound("nombre.contains=" + DEFAULT_NOMBRE);

        // Get all the proveedorList where nombre contains UPDATED_NOMBRE
        defaultProveedorShouldNotBeFound("nombre.contains=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllProveedorsByNombreNotContainsSomething() throws Exception {
        // Initialize the database
        proveedorRepository.saveAndFlush(proveedor);

        // Get all the proveedorList where nombre does not contain DEFAULT_NOMBRE
        defaultProveedorShouldNotBeFound("nombre.doesNotContain=" + DEFAULT_NOMBRE);

        // Get all the proveedorList where nombre does not contain UPDATED_NOMBRE
        defaultProveedorShouldBeFound("nombre.doesNotContain=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllProveedorsByPrimerTelefonoIsEqualToSomething() throws Exception {
        // Initialize the database
        proveedorRepository.saveAndFlush(proveedor);

        // Get all the proveedorList where primerTelefono equals to DEFAULT_PRIMER_TELEFONO
        defaultProveedorShouldBeFound("primerTelefono.equals=" + DEFAULT_PRIMER_TELEFONO);

        // Get all the proveedorList where primerTelefono equals to UPDATED_PRIMER_TELEFONO
        defaultProveedorShouldNotBeFound("primerTelefono.equals=" + UPDATED_PRIMER_TELEFONO);
    }

    @Test
    @Transactional
    void getAllProveedorsByPrimerTelefonoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        proveedorRepository.saveAndFlush(proveedor);

        // Get all the proveedorList where primerTelefono not equals to DEFAULT_PRIMER_TELEFONO
        defaultProveedorShouldNotBeFound("primerTelefono.notEquals=" + DEFAULT_PRIMER_TELEFONO);

        // Get all the proveedorList where primerTelefono not equals to UPDATED_PRIMER_TELEFONO
        defaultProveedorShouldBeFound("primerTelefono.notEquals=" + UPDATED_PRIMER_TELEFONO);
    }

    @Test
    @Transactional
    void getAllProveedorsByPrimerTelefonoIsInShouldWork() throws Exception {
        // Initialize the database
        proveedorRepository.saveAndFlush(proveedor);

        // Get all the proveedorList where primerTelefono in DEFAULT_PRIMER_TELEFONO or UPDATED_PRIMER_TELEFONO
        defaultProveedorShouldBeFound("primerTelefono.in=" + DEFAULT_PRIMER_TELEFONO + "," + UPDATED_PRIMER_TELEFONO);

        // Get all the proveedorList where primerTelefono equals to UPDATED_PRIMER_TELEFONO
        defaultProveedorShouldNotBeFound("primerTelefono.in=" + UPDATED_PRIMER_TELEFONO);
    }

    @Test
    @Transactional
    void getAllProveedorsByPrimerTelefonoIsNullOrNotNull() throws Exception {
        // Initialize the database
        proveedorRepository.saveAndFlush(proveedor);

        // Get all the proveedorList where primerTelefono is not null
        defaultProveedorShouldBeFound("primerTelefono.specified=true");

        // Get all the proveedorList where primerTelefono is null
        defaultProveedorShouldNotBeFound("primerTelefono.specified=false");
    }

    @Test
    @Transactional
    void getAllProveedorsByPrimerTelefonoContainsSomething() throws Exception {
        // Initialize the database
        proveedorRepository.saveAndFlush(proveedor);

        // Get all the proveedorList where primerTelefono contains DEFAULT_PRIMER_TELEFONO
        defaultProveedorShouldBeFound("primerTelefono.contains=" + DEFAULT_PRIMER_TELEFONO);

        // Get all the proveedorList where primerTelefono contains UPDATED_PRIMER_TELEFONO
        defaultProveedorShouldNotBeFound("primerTelefono.contains=" + UPDATED_PRIMER_TELEFONO);
    }

    @Test
    @Transactional
    void getAllProveedorsByPrimerTelefonoNotContainsSomething() throws Exception {
        // Initialize the database
        proveedorRepository.saveAndFlush(proveedor);

        // Get all the proveedorList where primerTelefono does not contain DEFAULT_PRIMER_TELEFONO
        defaultProveedorShouldNotBeFound("primerTelefono.doesNotContain=" + DEFAULT_PRIMER_TELEFONO);

        // Get all the proveedorList where primerTelefono does not contain UPDATED_PRIMER_TELEFONO
        defaultProveedorShouldBeFound("primerTelefono.doesNotContain=" + UPDATED_PRIMER_TELEFONO);
    }

    @Test
    @Transactional
    void getAllProveedorsBySegundoTelefonoIsEqualToSomething() throws Exception {
        // Initialize the database
        proveedorRepository.saveAndFlush(proveedor);

        // Get all the proveedorList where segundoTelefono equals to DEFAULT_SEGUNDO_TELEFONO
        defaultProveedorShouldBeFound("segundoTelefono.equals=" + DEFAULT_SEGUNDO_TELEFONO);

        // Get all the proveedorList where segundoTelefono equals to UPDATED_SEGUNDO_TELEFONO
        defaultProveedorShouldNotBeFound("segundoTelefono.equals=" + UPDATED_SEGUNDO_TELEFONO);
    }

    @Test
    @Transactional
    void getAllProveedorsBySegundoTelefonoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        proveedorRepository.saveAndFlush(proveedor);

        // Get all the proveedorList where segundoTelefono not equals to DEFAULT_SEGUNDO_TELEFONO
        defaultProveedorShouldNotBeFound("segundoTelefono.notEquals=" + DEFAULT_SEGUNDO_TELEFONO);

        // Get all the proveedorList where segundoTelefono not equals to UPDATED_SEGUNDO_TELEFONO
        defaultProveedorShouldBeFound("segundoTelefono.notEquals=" + UPDATED_SEGUNDO_TELEFONO);
    }

    @Test
    @Transactional
    void getAllProveedorsBySegundoTelefonoIsInShouldWork() throws Exception {
        // Initialize the database
        proveedorRepository.saveAndFlush(proveedor);

        // Get all the proveedorList where segundoTelefono in DEFAULT_SEGUNDO_TELEFONO or UPDATED_SEGUNDO_TELEFONO
        defaultProveedorShouldBeFound("segundoTelefono.in=" + DEFAULT_SEGUNDO_TELEFONO + "," + UPDATED_SEGUNDO_TELEFONO);

        // Get all the proveedorList where segundoTelefono equals to UPDATED_SEGUNDO_TELEFONO
        defaultProveedorShouldNotBeFound("segundoTelefono.in=" + UPDATED_SEGUNDO_TELEFONO);
    }

    @Test
    @Transactional
    void getAllProveedorsBySegundoTelefonoIsNullOrNotNull() throws Exception {
        // Initialize the database
        proveedorRepository.saveAndFlush(proveedor);

        // Get all the proveedorList where segundoTelefono is not null
        defaultProveedorShouldBeFound("segundoTelefono.specified=true");

        // Get all the proveedorList where segundoTelefono is null
        defaultProveedorShouldNotBeFound("segundoTelefono.specified=false");
    }

    @Test
    @Transactional
    void getAllProveedorsBySegundoTelefonoContainsSomething() throws Exception {
        // Initialize the database
        proveedorRepository.saveAndFlush(proveedor);

        // Get all the proveedorList where segundoTelefono contains DEFAULT_SEGUNDO_TELEFONO
        defaultProveedorShouldBeFound("segundoTelefono.contains=" + DEFAULT_SEGUNDO_TELEFONO);

        // Get all the proveedorList where segundoTelefono contains UPDATED_SEGUNDO_TELEFONO
        defaultProveedorShouldNotBeFound("segundoTelefono.contains=" + UPDATED_SEGUNDO_TELEFONO);
    }

    @Test
    @Transactional
    void getAllProveedorsBySegundoTelefonoNotContainsSomething() throws Exception {
        // Initialize the database
        proveedorRepository.saveAndFlush(proveedor);

        // Get all the proveedorList where segundoTelefono does not contain DEFAULT_SEGUNDO_TELEFONO
        defaultProveedorShouldNotBeFound("segundoTelefono.doesNotContain=" + DEFAULT_SEGUNDO_TELEFONO);

        // Get all the proveedorList where segundoTelefono does not contain UPDATED_SEGUNDO_TELEFONO
        defaultProveedorShouldBeFound("segundoTelefono.doesNotContain=" + UPDATED_SEGUNDO_TELEFONO);
    }

    @Test
    @Transactional
    void getAllProveedorsByMunicpioIsEqualToSomething() throws Exception {
        // Initialize the database
        proveedorRepository.saveAndFlush(proveedor);
        Municipio municpio = MunicipioResourceIT.createEntity(em);
        em.persist(municpio);
        em.flush();
        proveedor.setMunicpio(municpio);
        proveedorRepository.saveAndFlush(proveedor);
        Long municpioId = municpio.getId();

        // Get all the proveedorList where municpio equals to municpioId
        defaultProveedorShouldBeFound("municpioId.equals=" + municpioId);

        // Get all the proveedorList where municpio equals to (municpioId + 1)
        defaultProveedorShouldNotBeFound("municpioId.equals=" + (municpioId + 1));
    }

    @Test
    @Transactional
    void getAllProveedorsByCompraIsEqualToSomething() throws Exception {
        // Initialize the database
        proveedorRepository.saveAndFlush(proveedor);
        Compra compra = CompraResourceIT.createEntity(em);
        em.persist(compra);
        em.flush();
        proveedor.addCompra(compra);
        proveedorRepository.saveAndFlush(proveedor);
        Long compraId = compra.getId();

        // Get all the proveedorList where compra equals to compraId
        defaultProveedorShouldBeFound("compraId.equals=" + compraId);

        // Get all the proveedorList where compra equals to (compraId + 1)
        defaultProveedorShouldNotBeFound("compraId.equals=" + (compraId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultProveedorShouldBeFound(String filter) throws Exception {
        restProveedorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(proveedor.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].primerTelefono").value(hasItem(DEFAULT_PRIMER_TELEFONO)))
            .andExpect(jsonPath("$.[*].segundoTelefono").value(hasItem(DEFAULT_SEGUNDO_TELEFONO)));

        // Check, that the count call also returns 1
        restProveedorMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultProveedorShouldNotBeFound(String filter) throws Exception {
        restProveedorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProveedorMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingProveedor() throws Exception {
        // Get the proveedor
        restProveedorMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewProveedor() throws Exception {
        // Initialize the database
        proveedorRepository.saveAndFlush(proveedor);

        int databaseSizeBeforeUpdate = proveedorRepository.findAll().size();

        // Update the proveedor
        Proveedor updatedProveedor = proveedorRepository.findById(proveedor.getId()).get();
        // Disconnect from session so that the updates on updatedProveedor are not directly saved in db
        em.detach(updatedProveedor);
        updatedProveedor.nombre(UPDATED_NOMBRE).primerTelefono(UPDATED_PRIMER_TELEFONO).segundoTelefono(UPDATED_SEGUNDO_TELEFONO);
        ProveedorDTO proveedorDTO = proveedorMapper.toDto(updatedProveedor);

        restProveedorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, proveedorDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(proveedorDTO))
            )
            .andExpect(status().isOk());

        // Validate the Proveedor in the database
        List<Proveedor> proveedorList = proveedorRepository.findAll();
        assertThat(proveedorList).hasSize(databaseSizeBeforeUpdate);
        Proveedor testProveedor = proveedorList.get(proveedorList.size() - 1);
        assertThat(testProveedor.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testProveedor.getPrimerTelefono()).isEqualTo(UPDATED_PRIMER_TELEFONO);
        assertThat(testProveedor.getSegundoTelefono()).isEqualTo(UPDATED_SEGUNDO_TELEFONO);
    }

    @Test
    @Transactional
    void putNonExistingProveedor() throws Exception {
        int databaseSizeBeforeUpdate = proveedorRepository.findAll().size();
        proveedor.setId(count.incrementAndGet());

        // Create the Proveedor
        ProveedorDTO proveedorDTO = proveedorMapper.toDto(proveedor);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProveedorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, proveedorDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(proveedorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Proveedor in the database
        List<Proveedor> proveedorList = proveedorRepository.findAll();
        assertThat(proveedorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProveedor() throws Exception {
        int databaseSizeBeforeUpdate = proveedorRepository.findAll().size();
        proveedor.setId(count.incrementAndGet());

        // Create the Proveedor
        ProveedorDTO proveedorDTO = proveedorMapper.toDto(proveedor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProveedorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(proveedorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Proveedor in the database
        List<Proveedor> proveedorList = proveedorRepository.findAll();
        assertThat(proveedorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProveedor() throws Exception {
        int databaseSizeBeforeUpdate = proveedorRepository.findAll().size();
        proveedor.setId(count.incrementAndGet());

        // Create the Proveedor
        ProveedorDTO proveedorDTO = proveedorMapper.toDto(proveedor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProveedorMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(proveedorDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Proveedor in the database
        List<Proveedor> proveedorList = proveedorRepository.findAll();
        assertThat(proveedorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProveedorWithPatch() throws Exception {
        // Initialize the database
        proveedorRepository.saveAndFlush(proveedor);

        int databaseSizeBeforeUpdate = proveedorRepository.findAll().size();

        // Update the proveedor using partial update
        Proveedor partialUpdatedProveedor = new Proveedor();
        partialUpdatedProveedor.setId(proveedor.getId());

        partialUpdatedProveedor.nombre(UPDATED_NOMBRE).primerTelefono(UPDATED_PRIMER_TELEFONO);

        restProveedorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProveedor.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProveedor))
            )
            .andExpect(status().isOk());

        // Validate the Proveedor in the database
        List<Proveedor> proveedorList = proveedorRepository.findAll();
        assertThat(proveedorList).hasSize(databaseSizeBeforeUpdate);
        Proveedor testProveedor = proveedorList.get(proveedorList.size() - 1);
        assertThat(testProveedor.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testProveedor.getPrimerTelefono()).isEqualTo(UPDATED_PRIMER_TELEFONO);
        assertThat(testProveedor.getSegundoTelefono()).isEqualTo(DEFAULT_SEGUNDO_TELEFONO);
    }

    @Test
    @Transactional
    void fullUpdateProveedorWithPatch() throws Exception {
        // Initialize the database
        proveedorRepository.saveAndFlush(proveedor);

        int databaseSizeBeforeUpdate = proveedorRepository.findAll().size();

        // Update the proveedor using partial update
        Proveedor partialUpdatedProveedor = new Proveedor();
        partialUpdatedProveedor.setId(proveedor.getId());

        partialUpdatedProveedor.nombre(UPDATED_NOMBRE).primerTelefono(UPDATED_PRIMER_TELEFONO).segundoTelefono(UPDATED_SEGUNDO_TELEFONO);

        restProveedorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProveedor.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProveedor))
            )
            .andExpect(status().isOk());

        // Validate the Proveedor in the database
        List<Proveedor> proveedorList = proveedorRepository.findAll();
        assertThat(proveedorList).hasSize(databaseSizeBeforeUpdate);
        Proveedor testProveedor = proveedorList.get(proveedorList.size() - 1);
        assertThat(testProveedor.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testProveedor.getPrimerTelefono()).isEqualTo(UPDATED_PRIMER_TELEFONO);
        assertThat(testProveedor.getSegundoTelefono()).isEqualTo(UPDATED_SEGUNDO_TELEFONO);
    }

    @Test
    @Transactional
    void patchNonExistingProveedor() throws Exception {
        int databaseSizeBeforeUpdate = proveedorRepository.findAll().size();
        proveedor.setId(count.incrementAndGet());

        // Create the Proveedor
        ProveedorDTO proveedorDTO = proveedorMapper.toDto(proveedor);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProveedorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, proveedorDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(proveedorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Proveedor in the database
        List<Proveedor> proveedorList = proveedorRepository.findAll();
        assertThat(proveedorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProveedor() throws Exception {
        int databaseSizeBeforeUpdate = proveedorRepository.findAll().size();
        proveedor.setId(count.incrementAndGet());

        // Create the Proveedor
        ProveedorDTO proveedorDTO = proveedorMapper.toDto(proveedor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProveedorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(proveedorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Proveedor in the database
        List<Proveedor> proveedorList = proveedorRepository.findAll();
        assertThat(proveedorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProveedor() throws Exception {
        int databaseSizeBeforeUpdate = proveedorRepository.findAll().size();
        proveedor.setId(count.incrementAndGet());

        // Create the Proveedor
        ProveedorDTO proveedorDTO = proveedorMapper.toDto(proveedor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProveedorMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(proveedorDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Proveedor in the database
        List<Proveedor> proveedorList = proveedorRepository.findAll();
        assertThat(proveedorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProveedor() throws Exception {
        // Initialize the database
        proveedorRepository.saveAndFlush(proveedor);

        int databaseSizeBeforeDelete = proveedorRepository.findAll().size();

        // Delete the proveedor
        restProveedorMockMvc
            .perform(delete(ENTITY_API_URL_ID, proveedor.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Proveedor> proveedorList = proveedorRepository.findAll();
        assertThat(proveedorList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
