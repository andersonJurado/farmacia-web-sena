package org.norn.farmacia.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link org.norn.farmacia.domain.Proveedor} entity.
 */
public class ProveedorDTO implements Serializable {

    private Long id;

    @NotNull
    private String nombre;

    private String primerTelefono;

    private String segundoTelefono;

    private MunicipioDTO municpio;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPrimerTelefono() {
        return primerTelefono;
    }

    public void setPrimerTelefono(String primerTelefono) {
        this.primerTelefono = primerTelefono;
    }

    public String getSegundoTelefono() {
        return segundoTelefono;
    }

    public void setSegundoTelefono(String segundoTelefono) {
        this.segundoTelefono = segundoTelefono;
    }

    public MunicipioDTO getMunicpio() {
        return municpio;
    }

    public void setMunicpio(MunicipioDTO municpio) {
        this.municpio = municpio;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProveedorDTO)) {
            return false;
        }

        ProveedorDTO proveedorDTO = (ProveedorDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, proveedorDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProveedorDTO{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", primerTelefono='" + getPrimerTelefono() + "'" +
            ", segundoTelefono='" + getSegundoTelefono() + "'" +
            ", municpio=" + getMunicpio() +
            "}";
    }
}
