package org.norn.farmacia.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MunicipioMapperTest {

    private MunicipioMapper municipioMapper;

    @BeforeEach
    public void setUp() {
        municipioMapper = new MunicipioMapperImpl();
    }
}
