package org.norn.farmacia.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.norn.farmacia.web.rest.TestUtil;

class VentaProductoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(VentaProducto.class);
        VentaProducto ventaProducto1 = new VentaProducto();
        ventaProducto1.setId(1L);
        VentaProducto ventaProducto2 = new VentaProducto();
        ventaProducto2.setId(ventaProducto1.getId());
        assertThat(ventaProducto1).isEqualTo(ventaProducto2);
        ventaProducto2.setId(2L);
        assertThat(ventaProducto1).isNotEqualTo(ventaProducto2);
        ventaProducto1.setId(null);
        assertThat(ventaProducto1).isNotEqualTo(ventaProducto2);
    }
}
