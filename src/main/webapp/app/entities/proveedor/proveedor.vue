<template>
  <div>
    <h2 id="page-heading" data-cy="ProveedorHeading">
      <span v-text="$t('farmaciaApp.proveedor.home.title')" id="proveedor-heading">Proveedors</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="$t('farmaciaApp.proveedor.home.refreshListLabel')">Refresh List</span>
        </button>
        <router-link :to="{ name: 'ProveedorCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-proveedor"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="$t('farmaciaApp.proveedor.home.createLabel')"> Create a new Proveedor </span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && proveedors && proveedors.length === 0">
      <span v-text="$t('farmaciaApp.proveedor.home.notFound')">No proveedors found</span>
    </div>
    <div class="table-responsive" v-if="proveedors && proveedors.length > 0">
      <table class="table table-striped" aria-describedby="proveedors">
        <thead>
          <tr>
            <th scope="row" v-on:click="changeOrder('id')">
              <span v-text="$t('global.field.id')">ID</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('nombre')">
              <span v-text="$t('farmaciaApp.proveedor.nombre')">Nombre</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'nombre'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('primerTelefono')">
              <span v-text="$t('farmaciaApp.proveedor.primerTelefono')">Primer Telefono</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'primerTelefono'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('segundoTelefono')">
              <span v-text="$t('farmaciaApp.proveedor.segundoTelefono')">Segundo Telefono</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'segundoTelefono'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('municpio.nombre')">
              <span v-text="$t('farmaciaApp.proveedor.municpio')">Municpio</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'municpio.nombre'"></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="proveedor in proveedors" :key="proveedor.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'ProveedorView', params: { proveedorId: proveedor.id } }">{{ proveedor.id }}</router-link>
            </td>
            <td>{{ proveedor.nombre }}</td>
            <td>{{ proveedor.primerTelefono }}</td>
            <td>{{ proveedor.segundoTelefono }}</td>
            <td>
              <div v-if="proveedor.municpio">
                <router-link :to="{ name: 'MunicipioView', params: { municipioId: proveedor.municpio.id } }">{{
                  proveedor.municpio.nombre
                }}</router-link>
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'ProveedorView', params: { proveedorId: proveedor.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.view')">View</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'ProveedorEdit', params: { proveedorId: proveedor.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.edit')">Edit</span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(proveedor)"
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
        ><span id="farmaciaApp.proveedor.delete.question" data-cy="proveedorDeleteDialogHeading" v-text="$t('entity.delete.title')"
          >Confirm delete operation</span
        ></span
      >
      <div class="modal-body">
        <p id="jhi-delete-proveedor-heading" v-text="$t('farmaciaApp.proveedor.delete.question', { id: removeId })">
          Are you sure you want to delete this Proveedor?
        </p>
      </div>
      <div slot="modal-footer">
        <button type="button" class="btn btn-secondary" v-text="$t('entity.action.cancel')" v-on:click="closeDialog()">Cancel</button>
        <button
          type="button"
          class="btn btn-primary"
          id="jhi-confirm-delete-proveedor"
          data-cy="entityConfirmDeleteButton"
          v-text="$t('entity.action.delete')"
          v-on:click="removeProveedor()"
        >
          Delete
        </button>
      </div>
    </b-modal>
    <div v-show="proveedors && proveedors.length > 0">
      <div class="row justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :itemsPerPage="itemsPerPage"></jhi-item-count>
      </div>
      <div class="row justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage" :change="loadPage(page)"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./proveedor.component.ts"></script>
