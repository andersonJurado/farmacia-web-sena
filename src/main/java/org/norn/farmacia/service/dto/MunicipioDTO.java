package org.norn.farmacia.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link org.norn.farmacia.domain.Municipio} entity.
 */
public class MunicipioDTO implements Serializable {

    private Long id;

    @NotNull
    private String nombre;

    private DepartamentoDTO departamento;

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

    public DepartamentoDTO getDepartamento() {
        return departamento;
    }

    public void setDepartamento(DepartamentoDTO departamento) {
        this.departamento = departamento;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MunicipioDTO)) {
            return false;
        }

        MunicipioDTO municipioDTO = (MunicipioDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, municipioDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MunicipioDTO{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", departamento=" + getDepartamento() +
            "}";
    }
}
