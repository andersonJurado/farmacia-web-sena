<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="farmaciaApp.venta.home.createOrEditLabel"
          data-cy="VentaCreateUpdateHeading"
          v-text="$t('farmaciaApp.venta.home.createOrEditLabel')"
        >
          Create or edit a Venta
        </h2>
        <div>
          <div class="form-group" v-if="venta.id">
            <label for="id" v-text="$t('global.field.id')">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="venta.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('farmaciaApp.venta.nroFactura')" for="venta-nroFactura">Nro Factura</label>
            <input
              type="text"
              class="form-control"
              name="nroFactura"
              id="venta-nroFactura"
              data-cy="nroFactura"
              :class="{ valid: !$v.venta.nroFactura.$invalid, invalid: $v.venta.nroFactura.$invalid }"
              v-model="$v.venta.nroFactura.$model"
              required
            />
            <div v-if="$v.venta.nroFactura.$anyDirty && $v.venta.nroFactura.$invalid">
              <small class="form-text text-danger" v-if="!$v.venta.nroFactura.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('farmaciaApp.venta.fecha')" for="venta-fecha">Fecha</label>
            <div class="d-flex">
              <input
                id="venta-fecha"
                data-cy="fecha"
                type="datetime-local"
                class="form-control"
                name="fecha"
                :class="{ valid: !$v.venta.fecha.$invalid, invalid: $v.venta.fecha.$invalid }"
                :value="convertDateTimeFromServer($v.venta.fecha.$model)"
                @change="updateInstantField('fecha', $event)"
              />
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('farmaciaApp.venta.cliente')" for="venta-cliente">Cliente</label>
            <select class="form-control" id="venta-cliente" data-cy="cliente" name="cliente" v-model="venta.cliente">
              <option v-bind:value="null"></option>
              <option
                v-bind:value="venta.cliente && clienteOption.id === venta.cliente.id ? venta.cliente : clienteOption"
                v-for="clienteOption in clientes"
                :key="clienteOption.id"
              >
                {{ clienteOption.primerNombre }}
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
            :disabled="$v.venta.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./venta-update.component.ts"></script>
