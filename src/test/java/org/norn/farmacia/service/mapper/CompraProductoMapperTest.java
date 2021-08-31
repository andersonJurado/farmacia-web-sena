package org.norn.farmacia.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CompraProductoMapperTest {

    private CompraProductoMapper compraProductoMapper;

    @BeforeEach
    public void setUp() {
        compraProductoMapper = new CompraProductoMapperImpl();
    }
}
