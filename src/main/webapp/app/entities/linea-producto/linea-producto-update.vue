<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="farmaciaApp.lineaProducto.home.createOrEditLabel"
          data-cy="LineaProductoCreateUpdateHeading"
          v-text="$t('farmaciaApp.lineaProducto.home.createOrEditLabel')"
        >
          Create or edit a LineaProducto
        </h2>
        <div>
          <div class="form-group" v-if="lineaProducto.id">
            <label for="id" v-text="$t('global.field.id')">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="lineaProducto.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('farmaciaApp.lineaProducto.nombre')" for="linea-producto-nombre">Nombre</label>
            <input
              type="text"
              class="form-control"
              name="nombre"
              id="linea-producto-nombre"
              data-cy="nombre"
              :class="{ valid: !$v.lineaProducto.nombre.$invalid, invalid: $v.lineaProducto.nombre.$invalid }"
              v-model="$v.lineaProducto.nombre.$model"
              required
            />
            <div v-if="$v.lineaProducto.nombre.$anyDirty && $v.lineaProducto.nombre.$invalid">
              <small class="form-text text-danger" v-if="!$v.lineaProducto.nombre.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
            </div>
          </div>
        </div>
        <div>
          <button type="button" id="cancel-save" data-cy="entityCreateCancelButton" class="btn btn-secondary" v-on:click="previousState()">
            <font-awesome-icon icon="ban"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.cancel')">Cancel</span>
          </button>
          <button
            type="submit"
            id="save-entity"
            data-cy="entityCreateSaveButton"
            :disabled="$v.lineaProducto.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./linea-producto-update.component.ts"></script>
