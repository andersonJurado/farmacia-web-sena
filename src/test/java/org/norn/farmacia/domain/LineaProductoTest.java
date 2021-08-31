package org.norn.farmacia.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.norn.farmacia.web.rest.TestUtil;

class LineaProductoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LineaProducto.class);
        LineaProducto lineaProducto1 = new LineaProducto();
        lineaProducto1.setId(1L);
        LineaProducto lineaProducto2 = new LineaProducto();
        lineaProducto2.setId(lineaProducto1.getId());
        assertThat(lineaProducto1).isEqualTo(lineaProducto2);
        lineaProducto2.setId(2L);
        assertThat(lineaProducto1).isNotEqualTo(lineaProducto2);
        lineaProducto1.setId(null);
        assertThat(lineaProducto1).isNotEqualTo(lineaProducto2);
    }
}
