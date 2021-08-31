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
 * A Municipio.
 */
@Entity
@Table(name = "municipio")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Municipio implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "nombre", nullable = false)
    private String nombre;

    @ManyToOne
    @JsonIgnoreProperties(value = { "municipios" }, allowSetters = true)
    private Departamento departamento;

    @OneToMany(mappedBy = "municipio")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "municipio", "genero", "ventas" }, allowSetters = true)
    private Set<Cliente> clientes = new HashSet<>();

    @OneToMany(mappedBy = "municpio")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "municpio", "compras" }, allowSetters = true)
    private Set<Proveedor> proveedors = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Municipio id(Long id) {
        this.id = id;
        return this;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Municipio nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Departamento getDepartamento() {
        return this.departamento;
    }

    public Municipio departamento(Departamento departamento) {
        this.setDepartamento(departamento);
        return this;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }

    public Set<Cliente> getClientes() {
        return this.clientes;
    }

    public Municipio clientes(Set<Cliente> clientes) {
        this.setClientes(clientes);
        return this;
    }

    public Municipio addCliente(Cliente cliente) {
        this.clientes.add(cliente);
        cliente.setMunicipio(this);
        return this;
    }

    public Municipio removeCliente(Cliente cliente) {
        this.clientes.remove(cliente);
        cliente.setMunicipio(null);
        return this;
    }

    public void setClientes(Set<Cliente> clientes) {
        if (this.clientes != null) {
            this.clientes.forEach(i -> i.setMunicipio(null));
        }
        if (clientes != null) {
            clientes.forEach(i -> i.setMunicipio(this));
        }
        this.clientes = clientes;
    }

    public Set<Proveedor> getProveedors() {
        return this.proveedors;
    }

    public Municipio proveedors(Set<Proveedor> proveedors) {
        this.setProveedors(proveedors);
        return this;
    }

    public Municipio addProveedor(Proveedor proveedor) {
        this.proveedors.add(proveedor);
        proveedor.setMunicpio(this);
        return this;
    }

    public Municipio removeProveedor(Proveedor proveedor) {
        this.proveedors.remove(proveedor);
        proveedor.setMunicpio(null);
        return this;
    }

    public void setProveedors(Set<Proveedor> proveedors) {
        if (this.proveedors != null) {
            this.proveedors.forEach(i -> i.setMunicpio(null));
        }
        if (proveedors != null) {
            proveedors.forEach(i -> i.setMunicpio(this));
        }
        this.proveedors = proveedors;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Municipio)) {
            return false;
        }
        return id != null && id.equals(((Municipio) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Municipio{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            "}";
    }
}
