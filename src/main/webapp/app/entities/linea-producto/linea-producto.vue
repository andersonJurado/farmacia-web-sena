<template>
  <div>
    <h2 id="page-heading" data-cy="LineaProductoHeading">
      <span v-text="$t('farmaciaApp.lineaProducto.home.title')" id="linea-producto-heading">Linea Productos</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="$t('farmaciaApp.lineaProducto.home.refreshListLabel')">Refresh List</span>
        </button>
        <router-link :to="{ name: 'LineaProductoCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-linea-producto"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="$t('farmaciaApp.lineaProducto.home.createLabel')"> Create a new Linea Producto </span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && lineaProductos && lineaProductos.length === 0">
      <span v-text="$t('farmaciaApp.lineaProducto.home.notFound')">No lineaProductos found</span>
    </div>
    <div class="table-responsive" v-if="lineaProductos && lineaProductos.length > 0">
      <table class="table table-striped" aria-describedby="lineaProductos">
        <thead>
          <tr>
            <th scope="row" v-on:click="changeOrder('id')">
              <span v-text="$t('global.field.id')">ID</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('nombre')">
              <span v-text="$t('farmaciaApp.lineaProducto.nombre')">Nombre</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'nombre'"></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="lineaProducto in lineaProductos" :key="lineaProducto.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'LineaProductoView', params: { lineaProductoId: lineaProducto.id } }">{{
                lineaProducto.id
              }}</router-link>
            </td>
            <td>{{ lineaProducto.nombre }}</td>
            <td class="text-right">
              <div class="btn-group">
                <router-link
                  :to="{ name: 'LineaProductoView', params: { lineaProductoId: lineaProducto.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.view')">View</span>
                  </button>
                </router-link>
                <router-link
                  :to="{ name: 'LineaProductoEdit', params: { lineaProductoId: lineaProducto.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.edit')">Edit</span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(lineaProducto)"
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
        ><span id="farmaciaApp.lineaProducto.delete.question" data-cy="lineaProductoDeleteDialogHeading" v-text="$t('entity.delete.title')"
          >Confirm delete operation</span
        ></span
      >
      <div class="modal-body">
        <p id="jhi-delete-lineaProducto-heading" v-text="$t('farmaciaApp.lineaProducto.delete.question', { id: removeId })">
          Are you sure you want to delete this Linea Producto?
        </p>
      </div>
      <div slot="modal-footer">
        <button type="button" class="btn btn-secondary" v-text="$t('entity.action.cancel')" v-on:click="closeDialog()">Cancel</button>
        <button
          type="button"
          class="btn btn-primary"
          id="jhi-confirm-delete-lineaProducto"
          data-cy="entityConfirmDeleteButton"
          v-text="$t('entity.action.delete')"
          v-on:click="removeLineaProducto()"
        >
          Delete
        </button>
      </div>
    </b-modal>
    <div v-show="lineaProductos && lineaProductos.length > 0">
      <div class="row justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :itemsPerPage="itemsPerPage"></jhi-item-count>
      </div>
      <div class="row justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage" :change="loadPage(page)"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./linea-producto.component.ts"></script>
