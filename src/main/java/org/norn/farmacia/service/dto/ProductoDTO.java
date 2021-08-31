package org.norn.farmacia.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link org.norn.farmacia.domain.Producto} entity.
 */
public class ProductoDTO implements Serializable {

    private Long id;

    @NotNull
    private String nombreProducto;

    private Integer cantidad;

    private Double iva;

    private Double precioUdsVenta;

    private Double margenDeGanancia;

    private String invima;

    private PresentacionDTO presentacion;

    private LaboratorioDTO laboratorio;

    private LineaProductoDTO lineaProducto;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Double getIva() {
        return iva;
    }

    public void setIva(Double iva) {
        this.iva = iva;
    }

    public Double getPrecioUdsVenta() {
        return precioUdsVenta;
    }

    public void setPrecioUdsVenta(Double precioUdsVenta) {
        this.precioUdsVenta = precioUdsVenta;
    }

    public Double getMargenDeGanancia() {
        return margenDeGanancia;
    }

    public void setMargenDeGanancia(Double margenDeGanancia) {
        this.margenDeGanancia = margenDeGanancia;
    }

    public String getInvima() {
        return invima;
    }

    public void setInvima(String invima) {
        this.invima = invima;
    }

    public PresentacionDTO getPresentacion() {
        return presentacion;
    }

    public void setPresentacion(PresentacionDTO presentacion) {
        this.presentacion = presentacion;
    }

    public LaboratorioDTO getLaboratorio() {
        return laboratorio;
    }

    public void setLaboratorio(LaboratorioDTO laboratorio) {
        this.laboratorio = laboratorio;
    }

    public LineaProductoDTO getLineaProducto() {
        return lineaProducto;
    }

    public void setLineaProducto(LineaProductoDTO lineaProducto) {
        this.lineaProducto = lineaProducto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductoDTO)) {
            return false;
        }

        ProductoDTO productoDTO = (ProductoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, productoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductoDTO{" +
            "id=" + getId() +
            ", nombreProducto='" + getNombreProducto() + "'" +
            ", cantidad=" + getCantidad() +
            ", iva=" + getIva() +
            ", precioUdsVenta=" + getPrecioUdsVenta() +
            ", margenDeGanancia=" + getMargenDeGanancia() +
            ", invima='" + getInvima() + "'" +
            ", presentacion=" + getPresentacion() +
            ", laboratorio=" + getLaboratorio() +
            ", lineaProducto=" + getLineaProducto() +
            "}";
    }
}
