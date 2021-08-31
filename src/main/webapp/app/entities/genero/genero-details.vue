<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="genero">
        <h2 class="jh-entity-heading" data-cy="generoDetailsHeading">
          <span v-text="$t('farmaciaApp.genero.detail.title')">Genero</span> {{ genero.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('farmaciaApp.genero.nombre')">Nombre</span>
          </dt>
          <dd>
            <span>{{ genero.nombre }}</span>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
        <router-link v-if="genero.id" :to="{ name: 'GeneroEdit', params: { generoId: genero.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.edit')"> Edit</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./genero-details.component.ts"></script>
