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
 * A Laboratorio.
 */
@Entity
@Table(name = "laboratorio")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Laboratorio implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "nombre", nullable = false)
    private String nombre;

    @OneToMany(mappedBy = "laboratorio")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = { "presentacion", "laboratorio", "lineaProducto", "compraProductos", "ventaProductos" },
        allowSetters = true
    )
    private Set<Producto> productos = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Laboratorio id(Long id) {
        this.id = id;
        return this;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Laboratorio nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Set<Producto> getProductos() {
        return this.productos;
    }

    public Laboratorio productos(Set<Producto> productos) {
        this.setProductos(productos);
        return this;
    }

    public Laboratorio addProducto(Producto producto) {
        this.productos.add(producto);
        producto.setLaboratorio(this);
        return this;
    }

    public Laboratorio removeProducto(Producto producto) {
        this.productos.remove(producto);
        producto.setLaboratorio(null);
        return this;
    }

    public void setProductos(Set<Producto> productos) {
        if (this.productos != null) {
            this.productos.forEach(i -> i.setLaboratorio(null));
        }
        if (productos != null) {
            productos.forEach(i -> i.setLaboratorio(this));
        }
        this.productos = productos;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Laboratorio)) {
            return false;
        }
        return id != null && id.equals(((Laboratorio) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Laboratorio{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            "}";
    }
}
