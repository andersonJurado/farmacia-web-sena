<template>
  <div>
    <h2 id="page-heading" data-cy="CompraProductoHeading">
      <span v-text="$t('farmaciaApp.compraProducto.home.title')" id="compra-producto-heading">Compra Productos</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="$t('farmaciaApp.compraProducto.home.refreshListLabel')">Refresh List</span>
        </button>
        <router-link :to="{ name: 'CompraProductoCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-compra-producto"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="$t('farmaciaApp.compraProducto.home.createLabel')"> Create a new Compra Producto </span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && compraProductos && compraProductos.length === 0">
      <span v-text="$t('farmaciaApp.compraProducto.home.notFound')">No compraProductos found</span>
    </div>
    <div class="table-responsive" v-if="compraProductos && compraProductos.length > 0">
      <table class="table table-striped" aria-describedby="compraProductos">
        <thead>
          <tr>
            <th scope="row" v-on:click="changeOrder('id')">
              <span v-text="$t('global.field.id')">ID</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('cantidadUds')">
              <span v-text="$t('farmaciaApp.compraProducto.cantidadUds')">Cantidad Uds</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'cantidadUds'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('precioUdsCompra')">
              <span v-text="$t('farmaciaApp.compraProducto.precioUdsCompra')">Precio Uds Compra</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'precioUdsCompra'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('subTotal')">
              <span v-text="$t('farmaciaApp.compraProducto.subTotal')">Sub Total</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'subTotal'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('iva')">
              <span v-text="$t('farmaciaApp.compraProducto.iva')">Iva</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'iva'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('total')">
              <span v-text="$t('farmaciaApp.compraProducto.total')">Total</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'total'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('fechaVencimiento')">
              <span v-text="$t('farmaciaApp.compraProducto.fechaVencimiento')">Fecha Vencimiento</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'fechaVencimiento'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('lote')">
              <span v-text="$t('farmaciaApp.compraProducto.lote')">Lote</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'lote'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('producto.nombreProducto')">
              <span v-text="$t('farmaciaApp.compraProducto.producto')">Producto</span>
              <jhi-sort-indicator
                :current-order="propOrder"
                :reverse="reverse"
                :field-name="'producto.nombreProducto'"
              ></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('compra.nroFactura')">
              <span v-text="$t('farmaciaApp.compraProducto.compra')">Compra</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'compra.nroFactura'"></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="compraProducto in compraProductos" :key="compraProducto.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'CompraProductoView', params: { compraProductoId: compraProducto.id } }">{{
                compraProducto.id
              }}</router-link>
            </td>
            <td>{{ compraProducto.cantidadUds }}</td>
            <td>{{ compraProducto.precioUdsCompra }}</td>
            <td>{{ compraProducto.subTotal }}</td>
            <td>{{ compraProducto.iva }}</td>
            <td>{{ compraProducto.total }}</td>
            <td>{{ compraProducto.fechaVencimiento }}</td>
            <td>{{ compraProducto.lote }}</td>
            <td>
              <div v-if="compraProducto.producto">
                <router-link :to="{ name: 'ProductoView', params: { productoId: compraProducto.producto.id } }">{{
                  compraProducto.producto.nombreProducto
                }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="compraProducto.compra">
                <router-link :to="{ name: 'CompraView', params: { compraId: compraProducto.compra.id } }">{{
                  compraProducto.compra.nroFactura
                }}</router-link>
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link
                  :to="{ name: 'CompraProductoView', params: { compraProductoId: compraProducto.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.view')">View</span>
                  </button>
                </router-link>
                <router-link
                  :to="{ name: 'CompraProductoEdit', params: { compraProductoId: compraProducto.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.edit')">Edit</span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(compraProducto)"
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
        ><span
          id="farmaciaApp.compraProducto.delete.question"
          data-cy="compraProductoDeleteDialogHeading"
          v-text="$t('entity.delete.title')"
          >Confirm delete operation</span
        ></span
      >
      <div class="modal-body">
        <p id="jhi-delete-compraProducto-heading" v-text="$t('farmaciaApp.compraProducto.delete.question', { id: removeId })">
          Are you sure you want to delete this Compra Producto?
        </p>
      </div>
      <div slot="modal-footer">
        <button type="button" class="btn btn-secondary" v-text="$t('entity.action.cancel')" v-on:click="closeDialog()">Cancel</button>
        <button
          type="button"
          class="btn btn-primary"
          id="jhi-confirm-delete-compraProducto"
          data-cy="entityConfirmDeleteButton"
          v-text="$t('entity.action.delete')"
          v-on:click="removeCompraProducto()"
        >
          Delete
        </button>
      </div>
    </b-modal>
    <div v-show="compraProductos && compraProductos.length > 0">
      <div class="row justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :itemsPerPage="itemsPerPage"></jhi-item-count>
      </div>
      <div class="row justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage" :change="loadPage(page)"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./compra-producto.component.ts"></script>
