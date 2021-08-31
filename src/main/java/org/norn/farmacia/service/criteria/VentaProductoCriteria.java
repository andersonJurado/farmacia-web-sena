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
 * Criteria class for the {@link org.norn.farmacia.domain.VentaProducto} entity. This class is used
 * in {@link org.norn.farmacia.web.rest.VentaProductoResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /venta-productos?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class VentaProductoCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter cantidad;

    private DoubleFilter total;

    private LongFilter productoId;

    private LongFilter ventaId;

    public VentaProductoCriteria() {}

    public VentaProductoCriteria(VentaProductoCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.cantidad = other.cantidad == null ? null : other.cantidad.copy();
        this.total = other.total == null ? null : other.total.copy();
        this.productoId = other.productoId == null ? null : other.productoId.copy();
        this.ventaId = other.ventaId == null ? null : other.ventaId.copy();
    }

    @Override
    public VentaProductoCriteria copy() {
        return new VentaProductoCriteria(this);
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

    public LongFilter getVentaId() {
        return ventaId;
    }

    public LongFilter ventaId() {
        if (ventaId == null) {
            ventaId = new LongFilter();
        }
        return ventaId;
    }

    public void setVentaId(LongFilter ventaId) {
        this.ventaId = ventaId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final VentaProductoCriteria that = (VentaProductoCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(cantidad, that.cantidad) &&
            Objects.equals(total, that.total) &&
            Objects.equals(productoId, that.productoId) &&
            Objects.equals(ventaId, that.ventaId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, cantidad, total, productoId, ventaId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VentaProductoCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (cantidad != null ? "cantidad=" + cantidad + ", " : "") +
            (total != null ? "total=" + total + ", " : "") +
            (productoId != null ? "productoId=" + productoId + ", " : "") +
            (ventaId != null ? "ventaId=" + ventaId + ", " : "") +
            "}";
    }
}
