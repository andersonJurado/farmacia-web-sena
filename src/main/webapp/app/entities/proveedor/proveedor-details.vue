<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="proveedor">
        <h2 class="jh-entity-heading" data-cy="proveedorDetailsHeading">
          <span v-text="$t('farmaciaApp.proveedor.detail.title')">Proveedor</span> {{ proveedor.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('farmaciaApp.proveedor.nombre')">Nombre</span>
          </dt>
          <dd>
            <span>{{ proveedor.nombre }}</span>
          </dd>
          <dt>
            <span v-text="$t('farmaciaApp.proveedor.primerTelefono')">Primer Telefono</span>
          </dt>
          <dd>
            <span>{{ proveedor.primerTelefono }}</span>
          </dd>
          <dt>
            <span v-text="$t('farmaciaApp.proveedor.segundoTelefono')">Segundo Telefono</span>
          </dt>
          <dd>
            <span>{{ proveedor.segundoTelefono }}</span>
          </dd>
          <dt>
            <span v-text="$t('farmaciaApp.proveedor.municpio')">Municpio</span>
          </dt>
          <dd>
            <div v-if="proveedor.municpio">
              <router-link :to="{ name: 'MunicipioView', params: { municipioId: proveedor.municpio.id } }">{{
                proveedor.municpio.nombre
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
        <router-link
          v-if="proveedor.id"
          :to="{ name: 'ProveedorEdit', params: { proveedorId: proveedor.id } }"
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

<script lang="ts" src="./proveedor-details.component.ts"></script>
