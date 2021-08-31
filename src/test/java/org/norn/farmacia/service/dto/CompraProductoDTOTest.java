package org.norn.farmacia.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.norn.farmacia.web.rest.TestUtil;

class CompraProductoDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CompraProductoDTO.class);
        CompraProductoDTO compraProductoDTO1 = new CompraProductoDTO();
        compraProductoDTO1.setId(1L);
        CompraProductoDTO compraProductoDTO2 = new CompraProductoDTO();
        assertThat(compraProductoDTO1).isNotEqualTo(compraProductoDTO2);
        compraProductoDTO2.setId(compraProductoDTO1.getId());
        assertThat(compraProductoDTO1).isEqualTo(compraProductoDTO2);
        compraProductoDTO2.setId(2L);
        assertThat(compraProductoDTO1).isNotEqualTo(compraProductoDTO2);
        compraProductoDTO1.setId(null);
        assertThat(compraProductoDTO1).isNotEqualTo(compraProductoDTO2);
    }
}
