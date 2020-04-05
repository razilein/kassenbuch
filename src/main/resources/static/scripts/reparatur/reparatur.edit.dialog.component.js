Vue.component('edit-dialog', {
  i18n,
  template: createEditDialogTemplate(`
  <div class="m1">
    <label class="container radio" v-for="art in reparaturarten">
      <input
        name="art"
        type="radio"
        :value="art.key"
        v-model="entity.art"
      >{{art.value}}</input>
      <span class="checkmark"></span>
    </label>
  </div>
  <div class="m1">
    <zeichenzaehler-label :elem="entity.geraet" :forid="'reparaturEditForm_geraet'" :label="$t('reparatur.geraet')" :maxlength="'500'" :required="true"></zeichenzaehler-label>
    <input class="m1" id="reparaturEditForm_geraet" maxlength="500" type="text" v-model="entity.geraet"></input>
  </div>
  <div class="m1">
    <div class="m2m">
      <zeichenzaehler-label :elem="entity.seriennummer" :forid="'reparaturEditForm_seriennummer'" :label="$t('reparatur.seriennummer')" :maxlength="'500'"></zeichenzaehler-label>
      <input class="m2" id="reparaturEditForm_seriennummer" maxlength="500" type="text" v-model="entity.seriennummer"></input>
    </div>
  </div>
  <div class="m1">
    <zeichenzaehler-label :elem="entity.symptome" :forid="'reparaturEditForm_symptome'" :label="$t('reparatur.symptomeFehler')" :maxlength="'1000'"></zeichenzaehler-label>
    <textarea class="m1" id="reparaturEditForm_symptome" maxlength="1000" v-model="entity.symptome"></textarea>
  </div>
  <div class="m1">
    <zeichenzaehler-label :elem="entity.aufgaben" :forid="'reparaturEditForm_aufgaben'" :label="$t('reparatur.aufgaben')" :maxlength="'1000'"></zeichenzaehler-label>
    <textarea class="m1" id="reparaturEditForm_aufgaben" maxlength="1000" v-model="entity.aufgaben"></textarea>
  </div>
  <div class="m1">
    <label class="container radio" v-for="art in geraetepasswortarten">
      <input
        name="geraetepasswortart"
        type="radio"
        :value="art.key"
        v-model="entity.geraetepasswortArt"
        v-on:change="setGeraetepasswort();"
      >{{art.value}}</input>
      <span class="checkmark"></span>
    </label>
  </div>
  <div class="m1">
    <div class="m2">
      <zeichenzaehler-label :elem="entity.geraetepasswort" :forid="'reparaturEditForm_geraetepasswort'" :label="$t('reparatur.pw')" :maxlength="'50'" :required="true"></zeichenzaehler-label>
      <input class="m2" id="reparaturEditForm_geraetepasswort" maxlength="50" type="text" v-model="entity.geraetepasswort" :readonly="entity.geraetepasswortArt !== 0"></input>
    </div>
  </div>
  <div class="m1" v-if="entity.bestellung">
    <label for="reparaturEditForm_bestellung">{{ $t("bestellung.titelK") }}</label>
    <button class="bestellung btnSmall" :title="$t('bestellung.suchen')" @click="showBestellungDialog = true"></button>
    <button class="delete btnSmall" :title="$t('bestellung.deselektieren')" @click="entity.bestellung = {}"></button>
    <textarea class="m1" id="reparaturEditForm_bestellung" readonly v-model="entity.bestellung.beschreibung"></textarea>
  </div>
  <div class="m1" v-if="entity.kunde">
    <label for="reparaturEditForm_kunde">{{ $t("general.kunde") }}</label><button class="kunde btnSmall" :title="$t('kunde.suchen')" @click="showKundeDialog = true"></button>
    <textarea class="m1" id="reparaturEditForm_kunde" readonly v-model="entity.kunde.completeWithAdressAndPhone"></textarea>
  </div>
  <div class="m1">
    <label class="container checkbox">{{ $t("reparatur.express") }}
      <input id="reparaturEditForm_expressbearbeitung" type="checkbox" v-model="entity.expressbearbeitung" v-on:change="editKostenvoranschlag(); changeAbholdatumZeit();" />
      <span class="checkmark"></span>
    </label>
  </div>
  <div class="m1">
    <div class="m2m">
      <label for="reparaturEditForm_abholdatum">{{ $t("reparatur.abholdatum") }}</label>
      <input class="m2" id="reparaturEditForm_abholdatum" type="date" v-model="entity.abholdatum" />
    </div>
    <div class="m2">
      <label for="reparaturEditForm_abholzeit">{{ $t("reparatur.abholzeit") }}</label>
      <input class="m2" id="reparaturEditForm_abholzeit" max="19:00" min="9:00" type="time" v-model="entity.abholzeit" />
    </div>
  </div>
  <div class="m1">
    <zeichenzaehler-label :elem="entity.kostenvoranschlag" :forid="'reparaturEditForm_kostenvoranschlag'" :label="$t('reparatur.kostenvoranschlag')" :maxlength="'300'" :required="true"></zeichenzaehler-label>
    <input class="m1" id="reparaturEditForm_kostenvoranschlag" maxlength="300" type="text" v-model="entity.kostenvoranschlag"></input>
  </div>
  <div class="m1">
    <label class="container radio" v-for="status in pruefstatus">
      <input
        name="status"
        type="radio"
        :value="status.key"
        v-model="entity.funktionsfaehig"
      >{{status.value}}</input>
      <span class="checkmark"></span>
    </label>
  </div>
  <div class="m1">
    <zeichenzaehler-label :elem="entity.bemerkung" :forid="'reparaturEditForm_bemerkung'" :label="$t('general.bemerkung')" :maxlength="'4000'"></zeichenzaehler-label>
    <textarea class="m1" id="reparaturEditForm_bemerkung" maxlength="4000" v-model="entity.bemerkung"></textarea>
  </div>
  <div class="m1">
    <div class="m2" style="float: right;">
      <label for="reparaturEditForm_mitarbeiter">{{ $t("general.ersteller") }}</label>
      <input class="m2" id="reparaturEditForm_mitarbeiter" readonly type="text" :value="entity.mitarbeiter" />
    </div>
  </div>
  <bestellung-suchen-dialog
    :bestellung="entity.bestellung"
    :kunde="entity.kunde"
    v-if="showBestellungDialog"
    @close="showBestellungDialog = false"
    @saved="handleBestellungResponse"
  ></bestellung-suchen-dialog>
  <kunde-suchen-dialog
    :kunde="entity.kunde"
    v-if="showKundeDialog"
    @close="showKundeDialog = false"
    @saved="handleKundeResponse"
  ></edit-dialog>
      `),
  props: {
    restUrlGet: String,
    restUrlSave: String,
    title: String,
  },
  data: function() {
    this.loadEntity();
    return {
      entity: {
        bestellung: {},
        kunde: {},
      },
      geraetepasswortarten: [],
      pruefstatus: [],
      reparaturarten: {},
      showKundeDialog: false,
      showBestellungDialog: false
    };
  },
  methods: {
    areRequiredFieldsNotEmpty: function() {
      return this.entity && this.entity.kunde && hasAllPropertiesAndNotEmpty(this.entity, ['geraetepasswort', 'kunde.id', 'kostenvoranschlag']) &&
        this.entity.funktionsfaehig !== -1 && !this.entity.erledigt && this.entity.geraet;
    },
    changeAbholdatumZeit: function() {
      this.getAbholdatumZeit()
        .then(this.setAbholdatumZeit);
    },
    editKostenvoranschlag: function() {
      var kosten = this.entity.kostenvoranschlag || '';
      var isExpress = this.entity.expressbearbeitung;
      if (isExpress && !kosten.includes(this.$t('reparatur.expresspauschale'))) {
        kosten += ' ' + this.$t('reparatur.expresspauschale');
      } else if (!isExpress) {
        kosten = kosten.replace(this.$t('reparatur.expresspauschale'), '');
      }
      this.entity.kostenvoranschlag = kosten.trim() || null;
    },
    handleBestellungResponse: function(bestellung) {
      this.showBestellungDialog = false;
      this.entity.bestellung = bestellung;
      this.entity.kunde = bestellung.kunde;
      this.entity.kostenvoranschlag = bestellung.kosten;
      this.editKostenvoranschlag();
    },
    loadEntity: function() {
      showLoader();
      this.getEntity()
        .then(this.setEntity)
        .then(this.getReparaturarten)
        .then(this.setReparaturarten)
        .then(this.getGeraetepasswortarten)
        .then(this.setGeraetepasswortarten)
        .then(this.getPruefstatus)
        .then(this.setPruefstatus)
        .then(hideLoader);
    },
    saveFunc: function() {
      this.executeSave()
        .then(this.closeAndReturnResponse);
    },
    executeSave: function() {
      return axios.put(this.restUrlSave, this.entity);
    },
    closeAndReturnResponse: function(response) {
      this.$emit('saved', response.data);
    },
    handleKundeResponse: function(kunde) {
      this.showKundeDialog = false;
      this.entity.kunde = kunde;
    },
    setGeraetepasswort: function() {
      if (this.entity.geraetepasswortArt === 0) {
        this.entity.geraetepasswort = null;
      } else {
        this.entity.geraetepasswort = this.geraetepasswortarten[this.entity.geraetepasswortArt].value;
      }
    },
    getAbholdatumZeit: function() {
      return axios.get('/reparatur/abholdatum/' + this.entity.expressbearbeitung);
    },
    setAbholdatumZeit: function(response) {
      this.entity.abholdatum = response.data.abholdatum;
      this.entity.abholzeit = response.data.abholzeit;
    },
    getEntity: function() {
      return axios.get(this.restUrlGet);
    },
    setEntity: function(response) {
      var data = response.data;
      if (!data.bestellung) {
        data.bestellung = {};
      }
      this.entity = data;
    },
    getReparaturarten: function() {
      return axios.get('/reparatur/reparaturarten');
    },
    setReparaturarten: function(response) {
      this.reparaturarten = response.data;
    },
    getGeraetepasswortarten: function() {
      return axios.get('/reparatur/geraetepasswortarten');
    },
    setGeraetepasswortarten: function(response) {
      this.geraetepasswortarten = response.data;
    },
    getPruefstatus: function() {
      return axios.get('/reparatur/pruefstatus');
    },
    setPruefstatus: function(response) {
      this.pruefstatus = response.data;
    },
  }
});

