package org.norn.farmacia.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link org.norn.farmacia.domain.Producto} entity. This class is used
 * in {@link org.norn.farmacia.web.rest.ProductoResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /productos?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ProductoCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nombreProducto;

    private IntegerFilter cantidad;

    private DoubleFilter iva;

    private DoubleFilter precioUdsVenta;

    private DoubleFilter margenDeGanancia;

    private StringFilter invima;

    private LongFilter presentacionId;

    private LongFilter laboratorioId;

    private LongFilter lineaProductoId;

    private LongFilter compraProductoId;

    private LongFilter ventaProductoId;

    public ProductoCriteria() {}

    public ProductoCriteria(ProductoCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nombreProducto = other.nombreProducto == null ? null : other.nombreProducto.copy();
        this.cantidad = other.cantidad == null ? null : other.cantidad.copy();
        this.iva = other.iva == null ? null : other.iva.copy();
        this.precioUdsVenta = other.precioUdsVenta == null ? null : other.precioUdsVenta.copy();
        this.margenDeGanancia = other.margenDeGanancia == null ? null : other.margenDeGanancia.copy();
        this.invima = other.invima == null ? null : other.invima.copy();
        this.presentacionId = other.presentacionId == null ? null : other.presentacionId.copy();
        this.laboratorioId = other.laboratorioId == null ? null : other.laboratorioId.copy();
        this.lineaProductoId = other.lineaProductoId == null ? null : other.lineaProductoId.copy();
        this.compraProductoId = other.compraProductoId == null ? null : other.compraProductoId.copy();
        this.ventaProductoId = other.ventaProductoId == null ? null : other.ventaProductoId.copy();
    }

    @Override
    public ProductoCriteria copy() {
        return new ProductoCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getNombreProducto() {
        return nombreProducto;
    }

    public StringFilter nombreProducto() {
        if (nombreProducto == null) {
            nombreProducto = new StringFilter();
        }
        return nombreProducto;
    }

    public void setNombreProducto(StringFilter nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public IntegerFilter getCantidad() {
        return cantidad;
    }

    public IntegerFilter cantidad() {
        if (cantidad == null) {
            cantidad = new IntegerFilter();
        }
        return cantidad;
    }

    public void setCantidad(IntegerFilter cantidad) {
        this.cantidad = cantidad;
    }

    public DoubleFilter getIva() {
        return iva;
    }

    public DoubleFilter iva() {
        if (iva == null) {
            iva = new DoubleFilter();
        }
        return iva;
    }

    public void setIva(DoubleFilter iva) {
        this.iva = iva;
    }

    public DoubleFilter getPrecioUdsVenta() {
        return precioUdsVenta;
    }

    public DoubleFilter precioUdsVenta() {
        if (precioUdsVenta == null) {
            precioUdsVenta = new DoubleFilter();
        }
        return precioUdsVenta;
    }

    public void setPrecioUdsVenta(DoubleFilter precioUdsVenta) {
        this.precioUdsVenta = precioUdsVenta;
    }

    public DoubleFilter getMargenDeGanancia() {
        return margenDeGanancia;
    }

    public DoubleFilter margenDeGanancia() {
        if (margenDeGanancia == null) {
            margenDeGanancia = new DoubleFilter();
        }
        return margenDeGanancia;
    }

    public void setMargenDeGanancia(DoubleFilter margenDeGanancia) {
        this.margenDeGanancia = margenDeGanancia;
    }

    public StringFilter getInvima() {
        return invima;
    }

    public StringFilter invima() {
        if (invima == null) {
            invima = new StringFilter();
        }
        return invima;
    }

    public void setInvima(StringFilter invima) {
        this.invima = invima;
    }

    public LongFilter getPresentacionId() {
        return presentacionId;
    }

    public LongFilter presentacionId() {
        if (presentacionId == null) {
            presentacionId = new LongFilter();
        }
        return presentacionId;
    }

    public void setPresentacionId(LongFilter presentacionId) {
        this.presentacionId = presentacionId;
    }

    public LongFilter getLaboratorioId() {
        return laboratorioId;
    }

    public LongFilter laboratorioId() {
        if (laboratorioId == null) {
            laboratorioId = new LongFilter();
        }
        return laboratorioId;
    }

    public void setLaboratorioId(LongFilter laboratorioId) {
        this.laboratorioId = laboratorioId;
    }

    public LongFilter getLineaProductoId() {
        return lineaProductoId;
    }

    public LongFilter lineaProductoId() {
        if (lineaProductoId == null) {
            lineaProductoId = new LongFilter();
        }
        return lineaProductoId;
    }

    public void setLineaProductoId(LongFilter lineaProductoId) {
        this.lineaProductoId = lineaProductoId;
    }

    public LongFilter getCompraProductoId() {
        return compraProductoId;
    }

    public LongFilter compraProductoId() {
        if (compraProductoId == null) {
            compraProductoId = new LongFilter();
        }
        return compraProductoId;
    }

    public void setCompraProductoId(LongFilter compraProductoId) {
        this.compraProductoId = compraProductoId;
    }

    public LongFilter getVentaProductoId() {
        return ventaProductoId;
    }

    public LongFilter ventaProductoId() {
        if (ventaProductoId == null) {
            ventaProductoId = new LongFilter();
        }
        return ventaProductoId;
    }

    public void setVentaProductoId(LongFilter ventaProductoId) {
        this.ventaProductoId = ventaProductoId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ProductoCriteria that = (ProductoCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nombreProducto, that.nombreProducto) &&
            Objects.equals(cantidad, that.cantidad) &&
            Objects.equals(iva, that.iva) &&
            Objects.equals(precioUdsVenta, that.precioUdsVenta) &&
            Objects.equals(margenDeGanancia, that.margenDeGanancia) &&
            Objects.equals(invima, that.invima) &&
            Objects.equals(presentacionId, that.presentacionId) &&
            Objects.equals(laboratorioId, that.laboratorioId) &&
            Objects.equals(lineaProductoId, that.lineaProductoId) &&
            Objects.equals(compraProductoId, that.compraProductoId) &&
            Objects.equals(ventaProductoId, that.ventaProductoId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            nombreProducto,
            cantidad,
            iva,
            precioUdsVenta,
            margenDeGanancia,
            invima,
            presentacionId,
            laboratorioId,
            lineaProductoId,
            compraProductoId,
            ventaProductoId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductoCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (nombreProducto != null ? "nombreProducto=" + nombreProducto + ", " : "") +
            (cantidad != null ? "cantidad=" + cantidad + ", " : "") +
            (iva != null ? "iva=" + iva + ", " : "") +
            (precioUdsVenta != null ? "precioUdsVenta=" + precioUdsVenta + ", " : "") +
            (margenDeGanancia != null ? "margenDeGanancia=" + margenDeGanancia + ", " : "") +
            (invima != null ? "invima=" + invima + ", " : "") +
            (presentacionId != null ? "presentacionId=" + presentacionId + ", " : "") +
            (laboratorioId != null ? "laboratorioId=" + laboratorioId + ", " : "") +
            (lineaProductoId != null ? "lineaProductoId=" + lineaProductoId + ", " : "") +
            (compraProductoId != null ? "compraProductoId=" + compraProductoId + ", " : "") +
            (ventaProductoId != null ? "ventaProductoId=" + ventaProductoId + ", " : "") +
            "}";
    }
}
