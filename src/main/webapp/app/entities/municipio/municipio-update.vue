<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="farmaciaApp.municipio.home.createOrEditLabel"
          data-cy="MunicipioCreateUpdateHeading"
          v-text="$t('farmaciaApp.municipio.home.createOrEditLabel')"
        >
          Create or edit a Municipio
        </h2>
        <div>
          <div class="form-group" v-if="municipio.id">
            <label for="id" v-text="$t('global.field.id')">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="municipio.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('farmaciaApp.municipio.nombre')" for="municipio-nombre">Nombre</label>
            <input
              type="text"
              class="form-control"
              name="nombre"
              id="municipio-nombre"
              data-cy="nombre"
              :class="{ valid: !$v.municipio.nombre.$invalid, invalid: $v.municipio.nombre.$invalid }"
              v-model="$v.municipio.nombre.$model"
              required
            />
            <div v-if="$v.municipio.nombre.$anyDirty && $v.municipio.nombre.$invalid">
              <small class="form-text text-danger" v-if="!$v.municipio.nombre.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('farmaciaApp.municipio.departamento')" for="municipio-departamento"
              >Departamento</label
            >
            <select
              class="form-control"
              id="municipio-departamento"
              data-cy="departamento"
              name="departamento"
              v-model="municipio.departamento"
            >
              <option v-bind:value="null"></option>
              <option
                v-bind:value="
                  municipio.departamento && departamentoOption.id === municipio.departamento.id
                    ? municipio.departamento
                    : departamentoOption
                "
                v-for="departamentoOption in departamentos"
                :key="departamentoOption.id"
              >
                {{ departamentoOption.nombre }}
              </option>
            </select>
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
            :disabled="$v.municipio.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./municipio-update.component.ts"></script>
