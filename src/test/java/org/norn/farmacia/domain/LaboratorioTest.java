package org.norn.farmacia.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.norn.farmacia.web.rest.TestUtil;

class LaboratorioTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Laboratorio.class);
        Laboratorio laboratorio1 = new Laboratorio();
        laboratorio1.setId(1L);
        Laboratorio laboratorio2 = new Laboratorio();
        laboratorio2.setId(laboratorio1.getId());
        assertThat(laboratorio1).isEqualTo(laboratorio2);
        laboratorio2.setId(2L);
        assertThat(laboratorio1).isNotEqualTo(laboratorio2);
        laboratorio1.setId(null);
        assertThat(laboratorio1).isNotEqualTo(laboratorio2);
    }
}
