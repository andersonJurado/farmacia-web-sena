<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="producto">
        <h2 class="jh-entity-heading" data-cy="productoDetailsHeading">
          <span v-text="$t('farmaciaApp.producto.detail.title')">Producto</span> {{ producto.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('farmaciaApp.producto.nombreProducto')">Nombre Producto</span>
          </dt>
          <dd>
            <span>{{ producto.nombreProducto }}</span>
          </dd>
          <dt>
            <span v-text="$t('farmaciaApp.producto.cantidad')">Cantidad</span>
          </dt>
          <dd>
            <span>{{ producto.cantidad }}</span>
          </dd>
          <dt>
            <span v-text="$t('farmaciaApp.producto.iva')">Iva</span>
          </dt>
          <dd>
            <span>{{ producto.iva }}</span>
          </dd>
          <dt>
            <span v-text="$t('farmaciaApp.producto.precioUdsVenta')">Precio Uds Venta</span>
          </dt>
          <dd>
            <span>{{ producto.precioUdsVenta }}</span>
          </dd>
          <dt>
            <span v-text="$t('farmaciaApp.producto.margenDeGanancia')">Margen De Ganancia</span>
          </dt>
          <dd>
            <span>{{ producto.margenDeGanancia }}</span>
          </dd>
          <dt>
            <span v-text="$t('farmaciaApp.producto.invima')">Invima</span>
          </dt>
          <dd>
            <span>{{ producto.invima }}</span>
          </dd>
          <dt>
            <span v-text="$t('farmaciaApp.producto.presentacion')">Presentacion</span>
          </dt>
          <dd>
            <div v-if="producto.presentacion">
              <router-link :to="{ name: 'PresentacionView', params: { presentacionId: producto.presentacion.id } }">{{
                producto.presentacion.nombre
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span v-text="$t('farmaciaApp.producto.laboratorio')">Laboratorio</span>
          </dt>
          <dd>
            <div v-if="producto.laboratorio">
              <router-link :to="{ name: 'LaboratorioView', params: { laboratorioId: producto.laboratorio.id } }">{{
                producto.laboratorio.nombre
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span v-text="$t('farmaciaApp.producto.lineaProducto')">Linea Producto</span>
          </dt>
          <dd>
            <div v-if="producto.lineaProducto">
              <router-link :to="{ name: 'LineaProductoView', params: { lineaProductoId: producto.lineaProducto.id } }">{{
                producto.lineaProducto.nombre
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
        <router-link v-if="producto.id" :to="{ name: 'ProductoEdit', params: { productoId: producto.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.edit')"> Edit</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./producto-details.component.ts"></script>
