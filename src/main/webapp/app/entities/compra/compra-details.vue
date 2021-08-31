<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="compra">
        <h2 class="jh-entity-heading" data-cy="compraDetailsHeading">
          <span v-text="$t('farmaciaApp.compra.detail.title')">Compra</span> {{ compra.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('farmaciaApp.compra.nroFactura')">Nro Factura</span>
          </dt>
          <dd>
            <span>{{ compra.nroFactura }}</span>
          </dd>
          <dt>
            <span v-text="$t('farmaciaApp.compra.fecha')">Fecha</span>
          </dt>
          <dd>
            <span v-if="compra.fecha">{{ $d(Date.parse(compra.fecha), 'long') }}</span>
          </dd>
          <dt>
            <span v-text="$t('farmaciaApp.compra.proveedor')">Proveedor</span>
          </dt>
          <dd>
            <div v-if="compra.proveedor">
              <router-link :to="{ name: 'ProveedorView', params: { proveedorId: compra.proveedor.id } }">{{
                compra.proveedor.nombre
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
        <router-link v-if="compra.id" :to="{ name: 'CompraEdit', params: { compraId: compra.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.edit')"> Edit</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./compra-details.component.ts"></script>
