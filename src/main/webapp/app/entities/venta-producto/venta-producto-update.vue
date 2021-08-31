<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="farmaciaApp.ventaProducto.home.createOrEditLabel"
          data-cy="VentaProductoCreateUpdateHeading"
          v-text="$t('farmaciaApp.ventaProducto.home.createOrEditLabel')"
        >
          Create or edit a VentaProducto
        </h2>
        <div>
          <div class="form-group" v-if="ventaProducto.id">
            <label for="id" v-text="$t('global.field.id')">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="ventaProducto.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('farmaciaApp.ventaProducto.cantidad')" for="venta-producto-cantidad"
              >Cantidad</label
            >
            <input
              type="number"
              class="form-control"
              name="cantidad"
              id="venta-producto-cantidad"
              data-cy="cantidad"
              :class="{ valid: !$v.ventaProducto.cantidad.$invalid, invalid: $v.ventaProducto.cantidad.$invalid }"
              v-model.number="$v.ventaProducto.cantidad.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('farmaciaApp.ventaProducto.total')" for="venta-producto-total">Total</label>
            <input
              type="number"
              class="form-control"
              name="total"
              id="venta-producto-total"
              data-cy="total"
              :class="{ valid: !$v.ventaProducto.total.$invalid, invalid: $v.ventaProducto.total.$invalid }"
              v-model.number="$v.ventaProducto.total.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('farmaciaApp.ventaProducto.producto')" for="venta-producto-producto"
              >Producto</label
            >
            <select class="form-control" id="venta-producto-producto" data-cy="producto" name="producto" v-model="ventaProducto.producto">
              <option v-bind:value="null"></option>
              <option
                v-bind:value="
                  ventaProducto.producto && productoOption.id === ventaProducto.producto.id ? ventaProducto.producto : productoOption
                "
                v-for="productoOption in productos"
                :key="productoOption.id"
              >
                {{ productoOption.nombreProducto }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('farmaciaApp.ventaProducto.venta')" for="venta-producto-venta">Venta</label>
            <select class="form-control" id="venta-producto-venta" data-cy="venta" name="venta" v-model="ventaProducto.venta">
              <option v-bind:value="null"></option>
              <option
                v-bind:value="ventaProducto.venta && ventaOption.id === ventaProducto.venta.id ? ventaProducto.venta : ventaOption"
                v-for="ventaOption in ventas"
                :key="ventaOption.id"
              >
                {{ ventaOption.nroFactura }}
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
            :disabled="$v.ventaProducto.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./venta-producto-update.component.ts"></script>
