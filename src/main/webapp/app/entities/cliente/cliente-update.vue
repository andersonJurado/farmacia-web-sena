<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="farmaciaApp.cliente.home.createOrEditLabel"
          data-cy="ClienteCreateUpdateHeading"
          v-text="$t('farmaciaApp.cliente.home.createOrEditLabel')"
        >
          Create or edit a Cliente
        </h2>
        <div>
          <div class="form-group" v-if="cliente.id">
            <label for="id" v-text="$t('global.field.id')">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="cliente.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('farmaciaApp.cliente.primerNombre')" for="cliente-primerNombre"
              >Primer Nombre</label
            >
            <input
              type="text"
              class="form-control"
              name="primerNombre"
              id="cliente-primerNombre"
              data-cy="primerNombre"
              :class="{ valid: !$v.cliente.primerNombre.$invalid, invalid: $v.cliente.primerNombre.$invalid }"
              v-model="$v.cliente.primerNombre.$model"
              required
            />
            <div v-if="$v.cliente.primerNombre.$anyDirty && $v.cliente.primerNombre.$invalid">
              <small class="form-text text-danger" v-if="!$v.cliente.primerNombre.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('farmaciaApp.cliente.segundoNombre')" for="cliente-segundoNombre"
              >Segundo Nombre</label
            >
            <input
              type="text"
              class="form-control"
              name="segundoNombre"
              id="cliente-segundoNombre"
              data-cy="segundoNombre"
              :class="{ valid: !$v.cliente.segundoNombre.$invalid, invalid: $v.cliente.segundoNombre.$invalid }"
              v-model="$v.cliente.segundoNombre.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('farmaciaApp.cliente.primerApellido')" for="cliente-primerApellido"
              >Primer Apellido</label
            >
            <input
              type="text"
              class="form-control"
              name="primerApellido"
              id="cliente-primerApellido"
              data-cy="primerApellido"
              :class="{ valid: !$v.cliente.primerApellido.$invalid, invalid: $v.cliente.primerApellido.$invalid }"
              v-model="$v.cliente.primerApellido.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('farmaciaApp.cliente.segundoApellido')" for="cliente-segundoApellido"
              >Segundo Apellido</label
            >
            <input
              type="text"
              class="form-control"
              name="segundoApellido"
              id="cliente-segundoApellido"
              data-cy="segundoApellido"
              :class="{ valid: !$v.cliente.segundoApellido.$invalid, invalid: $v.cliente.segundoApellido.$invalid }"
              v-model="$v.cliente.segundoApellido.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('farmaciaApp.cliente.primerTelefono')" for="cliente-primerTelefono"
              >Primer Telefono</label
            >
            <input
              type="text"
              class="form-control"
              name="primerTelefono"
              id="cliente-primerTelefono"
              data-cy="primerTelefono"
              :class="{ valid: !$v.cliente.primerTelefono.$invalid, invalid: $v.cliente.primerTelefono.$invalid }"
              v-model="$v.cliente.primerTelefono.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('farmaciaApp.cliente.segundoTelefono')" for="cliente-segundoTelefono"
              >Segundo Telefono</label
            >
            <input
              type="text"
              class="form-control"
              name="segundoTelefono"
              id="cliente-segundoTelefono"
              data-cy="segundoTelefono"
              :class="{ valid: !$v.cliente.segundoTelefono.$invalid, invalid: $v.cliente.segundoTelefono.$invalid }"
              v-model="$v.cliente.segundoTelefono.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('farmaciaApp.cliente.municipio')" for="cliente-municipio">Municipio</label>
            <select class="form-control" id="cliente-municipio" data-cy="municipio" name="municipio" v-model="cliente.municipio">
              <option v-bind:value="null"></option>
              <option
                v-bind:value="cliente.municipio && municipioOption.id === cliente.municipio.id ? cliente.municipio : municipioOption"
                v-for="municipioOption in municipios"
                :key="municipioOption.id"
              >
                {{ municipioOption.nombre }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('farmaciaApp.cliente.genero')" for="cliente-genero">Genero</label>
            <select class="form-control" id="cliente-genero" data-cy="genero" name="genero" v-model="cliente.genero">
              <option v-bind:value="null"></option>
              <option
                v-bind:value="cliente.genero && generoOption.id === cliente.genero.id ? cliente.genero : generoOption"
                v-for="generoOption in generos"
                :key="generoOption.id"
              >
                {{ generoOption.nombre }}
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
            :disabled="$v.cliente.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./cliente-update.component.ts"></script>
