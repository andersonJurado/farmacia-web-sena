<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="farmaciaApp.compra.home.createOrEditLabel"
          data-cy="CompraCreateUpdateHeading"
          v-text="$t('farmaciaApp.compra.home.createOrEditLabel')"
        >
          Create or edit a Compra
        </h2>
        <div>
          <div class="form-group" v-if="compra.id">
            <label for="id" v-text="$t('global.field.id')">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="compra.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('farmaciaApp.compra.nroFactura')" for="compra-nroFactura">Nro Factura</label>
            <input
              type="text"
              class="form-control"
              name="nroFactura"
              id="compra-nroFactura"
              data-cy="nroFactura"
              :class="{ valid: !$v.compra.nroFactura.$invalid, invalid: $v.compra.nroFactura.$invalid }"
              v-model="$v.compra.nroFactura.$model"
              required
            />
            <div v-if="$v.compra.nroFactura.$anyDirty && $v.compra.nroFactura.$invalid">
              <small class="form-text text-danger" v-if="!$v.compra.nroFactura.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('farmaciaApp.compra.fecha')" for="compra-fecha">Fecha</label>
            <div class="d-flex">
              <input
                id="compra-fecha"
                data-cy="fecha"
                type="datetime-local"
                class="form-control"
                name="fecha"
                :class="{ valid: !$v.compra.fecha.$invalid, invalid: $v.compra.fecha.$invalid }"
                :value="convertDateTimeFromServer($v.compra.fecha.$model)"
                @change="updateInstantField('fecha', $event)"
              />
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('farmaciaApp.compra.proveedor')" for="compra-proveedor">Proveedor</label>
            <select class="form-control" id="compra-proveedor" data-cy="proveedor" name="proveedor" v-model="compra.proveedor">
              <option v-bind:value="null"></option>
              <option
                v-bind:value="compra.proveedor && proveedorOption.id === compra.proveedor.id ? compra.proveedor : proveedorOption"
                v-for="proveedorOption in proveedors"
                :key="proveedorOption.id"
              >
                {{ proveedorOption.nombre }}
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
            :disabled="$v.compra.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./compra-update.component.ts"></script>
