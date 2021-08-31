<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="compraProducto">
        <h2 class="jh-entity-heading" data-cy="compraProductoDetailsHeading">
          <span v-text="$t('farmaciaApp.compraProducto.detail.title')">CompraProducto</span> {{ compraProducto.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('farmaciaApp.compraProducto.cantidadUds')">Cantidad Uds</span>
          </dt>
          <dd>
            <span>{{ compraProducto.cantidadUds }}</span>
          </dd>
          <dt>
            <span v-text="$t('farmaciaApp.compraProducto.precioUdsCompra')">Precio Uds Compra</span>
          </dt>
          <dd>
            <span>{{ compraProducto.precioUdsCompra }}</span>
          </dd>
          <dt>
            <span v-text="$t('farmaciaApp.compraProducto.subTotal')">Sub Total</span>
          </dt>
          <dd>
            <span>{{ compraProducto.subTotal }}</span>
          </dd>
          <dt>
            <span v-text="$t('farmaciaApp.compraProducto.iva')">Iva</span>
          </dt>
          <dd>
            <span>{{ compraProducto.iva }}</span>
          </dd>
          <dt>
            <span v-text="$t('farmaciaApp.compraProducto.total')">Total</span>
          </dt>
          <dd>
            <span>{{ compraProducto.total }}</span>
          </dd>
          <dt>
            <span v-text="$t('farmaciaApp.compraProducto.fechaVencimiento')">Fecha Vencimiento</span>
          </dt>
          <dd>
            <span>{{ compraProducto.fechaVencimiento }}</span>
          </dd>
          <dt>
            <span v-text="$t('farmaciaApp.compraProducto.lote')">Lote</span>
          </dt>
          <dd>
            <span>{{ compraProducto.lote }}</span>
          </dd>
          <dt>
            <span v-text="$t('farmaciaApp.compraProducto.producto')">Producto</span>
          </dt>
          <dd>
            <div v-if="compraProducto.producto">
              <router-link :to="{ name: 'ProductoView', params: { productoId: compraProducto.producto.id } }">{{
                compraProducto.producto.nombreProducto
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span v-text="$t('farmaciaApp.compraProducto.compra')">Compra</span>
          </dt>
          <dd>
            <div v-if="compraProducto.compra">
              <router-link :to="{ name: 'CompraView', params: { compraId: compraProducto.compra.id } }">{{
                compraProducto.compra.nroFactura
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
        <router-link
          v-if="compraProducto.id"
          :to="{ name: 'CompraProductoEdit', params: { compraProductoId: compraProducto.id } }"
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

<script lang="ts" src="./compra-producto-details.component.ts"></script>
