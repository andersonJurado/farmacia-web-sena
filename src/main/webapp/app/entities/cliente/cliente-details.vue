<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="cliente">
        <h2 class="jh-entity-heading" data-cy="clienteDetailsHeading">
          <span v-text="$t('farmaciaApp.cliente.detail.title')">Cliente</span> {{ cliente.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('farmaciaApp.cliente.primerNombre')">Primer Nombre</span>
          </dt>
          <dd>
            <span>{{ cliente.primerNombre }}</span>
          </dd>
          <dt>
            <span v-text="$t('farmaciaApp.cliente.segundoNombre')">Segundo Nombre</span>
          </dt>
          <dd>
            <span>{{ cliente.segundoNombre }}</span>
          </dd>
          <dt>
            <span v-text="$t('farmaciaApp.cliente.primerApellido')">Primer Apellido</span>
          </dt>
          <dd>
            <span>{{ cliente.primerApellido }}</span>
          </dd>
          <dt>
            <span v-text="$t('farmaciaApp.cliente.segundoApellido')">Segundo Apellido</span>
          </dt>
          <dd>
            <span>{{ cliente.segundoApellido }}</span>
          </dd>
          <dt>
            <span v-text="$t('farmaciaApp.cliente.primerTelefono')">Primer Telefono</span>
          </dt>
          <dd>
            <span>{{ cliente.primerTelefono }}</span>
          </dd>
          <dt>
            <span v-text="$t('farmaciaApp.cliente.segundoTelefono')">Segundo Telefono</span>
          </dt>
          <dd>
            <span>{{ cliente.segundoTelefono }}</span>
          </dd>
          <dt>
            <span v-text="$t('farmaciaApp.cliente.municipio')">Municipio</span>
          </dt>
          <dd>
            <div v-if="cliente.municipio">
              <router-link :to="{ name: 'MunicipioView', params: { municipioId: cliente.municipio.id } }">{{
                cliente.municipio.nombre
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span v-text="$t('farmaciaApp.cliente.genero')">Genero</span>
          </dt>
          <dd>
            <div v-if="cliente.genero">
              <router-link :to="{ name: 'GeneroView', params: { generoId: cliente.genero.id } }">{{ cliente.genero.nombre }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
        <router-link v-if="cliente.id" :to="{ name: 'ClienteEdit', params: { clienteId: cliente.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.edit')"> Edit</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./cliente-details.component.ts"></script>
