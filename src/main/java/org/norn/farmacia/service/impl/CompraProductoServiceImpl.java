package org.norn.farmacia.service.impl;

import java.util.Optional;
import org.norn.farmacia.domain.CompraProducto;
import org.norn.farmacia.repository.CompraProductoRepository;
import org.norn.farmacia.service.CompraProductoService;
import org.norn.farmacia.service.dto.CompraProductoDTO;
import org.norn.farmacia.service.mapper.CompraProductoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CompraProducto}.
 */
@Service
@Transactional
public class CompraProductoServiceImpl implements CompraProductoService {

    private final Logger log = LoggerFactory.getLogger(CompraProductoServiceImpl.class);

    private final CompraProductoRepository compraProductoRepository;

    private final CompraProductoMapper compraProductoMapper;

    public CompraProductoServiceImpl(CompraProductoRepository compraProductoRepository, CompraProductoMapper compraProductoMapper) {
        this.compraProductoRepository = compraProductoRepository;
        this.compraProductoMapper = compraProductoMapper;
    }

    @Override
    public CompraProductoDTO save(CompraProductoDTO compraProductoDTO) {
        log.debug("Request to save CompraProducto : {}", compraProductoDTO);
        CompraProducto compraProducto = compraProductoMapper.toEntity(compraProductoDTO);
        compraProducto = compraProductoRepository.save(compraProducto);
        return compraProductoMapper.toDto(compraProducto);
    }

    @Override
    public Optional<CompraProductoDTO> partialUpdate(CompraProductoDTO compraProductoDTO) {
        log.debug("Request to partially update CompraProducto : {}", compraProductoDTO);

        return compraProductoRepository
            .findById(compraProductoDTO.getId())
            .map(
                existingCompraProducto -> {
                    compraProductoMapper.partialUpdate(existingCompraProducto, compraProductoDTO);

                    return existingCompraProducto;
                }
            )
            .map(compraProductoRepository::save)
            .map(compraProductoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CompraProductoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CompraProductos");
        return compraProductoRepository.findAll(pageable).map(compraProductoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CompraProductoDTO> findOne(Long id) {
        log.debug("Request to get CompraProducto : {}", id);
        return compraProductoRepository.findById(id).map(compraProductoMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CompraProducto : {}", id);
        compraProductoRepository.deleteById(id);
    }
}
