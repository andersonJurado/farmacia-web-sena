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
import org.norn.farmacia.domain.Presentacion;
import org.norn.farmacia.domain.Producto;
import org.norn.farmacia.repository.PresentacionRepository;
import org.norn.farmacia.service.criteria.PresentacionCriteria;
import org.norn.farmacia.service.dto.PresentacionDTO;
import org.norn.farmacia.service.mapper.PresentacionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link PresentacionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PresentacionResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/presentacions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PresentacionRepository presentacionRepository;

    @Autowired
    private PresentacionMapper presentacionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPresentacionMockMvc;

    private Presentacion presentacion;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Presentacion createEntity(EntityManager em) {
        Presentacion presentacion = new Presentacion().nombre(DEFAULT_NOMBRE);
        return presentacion;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Presentacion createUpdatedEntity(EntityManager em) {
        Presentacion presentacion = new Presentacion().nombre(UPDATED_NOMBRE);
        return presentacion;
    }

    @BeforeEach
    public void initTest() {
        presentacion = createEntity(em);
    }

    @Test
    @Transactional
    void createPresentacion() throws Exception {
        int databaseSizeBeforeCreate = presentacionRepository.findAll().size();
        // Create the Presentacion
        PresentacionDTO presentacionDTO = presentacionMapper.toDto(presentacion);
        restPresentacionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(presentacionDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Presentacion in the database
        List<Presentacion> presentacionList = presentacionRepository.findAll();
        assertThat(presentacionList).hasSize(databaseSizeBeforeCreate + 1);
        Presentacion testPresentacion = presentacionList.get(presentacionList.size() - 1);
        assertThat(testPresentacion.getNombre()).isEqualTo(DEFAULT_NOMBRE);
    }

    @Test
    @Transactional
    void createPresentacionWithExistingId() throws Exception {
        // Create the Presentacion with an existing ID
        presentacion.setId(1L);
        PresentacionDTO presentacionDTO = presentacionMapper.toDto(presentacion);

        int databaseSizeBeforeCreate = presentacionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPresentacionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(presentacionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Presentacion in the database
        List<Presentacion> presentacionList = presentacionRepository.findAll();
        assertThat(presentacionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = presentacionRepository.findAll().size();
        // set the field null
        presentacion.setNombre(null);

        // Create the Presentacion, which fails.
        PresentacionDTO presentacionDTO = presentacionMapper.toDto(presentacion);

        restPresentacionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(presentacionDTO))
            )
            .andExpect(status().isBadRequest());

        List<Presentacion> presentacionList = presentacionRepository.findAll();
        assertThat(presentacionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPresentacions() throws Exception {
        // Initialize the database
        presentacionRepository.saveAndFlush(presentacion);

        // Get all the presentacionList
        restPresentacionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(presentacion.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)));
    }

    @Test
    @Transactional
    void getPresentacion() throws Exception {
        // Initialize the database
        presentacionRepository.saveAndFlush(presentacion);

        // Get the presentacion
        restPresentacionMockMvc
            .perform(get(ENTITY_API_URL_ID, presentacion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(presentacion.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE));
    }

    @Test
    @Transactional
    void getPresentacionsByIdFiltering() throws Exception {
        // Initialize the database
        presentacionRepository.saveAndFlush(presentacion);

        Long id = presentacion.getId();

        defaultPresentacionShouldBeFound("id.equals=" + id);
        defaultPresentacionShouldNotBeFound("id.notEquals=" + id);

        defaultPresentacionShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPresentacionShouldNotBeFound("id.greaterThan=" + id);

        defaultPresentacionShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPresentacionShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPresentacionsByNombreIsEqualToSomething() throws Exception {
        // Initialize the database
        presentacionRepository.saveAndFlush(presentacion);

        // Get all the presentacionList where nombre equals to DEFAULT_NOMBRE
        defaultPresentacionShouldBeFound("nombre.equals=" + DEFAULT_NOMBRE);

        // Get all the presentacionList where nombre equals to UPDATED_NOMBRE
        defaultPresentacionShouldNotBeFound("nombre.equals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllPresentacionsByNombreIsNotEqualToSomething() throws Exception {
        // Initialize the database
        presentacionRepository.saveAndFlush(presentacion);

        // Get all the presentacionList where nombre not equals to DEFAULT_NOMBRE
        defaultPresentacionShouldNotBeFound("nombre.notEquals=" + DEFAULT_NOMBRE);

        // Get all the presentacionList where nombre not equals to UPDATED_NOMBRE
        defaultPresentacionShouldBeFound("nombre.notEquals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllPresentacionsByNombreIsInShouldWork() throws Exception {
        // Initialize the database
        presentacionRepository.saveAndFlush(presentacion);

        // Get all the presentacionList where nombre in DEFAULT_NOMBRE or UPDATED_NOMBRE
        defaultPresentacionShouldBeFound("nombre.in=" + DEFAULT_NOMBRE + "," + UPDATED_NOMBRE);

        // Get all the presentacionList where nombre equals to UPDATED_NOMBRE
        defaultPresentacionShouldNotBeFound("nombre.in=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllPresentacionsByNombreIsNullOrNotNull() throws Exception {
        // Initialize the database
        presentacionRepository.saveAndFlush(presentacion);

        // Get all the presentacionList where nombre is not null
        defaultPresentacionShouldBeFound("nombre.specified=true");

        // Get all the presentacionList where nombre is null
        defaultPresentacionShouldNotBeFound("nombre.specified=false");
    }

    @Test
    @Transactional
    void getAllPresentacionsByNombreContainsSomething() throws Exception {
        // Initialize the database
        presentacionRepository.saveAndFlush(presentacion);

        // Get all the presentacionList where nombre contains DEFAULT_NOMBRE
        defaultPresentacionShouldBeFound("nombre.contains=" + DEFAULT_NOMBRE);

        // Get all the presentacionList where nombre contains UPDATED_NOMBRE
        defaultPresentacionShouldNotBeFound("nombre.contains=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllPresentacionsByNombreNotContainsSomething() throws Exception {
        // Initialize the database
        presentacionRepository.saveAndFlush(presentacion);

        // Get all the presentacionList where nombre does not contain DEFAULT_NOMBRE
        defaultPresentacionShouldNotBeFound("nombre.doesNotContain=" + DEFAULT_NOMBRE);

        // Get all the presentacionList where nombre does not contain UPDATED_NOMBRE
        defaultPresentacionShouldBeFound("nombre.doesNotContain=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllPresentacionsByProductoIsEqualToSomething() throws Exception {
        // Initialize the database
        presentacionRepository.saveAndFlush(presentacion);
        Producto producto = ProductoResourceIT.createEntity(em);
        em.persist(producto);
        em.flush();
        presentacion.addProducto(producto);
        presentacionRepository.saveAndFlush(presentacion);
        Long productoId = producto.getId();

        // Get all the presentacionList where producto equals to productoId
        defaultPresentacionShouldBeFound("productoId.equals=" + productoId);

        // Get all the presentacionList where producto equals to (productoId + 1)
        defaultPresentacionShouldNotBeFound("productoId.equals=" + (productoId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPresentacionShouldBeFound(String filter) throws Exception {
        restPresentacionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(presentacion.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)));

        // Check, that the count call also returns 1
        restPresentacionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPresentacionShouldNotBeFound(String filter) throws Exception {
        restPresentacionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPresentacionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPresentacion() throws Exception {
        // Get the presentacion
        restPresentacionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPresentacion() throws Exception {
        // Initialize the database
        presentacionRepository.saveAndFlush(presentacion);

        int databaseSizeBeforeUpdate = presentacionRepository.findAll().size();

        // Update the presentacion
        Presentacion updatedPresentacion = presentacionRepository.findById(presentacion.getId()).get();
        // Disconnect from session so that the updates on updatedPresentacion are not directly saved in db
        em.detach(updatedPresentacion);
        updatedPresentacion.nombre(UPDATED_NOMBRE);
        PresentacionDTO presentacionDTO = presentacionMapper.toDto(updatedPresentacion);

        restPresentacionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, presentacionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(presentacionDTO))
            )
            .andExpect(status().isOk());

        // Validate the Presentacion in the database
        List<Presentacion> presentacionList = presentacionRepository.findAll();
        assertThat(presentacionList).hasSize(databaseSizeBeforeUpdate);
        Presentacion testPresentacion = presentacionList.get(presentacionList.size() - 1);
        assertThat(testPresentacion.getNombre()).isEqualTo(UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void putNonExistingPresentacion() throws Exception {
        int databaseSizeBeforeUpdate = presentacionRepository.findAll().size();
        presentacion.setId(count.incrementAndGet());

        // Create the Presentacion
        PresentacionDTO presentacionDTO = presentacionMapper.toDto(presentacion);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPresentacionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, presentacionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(presentacionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Presentacion in the database
        List<Presentacion> presentacionList = presentacionRepository.findAll();
        assertThat(presentacionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPresentacion() throws Exception {
        int databaseSizeBeforeUpdate = presentacionRepository.findAll().size();
        presentacion.setId(count.incrementAndGet());

        // Create the Presentacion
        PresentacionDTO presentacionDTO = presentacionMapper.toDto(presentacion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPresentacionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(presentacionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Presentacion in the database
        List<Presentacion> presentacionList = presentacionRepository.findAll();
        assertThat(presentacionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPresentacion() throws Exception {
        int databaseSizeBeforeUpdate = presentacionRepository.findAll().size();
        presentacion.setId(count.incrementAndGet());

        // Create the Presentacion
        PresentacionDTO presentacionDTO = presentacionMapper.toDto(presentacion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPresentacionMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(presentacionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Presentacion in the database
        List<Presentacion> presentacionList = presentacionRepository.findAll();
        assertThat(presentacionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePresentacionWithPatch() throws Exception {
        // Initialize the database
        presentacionRepository.saveAndFlush(presentacion);

        int databaseSizeBeforeUpdate = presentacionRepository.findAll().size();

        // Update the presentacion using partial update
        Presentacion partialUpdatedPresentacion = new Presentacion();
        partialUpdatedPresentacion.setId(presentacion.getId());

        restPresentacionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPresentacion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPresentacion))
            )
            .andExpect(status().isOk());

        // Validate the Presentacion in the database
        List<Presentacion> presentacionList = presentacionRepository.findAll();
        assertThat(presentacionList).hasSize(databaseSizeBeforeUpdate);
        Presentacion testPresentacion = presentacionList.get(presentacionList.size() - 1);
        assertThat(testPresentacion.getNombre()).isEqualTo(DEFAULT_NOMBRE);
    }

    @Test
    @Transactional
    void fullUpdatePresentacionWithPatch() throws Exception {
        // Initialize the database
        presentacionRepository.saveAndFlush(presentacion);

        int databaseSizeBeforeUpdate = presentacionRepository.findAll().size();

        // Update the presentacion using partial update
        Presentacion partialUpdatedPresentacion = new Presentacion();
        partialUpdatedPresentacion.setId(presentacion.getId());

        partialUpdatedPresentacion.nombre(UPDATED_NOMBRE);

        restPresentacionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPresentacion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPresentacion))
            )
            .andExpect(status().isOk());

        // Validate the Presentacion in the database
        List<Presentacion> presentacionList = presentacionRepository.findAll();
        assertThat(presentacionList).hasSize(databaseSizeBeforeUpdate);
        Presentacion testPresentacion = presentacionList.get(presentacionList.size() - 1);
        assertThat(testPresentacion.getNombre()).isEqualTo(UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void patchNonExistingPresentacion() throws Exception {
        int databaseSizeBeforeUpdate = presentacionRepository.findAll().size();
        presentacion.setId(count.incrementAndGet());

        // Create the Presentacion
        PresentacionDTO presentacionDTO = presentacionMapper.toDto(presentacion);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPresentacionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, presentacionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(presentacionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Presentacion in the database
        List<Presentacion> presentacionList = presentacionRepository.findAll();
        assertThat(presentacionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPresentacion() throws Exception {
        int databaseSizeBeforeUpdate = presentacionRepository.findAll().size();
        presentacion.setId(count.incrementAndGet());

        // Create the Presentacion
        PresentacionDTO presentacionDTO = presentacionMapper.toDto(presentacion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPresentacionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(presentacionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Presentacion in the database
        List<Presentacion> presentacionList = presentacionRepository.findAll();
        assertThat(presentacionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPresentacion() throws Exception {
        int databaseSizeBeforeUpdate = presentacionRepository.findAll().size();
        presentacion.setId(count.incrementAndGet());

        // Create the Presentacion
        PresentacionDTO presentacionDTO = presentacionMapper.toDto(presentacion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPresentacionMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(presentacionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Presentacion in the database
        List<Presentacion> presentacionList = presentacionRepository.findAll();
        assertThat(presentacionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePresentacion() throws Exception {
        // Initialize the database
        presentacionRepository.saveAndFlush(presentacion);

        int databaseSizeBeforeDelete = presentacionRepository.findAll().size();

        // Delete the presentacion
        restPresentacionMockMvc
            .perform(delete(ENTITY_API_URL_ID, presentacion.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Presentacion> presentacionList = presentacionRepository.findAll();
        assertThat(presentacionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
