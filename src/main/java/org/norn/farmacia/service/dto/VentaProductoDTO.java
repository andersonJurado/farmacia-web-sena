package org.norn.farmacia.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link org.norn.farmacia.domain.VentaProducto} entity.
 */
public class VentaProductoDTO implements Serializable {

    private Long id;

    private Integer cantidad;

    private Double total;

    private ProductoDTO producto;

    private VentaDTO venta;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public ProductoDTO getProducto() {
        return producto;
    }

    public void setProducto(ProductoDTO producto) {
        this.producto = producto;
    }

    public VentaDTO getVenta() {
        return venta;
    }

    public void setVenta(VentaDTO venta) {
        this.venta = venta;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VentaProductoDTO)) {
            return false;
        }

        VentaProductoDTO ventaProductoDTO = (VentaProductoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, ventaProductoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VentaProductoDTO{" +
            "id=" + getId() +
            ", cantidad=" + getCantidad() +
            ", total=" + getTotal() +
            ", producto=" + getProducto() +
            ", venta=" + getVenta() +
            "}";
    }
}
