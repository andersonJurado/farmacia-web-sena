package org.norn.farmacia.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.norn.farmacia.web.rest.TestUtil;

class VentaProductoDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(VentaProductoDTO.class);
        VentaProductoDTO ventaProductoDTO1 = new VentaProductoDTO();
        ventaProductoDTO1.setId(1L);
        VentaProductoDTO ventaProductoDTO2 = new VentaProductoDTO();
        assertThat(ventaProductoDTO1).isNotEqualTo(ventaProductoDTO2);
        ventaProductoDTO2.setId(ventaProductoDTO1.getId());
        assertThat(ventaProductoDTO1).isEqualTo(ventaProductoDTO2);
        ventaProductoDTO2.setId(2L);
        assertThat(ventaProductoDTO1).isNotEqualTo(ventaProductoDTO2);
        ventaProductoDTO1.setId(null);
        assertThat(ventaProductoDTO1).isNotEqualTo(ventaProductoDTO2);
    }
}
