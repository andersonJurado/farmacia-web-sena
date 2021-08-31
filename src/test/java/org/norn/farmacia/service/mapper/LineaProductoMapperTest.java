package org.norn.farmacia.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LineaProductoMapperTest {

    private LineaProductoMapper lineaProductoMapper;

    @BeforeEach
    public void setUp() {
        lineaProductoMapper = new LineaProductoMapperImpl();
    }
}
