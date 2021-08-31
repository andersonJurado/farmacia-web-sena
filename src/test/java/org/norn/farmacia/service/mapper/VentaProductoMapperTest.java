package org.norn.farmacia.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class VentaProductoMapperTest {

    private VentaProductoMapper ventaProductoMapper;

    @BeforeEach
    public void setUp() {
        ventaProductoMapper = new VentaProductoMapperImpl();
    }
}
