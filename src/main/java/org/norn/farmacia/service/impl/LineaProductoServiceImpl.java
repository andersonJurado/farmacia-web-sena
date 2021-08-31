package org.norn.farmacia.service.impl;

import java.util.Optional;
import org.norn.farmacia.domain.LineaProducto;
import org.norn.farmacia.repository.LineaProductoRepository;
import org.norn.farmacia.service.LineaProductoService;
import org.norn.farmacia.service.dto.LineaProductoDTO;
import org.norn.farmacia.service.mapper.LineaProductoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link LineaProducto}.
 */
@Service
@Transactional
public class LineaProductoServiceImpl implements LineaProductoService {

    private final Logger log = LoggerFactory.getLogger(LineaProductoServiceImpl.class);

    private final LineaProductoRepository lineaProductoRepository;

    private final LineaProductoMapper lineaProductoMapper;

    public LineaProductoServiceImpl(LineaProductoRepository lineaProductoRepository, LineaProductoMapper lineaProductoMapper) {
        this.lineaProductoRepository = lineaProductoRepository;
        this.lineaProductoMapper = lineaProductoMapper;
    }

    @Override
    public LineaProductoDTO save(LineaProductoDTO lineaProductoDTO) {
        log.debug("Request to save LineaProducto : {}", lineaProductoDTO);
        LineaProducto lineaProducto = lineaProductoMapper.toEntity(lineaProductoDTO);
        lineaProducto = lineaProductoRepository.save(lineaProducto);
        return lineaProductoMapper.toDto(lineaProducto);
    }

    @Override
    public Optional<LineaProductoDTO> partialUpdate(LineaProductoDTO lineaProductoDTO) {
        log.debug("Request to partially update LineaProducto : {}", lineaProductoDTO);

        return lineaProductoRepository
            .findById(lineaProductoDTO.getId())
            .map(
                existingLineaProducto -> {
                    lineaProductoMapper.partialUpdate(existingLineaProducto, lineaProductoDTO);

                    return existingLineaProducto;
                }
            )
            .map(lineaProductoRepository::save)
            .map(lineaProductoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LineaProductoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all LineaProductos");
        return lineaProductoRepository.findAll(pageable).map(lineaProductoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LineaProductoDTO> findOne(Long id) {
        log.debug("Request to get LineaProducto : {}", id);
        return lineaProductoRepository.findById(id).map(lineaProductoMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete LineaProducto : {}", id);
        lineaProductoRepository.deleteById(id);
    }
}
