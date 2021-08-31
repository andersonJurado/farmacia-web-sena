<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="lineaProducto">
        <h2 class="jh-entity-heading" data-cy="lineaProductoDetailsHeading">
          <span v-text="$t('farmaciaApp.lineaProducto.detail.title')">LineaProducto</span> {{ lineaProducto.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('farmaciaApp.lineaProducto.nombre')">Nombre</span>
          </dt>
          <dd>
            <span>{{ lineaProducto.nombre }}</span>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
        <router-link
          v-if="lineaProducto.id"
          :to="{ name: 'LineaProductoEdit', params: { lineaProductoId: lineaProducto.id } }"
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

<script lang="ts" src="./linea-producto-details.component.ts"></script>
