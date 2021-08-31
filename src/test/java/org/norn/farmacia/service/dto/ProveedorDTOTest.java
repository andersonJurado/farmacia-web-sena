package org.norn.farmacia.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.norn.farmacia.web.rest.TestUtil;

class ProveedorDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProveedorDTO.class);
        ProveedorDTO proveedorDTO1 = new ProveedorDTO();
        proveedorDTO1.setId(1L);
        ProveedorDTO proveedorDTO2 = new ProveedorDTO();
        assertThat(proveedorDTO1).isNotEqualTo(proveedorDTO2);
        proveedorDTO2.setId(proveedorDTO1.getId());
        assertThat(proveedorDTO1).isEqualTo(proveedorDTO2);
        proveedorDTO2.setId(2L);
        assertThat(proveedorDTO1).isNotEqualTo(proveedorDTO2);
        proveedorDTO1.setId(null);
        assertThat(proveedorDTO1).isNotEqualTo(proveedorDTO2);
    }
}
