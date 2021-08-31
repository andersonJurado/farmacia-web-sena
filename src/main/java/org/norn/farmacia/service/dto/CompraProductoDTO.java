package org.norn.farmacia.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link org.norn.farmacia.domain.CompraProducto} entity.
 */
public class CompraProductoDTO implements Serializable {

    private Long id;

    private Double cantidadUds;

    private Double precioUdsCompra;

    private Double subTotal;

    private Double iva;

    private Double total;

    private LocalDate fechaVencimiento;

    private String lote;

    private ProductoDTO producto;

    private CompraDTO compra;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getCantidadUds() {
        return cantidadUds;
    }

    public void setCantidadUds(Double cantidadUds) {
        this.cantidadUds = cantidadUds;
    }

    public Double getPrecioUdsCompra() {
        return precioUdsCompra;
    }

    public void setPrecioUdsCompra(Double precioUdsCompra) {
        this.precioUdsCompra = precioUdsCompra;
    }

    public Double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(Double subTotal) {
        this.subTotal = subTotal;
    }

    public Double getIva() {
        return iva;
    }

    public void setIva(Double iva) {
        this.iva = iva;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(LocalDate fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public String getLote() {
        return lote;
    }

    public void setLote(String lote) {
        this.lote = lote;
    }

    public ProductoDTO getProducto() {
        return producto;
    }

    public void setProducto(ProductoDTO producto) {
        this.producto = producto;
    }

    public CompraDTO getCompra() {
        return compra;
    }

    public void setCompra(CompraDTO compra) {
        this.compra = compra;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CompraProductoDTO)) {
            return false;
        }

        CompraProductoDTO compraProductoDTO = (CompraProductoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, compraProductoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CompraProductoDTO{" +
            "id=" + getId() +
            ", cantidadUds=" + getCantidadUds() +
            ", precioUdsCompra=" + getPrecioUdsCompra() +
            ", subTotal=" + getSubTotal() +
            ", iva=" + getIva() +
            ", total=" + getTotal() +
            ", fechaVencimiento='" + getFechaVencimiento() + "'" +
            ", lote='" + getLote() + "'" +
            ", producto=" + getProducto() +
            ", compra=" + getCompra() +
            "}";
    }
}
