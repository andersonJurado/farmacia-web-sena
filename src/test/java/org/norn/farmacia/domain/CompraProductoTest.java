package org.norn.farmacia.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.norn.farmacia.web.rest.TestUtil;

class CompraProductoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CompraProducto.class);
        CompraProducto compraProducto1 = new CompraProducto();
        compraProducto1.setId(1L);
        CompraProducto compraProducto2 = new CompraProducto();
        compraProducto2.setId(compraProducto1.getId());
        assertThat(compraProducto1).isEqualTo(compraProducto2);
        compraProducto2.setId(2L);
        assertThat(compraProducto1).isNotEqualTo(compraProducto2);
        compraProducto1.setId(null);
        assertThat(compraProducto1).isNotEqualTo(compraProducto2);
    }
}
