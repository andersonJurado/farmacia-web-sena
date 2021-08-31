<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="farmaciaApp.departamento.home.createOrEditLabel"
          data-cy="DepartamentoCreateUpdateHeading"
          v-text="$t('farmaciaApp.departamento.home.createOrEditLabel')"
        >
          Create or edit a Departamento
        </h2>
        <div>
          <div class="form-group" v-if="departamento.id">
            <label for="id" v-text="$t('global.field.id')">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="departamento.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('farmaciaApp.departamento.nombre')" for="departamento-nombre">Nombre</label>
            <input
              type="text"
              class="form-control"
              name="nombre"
              id="departamento-nombre"
              data-cy="nombre"
              :class="{ valid: !$v.departamento.nombre.$invalid, invalid: $v.departamento.nombre.$invalid }"
              v-model="$v.departamento.nombre.$model"
              required
            />
            <div v-if="$v.departamento.nombre.$anyDirty && $v.departamento.nombre.$invalid">
              <small class="form-text text-danger" v-if="!$v.departamento.nombre.required" v-text="$t('entity.validation.required')">
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
            :disabled="$v.departamento.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./departamento-update.component.ts"></script>
