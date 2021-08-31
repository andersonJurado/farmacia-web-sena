package org.norn.farmacia.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A CompraProducto.
 */
@Entity
@Table(name = "compra_producto")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CompraProducto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cantidad_uds")
    private Double cantidadUds;

    @Column(name = "precio_uds_compra")
    private Double precioUdsCompra;

    @Column(name = "sub_total")
    private Double subTotal;

    @Column(name = "iva")
    private Double iva;

    @Column(name = "total")
    private Double total;

    @Column(name = "fecha_vencimiento")
    private LocalDate fechaVencimiento;

    @Column(name = "lote")
    private String lote;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "presentacion", "laboratorio", "lineaProducto", "compraProductos", "ventaProductos" },
        allowSetters = true
    )
    private Producto producto;

    @ManyToOne
    @JsonIgnoreProperties(value = { "compraProductos", "proveedor" }, allowSetters = true)
    private Compra compra;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CompraProducto id(Long id) {
        this.id = id;
        return this;
    }

    public Double getCantidadUds() {
        return this.cantidadUds;
    }

    public CompraProducto cantidadUds(Double cantidadUds) {
        this.cantidadUds = cantidadUds;
        return this;
    }

    public void setCantidadUds(Double cantidadUds) {
        this.cantidadUds = cantidadUds;
    }

    public Double getPrecioUdsCompra() {
        return this.precioUdsCompra;
    }

    public CompraProducto precioUdsCompra(Double precioUdsCompra) {
        this.precioUdsCompra = precioUdsCompra;
        return this;
    }

    public void setPrecioUdsCompra(Double precioUdsCompra) {
        this.precioUdsCompra = precioUdsCompra;
    }

    public Double getSubTotal() {
        return this.subTotal;
    }

    public CompraProducto subTotal(Double subTotal) {
        this.subTotal = subTotal;
        return this;
    }

    public void setSubTotal(Double subTotal) {
        this.subTotal = subTotal;
    }

    public Double getIva() {
        return this.iva;
    }

    public CompraProducto iva(Double iva) {
        this.iva = iva;
        return this;
    }

    public void setIva(Double iva) {
        this.iva = iva;
    }

    public Double getTotal() {
        return this.total;
    }

    public CompraProducto total(Double total) {
        this.total = total;
        return this;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public LocalDate getFechaVencimiento() {
        return this.fechaVencimiento;
    }

    public CompraProducto fechaVencimiento(LocalDate fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
        return this;
    }

    public void setFechaVencimiento(LocalDate fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public String getLote() {
        return this.lote;
    }

    public CompraProducto lote(String lote) {
        this.lote = lote;
        return this;
    }

    public void setLote(String lote) {
        this.lote = lote;
    }

    public Producto getProducto() {
        return this.producto;
    }

    public CompraProducto producto(Producto producto) {
        this.setProducto(producto);
        return this;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public Compra getCompra() {
        return this.compra;
    }

    public CompraProducto compra(Compra compra) {
        this.setCompra(compra);
        return this;
    }

    public void setCompra(Compra compra) {
        this.compra = compra;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CompraProducto)) {
            return false;
        }
        return id != null && id.equals(((CompraProducto) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CompraProducto{" +
            "id=" + getId() +
            ", cantidadUds=" + getCantidadUds() +
            ", precioUdsCompra=" + getPrecioUdsCompra() +
            ", subTotal=" + getSubTotal() +
            ", iva=" + getIva() +
            ", total=" + getTotal() +
            ", fechaVencimiento='" + getFechaVencimiento() + "'" +
            ", lote='" + getLote() + "'" +
            "}";
    }
}
