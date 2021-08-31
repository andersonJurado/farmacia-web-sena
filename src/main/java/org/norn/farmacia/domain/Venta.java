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
 * A Venta.
 */
@Entity
@Table(name = "venta")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Venta implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "nro_factura", nullable = false)
    private String nroFactura;

    @Column(name = "fecha")
    private Instant fecha;

    @OneToMany(mappedBy = "venta")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "producto", "venta" }, allowSetters = true)
    private Set<VentaProducto> ventaProductos = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "municipio", "genero", "ventas" }, allowSetters = true)
    private Cliente cliente;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Venta id(Long id) {
        this.id = id;
        return this;
    }

    public String getNroFactura() {
        return this.nroFactura;
    }

    public Venta nroFactura(String nroFactura) {
        this.nroFactura = nroFactura;
        return this;
    }

    public void setNroFactura(String nroFactura) {
        this.nroFactura = nroFactura;
    }

    public Instant getFecha() {
        return this.fecha;
    }

    public Venta fecha(Instant fecha) {
        this.fecha = fecha;
        return this;
    }

    public void setFecha(Instant fecha) {
        this.fecha = fecha;
    }

    public Set<VentaProducto> getVentaProductos() {
        return this.ventaProductos;
    }

    public Venta ventaProductos(Set<VentaProducto> ventaProductos) {
        this.setVentaProductos(ventaProductos);
        return this;
    }

    public Venta addVentaProducto(VentaProducto ventaProducto) {
        this.ventaProductos.add(ventaProducto);
        ventaProducto.setVenta(this);
        return this;
    }

    public Venta removeVentaProducto(VentaProducto ventaProducto) {
        this.ventaProductos.remove(ventaProducto);
        ventaProducto.setVenta(null);
        return this;
    }

    public void setVentaProductos(Set<VentaProducto> ventaProductos) {
        if (this.ventaProductos != null) {
            this.ventaProductos.forEach(i -> i.setVenta(null));
        }
        if (ventaProductos != null) {
            ventaProductos.forEach(i -> i.setVenta(this));
        }
        this.ventaProductos = ventaProductos;
    }

    public Cliente getCliente() {
        return this.cliente;
    }

    public Venta cliente(Cliente cliente) {
        this.setCliente(cliente);
        return this;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Venta)) {
            return false;
        }
        return id != null && id.equals(((Venta) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Venta{" +
            "id=" + getId() +
            ", nroFactura='" + getNroFactura() + "'" +
            ", fecha='" + getFecha() + "'" +
            "}";
    }
}
