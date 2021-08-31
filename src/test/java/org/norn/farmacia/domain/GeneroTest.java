package org.norn.farmacia.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.norn.farmacia.web.rest.TestUtil;

class GeneroTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Genero.class);
        Genero genero1 = new Genero();
        genero1.setId(1L);
        Genero genero2 = new Genero();
        genero2.setId(genero1.getId());
        assertThat(genero1).isEqualTo(genero2);
        genero2.setId(2L);
        assertThat(genero1).isNotEqualTo(genero2);
        genero1.setId(null);
        assertThat(genero1).isNotEqualTo(genero2);
    }
}
