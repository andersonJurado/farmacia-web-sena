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
import org.norn.farmacia.domain.LineaProducto;
import org.norn.farmacia.domain.Producto;
import org.norn.farmacia.repository.LineaProductoRepository;
import org.norn.farmacia.service.criteria.LineaProductoCriteria;
import org.norn.farmacia.service.dto.LineaProductoDTO;
import org.norn.farmacia.service.mapper.LineaProductoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link LineaProductoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LineaProductoResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/linea-productos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LineaProductoRepository lineaProductoRepository;

    @Autowired
    private LineaProductoMapper lineaProductoMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLineaProductoMockMvc;

    private LineaProducto lineaProducto;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LineaProducto createEntity(EntityManager em) {
        LineaProducto lineaProducto = new LineaProducto().nombre(DEFAULT_NOMBRE);
        return lineaProducto;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LineaProducto createUpdatedEntity(EntityManager em) {
        LineaProducto lineaProducto = new LineaProducto().nombre(UPDATED_NOMBRE);
        return lineaProducto;
    }

    @BeforeEach
    public void initTest() {
        lineaProducto = createEntity(em);
    }

    @Test
    @Transactional
    void createLineaProducto() throws Exception {
        int databaseSizeBeforeCreate = lineaProductoRepository.findAll().size();
        // Create the LineaProducto
        LineaProductoDTO lineaProductoDTO = lineaProductoMapper.toDto(lineaProducto);
        restLineaProductoMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lineaProductoDTO))
            )
            .andExpect(status().isCreated());

        // Validate the LineaProducto in the database
        List<LineaProducto> lineaProductoList = lineaProductoRepository.findAll();
        assertThat(lineaProductoList).hasSize(databaseSizeBeforeCreate + 1);
        LineaProducto testLineaProducto = lineaProductoList.get(lineaProductoList.size() - 1);
        assertThat(testLineaProducto.getNombre()).isEqualTo(DEFAULT_NOMBRE);
    }

    @Test
    @Transactional
    void createLineaProductoWithExistingId() throws Exception {
        // Create the LineaProducto with an existing ID
        lineaProducto.setId(1L);
        LineaProductoDTO lineaProductoDTO = lineaProductoMapper.toDto(lineaProducto);

        int databaseSizeBeforeCreate = lineaProductoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLineaProductoMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lineaProductoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LineaProducto in the database
        List<LineaProducto> lineaProductoList = lineaProductoRepository.findAll();
        assertThat(lineaProductoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = lineaProductoRepository.findAll().size();
        // set the field null
        lineaProducto.setNombre(null);

        // Create the LineaProducto, which fails.
        LineaProductoDTO lineaProductoDTO = lineaProductoMapper.toDto(lineaProducto);

        restLineaProductoMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lineaProductoDTO))
            )
            .andExpect(status().isBadRequest());

        List<LineaProducto> lineaProductoList = lineaProductoRepository.findAll();
        assertThat(lineaProductoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllLineaProductos() throws Exception {
        // Initialize the database
        lineaProductoRepository.saveAndFlush(lineaProducto);

        // Get all the lineaProductoList
        restLineaProductoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(lineaProducto.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)));
    }

    @Test
    @Transactional
    void getLineaProducto() throws Exception {
        // Initialize the database
        lineaProductoRepository.saveAndFlush(lineaProducto);

        // Get the lineaProducto
        restLineaProductoMockMvc
            .perform(get(ENTITY_API_URL_ID, lineaProducto.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(lineaProducto.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE));
    }

    @Test
    @Transactional
    void getLineaProductosByIdFiltering() throws Exception {
        // Initialize the database
        lineaProductoRepository.saveAndFlush(lineaProducto);

        Long id = lineaProducto.getId();

        defaultLineaProductoShouldBeFound("id.equals=" + id);
        defaultLineaProductoShouldNotBeFound("id.notEquals=" + id);

        defaultLineaProductoShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultLineaProductoShouldNotBeFound("id.greaterThan=" + id);

        defaultLineaProductoShouldBeFound("id.lessThanOrEqual=" + id);
        defaultLineaProductoShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllLineaProductosByNombreIsEqualToSomething() throws Exception {
        // Initialize the database
        lineaProductoRepository.saveAndFlush(lineaProducto);

        // Get all the lineaProductoList where nombre equals to DEFAULT_NOMBRE
        defaultLineaProductoShouldBeFound("nombre.equals=" + DEFAULT_NOMBRE);

        // Get all the lineaProductoList where nombre equals to UPDATED_NOMBRE
        defaultLineaProductoShouldNotBeFound("nombre.equals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllLineaProductosByNombreIsNotEqualToSomething() throws Exception {
        // Initialize the database
        lineaProductoRepository.saveAndFlush(lineaProducto);

        // Get all the lineaProductoList where nombre not equals to DEFAULT_NOMBRE
        defaultLineaProductoShouldNotBeFound("nombre.notEquals=" + DEFAULT_NOMBRE);

        // Get all the lineaProductoList where nombre not equals to UPDATED_NOMBRE
        defaultLineaProductoShouldBeFound("nombre.notEquals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllLineaProductosByNombreIsInShouldWork() throws Exception {
        // Initialize the database
        lineaProductoRepository.saveAndFlush(lineaProducto);

        // Get all the lineaProductoList where nombre in DEFAULT_NOMBRE or UPDATED_NOMBRE
        defaultLineaProductoShouldBeFound("nombre.in=" + DEFAULT_NOMBRE + "," + UPDATED_NOMBRE);

        // Get all the lineaProductoList where nombre equals to UPDATED_NOMBRE
        defaultLineaProductoShouldNotBeFound("nombre.in=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllLineaProductosByNombreIsNullOrNotNull() throws Exception {
        // Initialize the database
        lineaProductoRepository.saveAndFlush(lineaProducto);

        // Get all the lineaProductoList where nombre is not null
        defaultLineaProductoShouldBeFound("nombre.specified=true");

        // Get all the lineaProductoList where nombre is null
        defaultLineaProductoShouldNotBeFound("nombre.specified=false");
    }

    @Test
    @Transactional
    void getAllLineaProductosByNombreContainsSomething() throws Exception {
        // Initialize the database
        lineaProductoRepository.saveAndFlush(lineaProducto);

        // Get all the lineaProductoList where nombre contains DEFAULT_NOMBRE
        defaultLineaProductoShouldBeFound("nombre.contains=" + DEFAULT_NOMBRE);

        // Get all the lineaProductoList where nombre contains UPDATED_NOMBRE
        defaultLineaProductoShouldNotBeFound("nombre.contains=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllLineaProductosByNombreNotContainsSomething() throws Exception {
        // Initialize the database
        lineaProductoRepository.saveAndFlush(lineaProducto);

        // Get all the lineaProductoList where nombre does not contain DEFAULT_NOMBRE
        defaultLineaProductoShouldNotBeFound("nombre.doesNotContain=" + DEFAULT_NOMBRE);

        // Get all the lineaProductoList where nombre does not contain UPDATED_NOMBRE
        defaultLineaProductoShouldBeFound("nombre.doesNotContain=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllLineaProductosByProductoIsEqualToSomething() throws Exception {
        // Initialize the database
        lineaProductoRepository.saveAndFlush(lineaProducto);
        Producto producto = ProductoResourceIT.createEntity(em);
        em.persist(producto);
        em.flush();
        lineaProducto.addProducto(producto);
        lineaProductoRepository.saveAndFlush(lineaProducto);
        Long productoId = producto.getId();

        // Get all the lineaProductoList where producto equals to productoId
        defaultLineaProductoShouldBeFound("productoId.equals=" + productoId);

        // Get all the lineaProductoList where producto equals to (productoId + 1)
        defaultLineaProductoShouldNotBeFound("productoId.equals=" + (productoId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultLineaProductoShouldBeFound(String filter) throws Exception {
        restLineaProductoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(lineaProducto.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)));

        // Check, that the count call also returns 1
        restLineaProductoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultLineaProductoShouldNotBeFound(String filter) throws Exception {
        restLineaProductoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restLineaProductoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingLineaProducto() throws Exception {
        // Get the lineaProducto
        restLineaProductoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewLineaProducto() throws Exception {
        // Initialize the database
        lineaProductoRepository.saveAndFlush(lineaProducto);

        int databaseSizeBeforeUpdate = lineaProductoRepository.findAll().size();

        // Update the lineaProducto
        LineaProducto updatedLineaProducto = lineaProductoRepository.findById(lineaProducto.getId()).get();
        // Disconnect from session so that the updates on updatedLineaProducto are not directly saved in db
        em.detach(updatedLineaProducto);
        updatedLineaProducto.nombre(UPDATED_NOMBRE);
        LineaProductoDTO lineaProductoDTO = lineaProductoMapper.toDto(updatedLineaProducto);

        restLineaProductoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, lineaProductoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(lineaProductoDTO))
            )
            .andExpect(status().isOk());

        // Validate the LineaProducto in the database
        List<LineaProducto> lineaProductoList = lineaProductoRepository.findAll();
        assertThat(lineaProductoList).hasSize(databaseSizeBeforeUpdate);
        LineaProducto testLineaProducto = lineaProductoList.get(lineaProductoList.size() - 1);
        assertThat(testLineaProducto.getNombre()).isEqualTo(UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void putNonExistingLineaProducto() throws Exception {
        int databaseSizeBeforeUpdate = lineaProductoRepository.findAll().size();
        lineaProducto.setId(count.incrementAndGet());

        // Create the LineaProducto
        LineaProductoDTO lineaProductoDTO = lineaProductoMapper.toDto(lineaProducto);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLineaProductoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, lineaProductoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(lineaProductoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LineaProducto in the database
        List<LineaProducto> lineaProductoList = lineaProductoRepository.findAll();
        assertThat(lineaProductoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLineaProducto() throws Exception {
        int databaseSizeBeforeUpdate = lineaProductoRepository.findAll().size();
        lineaProducto.setId(count.incrementAndGet());

        // Create the LineaProducto
        LineaProductoDTO lineaProductoDTO = lineaProductoMapper.toDto(lineaProducto);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLineaProductoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(lineaProductoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LineaProducto in the database
        List<LineaProducto> lineaProductoList = lineaProductoRepository.findAll();
        assertThat(lineaProductoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLineaProducto() throws Exception {
        int databaseSizeBeforeUpdate = lineaProductoRepository.findAll().size();
        lineaProducto.setId(count.incrementAndGet());

        // Create the LineaProducto
        LineaProductoDTO lineaProductoDTO = lineaProductoMapper.toDto(lineaProducto);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLineaProductoMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lineaProductoDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LineaProducto in the database
        List<LineaProducto> lineaProductoList = lineaProductoRepository.findAll();
        assertThat(lineaProductoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLineaProductoWithPatch() throws Exception {
        // Initialize the database
        lineaProductoRepository.saveAndFlush(lineaProducto);

        int databaseSizeBeforeUpdate = lineaProductoRepository.findAll().size();

        // Update the lineaProducto using partial update
        LineaProducto partialUpdatedLineaProducto = new LineaProducto();
        partialUpdatedLineaProducto.setId(lineaProducto.getId());

        restLineaProductoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLineaProducto.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLineaProducto))
            )
            .andExpect(status().isOk());

        // Validate the LineaProducto in the database
        List<LineaProducto> lineaProductoList = lineaProductoRepository.findAll();
        assertThat(lineaProductoList).hasSize(databaseSizeBeforeUpdate);
        LineaProducto testLineaProducto = lineaProductoList.get(lineaProductoList.size() - 1);
        assertThat(testLineaProducto.getNombre()).isEqualTo(DEFAULT_NOMBRE);
    }

    @Test
    @Transactional
    void fullUpdateLineaProductoWithPatch() throws Exception {
        // Initialize the database
        lineaProductoRepository.saveAndFlush(lineaProducto);

        int databaseSizeBeforeUpdate = lineaProductoRepository.findAll().size();

        // Update the lineaProducto using partial update
        LineaProducto partialUpdatedLineaProducto = new LineaProducto();
        partialUpdatedLineaProducto.setId(lineaProducto.getId());

        partialUpdatedLineaProducto.nombre(UPDATED_NOMBRE);

        restLineaProductoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLineaProducto.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLineaProducto))
            )
            .andExpect(status().isOk());

        // Validate the LineaProducto in the database
        List<LineaProducto> lineaProductoList = lineaProductoRepository.findAll();
        assertThat(lineaProductoList).hasSize(databaseSizeBeforeUpdate);
        LineaProducto testLineaProducto = lineaProductoList.get(lineaProductoList.size() - 1);
        assertThat(testLineaProducto.getNombre()).isEqualTo(UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void patchNonExistingLineaProducto() throws Exception {
        int databaseSizeBeforeUpdate = lineaProductoRepository.findAll().size();
        lineaProducto.setId(count.incrementAndGet());

        // Create the LineaProducto
        LineaProductoDTO lineaProductoDTO = lineaProductoMapper.toDto(lineaProducto);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLineaProductoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, lineaProductoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(lineaProductoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LineaProducto in the database
        List<LineaProducto> lineaProductoList = lineaProductoRepository.findAll();
        assertThat(lineaProductoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLineaProducto() throws Exception {
        int databaseSizeBeforeUpdate = lineaProductoRepository.findAll().size();
        lineaProducto.setId(count.incrementAndGet());

        // Create the LineaProducto
        LineaProductoDTO lineaProductoDTO = lineaProductoMapper.toDto(lineaProducto);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLineaProductoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(lineaProductoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LineaProducto in the database
        List<LineaProducto> lineaProductoList = lineaProductoRepository.findAll();
        assertThat(lineaProductoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLineaProducto() throws Exception {
        int databaseSizeBeforeUpdate = lineaProductoRepository.findAll().size();
        lineaProducto.setId(count.incrementAndGet());

        // Create the LineaProducto
        LineaProductoDTO lineaProductoDTO = lineaProductoMapper.toDto(lineaProducto);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLineaProductoMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(lineaProductoDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LineaProducto in the database
        List<LineaProducto> lineaProductoList = lineaProductoRepository.findAll();
        assertThat(lineaProductoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLineaProducto() throws Exception {
        // Initialize the database
        lineaProductoRepository.saveAndFlush(lineaProducto);

        int databaseSizeBeforeDelete = lineaProductoRepository.findAll().size();

        // Delete the lineaProducto
        restLineaProductoMockMvc
            .perform(delete(ENTITY_API_URL_ID, lineaProducto.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<LineaProducto> lineaProductoList = lineaProductoRepository.findAll();
        assertThat(lineaProductoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
