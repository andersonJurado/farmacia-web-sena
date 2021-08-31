package org.norn.farmacia.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.norn.farmacia.web.rest.TestUtil;

class LineaProductoDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LineaProductoDTO.class);
        LineaProductoDTO lineaProductoDTO1 = new LineaProductoDTO();
        lineaProductoDTO1.setId(1L);
        LineaProductoDTO lineaProductoDTO2 = new LineaProductoDTO();
        assertThat(lineaProductoDTO1).isNotEqualTo(lineaProductoDTO2);
        lineaProductoDTO2.setId(lineaProductoDTO1.getId());
        assertThat(lineaProductoDTO1).isEqualTo(lineaProductoDTO2);
        lineaProductoDTO2.setId(2L);
        assertThat(lineaProductoDTO1).isNotEqualTo(lineaProductoDTO2);
        lineaProductoDTO1.setId(null);
        assertThat(lineaProductoDTO1).isNotEqualTo(lineaProductoDTO2);
    }
}
