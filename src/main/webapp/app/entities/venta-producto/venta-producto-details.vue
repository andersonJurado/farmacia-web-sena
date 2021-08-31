<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="ventaProducto">
        <h2 class="jh-entity-heading" data-cy="ventaProductoDetailsHeading">
          <span v-text="$t('farmaciaApp.ventaProducto.detail.title')">VentaProducto</span> {{ ventaProducto.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('farmaciaApp.ventaProducto.cantidad')">Cantidad</span>
          </dt>
          <dd>
            <span>{{ ventaProducto.cantidad }}</span>
          </dd>
          <dt>
            <span v-text="$t('farmaciaApp.ventaProducto.total')">Total</span>
          </dt>
          <dd>
            <span>{{ ventaProducto.total }}</span>
          </dd>
          <dt>
            <span v-text="$t('farmaciaApp.ventaProducto.producto')">Producto</span>
          </dt>
          <dd>
            <div v-if="ventaProducto.producto">
              <router-link :to="{ name: 'ProductoView', params: { productoId: ventaProducto.producto.id } }">{{
                ventaProducto.producto.nombreProducto
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span v-text="$t('farmaciaApp.ventaProducto.venta')">Venta</span>
          </dt>
          <dd>
            <div v-if="ventaProducto.venta">
              <router-link :to="{ name: 'VentaView', params: { ventaId: ventaProducto.venta.id } }">{{
                ventaProducto.venta.nroFactura
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
        <router-link
          v-if="ventaProducto.id"
          :to="{ name: 'VentaProductoEdit', params: { ventaProductoId: ventaProducto.id } }"
          custom
          v-slot="{ navigate }"
        >
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.edit')"> Edit</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./venta-producto-details.component.ts"></script>
