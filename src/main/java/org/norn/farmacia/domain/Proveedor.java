package org.norn.farmacia.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Proveedor.
 */
@Entity
@Table(name = "proveedor")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Proveedor implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "primer_telefono")
    private String primerTelefono;

    @Column(name = "segundo_telefono")
    private String segundoTelefono;

    @ManyToOne
    @JsonIgnoreProperties(value = { "departamento", "clientes", "proveedors" }, allowSetters = true)
    private Municipio municpio;

    @OneToMany(mappedBy = "proveedor")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "compraProductos", "proveedor" }, allowSetters = true)
    private Set<Compra> compras = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Proveedor id(Long id) {
        this.id = id;
        return this;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Proveedor nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPrimerTelefono() {
        return this.primerTelefono;
    }

    public Proveedor primerTelefono(String primerTelefono) {
        this.primerTelefono = primerTelefono;
        return this;
    }

    public void setPrimerTelefono(String primerTelefono) {
        this.primerTelefono = primerTelefono;
    }

    public String getSegundoTelefono() {
        return this.segundoTelefono;
    }

    public Proveedor segundoTelefono(String segundoTelefono) {
        this.segundoTelefono = segundoTelefono;
        return this;
    }

    public void setSegundoTelefono(String segundoTelefono) {
        this.segundoTelefono = segundoTelefono;
    }

    public Municipio getMunicpio() {
        return this.municpio;
    }

    public Proveedor municpio(Municipio municipio) {
        this.setMunicpio(municipio);
        return this;
    }

    public void setMunicpio(Municipio municipio) {
        this.municpio = municipio;
    }

    public Set<Compra> getCompras() {
        return this.compras;
    }

    public Proveedor compras(Set<Compra> compras) {
        this.setCompras(compras);
        return this;
    }

    public Proveedor addCompra(Compra compra) {
        this.compras.add(compra);
        compra.setProveedor(this);
        return this;
    }

    public Proveedor removeCompra(Compra compra) {
        this.compras.remove(compra);
        compra.setProveedor(null);
        return this;
    }

    public void setCompras(Set<Compra> compras) {
        if (this.compras != null) {
            this.compras.forEach(i -> i.setProveedor(null));
        }
        if (compras != null) {
            compras.forEach(i -> i.setProveedor(this));
        }
        this.compras = compras;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Proveedor)) {
            return false;
        }
        return id != null && id.equals(((Proveedor) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Proveedor{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", primerTelefono='" + getPrimerTelefono() + "'" +
            ", segundoTelefono='" + getSegundoTelefono() + "'" +
            "}";
    }
}
