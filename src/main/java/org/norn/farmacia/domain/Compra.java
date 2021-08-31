package org.norn.farmacia.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Compra.
 */
@Entity
@Table(name = "compra")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Compra implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "nro_factura", nullable = false)
    private String nroFactura;

    @Column(name = "fecha")
    private Instant fecha;

    @OneToMany(mappedBy = "compra")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "producto", "compra" }, allowSetters = true)
    private Set<CompraProducto> compraProductos = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "municpio", "compras" }, allowSetters = true)
    private Proveedor proveedor;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Compra id(Long id) {
        this.id = id;
        return this;
    }

    public String getNroFactura() {
        return this.nroFactura;
    }

    public Compra nroFactura(String nroFactura) {
        this.nroFactura = nroFactura;
        return this;
    }

    public void setNroFactura(String nroFactura) {
        this.nroFactura = nroFactura;
    }

    public Instant getFecha() {
        return this.fecha;
    }

    public Compra fecha(Instant fecha) {
        this.fecha = fecha;
        return this;
    }

    public void setFecha(Instant fecha) {
        this.fecha = fecha;
    }

    public Set<CompraProducto> getCompraProductos() {
        return this.compraProductos;
    }

    public Compra compraProductos(Set<CompraProducto> compraProductos) {
        this.setCompraProductos(compraProductos);
        return this;
    }

    public Compra addCompraProducto(CompraProducto compraProducto) {
        this.compraProductos.add(compraProducto);
        compraProducto.setCompra(this);
        return this;
    }

    public Compra removeCompraProducto(CompraProducto compraProducto) {
        this.compraProductos.remove(compraProducto);
        compraProducto.setCompra(null);
        return this;
    }

    public void setCompraProductos(Set<CompraProducto> compraProductos) {
        if (this.compraProductos != null) {
            this.compraProductos.forEach(i -> i.setCompra(null));
        }
        if (compraProductos != null) {
            compraProductos.forEach(i -> i.setCompra(this));
        }
        this.compraProductos = compraProductos;
    }

    public Proveedor getProveedor() {
        return this.proveedor;
    }

    public Compra proveedor(Proveedor proveedor) {
        this.setProveedor(proveedor);
        return this;
    }

    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Compra)) {
            return false;
        }
        return id != null && id.equals(((Compra) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Compra{" +
            "id=" + getId() +
            ", nroFactura='" + getNroFactura() + "'" +
            ", fecha='" + getFecha() + "'" +
            "}";
    }
}
