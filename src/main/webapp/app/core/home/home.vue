<template>
  <div class="jumbotron">
    <div class="home row">
      <div class="col-md-3">
        <span class="hipster img-fluid rounded"></span>
      </div>
      <div class="col-md-9">
        <h1 class="display-4" v-text="$t('home.title')">Welcome, Java Hipster!</h1>
        <p class="lead" v-text="$t('home.subtitle')">This is your homepage</p>

        <div>
          <div class="alert alert-success" v-if="authenticated">
            <span v-if="username" v-text="$t('home.logged.message', { username: username })"
              >You are logged in as user "{{ username }}"</span
            >
          </div>

          <div class="alert alert-warning" v-if="!authenticated">
            <span v-text="$t('global.messages.info.authenticated.prefix')">If you want to </span>
            <a class="alert-link" v-on:click="openLogin()" v-text="$t('global.messages.info.authenticated.link')">sign in</a
            ><span v-html="$t('global.messages.info.authenticated.suffix')"
              >, you can try the default accounts:<br />- Administrator (login="admin" and password="admin") <br />- User (login="user" and
              password="user").</span
            >
          </div>
          <div class="alert alert-warning" v-if="!authenticated">
            <span v-text="$t('global.messages.info.register.noaccount')">You don't have an account yet?</span>&nbsp;
            <router-link class="alert-link" to="/register" v-text="$t('global.messages.info.register.link')"
              >Register a new account</router-link
            >
          </div>
        </div>

        <p v-text="$t('home.question')">If you have any question on JHipster:</p>

        <ul>
          <li>
            <a href="https://www.jhipster.tech/" target="_blank" rel="noopener noreferrer" v-text="$t('home.link.homepage')"
              >JHipster homepage</a
            >
          </li>
          <li>
            <a
              href="https://stackoverflow.com/questions/49695068/i-am-building-a-program-to-manage-a-pharmacy"
              target="_blank"
              rel="noopener noreferrer"
              v-text="$t('home.link.stackoverflow')"
              >JHipster on Stack Overflow</a
            >
          </li>
          <li>
            <a
              href="https://github.com/keliumJU/farmacia-web.git"
              target="_blank"
              rel="noopener noreferrer"
              v-text="$t('home.link.bugtracker')"
              >JHipster bug tracker</a
            >
          </li>
          <li>
            <a href="https://discord.com/invite/D2M657D" target="_blank" rel="noopener noreferrer" v-text="$t('home.link.chat')"
              >JHipster public chat room</a
            >
          </li>
          <li>
            <a
              href="https://twitter.com/Pharmacy_Times?ref_src=twsrc%5Egoogle%7Ctwcamp%5Eserp%7Ctwgr%5Eauthor"
              target="_blank"
              rel="noopener noreferrer"
              v-text="$t('home.link.follow')"
              >follow @jhipster on Twitter</a
            >
          </li>
        </ul>

        <p>
          <span v-text="$t('home.like')">If you like JHipster, don't forget to give us a star on</span>
          <a href="https://github.com/keliumJU/farmacia-web.git" target="_blank" rel="noopener noreferrer" v-text="$t('home.github')"
            >GitHub</a
          >!
        </p>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./home.component.ts"></script>
