<template>
  <div>
    <h2 id="page-heading" data-cy="VentaHeading">
      <span v-text="$t('farmaciaApp.venta.home.title')" id="venta-heading">Ventas</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="$t('farmaciaApp.venta.home.refreshListLabel')">Refresh List</span>
        </button>
        <router-link :to="{ name: 'VentaCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-venta"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="$t('farmaciaApp.venta.home.createLabel')"> Create a new Venta </span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && ventas && ventas.length === 0">
      <span v-text="$t('farmaciaApp.venta.home.notFound')">No ventas found</span>
    </div>
    <div class="table-responsive" v-if="ventas && ventas.length > 0">
      <table class="table table-striped" aria-describedby="ventas">
        <thead>
          <tr>
            <th scope="row" v-on:click="changeOrder('id')">
              <span v-text="$t('global.field.id')">ID</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('nroFactura')">
              <span v-text="$t('farmaciaApp.venta.nroFactura')">Nro Factura</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'nroFactura'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('fecha')">
              <span v-text="$t('farmaciaApp.venta.fecha')">Fecha</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'fecha'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('cliente.primerNombre')">
              <span v-text="$t('farmaciaApp.venta.cliente')">Cliente</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'cliente.primerNombre'"></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="venta in ventas" :key="venta.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'VentaView', params: { ventaId: venta.id } }">{{ venta.id }}</router-link>
            </td>
            <td>{{ venta.nroFactura }}</td>
            <td>{{ venta.fecha ? $d(Date.parse(venta.fecha), 'short') : '' }}</td>
            <td>
              <div v-if="venta.cliente">
                <router-link :to="{ name: 'ClienteView', params: { clienteId: venta.cliente.id } }">{{
                  venta.cliente.primerNombre
                }}</router-link>
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'VentaView', params: { ventaId: venta.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.view')">View</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'VentaEdit', params: { ventaId: venta.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.edit')">Edit</span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(venta)"
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
        ><span id="farmaciaApp.venta.delete.question" data-cy="ventaDeleteDialogHeading" v-text="$t('entity.delete.title')"
          >Confirm delete operation</span
        ></span
      >
      <div class="modal-body">
        <p id="jhi-delete-venta-heading" v-text="$t('farmaciaApp.venta.delete.question', { id: removeId })">
          Are you sure you want to delete this Venta?
        </p>
      </div>
      <div slot="modal-footer">
        <button type="button" class="btn btn-secondary" v-text="$t('entity.action.cancel')" v-on:click="closeDialog()">Cancel</button>
        <button
          type="button"
          class="btn btn-primary"
          id="jhi-confirm-delete-venta"
          data-cy="entityConfirmDeleteButton"
          v-text="$t('entity.action.delete')"
          v-on:click="removeVenta()"
        >
          Delete
        </button>
      </div>
    </b-modal>
    <div v-show="ventas && ventas.length > 0">
      <div class="row justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :itemsPerPage="itemsPerPage"></jhi-item-count>
      </div>
      <div class="row justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage" :change="loadPage(page)"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./venta.component.ts"></script>
