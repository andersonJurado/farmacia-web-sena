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
import org.norn.farmacia.repository.GeneroRepository;
import org.norn.farmacia.service.criteria.GeneroCriteria;
import org.norn.farmacia.service.dto.GeneroDTO;
import org.norn.farmacia.service.mapper.GeneroMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link GeneroResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class GeneroResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/generos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private GeneroRepository generoRepository;

    @Autowired
    private GeneroMapper generoMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restGeneroMockMvc;

    private Genero genero;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Genero createEntity(EntityManager em) {
        Genero genero = new Genero().nombre(DEFAULT_NOMBRE);
        return genero;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Genero createUpdatedEntity(EntityManager em) {
        Genero genero = new Genero().nombre(UPDATED_NOMBRE);
        return genero;
    }

    @BeforeEach
    public void initTest() {
        genero = createEntity(em);
    }

    @Test
    @Transactional
    void createGenero() throws Exception {
        int databaseSizeBeforeCreate = generoRepository.findAll().size();
        // Create the Genero
        GeneroDTO generoDTO = generoMapper.toDto(genero);
        restGeneroMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(generoDTO)))
            .andExpect(status().isCreated());

        // Validate the Genero in the database
        List<Genero> generoList = generoRepository.findAll();
        assertThat(generoList).hasSize(databaseSizeBeforeCreate + 1);
        Genero testGenero = generoList.get(generoList.size() - 1);
        assertThat(testGenero.getNombre()).isEqualTo(DEFAULT_NOMBRE);
    }

    @Test
    @Transactional
    void createGeneroWithExistingId() throws Exception {
        // Create the Genero with an existing ID
        genero.setId(1L);
        GeneroDTO generoDTO = generoMapper.toDto(genero);

        int databaseSizeBeforeCreate = generoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restGeneroMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(generoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Genero in the database
        List<Genero> generoList = generoRepository.findAll();
        assertThat(generoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = generoRepository.findAll().size();
        // set the field null
        genero.setNombre(null);

        // Create the Genero, which fails.
        GeneroDTO generoDTO = generoMapper.toDto(genero);

        restGeneroMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(generoDTO)))
            .andExpect(status().isBadRequest());

        List<Genero> generoList = generoRepository.findAll();
        assertThat(generoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllGeneros() throws Exception {
        // Initialize the database
        generoRepository.saveAndFlush(genero);

        // Get all the generoList
        restGeneroMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(genero.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)));
    }

    @Test
    @Transactional
    void getGenero() throws Exception {
        // Initialize the database
        generoRepository.saveAndFlush(genero);

        // Get the genero
        restGeneroMockMvc
            .perform(get(ENTITY_API_URL_ID, genero.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(genero.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE));
    }

    @Test
    @Transactional
    void getGenerosByIdFiltering() throws Exception {
        // Initialize the database
        generoRepository.saveAndFlush(genero);

        Long id = genero.getId();

        defaultGeneroShouldBeFound("id.equals=" + id);
        defaultGeneroShouldNotBeFound("id.notEquals=" + id);

        defaultGeneroShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultGeneroShouldNotBeFound("id.greaterThan=" + id);

        defaultGeneroShouldBeFound("id.lessThanOrEqual=" + id);
        defaultGeneroShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllGenerosByNombreIsEqualToSomething() throws Exception {
        // Initialize the database
        generoRepository.saveAndFlush(genero);

        // Get all the generoList where nombre equals to DEFAULT_NOMBRE
        defaultGeneroShouldBeFound("nombre.equals=" + DEFAULT_NOMBRE);

        // Get all the generoList where nombre equals to UPDATED_NOMBRE
        defaultGeneroShouldNotBeFound("nombre.equals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllGenerosByNombreIsNotEqualToSomething() throws Exception {
        // Initialize the database
        generoRepository.saveAndFlush(genero);

        // Get all the generoList where nombre not equals to DEFAULT_NOMBRE
        defaultGeneroShouldNotBeFound("nombre.notEquals=" + DEFAULT_NOMBRE);

        // Get all the generoList where nombre not equals to UPDATED_NOMBRE
        defaultGeneroShouldBeFound("nombre.notEquals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllGenerosByNombreIsInShouldWork() throws Exception {
        // Initialize the database
        generoRepository.saveAndFlush(genero);

        // Get all the generoList where nombre in DEFAULT_NOMBRE or UPDATED_NOMBRE
        defaultGeneroShouldBeFound("nombre.in=" + DEFAULT_NOMBRE + "," + UPDATED_NOMBRE);

        // Get all the generoList where nombre equals to UPDATED_NOMBRE
        defaultGeneroShouldNotBeFound("nombre.in=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllGenerosByNombreIsNullOrNotNull() throws Exception {
        // Initialize the database
        generoRepository.saveAndFlush(genero);

        // Get all the generoList where nombre is not null
        defaultGeneroShouldBeFound("nombre.specified=true");

        // Get all the generoList where nombre is null
        defaultGeneroShouldNotBeFound("nombre.specified=false");
    }

    @Test
    @Transactional
    void getAllGenerosByNombreContainsSomething() throws Exception {
        // Initialize the database
        generoRepository.saveAndFlush(genero);

        // Get all the generoList where nombre contains DEFAULT_NOMBRE
        defaultGeneroShouldBeFound("nombre.contains=" + DEFAULT_NOMBRE);

        // Get all the generoList where nombre contains UPDATED_NOMBRE
        defaultGeneroShouldNotBeFound("nombre.contains=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllGenerosByNombreNotContainsSomething() throws Exception {
        // Initialize the database
        generoRepository.saveAndFlush(genero);

        // Get all the generoList where nombre does not contain DEFAULT_NOMBRE
        defaultGeneroShouldNotBeFound("nombre.doesNotContain=" + DEFAULT_NOMBRE);

        // Get all the generoList where nombre does not contain UPDATED_NOMBRE
        defaultGeneroShouldBeFound("nombre.doesNotContain=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllGenerosByClienteIsEqualToSomething() throws Exception {
        // Initialize the database
        generoRepository.saveAndFlush(genero);
        Cliente cliente = ClienteResourceIT.createEntity(em);
        em.persist(cliente);
        em.flush();
        genero.addCliente(cliente);
        generoRepository.saveAndFlush(genero);
        Long clienteId = cliente.getId();

        // Get all the generoList where cliente equals to clienteId
        defaultGeneroShouldBeFound("clienteId.equals=" + clienteId);

        // Get all the generoList where cliente equals to (clienteId + 1)
        defaultGeneroShouldNotBeFound("clienteId.equals=" + (clienteId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultGeneroShouldBeFound(String filter) throws Exception {
        restGeneroMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(genero.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)));

        // Check, that the count call also returns 1
        restGeneroMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultGeneroShouldNotBeFound(String filter) throws Exception {
        restGeneroMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restGeneroMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingGenero() throws Exception {
        // Get the genero
        restGeneroMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewGenero() throws Exception {
        // Initialize the database
        generoRepository.saveAndFlush(genero);

        int databaseSizeBeforeUpdate = generoRepository.findAll().size();

        // Update the genero
        Genero updatedGenero = generoRepository.findById(genero.getId()).get();
        // Disconnect from session so that the updates on updatedGenero are not directly saved in db
        em.detach(updatedGenero);
        updatedGenero.nombre(UPDATED_NOMBRE);
        GeneroDTO generoDTO = generoMapper.toDto(updatedGenero);

        restGeneroMockMvc
            .perform(
                put(ENTITY_API_URL_ID, generoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(generoDTO))
            )
            .andExpect(status().isOk());

        // Validate the Genero in the database
        List<Genero> generoList = generoRepository.findAll();
        assertThat(generoList).hasSize(databaseSizeBeforeUpdate);
        Genero testGenero = generoList.get(generoList.size() - 1);
        assertThat(testGenero.getNombre()).isEqualTo(UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void putNonExistingGenero() throws Exception {
        int databaseSizeBeforeUpdate = generoRepository.findAll().size();
        genero.setId(count.incrementAndGet());

        // Create the Genero
        GeneroDTO generoDTO = generoMapper.toDto(genero);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGeneroMockMvc
            .perform(
                put(ENTITY_API_URL_ID, generoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(generoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Genero in the database
        List<Genero> generoList = generoRepository.findAll();
        assertThat(generoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchGenero() throws Exception {
        int databaseSizeBeforeUpdate = generoRepository.findAll().size();
        genero.setId(count.incrementAndGet());

        // Create the Genero
        GeneroDTO generoDTO = generoMapper.toDto(genero);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGeneroMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(generoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Genero in the database
        List<Genero> generoList = generoRepository.findAll();
        assertThat(generoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamGenero() throws Exception {
        int databaseSizeBeforeUpdate = generoRepository.findAll().size();
        genero.setId(count.incrementAndGet());

        // Create the Genero
        GeneroDTO generoDTO = generoMapper.toDto(genero);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGeneroMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(generoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Genero in the database
        List<Genero> generoList = generoRepository.findAll();
        assertThat(generoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateGeneroWithPatch() throws Exception {
        // Initialize the database
        generoRepository.saveAndFlush(genero);

        int databaseSizeBeforeUpdate = generoRepository.findAll().size();

        // Update the genero using partial update
        Genero partialUpdatedGenero = new Genero();
        partialUpdatedGenero.setId(genero.getId());

        restGeneroMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGenero.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGenero))
            )
            .andExpect(status().isOk());

        // Validate the Genero in the database
        List<Genero> generoList = generoRepository.findAll();
        assertThat(generoList).hasSize(databaseSizeBeforeUpdate);
        Genero testGenero = generoList.get(generoList.size() - 1);
        assertThat(testGenero.getNombre()).isEqualTo(DEFAULT_NOMBRE);
    }

    @Test
    @Transactional
    void fullUpdateGeneroWithPatch() throws Exception {
        // Initialize the database
        generoRepository.saveAndFlush(genero);

        int databaseSizeBeforeUpdate = generoRepository.findAll().size();

        // Update the genero using partial update
        Genero partialUpdatedGenero = new Genero();
        partialUpdatedGenero.setId(genero.getId());

        partialUpdatedGenero.nombre(UPDATED_NOMBRE);

        restGeneroMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGenero.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGenero))
            )
            .andExpect(status().isOk());

        // Validate the Genero in the database
        List<Genero> generoList = generoRepository.findAll();
        assertThat(generoList).hasSize(databaseSizeBeforeUpdate);
        Genero testGenero = generoList.get(generoList.size() - 1);
        assertThat(testGenero.getNombre()).isEqualTo(UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void patchNonExistingGenero() throws Exception {
        int databaseSizeBeforeUpdate = generoRepository.findAll().size();
        genero.setId(count.incrementAndGet());

        // Create the Genero
        GeneroDTO generoDTO = generoMapper.toDto(genero);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGeneroMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, generoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(generoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Genero in the database
        List<Genero> generoList = generoRepository.findAll();
        assertThat(generoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchGenero() throws Exception {
        int databaseSizeBeforeUpdate = generoRepository.findAll().size();
        genero.setId(count.incrementAndGet());

        // Create the Genero
        GeneroDTO generoDTO = generoMapper.toDto(genero);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGeneroMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(generoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Genero in the database
        List<Genero> generoList = generoRepository.findAll();
        assertThat(generoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamGenero() throws Exception {
        int databaseSizeBeforeUpdate = generoRepository.findAll().size();
        genero.setId(count.incrementAndGet());

        // Create the Genero
        GeneroDTO generoDTO = generoMapper.toDto(genero);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGeneroMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(generoDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Genero in the database
        List<Genero> generoList = generoRepository.findAll();
        assertThat(generoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteGenero() throws Exception {
        // Initialize the database
        generoRepository.saveAndFlush(genero);

        int databaseSizeBeforeDelete = generoRepository.findAll().size();

        // Delete the genero
        restGeneroMockMvc
            .perform(delete(ENTITY_API_URL_ID, genero.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Genero> generoList = generoRepository.findAll();
        assertThat(generoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
