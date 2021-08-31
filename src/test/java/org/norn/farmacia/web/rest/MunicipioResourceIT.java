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
import org.norn.farmacia.domain.Departamento;
import org.norn.farmacia.domain.Municipio;
import org.norn.farmacia.domain.Proveedor;
import org.norn.farmacia.repository.MunicipioRepository;
import org.norn.farmacia.service.criteria.MunicipioCriteria;
import org.norn.farmacia.service.dto.MunicipioDTO;
import org.norn.farmacia.service.mapper.MunicipioMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link MunicipioResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MunicipioResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/municipios";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MunicipioRepository municipioRepository;

    @Autowired
    private MunicipioMapper municipioMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMunicipioMockMvc;

    private Municipio municipio;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Municipio createEntity(EntityManager em) {
        Municipio municipio = new Municipio().nombre(DEFAULT_NOMBRE);
        return municipio;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Municipio createUpdatedEntity(EntityManager em) {
        Municipio municipio = new Municipio().nombre(UPDATED_NOMBRE);
        return municipio;
    }

    @BeforeEach
    public void initTest() {
        municipio = createEntity(em);
    }

    @Test
    @Transactional
    void createMunicipio() throws Exception {
        int databaseSizeBeforeCreate = municipioRepository.findAll().size();
        // Create the Municipio
        MunicipioDTO municipioDTO = municipioMapper.toDto(municipio);
        restMunicipioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(municipioDTO)))
            .andExpect(status().isCreated());

        // Validate the Municipio in the database
        List<Municipio> municipioList = municipioRepository.findAll();
        assertThat(municipioList).hasSize(databaseSizeBeforeCreate + 1);
        Municipio testMunicipio = municipioList.get(municipioList.size() - 1);
        assertThat(testMunicipio.getNombre()).isEqualTo(DEFAULT_NOMBRE);
    }

    @Test
    @Transactional
    void createMunicipioWithExistingId() throws Exception {
        // Create the Municipio with an existing ID
        municipio.setId(1L);
        MunicipioDTO municipioDTO = municipioMapper.toDto(municipio);

        int databaseSizeBeforeCreate = municipioRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMunicipioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(municipioDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Municipio in the database
        List<Municipio> municipioList = municipioRepository.findAll();
        assertThat(municipioList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = municipioRepository.findAll().size();
        // set the field null
        municipio.setNombre(null);

        // Create the Municipio, which fails.
        MunicipioDTO municipioDTO = municipioMapper.toDto(municipio);

        restMunicipioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(municipioDTO)))
            .andExpect(status().isBadRequest());

        List<Municipio> municipioList = municipioRepository.findAll();
        assertThat(municipioList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMunicipios() throws Exception {
        // Initialize the database
        municipioRepository.saveAndFlush(municipio);

        // Get all the municipioList
        restMunicipioMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(municipio.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)));
    }

    @Test
    @Transactional
    void getMunicipio() throws Exception {
        // Initialize the database
        municipioRepository.saveAndFlush(municipio);

        // Get the municipio
        restMunicipioMockMvc
            .perform(get(ENTITY_API_URL_ID, municipio.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(municipio.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE));
    }

    @Test
    @Transactional
    void getMunicipiosByIdFiltering() throws Exception {
        // Initialize the database
        municipioRepository.saveAndFlush(municipio);

        Long id = municipio.getId();

        defaultMunicipioShouldBeFound("id.equals=" + id);
        defaultMunicipioShouldNotBeFound("id.notEquals=" + id);

        defaultMunicipioShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultMunicipioShouldNotBeFound("id.greaterThan=" + id);

        defaultMunicipioShouldBeFound("id.lessThanOrEqual=" + id);
        defaultMunicipioShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllMunicipiosByNombreIsEqualToSomething() throws Exception {
        // Initialize the database
        municipioRepository.saveAndFlush(municipio);

        // Get all the municipioList where nombre equals to DEFAULT_NOMBRE
        defaultMunicipioShouldBeFound("nombre.equals=" + DEFAULT_NOMBRE);

        // Get all the municipioList where nombre equals to UPDATED_NOMBRE
        defaultMunicipioShouldNotBeFound("nombre.equals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllMunicipiosByNombreIsNotEqualToSomething() throws Exception {
        // Initialize the database
        municipioRepository.saveAndFlush(municipio);

        // Get all the municipioList where nombre not equals to DEFAULT_NOMBRE
        defaultMunicipioShouldNotBeFound("nombre.notEquals=" + DEFAULT_NOMBRE);

        // Get all the municipioList where nombre not equals to UPDATED_NOMBRE
        defaultMunicipioShouldBeFound("nombre.notEquals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllMunicipiosByNombreIsInShouldWork() throws Exception {
        // Initialize the database
        municipioRepository.saveAndFlush(municipio);

        // Get all the municipioList where nombre in DEFAULT_NOMBRE or UPDATED_NOMBRE
        defaultMunicipioShouldBeFound("nombre.in=" + DEFAULT_NOMBRE + "," + UPDATED_NOMBRE);

        // Get all the municipioList where nombre equals to UPDATED_NOMBRE
        defaultMunicipioShouldNotBeFound("nombre.in=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllMunicipiosByNombreIsNullOrNotNull() throws Exception {
        // Initialize the database
        municipioRepository.saveAndFlush(municipio);

        // Get all the municipioList where nombre is not null
        defaultMunicipioShouldBeFound("nombre.specified=true");

        // Get all the municipioList where nombre is null
        defaultMunicipioShouldNotBeFound("nombre.specified=false");
    }

    @Test
    @Transactional
    void getAllMunicipiosByNombreContainsSomething() throws Exception {
        // Initialize the database
        municipioRepository.saveAndFlush(municipio);

        // Get all the municipioList where nombre contains DEFAULT_NOMBRE
        defaultMunicipioShouldBeFound("nombre.contains=" + DEFAULT_NOMBRE);

        // Get all the municipioList where nombre contains UPDATED_NOMBRE
        defaultMunicipioShouldNotBeFound("nombre.contains=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllMunicipiosByNombreNotContainsSomething() throws Exception {
        // Initialize the database
        municipioRepository.saveAndFlush(municipio);

        // Get all the municipioList where nombre does not contain DEFAULT_NOMBRE
        defaultMunicipioShouldNotBeFound("nombre.doesNotContain=" + DEFAULT_NOMBRE);

        // Get all the municipioList where nombre does not contain UPDATED_NOMBRE
        defaultMunicipioShouldBeFound("nombre.doesNotContain=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllMunicipiosByDepartamentoIsEqualToSomething() throws Exception {
        // Initialize the database
        municipioRepository.saveAndFlush(municipio);
        Departamento departamento = DepartamentoResourceIT.createEntity(em);
        em.persist(departamento);
        em.flush();
        municipio.setDepartamento(departamento);
        municipioRepository.saveAndFlush(municipio);
        Long departamentoId = departamento.getId();

        // Get all the municipioList where departamento equals to departamentoId
        defaultMunicipioShouldBeFound("departamentoId.equals=" + departamentoId);

        // Get all the municipioList where departamento equals to (departamentoId + 1)
        defaultMunicipioShouldNotBeFound("departamentoId.equals=" + (departamentoId + 1));
    }

    @Test
    @Transactional
    void getAllMunicipiosByClienteIsEqualToSomething() throws Exception {
        // Initialize the database
        municipioRepository.saveAndFlush(municipio);
        Cliente cliente = ClienteResourceIT.createEntity(em);
        em.persist(cliente);
        em.flush();
        municipio.addCliente(cliente);
        municipioRepository.saveAndFlush(municipio);
        Long clienteId = cliente.getId();

        // Get all the municipioList where cliente equals to clienteId
        defaultMunicipioShouldBeFound("clienteId.equals=" + clienteId);

        // Get all the municipioList where cliente equals to (clienteId + 1)
        defaultMunicipioShouldNotBeFound("clienteId.equals=" + (clienteId + 1));
    }

    @Test
    @Transactional
    void getAllMunicipiosByProveedorIsEqualToSomething() throws Exception {
        // Initialize the database
        municipioRepository.saveAndFlush(municipio);
        Proveedor proveedor = ProveedorResourceIT.createEntity(em);
        em.persist(proveedor);
        em.flush();
        municipio.addProveedor(proveedor);
        municipioRepository.saveAndFlush(municipio);
        Long proveedorId = proveedor.getId();

        // Get all the municipioList where proveedor equals to proveedorId
        defaultMunicipioShouldBeFound("proveedorId.equals=" + proveedorId);

        // Get all the municipioList where proveedor equals to (proveedorId + 1)
        defaultMunicipioShouldNotBeFound("proveedorId.equals=" + (proveedorId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMunicipioShouldBeFound(String filter) throws Exception {
        restMunicipioMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(municipio.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)));

        // Check, that the count call also returns 1
        restMunicipioMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMunicipioShouldNotBeFound(String filter) throws Exception {
        restMunicipioMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMunicipioMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingMunicipio() throws Exception {
        // Get the municipio
        restMunicipioMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewMunicipio() throws Exception {
        // Initialize the database
        municipioRepository.saveAndFlush(municipio);

        int databaseSizeBeforeUpdate = municipioRepository.findAll().size();

        // Update the municipio
        Municipio updatedMunicipio = municipioRepository.findById(municipio.getId()).get();
        // Disconnect from session so that the updates on updatedMunicipio are not directly saved in db
        em.detach(updatedMunicipio);
        updatedMunicipio.nombre(UPDATED_NOMBRE);
        MunicipioDTO municipioDTO = municipioMapper.toDto(updatedMunicipio);

        restMunicipioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, municipioDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(municipioDTO))
            )
            .andExpect(status().isOk());

        // Validate the Municipio in the database
        List<Municipio> municipioList = municipioRepository.findAll();
        assertThat(municipioList).hasSize(databaseSizeBeforeUpdate);
        Municipio testMunicipio = municipioList.get(municipioList.size() - 1);
        assertThat(testMunicipio.getNombre()).isEqualTo(UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void putNonExistingMunicipio() throws Exception {
        int databaseSizeBeforeUpdate = municipioRepository.findAll().size();
        municipio.setId(count.incrementAndGet());

        // Create the Municipio
        MunicipioDTO municipioDTO = municipioMapper.toDto(municipio);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMunicipioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, municipioDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(municipioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Municipio in the database
        List<Municipio> municipioList = municipioRepository.findAll();
        assertThat(municipioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMunicipio() throws Exception {
        int databaseSizeBeforeUpdate = municipioRepository.findAll().size();
        municipio.setId(count.incrementAndGet());

        // Create the Municipio
        MunicipioDTO municipioDTO = municipioMapper.toDto(municipio);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMunicipioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(municipioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Municipio in the database
        List<Municipio> municipioList = municipioRepository.findAll();
        assertThat(municipioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMunicipio() throws Exception {
        int databaseSizeBeforeUpdate = municipioRepository.findAll().size();
        municipio.setId(count.incrementAndGet());

        // Create the Municipio
        MunicipioDTO municipioDTO = municipioMapper.toDto(municipio);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMunicipioMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(municipioDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Municipio in the database
        List<Municipio> municipioList = municipioRepository.findAll();
        assertThat(municipioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMunicipioWithPatch() throws Exception {
        // Initialize the database
        municipioRepository.saveAndFlush(municipio);

        int databaseSizeBeforeUpdate = municipioRepository.findAll().size();

        // Update the municipio using partial update
        Municipio partialUpdatedMunicipio = new Municipio();
        partialUpdatedMunicipio.setId(municipio.getId());

        partialUpdatedMunicipio.nombre(UPDATED_NOMBRE);

        restMunicipioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMunicipio.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMunicipio))
            )
            .andExpect(status().isOk());

        // Validate the Municipio in the database
        List<Municipio> municipioList = municipioRepository.findAll();
        assertThat(municipioList).hasSize(databaseSizeBeforeUpdate);
        Municipio testMunicipio = municipioList.get(municipioList.size() - 1);
        assertThat(testMunicipio.getNombre()).isEqualTo(UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void fullUpdateMunicipioWithPatch() throws Exception {
        // Initialize the database
        municipioRepository.saveAndFlush(municipio);

        int databaseSizeBeforeUpdate = municipioRepository.findAll().size();

        // Update the municipio using partial update
        Municipio partialUpdatedMunicipio = new Municipio();
        partialUpdatedMunicipio.setId(municipio.getId());

        partialUpdatedMunicipio.nombre(UPDATED_NOMBRE);

        restMunicipioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMunicipio.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMunicipio))
            )
            .andExpect(status().isOk());

        // Validate the Municipio in the database
        List<Municipio> municipioList = municipioRepository.findAll();
        assertThat(municipioList).hasSize(databaseSizeBeforeUpdate);
        Municipio testMunicipio = municipioList.get(municipioList.size() - 1);
        assertThat(testMunicipio.getNombre()).isEqualTo(UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void patchNonExistingMunicipio() throws Exception {
        int databaseSizeBeforeUpdate = municipioRepository.findAll().size();
        municipio.setId(count.incrementAndGet());

        // Create the Municipio
        MunicipioDTO municipioDTO = municipioMapper.toDto(municipio);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMunicipioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, municipioDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(municipioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Municipio in the database
        List<Municipio> municipioList = municipioRepository.findAll();
        assertThat(municipioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMunicipio() throws Exception {
        int databaseSizeBeforeUpdate = municipioRepository.findAll().size();
        municipio.setId(count.incrementAndGet());

        // Create the Municipio
        MunicipioDTO municipioDTO = municipioMapper.toDto(municipio);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMunicipioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(municipioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Municipio in the database
        List<Municipio> municipioList = municipioRepository.findAll();
        assertThat(municipioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMunicipio() throws Exception {
        int databaseSizeBeforeUpdate = municipioRepository.findAll().size();
        municipio.setId(count.incrementAndGet());

        // Create the Municipio
        MunicipioDTO municipioDTO = municipioMapper.toDto(municipio);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMunicipioMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(municipioDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Municipio in the database
        List<Municipio> municipioList = municipioRepository.findAll();
        assertThat(municipioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMunicipio() throws Exception {
        // Initialize the database
        municipioRepository.saveAndFlush(municipio);

        int databaseSizeBeforeDelete = municipioRepository.findAll().size();

        // Delete the municipio
        restMunicipioMockMvc
            .perform(delete(ENTITY_API_URL_ID, municipio.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Municipio> municipioList = municipioRepository.findAll();
        assertThat(municipioList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
