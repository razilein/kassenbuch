Vue.component('page-header', {
  i18n,
  props: ['marker'],
  template: `<div id="header">
     <a class="logoutLink" href="/logout" v-if="username">{{ $t("general.ausloggen") }}</a>
     <a class="logoutLink" href="/mitarbeiter-profil.html" v-if="username">{{ $t("profil.meinProfil") }}</a>
     <a class="logoutLink" href="/hilfe.html">{{ $t("general.hilfe") }}</a>
     <span class="right" style="padding-right: 10px" v-if="username">
       {{ $t("general.eingeloggt") }} {{username}}
       <br>
       <span style="font-size: 25px;">
         {{ $t("general.filiale") }}: {{filiale}}
       </span>
       <span v-if="showInventurHinweis">
         <br>
         <a class="logoutLink" href="/inventur.html" style="font-size: 25px; color: red;">{{ $t("inventar.inventur.erstellen") }}</a>
       </span>
     </span>
     <div class="loading hide" id="loader">Loading&#8230;</div>
     <img src="themes/icons/logo.png" height="100" />
     <div id="navigation">
       <a class="navigationLink" :class="marker === 'Reparaturen' ? 'active' : ''" href="reparatur.html">
         <img src="themes/icons/zahnrad.png" height="20" /> {{ $t("general.reparaturen") }}
       </a>
       <a class="navigationLink" :class="marker === 'Rechnungen' ? 'active' : ''" href="rechnung.html">
         <img src="themes/icons/rechnung.png" height="20" /> {{ $t("general.rechnungen") }}
       </a>
       <a class="navigationLink" :class="marker === 'Angebot' ? 'active' : ''" href="angebot.html">
         <img src="themes/icons/angebot.png" height="20" /> {{ $t("general.angebote") }}
       </a>
       <a class="navigationLink" :class="marker === 'Bestellung' ? 'active' : ''" href="bestellung.html">
         <img src="themes/icons/bestellung.png" height="20" /> {{ $t("bestellung.titelK") }}
       </a>
       <a class="navigationLink" :class="marker === 'Kunden' ? 'active' : ''" href="kunden.html">
         <img src="themes/icons/kunde.png" height="20" /> {{ $t("general.kunden") }}
       </a>
       <a class="navigationLink" :class="marker === 'Inventar' ? 'active' : ''" href="produkt.html">
         <img src="themes/icons/inventar.png" height="20" /> {{ $t("general.inventar") }}
       </a>
       <a class="navigationLink" :class="marker === 'Kassenbuch' ? 'active' : ''" href="kassenbuch.html">
         <img src="themes/icons/kasse.png" height="20" /> {{ $t("general.kassenbuch") }}
       </a>
       <a class="navigationLink" :class="marker === 'Einkauf' ? 'active' : ''" href="einkauf.html">
         <img src="themes/icons/einkauf.png" height="20" /> {{ $t("general.einkauf") }}
       </a>
       <a class="navigationLink" :class="marker === 'Einstellungen' ? 'active' : ''" href="einstellungen.html">
         <img src="themes/icons/einstellungen.png" height="20" /> {{ $t("general.einstellungen") }}
       </a>
      </div>
    </div>
`,
  data: function() {
    this.loadUser();
    return {
      marker: '',
      username: null,
      filiale: null,
      showInventurHinweis: false
    };
  },
  methods: {
    loadUser: function() {
      this.getUsername()
        .then(this.setUsername)
        .then(this.getFiliale)
        .then(this.setFiliale)
        .then(this.getShowInventurHinweis)
        .then(this.setShowInventurHinweis);
    },
    getFiliale: function() {
      return axios.get('/current-filiale');
    },
    setFiliale: function(response) {
      this.filiale = response.data;
    },
    getShowInventurHinweis: function() {
      return axios.get('/show-inventur-hinweis');
    },
    setShowInventurHinweis: function(response) {
      this.showInventurHinweis = response.data;
    },
    getUsername: function() {
      return axios.get('/current-user');
    },
    setUsername: function(response) {
      this.username = response.data;
    }
  }
});
