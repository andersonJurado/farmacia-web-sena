import { Authority } from '@/shared/security/authority';
/* tslint:disable */
// prettier-ignore

// prettier-ignore
const Cliente = () => import('@/entities/cliente/cliente.vue');
// prettier-ignore
const ClienteUpdate = () => import('@/entities/cliente/cliente-update.vue');
// prettier-ignore
const ClienteDetails = () => import('@/entities/cliente/cliente-details.vue');
// prettier-ignore
const Compra = () => import('@/entities/compra/compra.vue');
// prettier-ignore
const CompraUpdate = () => import('@/entities/compra/compra-update.vue');
// prettier-ignore
const CompraDetails = () => import('@/entities/compra/compra-details.vue');
// prettier-ignore
const CompraProducto = () => import('@/entities/compra-producto/compra-producto.vue');
// prettier-ignore
const CompraProductoUpdate = () => import('@/entities/compra-producto/compra-producto-update.vue');
// prettier-ignore
const CompraProductoDetails = () => import('@/entities/compra-producto/compra-producto-details.vue');
// prettier-ignore
const Departamento = () => import('@/entities/departamento/departamento.vue');
// prettier-ignore
const DepartamentoUpdate = () => import('@/entities/departamento/departamento-update.vue');
// prettier-ignore
const DepartamentoDetails = () => import('@/entities/departamento/departamento-details.vue');
// prettier-ignore
const Laboratorio = () => import('@/entities/laboratorio/laboratorio.vue');
// prettier-ignore
const LaboratorioUpdate = () => import('@/entities/laboratorio/laboratorio-update.vue');
// prettier-ignore
const LaboratorioDetails = () => import('@/entities/laboratorio/laboratorio-details.vue');
// prettier-ignore
const LineaProducto = () => import('@/entities/linea-producto/linea-producto.vue');
// prettier-ignore
const LineaProductoUpdate = () => import('@/entities/linea-producto/linea-producto-update.vue');
// prettier-ignore
const LineaProductoDetails = () => import('@/entities/linea-producto/linea-producto-details.vue');
// prettier-ignore
const Municipio = () => import('@/entities/municipio/municipio.vue');
// prettier-ignore
const MunicipioUpdate = () => import('@/entities/municipio/municipio-update.vue');
// prettier-ignore
const MunicipioDetails = () => import('@/entities/municipio/municipio-details.vue');
// prettier-ignore
const Presentacion = () => import('@/entities/presentacion/presentacion.vue');
// prettier-ignore
const PresentacionUpdate = () => import('@/entities/presentacion/presentacion-update.vue');
// prettier-ignore
const PresentacionDetails = () => import('@/entities/presentacion/presentacion-details.vue');
// prettier-ignore
const Producto = () => import('@/entities/producto/producto.vue');
// prettier-ignore
const ProductoUpdate = () => import('@/entities/producto/producto-update.vue');
// prettier-ignore
const ProductoDetails = () => import('@/entities/producto/producto-details.vue');
// prettier-ignore
const Proveedor = () => import('@/entities/proveedor/proveedor.vue');
// prettier-ignore
const ProveedorUpdate = () => import('@/entities/proveedor/proveedor-update.vue');
// prettier-ignore
const ProveedorDetails = () => import('@/entities/proveedor/proveedor-details.vue');
// prettier-ignore
const Genero = () => import('@/entities/genero/genero.vue');
// prettier-ignore
const GeneroUpdate = () => import('@/entities/genero/genero-update.vue');
// prettier-ignore
const GeneroDetails = () => import('@/entities/genero/genero-details.vue');
// prettier-ignore
const Venta = () => import('@/entities/venta/venta.vue');
// prettier-ignore
const VentaUpdate = () => import('@/entities/venta/venta-update.vue');
// prettier-ignore
const VentaDetails = () => import('@/entities/venta/venta-details.vue');
// prettier-ignore
const VentaProducto = () => import('@/entities/venta-producto/venta-producto.vue');
// prettier-ignore
const VentaProductoUpdate = () => import('@/entities/venta-producto/venta-producto-update.vue');
// prettier-ignore
const VentaProductoDetails = () => import('@/entities/venta-producto/venta-producto-details.vue');
// jhipster-needle-add-entity-to-router-import - JHipster will import entities to the router here

export default [
  {
    path: '/cliente',
    name: 'Cliente',
    component: Cliente,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/cliente/new',
    name: 'ClienteCreate',
    component: ClienteUpdate,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/cliente/:clienteId/edit',
    name: 'ClienteEdit',
    component: ClienteUpdate,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/cliente/:clienteId/view',
    name: 'ClienteView',
    component: ClienteDetails,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/compra',
    name: 'Compra',
    component: Compra,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/compra/new',
    name: 'CompraCreate',
    component: CompraUpdate,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/compra/:compraId/edit',
    name: 'CompraEdit',
    component: CompraUpdate,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/compra/:compraId/view',
    name: 'CompraView',
    component: CompraDetails,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/compra-producto',
    name: 'CompraProducto',
    component: CompraProducto,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/compra-producto/new',
    name: 'CompraProductoCreate',
    component: CompraProductoUpdate,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/compra-producto/:compraProductoId/edit',
    name: 'CompraProductoEdit',
    component: CompraProductoUpdate,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/compra-producto/:compraProductoId/view',
    name: 'CompraProductoView',
    component: CompraProductoDetails,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/departamento',
    name: 'Departamento',
    component: Departamento,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/departamento/new',
    name: 'DepartamentoCreate',
    component: DepartamentoUpdate,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/departamento/:departamentoId/edit',
    name: 'DepartamentoEdit',
    component: DepartamentoUpdate,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/departamento/:departamentoId/view',
    name: 'DepartamentoView',
    component: DepartamentoDetails,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/laboratorio',
    name: 'Laboratorio',
    component: Laboratorio,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/laboratorio/new',
    name: 'LaboratorioCreate',
    component: LaboratorioUpdate,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/laboratorio/:laboratorioId/edit',
    name: 'LaboratorioEdit',
    component: LaboratorioUpdate,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/laboratorio/:laboratorioId/view',
    name: 'LaboratorioView',
    component: LaboratorioDetails,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/linea-producto',
    name: 'LineaProducto',
    component: LineaProducto,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/linea-producto/new',
    name: 'LineaProductoCreate',
    component: LineaProductoUpdate,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/linea-producto/:lineaProductoId/edit',
    name: 'LineaProductoEdit',
    component: LineaProductoUpdate,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/linea-producto/:lineaProductoId/view',
    name: 'LineaProductoView',
    component: LineaProductoDetails,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/municipio',
    name: 'Municipio',
    component: Municipio,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/municipio/new',
    name: 'MunicipioCreate',
    component: MunicipioUpdate,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/municipio/:municipioId/edit',
    name: 'MunicipioEdit',
    component: MunicipioUpdate,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/municipio/:municipioId/view',
    name: 'MunicipioView',
    component: MunicipioDetails,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/presentacion',
    name: 'Presentacion',
    component: Presentacion,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/presentacion/new',
    name: 'PresentacionCreate',
    component: PresentacionUpdate,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/presentacion/:presentacionId/edit',
    name: 'PresentacionEdit',
    component: PresentacionUpdate,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/presentacion/:presentacionId/view',
    name: 'PresentacionView',
    component: PresentacionDetails,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/producto',
    name: 'Producto',
    component: Producto,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/producto/new',
    name: 'ProductoCreate',
    component: ProductoUpdate,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/producto/:productoId/edit',
    name: 'ProductoEdit',
    component: ProductoUpdate,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/producto/:productoId/view',
    name: 'ProductoView',
    component: ProductoDetails,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/proveedor',
    name: 'Proveedor',
    component: Proveedor,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/proveedor/new',
    name: 'ProveedorCreate',
    component: ProveedorUpdate,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/proveedor/:proveedorId/edit',
    name: 'ProveedorEdit',
    component: ProveedorUpdate,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/proveedor/:proveedorId/view',
    name: 'ProveedorView',
    component: ProveedorDetails,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/genero',
    name: 'Genero',
    component: Genero,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/genero/new',
    name: 'GeneroCreate',
    component: GeneroUpdate,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/genero/:generoId/edit',
    name: 'GeneroEdit',
    component: GeneroUpdate,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/genero/:generoId/view',
    name: 'GeneroView',
    component: GeneroDetails,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/venta',
    name: 'Venta',
    component: Venta,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/venta/new',
    name: 'VentaCreate',
    component: VentaUpdate,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/venta/:ventaId/edit',
    name: 'VentaEdit',
    component: VentaUpdate,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/venta/:ventaId/view',
    name: 'VentaView',
    component: VentaDetails,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/venta-producto',
    name: 'VentaProducto',
    component: VentaProducto,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/venta-producto/new',
    name: 'VentaProductoCreate',
    component: VentaProductoUpdate,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/venta-producto/:ventaProductoId/edit',
    name: 'VentaProductoEdit',
    component: VentaProductoUpdate,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/venta-producto/:ventaProductoId/view',
    name: 'VentaProductoView',
    component: VentaProductoDetails,
    meta: { authorities: [Authority.USER] },
  },
  // jhipster-needle-add-entity-to-router - JHipster will add entities to the router here
];
