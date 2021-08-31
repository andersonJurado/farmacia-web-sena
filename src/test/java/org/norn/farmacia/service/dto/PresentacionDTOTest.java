package org.norn.farmacia.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.norn.farmacia.web.rest.TestUtil;

class PresentacionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PresentacionDTO.class);
        PresentacionDTO presentacionDTO1 = new PresentacionDTO();
        presentacionDTO1.setId(1L);
        PresentacionDTO presentacionDTO2 = new PresentacionDTO();
        assertThat(presentacionDTO1).isNotEqualTo(presentacionDTO2);
        presentacionDTO2.setId(presentacionDTO1.getId());
        assertThat(presentacionDTO1).isEqualTo(presentacionDTO2);
        presentacionDTO2.setId(2L);
        assertThat(presentacionDTO1).isNotEqualTo(presentacionDTO2);
        presentacionDTO1.setId(null);
        assertThat(presentacionDTO1).isNotEqualTo(presentacionDTO2);
    }
}
