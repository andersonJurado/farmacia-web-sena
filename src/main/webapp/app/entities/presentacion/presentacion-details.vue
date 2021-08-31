<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="presentacion">
        <h2 class="jh-entity-heading" data-cy="presentacionDetailsHeading">
          <span v-text="$t('farmaciaApp.presentacion.detail.title')">Presentacion</span> {{ presentacion.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('farmaciaApp.presentacion.nombre')">Nombre</span>
          </dt>
          <dd>
            <span>{{ presentacion.nombre }}</span>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
        <router-link
          v-if="presentacion.id"
          :to="{ name: 'PresentacionEdit', params: { presentacionId: presentacion.id } }"
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

<script lang="ts" src="./presentacion-details.component.ts"></script>
