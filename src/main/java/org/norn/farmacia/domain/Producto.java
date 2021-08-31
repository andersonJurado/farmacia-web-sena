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
 * A Producto.
 */
@Entity
@Table(name = "producto")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Producto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "nombre_producto", nullable = false)
    private String nombreProducto;

    @Column(name = "cantidad")
    private Integer cantidad;

    @Column(name = "iva")
    private Double iva;

    @Column(name = "precio_uds_venta")
    private Double precioUdsVenta;

    @Column(name = "margen_de_ganancia")
    private Double margenDeGanancia;

    @Column(name = "invima")
    private String invima;

    @ManyToOne
    @JsonIgnoreProperties(value = { "productos" }, allowSetters = true)
    private Presentacion presentacion;

    @ManyToOne
    @JsonIgnoreProperties(value = { "productos" }, allowSetters = true)
    private Laboratorio laboratorio;

    @ManyToOne
    @JsonIgnoreProperties(value = { "productos" }, allowSetters = true)
    private LineaProducto lineaProducto;

    @OneToMany(mappedBy = "producto")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "producto", "compra" }, allowSetters = true)
    private Set<CompraProducto> compraProductos = new HashSet<>();

    @OneToMany(mappedBy = "producto")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "producto", "venta" }, allowSetters = true)
    private Set<VentaProducto> ventaProductos = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Producto id(Long id) {
        this.id = id;
        return this;
    }

    public String getNombreProducto() {
        return this.nombreProducto;
    }

    public Producto nombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
        return this;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public Integer getCantidad() {
        return this.cantidad;
    }

    public Producto cantidad(Integer cantidad) {
        this.cantidad = cantidad;
        return this;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Double getIva() {
        return this.iva;
    }

    public Producto iva(Double iva) {
        this.iva = iva;
        return this;
    }

    public void setIva(Double iva) {
        this.iva = iva;
    }

    public Double getPrecioUdsVenta() {
        return this.precioUdsVenta;
    }

    public Producto precioUdsVenta(Double precioUdsVenta) {
        this.precioUdsVenta = precioUdsVenta;
        return this;
    }

    public void setPrecioUdsVenta(Double precioUdsVenta) {
        this.precioUdsVenta = precioUdsVenta;
    }

    public Double getMargenDeGanancia() {
        return this.margenDeGanancia;
    }

    public Producto margenDeGanancia(Double margenDeGanancia) {
        this.margenDeGanancia = margenDeGanancia;
        return this;
    }

    public void setMargenDeGanancia(Double margenDeGanancia) {
        this.margenDeGanancia = margenDeGanancia;
    }

    public String getInvima() {
        return this.invima;
    }

    public Producto invima(String invima) {
        this.invima = invima;
        return this;
    }

    public void setInvima(String invima) {
        this.invima = invima;
    }

    public Presentacion getPresentacion() {
        return this.presentacion;
    }

    public Producto presentacion(Presentacion presentacion) {
        this.setPresentacion(presentacion);
        return this;
    }

    public void setPresentacion(Presentacion presentacion) {
        this.presentacion = presentacion;
    }

    public Laboratorio getLaboratorio() {
        return this.laboratorio;
    }

    public Producto laboratorio(Laboratorio laboratorio) {
        this.setLaboratorio(laboratorio);
        return this;
    }

    public void setLaboratorio(Laboratorio laboratorio) {
        this.laboratorio = laboratorio;
    }

    public LineaProducto getLineaProducto() {
        return this.lineaProducto;
    }

    public Producto lineaProducto(LineaProducto lineaProducto) {
        this.setLineaProducto(lineaProducto);
        return this;
    }

    public void setLineaProducto(LineaProducto lineaProducto) {
        this.lineaProducto = lineaProducto;
    }

    public Set<CompraProducto> getCompraProductos() {
        return this.compraProductos;
    }

    public Producto compraProductos(Set<CompraProducto> compraProductos) {
        this.setCompraProductos(compraProductos);
        return this;
    }

    public Producto addCompraProducto(CompraProducto compraProducto) {
        this.compraProductos.add(compraProducto);
        compraProducto.setProducto(this);
        return this;
    }

    public Producto removeCompraProducto(CompraProducto compraProducto) {
        this.compraProductos.remove(compraProducto);
        compraProducto.setProducto(null);
        return this;
    }

    public void setCompraProductos(Set<CompraProducto> compraProductos) {
        if (this.compraProductos != null) {
            this.compraProductos.forEach(i -> i.setProducto(null));
        }
        if (compraProductos != null) {
            compraProductos.forEach(i -> i.setProducto(this));
        }
        this.compraProductos = compraProductos;
    }

    public Set<VentaProducto> getVentaProductos() {
        return this.ventaProductos;
    }

    public Producto ventaProductos(Set<VentaProducto> ventaProductos) {
        this.setVentaProductos(ventaProductos);
        return this;
    }

    public Producto addVentaProducto(VentaProducto ventaProducto) {
        this.ventaProductos.add(ventaProducto);
        ventaProducto.setProducto(this);
        return this;
    }

    public Producto removeVentaProducto(VentaProducto ventaProducto) {
        this.ventaProductos.remove(ventaProducto);
        ventaProducto.setProducto(null);
        return this;
    }

    public void setVentaProductos(Set<VentaProducto> ventaProductos) {
        if (this.ventaProductos != null) {
            this.ventaProductos.forEach(i -> i.setProducto(null));
        }
        if (ventaProductos != null) {
            ventaProductos.forEach(i -> i.setProducto(this));
        }
        this.ventaProductos = ventaProductos;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Producto)) {
            return false;
        }
        return id != null && id.equals(((Producto) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Producto{" +
            "id=" + getId() +
            ", nombreProducto='" + getNombreProducto() + "'" +
            ", cantidad=" + getCantidad() +
            ", iva=" + getIva() +
            ", precioUdsVenta=" + getPrecioUdsVenta() +
            ", margenDeGanancia=" + getMargenDeGanancia() +
            ", invima='" + getInvima() + "'" +
            "}";
    }
}
