package org.norn.farmacia.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DepartamentoMapperTest {

    private DepartamentoMapper departamentoMapper;

    @BeforeEach
    public void setUp() {
        departamentoMapper = new DepartamentoMapperImpl();
    }
}
