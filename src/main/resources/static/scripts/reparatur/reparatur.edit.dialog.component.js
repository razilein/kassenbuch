Vue.component('edit-dialog', {
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
    <label for="reparaturEditForm_geraet">Gerät / Zubehör</label>
    <input class="m1" id="reparaturEditForm_geraet" maxlength="200" type="text" v-model="entity.geraet"></input>
  </div>
  <div class="m1">
    <div class="m2m">
      <label for="reparaturEditForm_seriennummer">Seriennummer</label>
      <input class="m2" id="reparaturEditForm_seriennummer" maxlength="200" type="text" v-model="entity.seriennummer"></input>
    </div>
  </div>
  <div class="m1">
    <label for="reparaturEditForm_symptome">Symptome / Fehler</label>
    <textarea class="m1" id="reparaturEditForm_symptome" maxlength="4000" v-model="entity.symptome"></textarea>
  </div>
  <div class="m1">
    <label for="reparaturEditForm_aufgaben">Geplante Aufgaben</label>
    <textarea class="m1" id="reparaturEditForm_aufgaben" maxlength="4000" v-model="entity.aufgaben"></textarea>
  </div>
  <div class="m1">
    <div class="m2">
      <label for="reparaturEditForm_geraetepasswort">Gerätepasswort</label>
      <input class="m2" id="reparaturEditForm_geraetepasswort" maxlength="50" type="text" v-model="entity.geraetepasswort"></input>
    </div>
  </div>
  <div class="m1" v-if="entity.kunde">
    <label for="reparaturEditForm_kunde">Kunde</label><button class="kunde" title="Kunde suchen" @click="showKundeDialog = true"></button>
    <textarea class="m1" id="reparaturEditForm_kunde" readonly v-model="entity.kunde.completeWithAdressAndPhone"></textarea>
  </div>
  <div class="m1">
    <label class="container checkbox">Expressbearbeitung
      <input id="reparaturEditForm_expressbearbeitung" type="checkbox" v-model="entity.expressbearbeitung" v-on:change="editKostenvoranschlag(); changeAbholdatumZeit();" />
      <span class="checkmark"></span>
    </label>
  </div>
  <div class="m1">
    <div class="m2m">
      <label for="reparaturEditForm_abholdatum">Abholdatum</label>
      <input class="m2" id="reparaturEditForm_abholdatum" type="date" v-model="entity.abholdatum" />
    </div>
    <div class="m2">
      <label for="reparaturEditForm_abholzeit">Abholzeit</label>
      <input class="m2" id="reparaturEditForm_abholzeit" max="19:00" min="9:00" type="time" v-model="entity.abholzeit" />
    </div>
  </div>
  <div class="m1">
    <div class="m2m">
      <label for="reparaturEditForm_kostenvoranschlag">Kostenvoranschlag</label>
      <input class="m2" id="reparaturEditForm_kostenvoranschlag" maxlength="100" type="text" v-model="entity.kostenvoranschlag"></input>
    </div>
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
    <label for="reparaturEditForm_bemerkung">Bemerkungen</label>
    <textarea class="m1" id="reparaturEditForm_bemerkung" maxlength="4000" v-model="entity.bemerkung"></textarea>
  </div>
  <div class="m1">
    <div class="m2" style="float: right;">
      <label for="reparaturEditForm_mitarbeiter">Mitarbeiter</label>
      <input class="m2" id="reparaturEditForm_mitarbeiter" readonly type="text" :value="entity.mitarbeiter" />
    </div>
  </div>
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
      entity: {},
      mitarbeiter: {},
      pruefstatus: [
        { key: true, value: 'Gerät funktioniert' },
        { key: false, value: 'Gerät funktioniert nicht' }
      ],
      reparaturarten: {},
      showKundeDialog: false
    };
  },
  methods: {
    areRequiredFieldsNotEmpty: function() {
      return this.entity && this.entity.kunde && hasAllProperties(this.entity, ['kunde.id']) && !this.entity.erledigt;
    },
    changeAbholdatumZeit: function() {
      this.getAbholdatumZeit()
        .then(this.setAbholdatumZeit);
    },
    editKostenvoranschlag: function() {
      var kosten = this.entity.kostenvoranschlag || '';
      var isExpress = this.entity.expressbearbeitung;
      if (isExpress && !kosten.includes('+ 25,- Expresspauschale')) {
        kosten += ' + 25,- Expresspauschale';
      } else if (!isExpress) {
        kosten = kosten.replace('+ 25,- Expresspauschale', '');
      }
      this.entity.kostenvoranschlag = kosten.trim() || null;
    },
    loadEntity: function() {
      showLoader();
      this.getEntity()
        .then(this.setEntity)
        .then(this.getMitarbeiter)
        .then(this.setMitarbeiter)
        .then(this.getReparaturarten)
        .then(this.setReparaturarten)
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
      this.entity = response.data;
    },
    getMitarbeiter: function() {
      return axios.get('/reparatur/mitarbeiter');
    },
    setMitarbeiter: function(response) {
      this.mitarbeiter = response.data;
    },
    getReparaturarten: function() {
      return axios.get('/reparatur/reparaturarten');
    },
    setReparaturarten: function(response) {
      this.reparaturarten = response.data;
    },
  }
});

