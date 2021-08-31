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
 * Criteria class for the {@link org.norn.farmacia.domain.Cliente} entity. This class is used
 * in {@link org.norn.farmacia.web.rest.ClienteResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /clientes?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ClienteCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter primerNombre;

    private StringFilter segundoNombre;

    private StringFilter primerApellido;

    private StringFilter segundoApellido;

    private StringFilter primerTelefono;

    private StringFilter segundoTelefono;

    private LongFilter municipioId;

    private LongFilter generoId;

    private LongFilter ventaId;

    public ClienteCriteria() {}

    public ClienteCriteria(ClienteCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.primerNombre = other.primerNombre == null ? null : other.primerNombre.copy();
        this.segundoNombre = other.segundoNombre == null ? null : other.segundoNombre.copy();
        this.primerApellido = other.primerApellido == null ? null : other.primerApellido.copy();
        this.segundoApellido = other.segundoApellido == null ? null : other.segundoApellido.copy();
        this.primerTelefono = other.primerTelefono == null ? null : other.primerTelefono.copy();
        this.segundoTelefono = other.segundoTelefono == null ? null : other.segundoTelefono.copy();
        this.municipioId = other.municipioId == null ? null : other.municipioId.copy();
        this.generoId = other.generoId == null ? null : other.generoId.copy();
        this.ventaId = other.ventaId == null ? null : other.ventaId.copy();
    }

    @Override
    public ClienteCriteria copy() {
        return new ClienteCriteria(this);
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

    public StringFilter getPrimerNombre() {
        return primerNombre;
    }

    public StringFilter primerNombre() {
        if (primerNombre == null) {
            primerNombre = new StringFilter();
        }
        return primerNombre;
    }

    public void setPrimerNombre(StringFilter primerNombre) {
        this.primerNombre = primerNombre;
    }

    public StringFilter getSegundoNombre() {
        return segundoNombre;
    }

    public StringFilter segundoNombre() {
        if (segundoNombre == null) {
            segundoNombre = new StringFilter();
        }
        return segundoNombre;
    }

    public void setSegundoNombre(StringFilter segundoNombre) {
        this.segundoNombre = segundoNombre;
    }

    public StringFilter getPrimerApellido() {
        return primerApellido;
    }

    public StringFilter primerApellido() {
        if (primerApellido == null) {
            primerApellido = new StringFilter();
        }
        return primerApellido;
    }

    public void setPrimerApellido(StringFilter primerApellido) {
        this.primerApellido = primerApellido;
    }

    public StringFilter getSegundoApellido() {
        return segundoApellido;
    }

    public StringFilter segundoApellido() {
        if (segundoApellido == null) {
            segundoApellido = new StringFilter();
        }
        return segundoApellido;
    }

    public void setSegundoApellido(StringFilter segundoApellido) {
        this.segundoApellido = segundoApellido;
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

    public LongFilter getMunicipioId() {
        return municipioId;
    }

    public LongFilter municipioId() {
        if (municipioId == null) {
            municipioId = new LongFilter();
        }
        return municipioId;
    }

    public void setMunicipioId(LongFilter municipioId) {
        this.municipioId = municipioId;
    }

    public LongFilter getGeneroId() {
        return generoId;
    }

    public LongFilter generoId() {
        if (generoId == null) {
            generoId = new LongFilter();
        }
        return generoId;
    }

    public void setGeneroId(LongFilter generoId) {
        this.generoId = generoId;
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
        final ClienteCriteria that = (ClienteCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(primerNombre, that.primerNombre) &&
            Objects.equals(segundoNombre, that.segundoNombre) &&
            Objects.equals(primerApellido, that.primerApellido) &&
            Objects.equals(segundoApellido, that.segundoApellido) &&
            Objects.equals(primerTelefono, that.primerTelefono) &&
            Objects.equals(segundoTelefono, that.segundoTelefono) &&
            Objects.equals(municipioId, that.municipioId) &&
            Objects.equals(generoId, that.generoId) &&
            Objects.equals(ventaId, that.ventaId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            primerNombre,
            segundoNombre,
            primerApellido,
            segundoApellido,
            primerTelefono,
            segundoTelefono,
            municipioId,
            generoId,
            ventaId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ClienteCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (primerNombre != null ? "primerNombre=" + primerNombre + ", " : "") +
            (segundoNombre != null ? "segundoNombre=" + segundoNombre + ", " : "") +
            (primerApellido != null ? "primerApellido=" + primerApellido + ", " : "") +
            (segundoApellido != null ? "segundoApellido=" + segundoApellido + ", " : "") +
            (primerTelefono != null ? "primerTelefono=" + primerTelefono + ", " : "") +
            (segundoTelefono != null ? "segundoTelefono=" + segundoTelefono + ", " : "") +
            (municipioId != null ? "municipioId=" + municipioId + ", " : "") +
            (generoId != null ? "generoId=" + generoId + ", " : "") +
            (ventaId != null ? "ventaId=" + ventaId + ", " : "") +
            "}";
    }
}
