Vue.component('edit-dialog', {
  template: createEditDialogTemplate(`
  <div class="m1">
    <div class="m2m">
      <zeichenzaehler-label :elem="entity.filiale.name" :forid="'filialeEditForm_name'" :label="'Name'" :maxlength="'50'" :required="true"></zeichenzaehler-label>
      <input class="m2" id="filialeEditForm_name" maxlength="50" type="text" v-model="entity.filiale.name"></input>
    </div>
    <div class="m2">
      <zeichenzaehler-label :elem="entity.filiale.kuerzel" :forid="'filialeEditForm_kuerzel'" :label="'Kürzel für Aufträge'" :maxlength="'3'" :required="true"></zeichenzaehler-label>
      <input class="m2" id="filialeEditForm_kuerzel" maxlength="3" type="text" v-model="entity.filiale.kuerzel"></input>
    </div>
  </div>
  <div class="m1">
    <div class="m2m">
      <zeichenzaehler-label :elem="entity.filiale.strasse" :forid="'filialeEditForm_strasse'" :label="'Straße'" :maxlength="'100'" :required="true"></zeichenzaehler-label>
      <input class="m2" id="filialeEditForm_strasse" maxlength="100" type="text" v-model="entity.filiale.strasse"></input>
    </div>
    <div class="m6m">
      <zeichenzaehler-label :elem="entity.filiale.plz" :forid="'filialeEditForm_plz'" :label="'PLZ'" :maxlength="'8'" :required="true"></zeichenzaehler-label>
      <input class="m6" id="filialeEditForm_plz" maxlength="8" type="text" v-model="entity.filiale.plz"></input>
    </div>
    <div class="m3">
      <zeichenzaehler-label :elem="entity.filiale.ort" :forid="'filialeEditForm_ort'" :label="'Ort'" :maxlength="'50'" :required="true"></zeichenzaehler-label>
      <input class="m3" id="filialeEditForm_ort" maxlength="50" type="text" v-model="entity.filiale.ort"></input>
    </div>
  </div>
  <div class="m1">
    <div class="m2m">
      <zeichenzaehler-label :elem="entity.filiale.telefon" :forid="'filialeEditForm_telefon'" :label="'Telefon'" :maxlength="'50'" :required="true"></zeichenzaehler-label>
      <input class="m2" id="filialeEditForm_telefon" maxlength="50" type="text" v-model="entity.filiale.telefon"></input>
    </div>
    <div class="m2">
      <zeichenzaehler-label :elem="entity.filiale.email" :forid="'filialeEditForm_email'" :label="'E-Mail'" :maxlength="'50'" :required="true"></zeichenzaehler-label>
      <input class="m2" id="filialeEditForm_email" maxlength="50" type="text" v-model="entity.filiale.email"></input>
    </div>
  </div>
  <div class="m1">
    <zeichenzaehler-label :elem="entity.filiale.rechnungTextAndereFiliale" :forid="'filialeEditForm_rechnung_text_andere_filiale'" :label="'Rechnungstext: Besuchen Sie uns auch in unserer Filiale in'" :maxlength="'100'" :required="true"></zeichenzaehler-label>
    <input class="m1" id="filialeEditForm_rechnung_text_andere_filiale" maxlength="100" type="text" v-model="entity.filiale.rechnungTextAndereFiliale"></input>
  </div>
  <div class="m1">
    <div class="m4m">
      <label for="filialeEditForm_zaehlerRechnung">Rechnungsnummer</label>
      <input class="m4" id="filialeEditForm_zaehlerRechnung" type="number" v-model="entity.filiale.zaehlerRechnung" />
    </div>
    <div class="m4">
      <label for="filialeEditForm_zaehlerReparaturauftrag">Reparaturnummer</label>
      <input class="m4" id="filialeEditForm_zaehlerReparaturauftrag" type="number" v-model="entity.filiale.zaehlerReparaturauftrag" />
    </div>
  </div>
  <div class="m1">
    <hr>
    <h1>Kontierung</h1>
  </div>
  <div class="m1">
    <div class="m4m">
      <label for="filialeEditForm_sollBar">Soll Bar</label>
      <input class="m4" id="filialeEditForm_sollBar" min="0" max="9999" type="number" v-model="entity.filialeKonten.sollBar"></input>
    </div>
    <div class="m4m">
      <label for="filialeEditForm_habenBar">Haben Bar</label>
      <input class="m4" id="filialeEditForm_habenBar" min="0" max="9999" type="number" v-model="entity.filialeKonten.habenBar"></input>
    </div>
  </div>
  <div class="m1">
    <div class="m4m">
      <label for="filialeEditForm_sollEc">Soll EC</label>
      <input class="m4" id="filialeEditForm_sollEc" min="0" max="9999" type="number" v-model="entity.filialeKonten.sollEc"></input>
    </div>
    <div class="m4m">
      <label for="filialeEditForm_habenEc">Haben EC</label>
      <input class="m4" id="filialeEditForm_habenEc" min="0" max="9999" type="number" v-model="entity.filialeKonten.habenEc"></input>
    </div>
  </div>
  <div class="m1">
    <div class="m4m">
      <label for="filialeEditForm_sollPaypal">Soll Paypal</label>
      <input class="m4" id="filialeEditForm_sollPaypal" min="0" max="9999" type="number" v-model="entity.filialeKonten.sollPaypal"></input>
    </div>
    <div class="m4m">
      <label for="filialeEditForm_habenPaypal">Haben Paypal</label>
      <input class="m4" id="filialeEditForm_habenPaypal" min="0" max="9999" type="number" v-model="entity.filialeKonten.habenPaypal"></input>
    </div>
  </div>
  <div class="m1">
    <div class="m4m">
      <label for="filialeEditForm_sollUeberweisung">Soll Überweisung</label>
      <input class="m4" id="filialeEditForm_sollUeberweisung" min="0" max="9999" type="number" v-model="entity.filialeKonten.sollUeberweisung"></input>
    </div>
    <div class="m3m">
      <label for="filialeEditForm_habenUeberweisung">Haben Überweisung</label>
      <input class="m4" id="filialeEditForm_habenUeberweisung" min="0" max="9999" type="number" v-model="entity.filialeKonten.habenUeberweisung"></input>
    </div>
  </div>
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
    };
  },
  methods: {
    areRequiredFieldsNotEmpty: function() {
      return this.entity && this.entity.filiale && hasAllProperties(this.entity.filiale, ['name', 'kuerzel', 'strasse', 'plz', 'ort', 'telefon', 'email']);
    },
    loadEntity: function() {
      showLoader();
      this.getEntity()
        .then(this.setEntity)
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
    getEntity: function() {
      return axios.get(this.restUrlGet);
    },
    setEntity: function(response) {
      this.entity = response.data;
    },
  }
});

