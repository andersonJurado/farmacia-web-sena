package org.norn.farmacia.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.norn.farmacia.web.rest.TestUtil;

class PresentacionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Presentacion.class);
        Presentacion presentacion1 = new Presentacion();
        presentacion1.setId(1L);
        Presentacion presentacion2 = new Presentacion();
        presentacion2.setId(presentacion1.getId());
        assertThat(presentacion1).isEqualTo(presentacion2);
        presentacion2.setId(2L);
        assertThat(presentacion1).isNotEqualTo(presentacion2);
        presentacion1.setId(null);
        assertThat(presentacion1).isNotEqualTo(presentacion2);
    }
}
