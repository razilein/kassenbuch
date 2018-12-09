Vue.component('page-header', {
  template: `<div id="header">
     <a class="logoutLink" href="/logout" v-if="username">ausloggen</a>
     <a class="logoutLink" href="/mitarbeiter-profil.html" v-if="username">Mein Profil</a>
     <span class="right" style="padding-right: 10px" v-if="username">Eingeloggt als {{username}}</span>
     <div class="loading hide" id="loader">Loading&#8230;</div>
     <img src="themes/icons/logo.png" height="100" />
     <div id="navigation">
       <a class="navigationLink" href="kassenbuch.html">
         <img src="themes/icons/kasse.png" height="20" /> Kassenbuch
       </a>
       <a class="navigationLink" href="reparatur.html">
         <img src="themes/icons/zahnrad.png" height="20" /> Reparaturen
       </a>
       <a class="navigationLink" href="einstellungen.html">
         <img src="themes/icons/einstellungen.png" height="20" /> Einstellungen
       </a>
      </div>
    </div>
`,
  data: function() {
    this.loadUser();
    return {
      username: null
    };
  },
  methods: {
    loadUser: function() {
      this.getUsername().then(this.setUsername);
    },
    getUsername: function() {
      return axios.get('/current-user');
    },
    setUsername: function(response) {
      this.username = response.data;
    }
  }
});
