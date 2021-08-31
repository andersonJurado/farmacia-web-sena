package org.norn.farmacia.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LocalDateFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link org.norn.farmacia.domain.CompraProducto} entity. This class is used
 * in {@link org.norn.farmacia.web.rest.CompraProductoResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /compra-productos?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CompraProductoCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private DoubleFilter cantidadUds;

    private DoubleFilter precioUdsCompra;

    private DoubleFilter subTotal;

    private DoubleFilter iva;

    private DoubleFilter total;

    private LocalDateFilter fechaVencimiento;

    private StringFilter lote;

    private LongFilter productoId;

    private LongFilter compraId;

    public CompraProductoCriteria() {}

    public CompraProductoCriteria(CompraProductoCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.cantidadUds = other.cantidadUds == null ? null : other.cantidadUds.copy();
        this.precioUdsCompra = other.precioUdsCompra == null ? null : other.precioUdsCompra.copy();
        this.subTotal = other.subTotal == null ? null : other.subTotal.copy();
        this.iva = other.iva == null ? null : other.iva.copy();
        this.total = other.total == null ? null : other.total.copy();
        this.fechaVencimiento = other.fechaVencimiento == null ? null : other.fechaVencimiento.copy();
        this.lote = other.lote == null ? null : other.lote.copy();
        this.productoId = other.productoId == null ? null : other.productoId.copy();
        this.compraId = other.compraId == null ? null : other.compraId.copy();
    }

    @Override
    public CompraProductoCriteria copy() {
        return new CompraProductoCriteria(this);
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

    public DoubleFilter getCantidadUds() {
        return cantidadUds;
    }

    public DoubleFilter cantidadUds() {
        if (cantidadUds == null) {
            cantidadUds = new DoubleFilter();
        }
        return cantidadUds;
    }

    public void setCantidadUds(DoubleFilter cantidadUds) {
        this.cantidadUds = cantidadUds;
    }

    public DoubleFilter getPrecioUdsCompra() {
        return precioUdsCompra;
    }

    public DoubleFilter precioUdsCompra() {
        if (precioUdsCompra == null) {
            precioUdsCompra = new DoubleFilter();
        }
        return precioUdsCompra;
    }

    public void setPrecioUdsCompra(DoubleFilter precioUdsCompra) {
        this.precioUdsCompra = precioUdsCompra;
    }

    public DoubleFilter getSubTotal() {
        return subTotal;
    }

    public DoubleFilter subTotal() {
        if (subTotal == null) {
            subTotal = new DoubleFilter();
        }
        return subTotal;
    }

    public void setSubTotal(DoubleFilter subTotal) {
        this.subTotal = subTotal;
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

    public DoubleFilter getTotal() {
        return total;
    }

    public DoubleFilter total() {
        if (total == null) {
            total = new DoubleFilter();
        }
        return total;
    }

    public void setTotal(DoubleFilter total) {
        this.total = total;
    }

    public LocalDateFilter getFechaVencimiento() {
        return fechaVencimiento;
    }

    public LocalDateFilter fechaVencimiento() {
        if (fechaVencimiento == null) {
            fechaVencimiento = new LocalDateFilter();
        }
        return fechaVencimiento;
    }

    public void setFechaVencimiento(LocalDateFilter fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public StringFilter getLote() {
        return lote;
    }

    public StringFilter lote() {
        if (lote == null) {
            lote = new StringFilter();
        }
        return lote;
    }

    public void setLote(StringFilter lote) {
        this.lote = lote;
    }

    public LongFilter getProductoId() {
        return productoId;
    }

    public LongFilter productoId() {
        if (productoId == null) {
            productoId = new LongFilter();
        }
        return productoId;
    }

    public void setProductoId(LongFilter productoId) {
        this.productoId = productoId;
    }

    public LongFilter getCompraId() {
        return compraId;
    }

    public LongFilter compraId() {
        if (compraId == null) {
            compraId = new LongFilter();
        }
        return compraId;
    }

    public void setCompraId(LongFilter compraId) {
        this.compraId = compraId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final CompraProductoCriteria that = (CompraProductoCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(cantidadUds, that.cantidadUds) &&
            Objects.equals(precioUdsCompra, that.precioUdsCompra) &&
            Objects.equals(subTotal, that.subTotal) &&
            Objects.equals(iva, that.iva) &&
            Objects.equals(total, that.total) &&
            Objects.equals(fechaVencimiento, that.fechaVencimiento) &&
            Objects.equals(lote, that.lote) &&
            Objects.equals(productoId, that.productoId) &&
            Objects.equals(compraId, that.compraId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, cantidadUds, precioUdsCompra, subTotal, iva, total, fechaVencimiento, lote, productoId, compraId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CompraProductoCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (cantidadUds != null ? "cantidadUds=" + cantidadUds + ", " : "") +
            (precioUdsCompra != null ? "precioUdsCompra=" + precioUdsCompra + ", " : "") +
            (subTotal != null ? "subTotal=" + subTotal + ", " : "") +
            (iva != null ? "iva=" + iva + ", " : "") +
            (total != null ? "total=" + total + ", " : "") +
            (fechaVencimiento != null ? "fechaVencimiento=" + fechaVencimiento + ", " : "") +
            (lote != null ? "lote=" + lote + ", " : "") +
            (productoId != null ? "productoId=" + productoId + ", " : "") +
            (compraId != null ? "compraId=" + compraId + ", " : "") +
            "}";
    }
}
