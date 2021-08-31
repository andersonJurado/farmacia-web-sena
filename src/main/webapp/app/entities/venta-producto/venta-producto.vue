<template>
  <div>
    <h2 id="page-heading" data-cy="VentaProductoHeading">
      <span v-text="$t('farmaciaApp.ventaProducto.home.title')" id="venta-producto-heading">Venta Productos</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="$t('farmaciaApp.ventaProducto.home.refreshListLabel')">Refresh List</span>
        </button>
        <router-link :to="{ name: 'VentaProductoCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-venta-producto"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="$t('farmaciaApp.ventaProducto.home.createLabel')"> Create a new Venta Producto </span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && ventaProductos && ventaProductos.length === 0">
      <span v-text="$t('farmaciaApp.ventaProducto.home.notFound')">No ventaProductos found</span>
    </div>
    <div class="table-responsive" v-if="ventaProductos && ventaProductos.length > 0">
      <table class="table table-striped" aria-describedby="ventaProductos">
        <thead>
          <tr>
            <th scope="row" v-on:click="changeOrder('id')">
              <span v-text="$t('global.field.id')">ID</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('cantidad')">
              <span v-text="$t('farmaciaApp.ventaProducto.cantidad')">Cantidad</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'cantidad'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('total')">
              <span v-text="$t('farmaciaApp.ventaProducto.total')">Total</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'total'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('producto.nombreProducto')">
              <span v-text="$t('farmaciaApp.ventaProducto.producto')">Producto</span>
              <jhi-sort-indicator
                :current-order="propOrder"
                :reverse="reverse"
                :field-name="'producto.nombreProducto'"
              ></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('venta.nroFactura')">
              <span v-text="$t('farmaciaApp.ventaProducto.venta')">Venta</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'venta.nroFactura'"></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="ventaProducto in ventaProductos" :key="ventaProducto.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'VentaProductoView', params: { ventaProductoId: ventaProducto.id } }">{{
                ventaProducto.id
              }}</router-link>
            </td>
            <td>{{ ventaProducto.cantidad }}</td>
            <td>{{ ventaProducto.total }}</td>
            <td>
              <div v-if="ventaProducto.producto">
                <router-link :to="{ name: 'ProductoView', params: { productoId: ventaProducto.producto.id } }">{{
                  ventaProducto.producto.nombreProducto
                }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="ventaProducto.venta">
                <router-link :to="{ name: 'VentaView', params: { ventaId: ventaProducto.venta.id } }">{{
                  ventaProducto.venta.nroFactura
                }}</router-link>
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link
                  :to="{ name: 'VentaProductoView', params: { ventaProductoId: ventaProducto.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.view')">View</span>
                  </button>
                </router-link>
                <router-link
                  :to="{ name: 'VentaProductoEdit', params: { ventaProductoId: ventaProducto.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.edit')">Edit</span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(ventaProducto)"
                  variant="danger"
                  class="btn btn-sm"
                  data-cy="entityDeleteButton"
                  v-b-modal.removeEntity
                >
                  <font-awesome-icon icon="times"></font-awesome-icon>
                  <span class="d-none d-md-inline" v-text="$t('entity.action.delete')">Delete</span>
                </b-button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <b-modal ref="removeEntity" id="removeEntity">
      <span slot="modal-title"
        ><span id="farmaciaApp.ventaProducto.delete.question" data-cy="ventaProductoDeleteDialogHeading" v-text="$t('entity.delete.title')"
          >Confirm delete operation</span
        ></span
      >
      <div class="modal-body">
        <p id="jhi-delete-ventaProducto-heading" v-text="$t('farmaciaApp.ventaProducto.delete.question', { id: removeId })">
          Are you sure you want to delete this Venta Producto?
        </p>
      </div>
      <div slot="modal-footer">
        <button type="button" class="btn btn-secondary" v-text="$t('entity.action.cancel')" v-on:click="closeDialog()">Cancel</button>
        <button
          type="button"
          class="btn btn-primary"
          id="jhi-confirm-delete-ventaProducto"
          data-cy="entityConfirmDeleteButton"
          v-text="$t('entity.action.delete')"
          v-on:click="removeVentaProducto()"
        >
          Delete
        </button>
      </div>
    </b-modal>
    <div v-show="ventaProductos && ventaProductos.length > 0">
      <div class="row justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :itemsPerPage="itemsPerPage"></jhi-item-count>
      </div>
      <div class="row justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage" :change="loadPage(page)"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./venta-producto.component.ts"></script>
