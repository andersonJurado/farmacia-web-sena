<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="farmaciaApp.producto.home.createOrEditLabel"
          data-cy="ProductoCreateUpdateHeading"
          v-text="$t('farmaciaApp.producto.home.createOrEditLabel')"
        >
          Create or edit a Producto
        </h2>
        <div>
          <div class="form-group" v-if="producto.id">
            <label for="id" v-text="$t('global.field.id')">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="producto.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('farmaciaApp.producto.nombreProducto')" for="producto-nombreProducto"
              >Nombre Producto</label
            >
            <input
              type="text"
              class="form-control"
              name="nombreProducto"
              id="producto-nombreProducto"
              data-cy="nombreProducto"
              :class="{ valid: !$v.producto.nombreProducto.$invalid, invalid: $v.producto.nombreProducto.$invalid }"
              v-model="$v.producto.nombreProducto.$model"
              required
            />
            <div v-if="$v.producto.nombreProducto.$anyDirty && $v.producto.nombreProducto.$invalid">
              <small class="form-text text-danger" v-if="!$v.producto.nombreProducto.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('farmaciaApp.producto.cantidad')" for="producto-cantidad">Cantidad</label>
            <input
              type="number"
              class="form-control"
              name="cantidad"
              id="producto-cantidad"
              data-cy="cantidad"
              :class="{ valid: !$v.producto.cantidad.$invalid, invalid: $v.producto.cantidad.$invalid }"
              v-model.number="$v.producto.cantidad.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('farmaciaApp.producto.iva')" for="producto-iva">Iva</label>
            <input
              type="number"
              class="form-control"
              name="iva"
              id="producto-iva"
              data-cy="iva"
              :class="{ valid: !$v.producto.iva.$invalid, invalid: $v.producto.iva.$invalid }"
              v-model.number="$v.producto.iva.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('farmaciaApp.producto.precioUdsVenta')" for="producto-precioUdsVenta"
              >Precio Uds Venta</label
            >
            <input
              type="number"
              class="form-control"
              name="precioUdsVenta"
              id="producto-precioUdsVenta"
              data-cy="precioUdsVenta"
              :class="{ valid: !$v.producto.precioUdsVenta.$invalid, invalid: $v.producto.precioUdsVenta.$invalid }"
              v-model.number="$v.producto.precioUdsVenta.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('farmaciaApp.producto.margenDeGanancia')" for="producto-margenDeGanancia"
              >Margen De Ganancia</label
            >
            <input
              type="number"
              class="form-control"
              name="margenDeGanancia"
              id="producto-margenDeGanancia"
              data-cy="margenDeGanancia"
              :class="{ valid: !$v.producto.margenDeGanancia.$invalid, invalid: $v.producto.margenDeGanancia.$invalid }"
              v-model.number="$v.producto.margenDeGanancia.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('farmaciaApp.producto.invima')" for="producto-invima">Invima</label>
            <input
              type="text"
              class="form-control"
              name="invima"
              id="producto-invima"
              data-cy="invima"
              :class="{ valid: !$v.producto.invima.$invalid, invalid: $v.producto.invima.$invalid }"
              v-model="$v.producto.invima.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('farmaciaApp.producto.presentacion')" for="producto-presentacion"
              >Presentacion</label
            >
            <select
              class="form-control"
              id="producto-presentacion"
              data-cy="presentacion"
              name="presentacion"
              v-model="producto.presentacion"
            >
              <option v-bind:value="null"></option>
              <option
                v-bind:value="
                  producto.presentacion && presentacionOption.id === producto.presentacion.id ? producto.presentacion : presentacionOption
                "
                v-for="presentacionOption in presentacions"
                :key="presentacionOption.id"
              >
                {{ presentacionOption.nombre }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('farmaciaApp.producto.laboratorio')" for="producto-laboratorio">Laboratorio</label>
            <select class="form-control" id="producto-laboratorio" data-cy="laboratorio" name="laboratorio" v-model="producto.laboratorio">
              <option v-bind:value="null"></option>
              <option
                v-bind:value="
                  producto.laboratorio && laboratorioOption.id === producto.laboratorio.id ? producto.laboratorio : laboratorioOption
                "
                v-for="laboratorioOption in laboratorios"
                :key="laboratorioOption.id"
              >
                {{ laboratorioOption.nombre }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('farmaciaApp.producto.lineaProducto')" for="producto-lineaProducto"
              >Linea Producto</label
            >
            <select
              class="form-control"
              id="producto-lineaProducto"
              data-cy="lineaProducto"
              name="lineaProducto"
              v-model="producto.lineaProducto"
            >
              <option v-bind:value="null"></option>
              <option
                v-bind:value="
                  producto.lineaProducto && lineaProductoOption.id === producto.lineaProducto.id
                    ? producto.lineaProducto
                    : lineaProductoOption
                "
                v-for="lineaProductoOption in lineaProductos"
                :key="lineaProductoOption.id"
              >
                {{ lineaProductoOption.nombre }}
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
            :disabled="$v.producto.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./producto-update.component.ts"></script>
