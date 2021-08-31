package org.norn.farmacia.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.InstantFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link org.norn.farmacia.domain.Compra} entity. This class is used
 * in {@link org.norn.farmacia.web.rest.CompraResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /compras?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CompraCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nroFactura;

    private InstantFilter fecha;

    private LongFilter compraProductoId;

    private LongFilter proveedorId;

    public CompraCriteria() {}

    public CompraCriteria(CompraCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nroFactura = other.nroFactura == null ? null : other.nroFactura.copy();
        this.fecha = other.fecha == null ? null : other.fecha.copy();
        this.compraProductoId = other.compraProductoId == null ? null : other.compraProductoId.copy();
        this.proveedorId = other.proveedorId == null ? null : other.proveedorId.copy();
    }

    @Override
    public CompraCriteria copy() {
        return new CompraCriteria(this);
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

    public StringFilter getNroFactura() {
        return nroFactura;
    }

    public StringFilter nroFactura() {
        if (nroFactura == null) {
            nroFactura = new StringFilter();
        }
        return nroFactura;
    }

    public void setNroFactura(StringFilter nroFactura) {
        this.nroFactura = nroFactura;
    }

    public InstantFilter getFecha() {
        return fecha;
    }

    public InstantFilter fecha() {
        if (fecha == null) {
            fecha = new InstantFilter();
        }
        return fecha;
    }

    public void setFecha(InstantFilter fecha) {
        this.fecha = fecha;
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

    public LongFilter getProveedorId() {
        return proveedorId;
    }

    public LongFilter proveedorId() {
        if (proveedorId == null) {
            proveedorId = new LongFilter();
        }
        return proveedorId;
    }

    public void setProveedorId(LongFilter proveedorId) {
        this.proveedorId = proveedorId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final CompraCriteria that = (CompraCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nroFactura, that.nroFactura) &&
            Objects.equals(fecha, that.fecha) &&
            Objects.equals(compraProductoId, that.compraProductoId) &&
            Objects.equals(proveedorId, that.proveedorId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nroFactura, fecha, compraProductoId, proveedorId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CompraCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (nroFactura != null ? "nroFactura=" + nroFactura + ", " : "") +
            (fecha != null ? "fecha=" + fecha + ", " : "") +
            (compraProductoId != null ? "compraProductoId=" + compraProductoId + ", " : "") +
            (proveedorId != null ? "proveedorId=" + proveedorId + ", " : "") +
            "}";
    }
}
