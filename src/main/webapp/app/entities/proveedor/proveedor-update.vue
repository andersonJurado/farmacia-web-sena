<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="farmaciaApp.proveedor.home.createOrEditLabel"
          data-cy="ProveedorCreateUpdateHeading"
          v-text="$t('farmaciaApp.proveedor.home.createOrEditLabel')"
        >
          Create or edit a Proveedor
        </h2>
        <div>
          <div class="form-group" v-if="proveedor.id">
            <label for="id" v-text="$t('global.field.id')">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="proveedor.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('farmaciaApp.proveedor.nombre')" for="proveedor-nombre">Nombre</label>
            <input
              type="text"
              class="form-control"
              name="nombre"
              id="proveedor-nombre"
              data-cy="nombre"
              :class="{ valid: !$v.proveedor.nombre.$invalid, invalid: $v.proveedor.nombre.$invalid }"
              v-model="$v.proveedor.nombre.$model"
              required
            />
            <div v-if="$v.proveedor.nombre.$anyDirty && $v.proveedor.nombre.$invalid">
              <small class="form-text text-danger" v-if="!$v.proveedor.nombre.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('farmaciaApp.proveedor.primerTelefono')" for="proveedor-primerTelefono"
              >Primer Telefono</label
            >
            <input
              type="text"
              class="form-control"
              name="primerTelefono"
              id="proveedor-primerTelefono"
              data-cy="primerTelefono"
              :class="{ valid: !$v.proveedor.primerTelefono.$invalid, invalid: $v.proveedor.primerTelefono.$invalid }"
              v-model="$v.proveedor.primerTelefono.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('farmaciaApp.proveedor.segundoTelefono')" for="proveedor-segundoTelefono"
              >Segundo Telefono</label
            >
            <input
              type="text"
              class="form-control"
              name="segundoTelefono"
              id="proveedor-segundoTelefono"
              data-cy="segundoTelefono"
              :class="{ valid: !$v.proveedor.segundoTelefono.$invalid, invalid: $v.proveedor.segundoTelefono.$invalid }"
              v-model="$v.proveedor.segundoTelefono.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('farmaciaApp.proveedor.municpio')" for="proveedor-municpio">Municpio</label>
            <select class="form-control" id="proveedor-municpio" data-cy="municpio" name="municpio" v-model="proveedor.municpio">
              <option v-bind:value="null"></option>
              <option
                v-bind:value="proveedor.municpio && municipioOption.id === proveedor.municpio.id ? proveedor.municpio : municipioOption"
                v-for="municipioOption in municipios"
                :key="municipioOption.id"
              >
                {{ municipioOption.nombre }}
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
            :disabled="$v.proveedor.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./proveedor-update.component.ts"></script>
