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
 * Criteria class for the {@link org.norn.farmacia.domain.Municipio} entity. This class is used
 * in {@link org.norn.farmacia.web.rest.MunicipioResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /municipios?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class MunicipioCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nombre;

    private LongFilter departamentoId;

    private LongFilter clienteId;

    private LongFilter proveedorId;

    public MunicipioCriteria() {}

    public MunicipioCriteria(MunicipioCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nombre = other.nombre == null ? null : other.nombre.copy();
        this.departamentoId = other.departamentoId == null ? null : other.departamentoId.copy();
        this.clienteId = other.clienteId == null ? null : other.clienteId.copy();
        this.proveedorId = other.proveedorId == null ? null : other.proveedorId.copy();
    }

    @Override
    public MunicipioCriteria copy() {
        return new MunicipioCriteria(this);
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

    public StringFilter getNombre() {
        return nombre;
    }

    public StringFilter nombre() {
        if (nombre == null) {
            nombre = new StringFilter();
        }
        return nombre;
    }

    public void setNombre(StringFilter nombre) {
        this.nombre = nombre;
    }

    public LongFilter getDepartamentoId() {
        return departamentoId;
    }

    public LongFilter departamentoId() {
        if (departamentoId == null) {
            departamentoId = new LongFilter();
        }
        return departamentoId;
    }

    public void setDepartamentoId(LongFilter departamentoId) {
        this.departamentoId = departamentoId;
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
        final MunicipioCriteria that = (MunicipioCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nombre, that.nombre) &&
            Objects.equals(departamentoId, that.departamentoId) &&
            Objects.equals(clienteId, that.clienteId) &&
            Objects.equals(proveedorId, that.proveedorId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nombre, departamentoId, clienteId, proveedorId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MunicipioCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (nombre != null ? "nombre=" + nombre + ", " : "") +
            (departamentoId != null ? "departamentoId=" + departamentoId + ", " : "") +
            (clienteId != null ? "clienteId=" + clienteId + ", " : "") +
            (proveedorId != null ? "proveedorId=" + proveedorId + ", " : "") +
            "}";
    }
}
