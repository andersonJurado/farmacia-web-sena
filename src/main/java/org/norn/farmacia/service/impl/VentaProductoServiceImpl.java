package org.norn.farmacia.service.impl;

import java.util.Optional;
import org.norn.farmacia.domain.VentaProducto;
import org.norn.farmacia.repository.VentaProductoRepository;
import org.norn.farmacia.service.VentaProductoService;
import org.norn.farmacia.service.dto.VentaProductoDTO;
import org.norn.farmacia.service.mapper.VentaProductoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link VentaProducto}.
 */
@Service
@Transactional
public class VentaProductoServiceImpl implements VentaProductoService {

    private final Logger log = LoggerFactory.getLogger(VentaProductoServiceImpl.class);

    private final VentaProductoRepository ventaProductoRepository;

    private final VentaProductoMapper ventaProductoMapper;

    public VentaProductoServiceImpl(VentaProductoRepository ventaProductoRepository, VentaProductoMapper ventaProductoMapper) {
        this.ventaProductoRepository = ventaProductoRepository;
        this.ventaProductoMapper = ventaProductoMapper;
    }

    @Override
    public VentaProductoDTO save(VentaProductoDTO ventaProductoDTO) {
        log.debug("Request to save VentaProducto : {}", ventaProductoDTO);
        VentaProducto ventaProducto = ventaProductoMapper.toEntity(ventaProductoDTO);
        ventaProducto = ventaProductoRepository.save(ventaProducto);
        return ventaProductoMapper.toDto(ventaProducto);
    }

    @Override
    public Optional<VentaProductoDTO> partialUpdate(VentaProductoDTO ventaProductoDTO) {
        log.debug("Request to partially update VentaProducto : {}", ventaProductoDTO);

        return ventaProductoRepository
            .findById(ventaProductoDTO.getId())
            .map(
                existingVentaProducto -> {
                    ventaProductoMapper.partialUpdate(existingVentaProducto, ventaProductoDTO);

                    return existingVentaProducto;
                }
            )
            .map(ventaProductoRepository::save)
            .map(ventaProductoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<VentaProductoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all VentaProductos");
        return ventaProductoRepository.findAll(pageable).map(ventaProductoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<VentaProductoDTO> findOne(Long id) {
        log.debug("Request to get VentaProducto : {}", id);
        return ventaProductoRepository.findById(id).map(ventaProductoMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete VentaProducto : {}", id);
        ventaProductoRepository.deleteById(id);
    }
}
