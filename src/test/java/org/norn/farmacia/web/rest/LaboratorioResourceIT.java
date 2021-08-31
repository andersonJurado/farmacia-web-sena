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
import org.norn.farmacia.domain.Laboratorio;
import org.norn.farmacia.domain.Producto;
import org.norn.farmacia.repository.LaboratorioRepository;
import org.norn.farmacia.service.criteria.LaboratorioCriteria;
import org.norn.farmacia.service.dto.LaboratorioDTO;
import org.norn.farmacia.service.mapper.LaboratorioMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link LaboratorioResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LaboratorioResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/laboratorios";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LaboratorioRepository laboratorioRepository;

    @Autowired
    private LaboratorioMapper laboratorioMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLaboratorioMockMvc;

    private Laboratorio laboratorio;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Laboratorio createEntity(EntityManager em) {
        Laboratorio laboratorio = new Laboratorio().nombre(DEFAULT_NOMBRE);
        return laboratorio;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Laboratorio createUpdatedEntity(EntityManager em) {
        Laboratorio laboratorio = new Laboratorio().nombre(UPDATED_NOMBRE);
        return laboratorio;
    }

    @BeforeEach
    public void initTest() {
        laboratorio = createEntity(em);
    }

    @Test
    @Transactional
    void createLaboratorio() throws Exception {
        int databaseSizeBeforeCreate = laboratorioRepository.findAll().size();
        // Create the Laboratorio
        LaboratorioDTO laboratorioDTO = laboratorioMapper.toDto(laboratorio);
        restLaboratorioMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(laboratorioDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Laboratorio in the database
        List<Laboratorio> laboratorioList = laboratorioRepository.findAll();
        assertThat(laboratorioList).hasSize(databaseSizeBeforeCreate + 1);
        Laboratorio testLaboratorio = laboratorioList.get(laboratorioList.size() - 1);
        assertThat(testLaboratorio.getNombre()).isEqualTo(DEFAULT_NOMBRE);
    }

    @Test
    @Transactional
    void createLaboratorioWithExistingId() throws Exception {
        // Create the Laboratorio with an existing ID
        laboratorio.setId(1L);
        LaboratorioDTO laboratorioDTO = laboratorioMapper.toDto(laboratorio);

        int databaseSizeBeforeCreate = laboratorioRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLaboratorioMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(laboratorioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Laboratorio in the database
        List<Laboratorio> laboratorioList = laboratorioRepository.findAll();
        assertThat(laboratorioList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = laboratorioRepository.findAll().size();
        // set the field null
        laboratorio.setNombre(null);

        // Create the Laboratorio, which fails.
        LaboratorioDTO laboratorioDTO = laboratorioMapper.toDto(laboratorio);

        restLaboratorioMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(laboratorioDTO))
            )
            .andExpect(status().isBadRequest());

        List<Laboratorio> laboratorioList = laboratorioRepository.findAll();
        assertThat(laboratorioList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllLaboratorios() throws Exception {
        // Initialize the database
        laboratorioRepository.saveAndFlush(laboratorio);

        // Get all the laboratorioList
        restLaboratorioMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(laboratorio.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)));
    }

    @Test
    @Transactional
    void getLaboratorio() throws Exception {
        // Initialize the database
        laboratorioRepository.saveAndFlush(laboratorio);

        // Get the laboratorio
        restLaboratorioMockMvc
            .perform(get(ENTITY_API_URL_ID, laboratorio.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(laboratorio.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE));
    }

    @Test
    @Transactional
    void getLaboratoriosByIdFiltering() throws Exception {
        // Initialize the database
        laboratorioRepository.saveAndFlush(laboratorio);

        Long id = laboratorio.getId();

        defaultLaboratorioShouldBeFound("id.equals=" + id);
        defaultLaboratorioShouldNotBeFound("id.notEquals=" + id);

        defaultLaboratorioShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultLaboratorioShouldNotBeFound("id.greaterThan=" + id);

        defaultLaboratorioShouldBeFound("id.lessThanOrEqual=" + id);
        defaultLaboratorioShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllLaboratoriosByNombreIsEqualToSomething() throws Exception {
        // Initialize the database
        laboratorioRepository.saveAndFlush(laboratorio);

        // Get all the laboratorioList where nombre equals to DEFAULT_NOMBRE
        defaultLaboratorioShouldBeFound("nombre.equals=" + DEFAULT_NOMBRE);

        // Get all the laboratorioList where nombre equals to UPDATED_NOMBRE
        defaultLaboratorioShouldNotBeFound("nombre.equals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllLaboratoriosByNombreIsNotEqualToSomething() throws Exception {
        // Initialize the database
        laboratorioRepository.saveAndFlush(laboratorio);

        // Get all the laboratorioList where nombre not equals to DEFAULT_NOMBRE
        defaultLaboratorioShouldNotBeFound("nombre.notEquals=" + DEFAULT_NOMBRE);

        // Get all the laboratorioList where nombre not equals to UPDATED_NOMBRE
        defaultLaboratorioShouldBeFound("nombre.notEquals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllLaboratoriosByNombreIsInShouldWork() throws Exception {
        // Initialize the database
        laboratorioRepository.saveAndFlush(laboratorio);

        // Get all the laboratorioList where nombre in DEFAULT_NOMBRE or UPDATED_NOMBRE
        defaultLaboratorioShouldBeFound("nombre.in=" + DEFAULT_NOMBRE + "," + UPDATED_NOMBRE);

        // Get all the laboratorioList where nombre equals to UPDATED_NOMBRE
        defaultLaboratorioShouldNotBeFound("nombre.in=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllLaboratoriosByNombreIsNullOrNotNull() throws Exception {
        // Initialize the database
        laboratorioRepository.saveAndFlush(laboratorio);

        // Get all the laboratorioList where nombre is not null
        defaultLaboratorioShouldBeFound("nombre.specified=true");

        // Get all the laboratorioList where nombre is null
        defaultLaboratorioShouldNotBeFound("nombre.specified=false");
    }

    @Test
    @Transactional
    void getAllLaboratoriosByNombreContainsSomething() throws Exception {
        // Initialize the database
        laboratorioRepository.saveAndFlush(laboratorio);

        // Get all the laboratorioList where nombre contains DEFAULT_NOMBRE
        defaultLaboratorioShouldBeFound("nombre.contains=" + DEFAULT_NOMBRE);

        // Get all the laboratorioList where nombre contains UPDATED_NOMBRE
        defaultLaboratorioShouldNotBeFound("nombre.contains=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllLaboratoriosByNombreNotContainsSomething() throws Exception {
        // Initialize the database
        laboratorioRepository.saveAndFlush(laboratorio);

        // Get all the laboratorioList where nombre does not contain DEFAULT_NOMBRE
        defaultLaboratorioShouldNotBeFound("nombre.doesNotContain=" + DEFAULT_NOMBRE);

        // Get all the laboratorioList where nombre does not contain UPDATED_NOMBRE
        defaultLaboratorioShouldBeFound("nombre.doesNotContain=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllLaboratoriosByProductoIsEqualToSomething() throws Exception {
        // Initialize the database
        laboratorioRepository.saveAndFlush(laboratorio);
        Producto producto = ProductoResourceIT.createEntity(em);
        em.persist(producto);
        em.flush();
        laboratorio.addProducto(producto);
        laboratorioRepository.saveAndFlush(laboratorio);
        Long productoId = producto.getId();

        // Get all the laboratorioList where producto equals to productoId
        defaultLaboratorioShouldBeFound("productoId.equals=" + productoId);

        // Get all the laboratorioList where producto equals to (productoId + 1)
        defaultLaboratorioShouldNotBeFound("productoId.equals=" + (productoId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultLaboratorioShouldBeFound(String filter) throws Exception {
        restLaboratorioMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(laboratorio.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)));

        // Check, that the count call also returns 1
        restLaboratorioMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultLaboratorioShouldNotBeFound(String filter) throws Exception {
        restLaboratorioMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restLaboratorioMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingLaboratorio() throws Exception {
        // Get the laboratorio
        restLaboratorioMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewLaboratorio() throws Exception {
        // Initialize the database
        laboratorioRepository.saveAndFlush(laboratorio);

        int databaseSizeBeforeUpdate = laboratorioRepository.findAll().size();

        // Update the laboratorio
        Laboratorio updatedLaboratorio = laboratorioRepository.findById(laboratorio.getId()).get();
        // Disconnect from session so that the updates on updatedLaboratorio are not directly saved in db
        em.detach(updatedLaboratorio);
        updatedLaboratorio.nombre(UPDATED_NOMBRE);
        LaboratorioDTO laboratorioDTO = laboratorioMapper.toDto(updatedLaboratorio);

        restLaboratorioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, laboratorioDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(laboratorioDTO))
            )
            .andExpect(status().isOk());

        // Validate the Laboratorio in the database
        List<Laboratorio> laboratorioList = laboratorioRepository.findAll();
        assertThat(laboratorioList).hasSize(databaseSizeBeforeUpdate);
        Laboratorio testLaboratorio = laboratorioList.get(laboratorioList.size() - 1);
        assertThat(testLaboratorio.getNombre()).isEqualTo(UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void putNonExistingLaboratorio() throws Exception {
        int databaseSizeBeforeUpdate = laboratorioRepository.findAll().size();
        laboratorio.setId(count.incrementAndGet());

        // Create the Laboratorio
        LaboratorioDTO laboratorioDTO = laboratorioMapper.toDto(laboratorio);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLaboratorioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, laboratorioDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(laboratorioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Laboratorio in the database
        List<Laboratorio> laboratorioList = laboratorioRepository.findAll();
        assertThat(laboratorioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLaboratorio() throws Exception {
        int databaseSizeBeforeUpdate = laboratorioRepository.findAll().size();
        laboratorio.setId(count.incrementAndGet());

        // Create the Laboratorio
        LaboratorioDTO laboratorioDTO = laboratorioMapper.toDto(laboratorio);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLaboratorioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(laboratorioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Laboratorio in the database
        List<Laboratorio> laboratorioList = laboratorioRepository.findAll();
        assertThat(laboratorioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLaboratorio() throws Exception {
        int databaseSizeBeforeUpdate = laboratorioRepository.findAll().size();
        laboratorio.setId(count.incrementAndGet());

        // Create the Laboratorio
        LaboratorioDTO laboratorioDTO = laboratorioMapper.toDto(laboratorio);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLaboratorioMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(laboratorioDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Laboratorio in the database
        List<Laboratorio> laboratorioList = laboratorioRepository.findAll();
        assertThat(laboratorioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLaboratorioWithPatch() throws Exception {
        // Initialize the database
        laboratorioRepository.saveAndFlush(laboratorio);

        int databaseSizeBeforeUpdate = laboratorioRepository.findAll().size();

        // Update the laboratorio using partial update
        Laboratorio partialUpdatedLaboratorio = new Laboratorio();
        partialUpdatedLaboratorio.setId(laboratorio.getId());

        partialUpdatedLaboratorio.nombre(UPDATED_NOMBRE);

        restLaboratorioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLaboratorio.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLaboratorio))
            )
            .andExpect(status().isOk());

        // Validate the Laboratorio in the database
        List<Laboratorio> laboratorioList = laboratorioRepository.findAll();
        assertThat(laboratorioList).hasSize(databaseSizeBeforeUpdate);
        Laboratorio testLaboratorio = laboratorioList.get(laboratorioList.size() - 1);
        assertThat(testLaboratorio.getNombre()).isEqualTo(UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void fullUpdateLaboratorioWithPatch() throws Exception {
        // Initialize the database
        laboratorioRepository.saveAndFlush(laboratorio);

        int databaseSizeBeforeUpdate = laboratorioRepository.findAll().size();

        // Update the laboratorio using partial update
        Laboratorio partialUpdatedLaboratorio = new Laboratorio();
        partialUpdatedLaboratorio.setId(laboratorio.getId());

        partialUpdatedLaboratorio.nombre(UPDATED_NOMBRE);

        restLaboratorioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLaboratorio.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLaboratorio))
            )
            .andExpect(status().isOk());

        // Validate the Laboratorio in the database
        List<Laboratorio> laboratorioList = laboratorioRepository.findAll();
        assertThat(laboratorioList).hasSize(databaseSizeBeforeUpdate);
        Laboratorio testLaboratorio = laboratorioList.get(laboratorioList.size() - 1);
        assertThat(testLaboratorio.getNombre()).isEqualTo(UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void patchNonExistingLaboratorio() throws Exception {
        int databaseSizeBeforeUpdate = laboratorioRepository.findAll().size();
        laboratorio.setId(count.incrementAndGet());

        // Create the Laboratorio
        LaboratorioDTO laboratorioDTO = laboratorioMapper.toDto(laboratorio);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLaboratorioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, laboratorioDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(laboratorioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Laboratorio in the database
        List<Laboratorio> laboratorioList = laboratorioRepository.findAll();
        assertThat(laboratorioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLaboratorio() throws Exception {
        int databaseSizeBeforeUpdate = laboratorioRepository.findAll().size();
        laboratorio.setId(count.incrementAndGet());

        // Create the Laboratorio
        LaboratorioDTO laboratorioDTO = laboratorioMapper.toDto(laboratorio);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLaboratorioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(laboratorioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Laboratorio in the database
        List<Laboratorio> laboratorioList = laboratorioRepository.findAll();
        assertThat(laboratorioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLaboratorio() throws Exception {
        int databaseSizeBeforeUpdate = laboratorioRepository.findAll().size();
        laboratorio.setId(count.incrementAndGet());

        // Create the Laboratorio
        LaboratorioDTO laboratorioDTO = laboratorioMapper.toDto(laboratorio);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLaboratorioMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(laboratorioDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Laboratorio in the database
        List<Laboratorio> laboratorioList = laboratorioRepository.findAll();
        assertThat(laboratorioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLaboratorio() throws Exception {
        // Initialize the database
        laboratorioRepository.saveAndFlush(laboratorio);

        int databaseSizeBeforeDelete = laboratorioRepository.findAll().size();

        // Delete the laboratorio
        restLaboratorioMockMvc
            .perform(delete(ENTITY_API_URL_ID, laboratorio.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Laboratorio> laboratorioList = laboratorioRepository.findAll();
        assertThat(laboratorioList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
