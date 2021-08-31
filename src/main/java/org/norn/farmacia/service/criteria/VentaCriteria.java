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
 * Criteria class for the {@link org.norn.farmacia.domain.Venta} entity. This class is used
 * in {@link org.norn.farmacia.web.rest.VentaResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /ventas?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class VentaCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nroFactura;

    private InstantFilter fecha;

    private LongFilter ventaProductoId;

    private LongFilter clienteId;

    public VentaCriteria() {}

    public VentaCriteria(VentaCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nroFactura = other.nroFactura == null ? null : other.nroFactura.copy();
        this.fecha = other.fecha == null ? null : other.fecha.copy();
        this.ventaProductoId = other.ventaProductoId == null ? null : other.ventaProductoId.copy();
        this.clienteId = other.clienteId == null ? null : other.clienteId.copy();
    }

    @Override
    public VentaCriteria copy() {
        return new VentaCriteria(this);
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

    public LongFilter getClienteId() {
        return clienteId;
    }

    public LongFilter clienteId() {
        if (clienteId == null) {
            clienteId = new LongFilter();
        }
        return clienteId;
    }

    public void setClienteId(LongFilter clienteId) {
        this.clienteId = clienteId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final VentaCriteria that = (VentaCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nroFactura, that.nroFactura) &&
            Objects.equals(fecha, that.fecha) &&
            Objects.equals(ventaProductoId, that.ventaProductoId) &&
            Objects.equals(clienteId, that.clienteId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nroFactura, fecha, ventaProductoId, clienteId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VentaCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (nroFactura != null ? "nroFactura=" + nroFactura + ", " : "") +
            (fecha != null ? "fecha=" + fecha + ", " : "") +
            (ventaProductoId != null ? "ventaProductoId=" + ventaProductoId + ", " : "") +
            (clienteId != null ? "clienteId=" + clienteId + ", " : "") +
            "}";
    }
}
