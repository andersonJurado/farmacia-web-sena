<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="venta">
        <h2 class="jh-entity-heading" data-cy="ventaDetailsHeading">
          <span v-text="$t('farmaciaApp.venta.detail.title')">Venta</span> {{ venta.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('farmaciaApp.venta.nroFactura')">Nro Factura</span>
          </dt>
          <dd>
            <span>{{ venta.nroFactura }}</span>
          </dd>
          <dt>
            <span v-text="$t('farmaciaApp.venta.fecha')">Fecha</span>
          </dt>
          <dd>
            <span v-if="venta.fecha">{{ $d(Date.parse(venta.fecha), 'long') }}</span>
          </dd>
          <dt>
            <span v-text="$t('farmaciaApp.venta.cliente')">Cliente</span>
          </dt>
          <dd>
            <div v-if="venta.cliente">
              <router-link :to="{ name: 'ClienteView', params: { clienteId: venta.cliente.id } }">{{
                venta.cliente.primerNombre
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
        <router-link v-if="venta.id" :to="{ name: 'VentaEdit', params: { ventaId: venta.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.edit')"> Edit</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./venta-details.component.ts"></script>
