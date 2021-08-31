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
 * Criteria class for the {@link org.norn.farmacia.domain.Proveedor} entity. This class is used
 * in {@link org.norn.farmacia.web.rest.ProveedorResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /proveedors?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ProveedorCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nombre;

    private StringFilter primerTelefono;

    private StringFilter segundoTelefono;

    private LongFilter municpioId;

    private LongFilter compraId;

    public ProveedorCriteria() {}

    public ProveedorCriteria(ProveedorCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nombre = other.nombre == null ? null : other.nombre.copy();
        this.primerTelefono = other.primerTelefono == null ? null : other.primerTelefono.copy();
        this.segundoTelefono = other.segundoTelefono == null ? null : other.segundoTelefono.copy();
        this.municpioId = other.municpioId == null ? null : other.municpioId.copy();
        this.compraId = other.compraId == null ? null : other.compraId.copy();
    }

    @Override
    public ProveedorCriteria copy() {
        return new ProveedorCriteria(this);
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

    public StringFilter getPrimerTelefono() {
        return primerTelefono;
    }

    public StringFilter primerTelefono() {
        if (primerTelefono == null) {
            primerTelefono = new StringFilter();
        }
        return primerTelefono;
    }

    public void setPrimerTelefono(StringFilter primerTelefono) {
        this.primerTelefono = primerTelefono;
    }

    public StringFilter getSegundoTelefono() {
        return segundoTelefono;
    }

    public StringFilter segundoTelefono() {
        if (segundoTelefono == null) {
            segundoTelefono = new StringFilter();
        }
        return segundoTelefono;
    }

    public void setSegundoTelefono(StringFilter segundoTelefono) {
        this.segundoTelefono = segundoTelefono;
    }

    public LongFilter getMunicpioId() {
        return municpioId;
    }

    public LongFilter municpioId() {
        if (municpioId == null) {
            municpioId = new LongFilter();
        }
        return municpioId;
    }

    public void setMunicpioId(LongFilter municpioId) {
        this.municpioId = municpioId;
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
        final ProveedorCriteria that = (ProveedorCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nombre, that.nombre) &&
            Objects.equals(primerTelefono, that.primerTelefono) &&
            Objects.equals(segundoTelefono, that.segundoTelefono) &&
            Objects.equals(municpioId, that.municpioId) &&
            Objects.equals(compraId, that.compraId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nombre, primerTelefono, segundoTelefono, municpioId, compraId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProveedorCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (nombre != null ? "nombre=" + nombre + ", " : "") +
            (primerTelefono != null ? "primerTelefono=" + primerTelefono + ", " : "") +
            (segundoTelefono != null ? "segundoTelefono=" + segundoTelefono + ", " : "") +
            (municpioId != null ? "municpioId=" + municpioId + ", " : "") +
            (compraId != null ? "compraId=" + compraId + ", " : "") +
            "}";
    }
}
