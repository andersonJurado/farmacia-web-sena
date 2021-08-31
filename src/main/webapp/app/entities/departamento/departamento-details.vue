<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="departamento">
        <h2 class="jh-entity-heading" data-cy="departamentoDetailsHeading">
          <span v-text="$t('farmaciaApp.departamento.detail.title')">Departamento</span> {{ departamento.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('farmaciaApp.departamento.nombre')">Nombre</span>
          </dt>
          <dd>
            <span>{{ departamento.nombre }}</span>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
        <router-link
          v-if="departamento.id"
          :to="{ name: 'DepartamentoEdit', params: { departamentoId: departamento.id } }"
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

<script lang="ts" src="./departamento-details.component.ts"></script>
