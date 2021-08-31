<template>
  <div>
    <h2 id="page-heading" data-cy="PresentacionHeading">
      <span v-text="$t('farmaciaApp.presentacion.home.title')" id="presentacion-heading">Presentacions</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="$t('farmaciaApp.presentacion.home.refreshListLabel')">Refresh List</span>
        </button>
        <router-link :to="{ name: 'PresentacionCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-presentacion"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="$t('farmaciaApp.presentacion.home.createLabel')"> Create a new Presentacion </span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && presentacions && presentacions.length === 0">
      <span v-text="$t('farmaciaApp.presentacion.home.notFound')">No presentacions found</span>
    </div>
    <div class="table-responsive" v-if="presentacions && presentacions.length > 0">
      <table class="table table-striped" aria-describedby="presentacions">
        <thead>
          <tr>
            <th scope="row" v-on:click="changeOrder('id')">
              <span v-text="$t('global.field.id')">ID</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('nombre')">
              <span v-text="$t('farmaciaApp.presentacion.nombre')">Nombre</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'nombre'"></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="presentacion in presentacions" :key="presentacion.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'PresentacionView', params: { presentacionId: presentacion.id } }">{{
                presentacion.id
              }}</router-link>
            </td>
            <td>{{ presentacion.nombre }}</td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'PresentacionView', params: { presentacionId: presentacion.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.view')">View</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'PresentacionEdit', params: { presentacionId: presentacion.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.edit')">Edit</span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(presentacion)"
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
        ><span id="farmaciaApp.presentacion.delete.question" data-cy="presentacionDeleteDialogHeading" v-text="$t('entity.delete.title')"
          >Confirm delete operation</span
        ></span
      >
      <div class="modal-body">
        <p id="jhi-delete-presentacion-heading" v-text="$t('farmaciaApp.presentacion.delete.question', { id: removeId })">
          Are you sure you want to delete this Presentacion?
        </p>
      </div>
      <div slot="modal-footer">
        <button type="button" class="btn btn-secondary" v-text="$t('entity.action.cancel')" v-on:click="closeDialog()">Cancel</button>
        <button
          type="button"
          class="btn btn-primary"
          id="jhi-confirm-delete-presentacion"
          data-cy="entityConfirmDeleteButton"
          v-text="$t('entity.action.delete')"
          v-on:click="removePresentacion()"
        >
          Delete
        </button>
      </div>
    </b-modal>
    <div v-show="presentacions && presentacions.length > 0">
      <div class="row justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :itemsPerPage="itemsPerPage"></jhi-item-count>
      </div>
      <div class="row justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage" :change="loadPage(page)"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./presentacion.component.ts"></script>
