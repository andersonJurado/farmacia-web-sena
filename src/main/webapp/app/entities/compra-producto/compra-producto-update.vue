<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="farmaciaApp.compraProducto.home.createOrEditLabel"
          data-cy="CompraProductoCreateUpdateHeading"
          v-text="$t('farmaciaApp.compraProducto.home.createOrEditLabel')"
        >
          Create or edit a CompraProducto
        </h2>
        <div>
          <div class="form-group" v-if="compraProducto.id">
            <label for="id" v-text="$t('global.field.id')">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="compraProducto.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('farmaciaApp.compraProducto.cantidadUds')" for="compra-producto-cantidadUds"
              >Cantidad Uds</label
            >
            <input
              type="number"
              class="form-control"
              name="cantidadUds"
              id="compra-producto-cantidadUds"
              data-cy="cantidadUds"
              :class="{ valid: !$v.compraProducto.cantidadUds.$invalid, invalid: $v.compraProducto.cantidadUds.$invalid }"
              v-model.number="$v.compraProducto.cantidadUds.$model"
            />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="$t('farmaciaApp.compraProducto.precioUdsCompra')"
              for="compra-producto-precioUdsCompra"
              >Precio Uds Compra</label
            >
            <input
              type="number"
              class="form-control"
              name="precioUdsCompra"
              id="compra-producto-precioUdsCompra"
              data-cy="precioUdsCompra"
              :class="{ valid: !$v.compraProducto.precioUdsCompra.$invalid, invalid: $v.compraProducto.precioUdsCompra.$invalid }"
              v-model.number="$v.compraProducto.precioUdsCompra.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('farmaciaApp.compraProducto.subTotal')" for="compra-producto-subTotal"
              >Sub Total</label
            >
            <input
              type="number"
              class="form-control"
              name="subTotal"
              id="compra-producto-subTotal"
              data-cy="subTotal"
              :class="{ valid: !$v.compraProducto.subTotal.$invalid, invalid: $v.compraProducto.subTotal.$invalid }"
              v-model.number="$v.compraProducto.subTotal.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('farmaciaApp.compraProducto.iva')" for="compra-producto-iva">Iva</label>
            <input
              type="number"
              class="form-control"
              name="iva"
              id="compra-producto-iva"
              data-cy="iva"
              :class="{ valid: !$v.compraProducto.iva.$invalid, invalid: $v.compraProducto.iva.$invalid }"
              v-model.number="$v.compraProducto.iva.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('farmaciaApp.compraProducto.total')" for="compra-producto-total">Total</label>
            <input
              type="number"
              class="form-control"
              name="total"
              id="compra-producto-total"
              data-cy="total"
              :class="{ valid: !$v.compraProducto.total.$invalid, invalid: $v.compraProducto.total.$invalid }"
              v-model.number="$v.compraProducto.total.$model"
            />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="$t('farmaciaApp.compraProducto.fechaVencimiento')"
              for="compra-producto-fechaVencimiento"
              >Fecha Vencimiento</label
            >
            <b-input-group class="mb-3">
              <b-input-group-prepend>
                <b-form-datepicker
                  aria-controls="compra-producto-fechaVencimiento"
                  v-model="$v.compraProducto.fechaVencimiento.$model"
                  name="fechaVencimiento"
                  class="form-control"
                  :locale="currentLanguage"
                  button-only
                  today-button
                  reset-button
                  close-button
                >
                </b-form-datepicker>
              </b-input-group-prepend>
              <b-form-input
                id="compra-producto-fechaVencimiento"
                data-cy="fechaVencimiento"
                type="text"
                class="form-control"
                name="fechaVencimiento"
                :class="{ valid: !$v.compraProducto.fechaVencimiento.$invalid, invalid: $v.compraProducto.fechaVencimiento.$invalid }"
                v-model="$v.compraProducto.fechaVencimiento.$model"
              />
            </b-input-group>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('farmaciaApp.compraProducto.lote')" for="compra-producto-lote">Lote</label>
            <input
              type="text"
              class="form-control"
              name="lote"
              id="compra-producto-lote"
              data-cy="lote"
              :class="{ valid: !$v.compraProducto.lote.$invalid, invalid: $v.compraProducto.lote.$invalid }"
              v-model="$v.compraProducto.lote.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('farmaciaApp.compraProducto.producto')" for="compra-producto-producto"
              >Producto</label
            >
            <select class="form-control" id="compra-producto-producto" data-cy="producto" name="producto" v-model="compraProducto.producto">
              <option v-bind:value="null"></option>
              <option
                v-bind:value="
                  compraProducto.producto && productoOption.id === compraProducto.producto.id ? compraProducto.producto : productoOption
                "
                v-for="productoOption in productos"
                :key="productoOption.id"
              >
                {{ productoOption.nombreProducto }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('farmaciaApp.compraProducto.compra')" for="compra-producto-compra">Compra</label>
            <select class="form-control" id="compra-producto-compra" data-cy="compra" name="compra" v-model="compraProducto.compra">
              <option v-bind:value="null"></option>
              <option
                v-bind:value="compraProducto.compra && compraOption.id === compraProducto.compra.id ? compraProducto.compra : compraOption"
                v-for="compraOption in compras"
                :key="compraOption.id"
              >
                {{ compraOption.nroFactura }}
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
            :disabled="$v.compraProducto.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./compra-producto-update.component.ts"></script>
