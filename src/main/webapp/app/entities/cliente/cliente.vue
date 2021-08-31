<template>
  <div>
    <h2 id="page-heading" data-cy="ClienteHeading">
      <span v-text="$t('farmaciaApp.cliente.home.title')" id="cliente-heading">Clientes</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="$t('farmaciaApp.cliente.home.refreshListLabel')">Refresh List</span>
        </button>
        <router-link :to="{ name: 'ClienteCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-cliente"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="$t('farmaciaApp.cliente.home.createLabel')"> Create a new Cliente </span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && clientes && clientes.length === 0">
      <span v-text="$t('farmaciaApp.cliente.home.notFound')">No clientes found</span>
    </div>
    <div class="table-responsive" v-if="clientes && clientes.length > 0">
      <table class="table table-striped" aria-describedby="clientes">
        <thead>
          <tr>
            <th scope="row" v-on:click="changeOrder('id')">
              <span v-text="$t('global.field.id')">ID</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('primerNombre')">
              <span v-text="$t('farmaciaApp.cliente.primerNombre')">Primer Nombre</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'primerNombre'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('segundoNombre')">
              <span v-text="$t('farmaciaApp.cliente.segundoNombre')">Segundo Nombre</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'segundoNombre'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('primerApellido')">
              <span v-text="$t('farmaciaApp.cliente.primerApellido')">Primer Apellido</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'primerApellido'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('segundoApellido')">
              <span v-text="$t('farmaciaApp.cliente.segundoApellido')">Segundo Apellido</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'segundoApellido'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('primerTelefono')">
              <span v-text="$t('farmaciaApp.cliente.primerTelefono')">Primer Telefono</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'primerTelefono'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('segundoTelefono')">
              <span v-text="$t('farmaciaApp.cliente.segundoTelefono')">Segundo Telefono</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'segundoTelefono'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('municipio.nombre')">
              <span v-text="$t('farmaciaApp.cliente.municipio')">Municipio</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'municipio.nombre'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('genero.nombre')">
              <span v-text="$t('farmaciaApp.cliente.genero')">Genero</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'genero.nombre'"></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="cliente in clientes" :key="cliente.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'ClienteView', params: { clienteId: cliente.id } }">{{ cliente.id }}</router-link>
            </td>
            <td>{{ cliente.primerNombre }}</td>
            <td>{{ cliente.segundoNombre }}</td>
            <td>{{ cliente.primerApellido }}</td>
            <td>{{ cliente.segundoApellido }}</td>
            <td>{{ cliente.primerTelefono }}</td>
            <td>{{ cliente.segundoTelefono }}</td>
            <td>
              <div v-if="cliente.municipio">
                <router-link :to="{ name: 'MunicipioView', params: { municipioId: cliente.municipio.id } }">{{
                  cliente.municipio.nombre
                }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="cliente.genero">
                <router-link :to="{ name: 'GeneroView', params: { generoId: cliente.genero.id } }">{{ cliente.genero.nombre }}</router-link>
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'ClienteView', params: { clienteId: cliente.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.view')">View</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'ClienteEdit', params: { clienteId: cliente.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.edit')">Edit</span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(cliente)"
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
        ><span id="farmaciaApp.cliente.delete.question" data-cy="clienteDeleteDialogHeading" v-text="$t('entity.delete.title')"
          >Confirm delete operation</span
        ></span
      >
      <div class="modal-body">
        <p id="jhi-delete-cliente-heading" v-text="$t('farmaciaApp.cliente.delete.question', { id: removeId })">
          Are you sure you want to delete this Cliente?
        </p>
      </div>
      <div slot="modal-footer">
        <button type="button" class="btn btn-secondary" v-text="$t('entity.action.cancel')" v-on:click="closeDialog()">Cancel</button>
        <button
          type="button"
          class="btn btn-primary"
          id="jhi-confirm-delete-cliente"
          data-cy="entityConfirmDeleteButton"
          v-text="$t('entity.action.delete')"
          v-on:click="removeCliente()"
        >
          Delete
        </button>
      </div>
    </b-modal>
    <div v-show="clientes && clientes.length > 0">
      <div class="row justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :itemsPerPage="itemsPerPage"></jhi-item-count>
      </div>
      <div class="row justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage" :change="loadPage(page)"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./cliente.component.ts"></script>
