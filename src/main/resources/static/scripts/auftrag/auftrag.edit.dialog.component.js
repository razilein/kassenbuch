Vue.component('edit-dialog', {
  template: createEditDialogTemplate(`
  <div class="m1" v-if="entity.angebot">
    <label for="auftragEditForm_angebot">Angebot</label>
    <button class="angebot btnSmall" title="Angebot suchen" @click="showAngebotDialog = true"></button>
    <button class="delete btnSmall" title="Angebot deselektieren" @click="entity.angebot = {};"></button>
    <textarea class="m1" id="auftragEditForm_angebot" readonly v-if="entity.angebot.text" v-model="entity.angebot.text"></textarea>
    <input class="m1" id="auftragEditForm_angebot" readonly v-if="!entity.angebot.text" v-model="entity.angebot.nummer"></textarea>
  </div>
  <div class="m1" v-if="entity.kunde">
    <label class="required" for="auftragEditForm_kunde">Kunde</label>
    <button class="kunde btnSmall" title="Kunde suchen" @click="showKundeDialog = true"></button>
    <textarea class="m1" id="auftragEditForm_kunde" readonly v-model="entity.kunde.completeWithAdress"></textarea>
  </div>
  <div class="m1">
    <div class="m4m">
      <label class="required" for="auftragEditForm_datum">Lieferdatum</label>
      <input class="m4" id="auftragEditForm_datum" type="date" v-model="entity.datum" v-on:change="wochentagdatum = formatDayOfWeek(entity.datum);"/>
    </div>
    <div class="m4m">
      <label for="auftragEditForm_wochentag_datum">Wochentag</label>
      <input class="m4" id="auftragEditForm_wochentag_datum" readonly type="text" v-model="wochentagdatum" />
    </div>
  </div>
  <div class="m1">
    <zeichenzaehler-label :elem="entity.beschreibung" :forid="'auftragEditForm_beschreibung'" :label="'Bestellung'" :maxlength="'2000'" :required="true"></zeichenzaehler-label>
    <textarea class="m1 big" id="auftragEditForm_beschreibung" maxlength="2000" v-model="entity.beschreibung"></textarea>
  </div>
  <div class="m1">
    <zeichenzaehler-label :elem="entity.kosten" :forid="'auftragEditForm_kosten'" :label="'Kosten'" :maxlength="'300'" :required="true"></zeichenzaehler-label>
    <input class="m1" id="auftragEditForm_kosten" maxlength="300" type="text" v-model="entity.kosten"></input>
  </div>
  <div class="m1">
    <zeichenzaehler-label :elem="entity.anzahlung" :forid="'auftragEditForm_anzahlung'" :label="'Anzahlung'" :maxlength="'300'" :required="true"></zeichenzaehler-label>
    <input class="m1" id="auftragEditForm_anzahlung" maxlength="300" type="text" v-model="entity.anzahlung"></input>
  </div>
  <div class="m1">
    <div class="m2" style="float: right;">
      <label for="auftragEditForm_ersteller">Mitarbeiter</label>
      <input class="m2" id="auftragEditForm_ersteller" readonly type="text" :value="entity.ersteller" />
    </div>
  </div>
  <angebot-suchen-dialog
    :angebot="entity.angebot"
    :kunde="entity.kunde"
    v-if="showAngebotDialog"
    @close="showAngebotDialog = false"
    @saved="handleAngebotResponse"
  ></angebot-suchen-dialog>
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
        angebot: {},
        kunde: {},
        filiale: {}
      },
      mitarbeiter: {},
      showAngebotDialog: false,
      showKundeDialog: false,
      wochentagdatum: ''
    };
  },
  methods: {
    areRequiredFieldsNotEmpty: function() {
      return this.entity && this.entity.kunde && hasAllPropertiesAndNotEmpty(this.entity, ['kunde.id', 'kosten', 'beschreibung']) && !this.entity.erledigt;
    },
    changeAbholdatum: function() {
      this.getAbholdatum()
        .then(this.setAbholdatum);
    },
    handleAngebotResponse: function(angebot) {
      this.showAngebotDialog = false;
      this.entity.angebot = angebot.angebot;
      this.entity.angebot.text = angebot.text;
      this.entity.kunde = angebot.angebot.kunde;
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
    getAbholdatum: function() {
      this.wochentagdatum = formatDayOfWeek(response.data.datum);
      return axios.get('/auftrag/datum/');
    },
    setAbholdatum: function(response) {
      this.entity.datum = response.data.datum;
    },
    getEntity: function() {
      return axios.get(this.restUrlGet);
    },
    setEntity: function(response) {
      var data = response.data;
      this.wochentagdatum = formatDayOfWeek(data.datum);
      if (!data.angebot) {
        data.angebot = {};
      }
      if (!data.kunde) {
        data.kunde = {};
      }
      this.entity = response.data;
    },
    getMitarbeiter: function() {
      return axios.get('/reparatur/mitarbeiter');
    },
    setMitarbeiter: function(response) {
      this.mitarbeiter = response.data;
    },
  }
});

