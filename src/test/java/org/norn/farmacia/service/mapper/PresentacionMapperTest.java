package org.norn.farmacia.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PresentacionMapperTest {

    private PresentacionMapper presentacionMapper;

    @BeforeEach
    public void setUp() {
        presentacionMapper = new PresentacionMapperImpl();
    }
}
