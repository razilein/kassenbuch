Vue.component('kunde-edit-dialog', {
  template: createEditDialogTemplate(`
  <div class="m1">
      <zeichenzaehler-label :elem="entity.firmenname" :forid="'kundeEditForm_firmenname'" :label="'Firmenname'" :maxlength="'200'" :required="true"></zeichenzaehler-label>
      <input class="m1" id="kundeEditForm_firmenname" maxlength="200" type="text" v-model="entity.firmenname"></input>
  </div>
  <div class="m1">
    <div class="m2m">
      <zeichenzaehler-label :elem="entity.nachname" :forid="'kundeEditForm_nachname'" :label="'Nachname'" :maxlength="'100'" :required="true"></zeichenzaehler-label>
      <input class="m2" id="kundeEditForm_nachname" maxlength="100" type="text" v-model="entity.nachname"></input>
    </div>
    <div class="m2">
      <zeichenzaehler-label :elem="entity.vorname" :forid="'kundeEditForm_vorname'" :label="'Vorname'" :maxlength="'50'"></zeichenzaehler-label>
      <input class="m2" id="kundeEditForm_vorname" maxlength="50" type="text" v-model="entity.vorname"></input>
    </div>
  </div>
  <div class="m1">
    <div class="m2m">
      <zeichenzaehler-label :elem="entity.strasse" :forid="'kundeEditForm_strasse'" :label="'Straße'" :maxlength="'100'"></zeichenzaehler-label>
      <input class="m2" id="kundeEditForm_strasse" maxlength="100" type="text" v-model="entity.strasse"></input>
    </div>
    <div class="m6m">
      <zeichenzaehler-label :elem="entity.plz" :forid="'kundeEditForm_plz'" :label="'PLZ'" :maxlength="'8'"></zeichenzaehler-label>
      <input class="m6" id="kundeEditForm_plz" maxlength="8" type="text" v-model="entity.plz"></input>
    </div>
    <div class="m3">
      <zeichenzaehler-label :elem="entity.ort" :forid="'kundeEditForm_ort'" :label="'Ort'" :maxlength="'50'"></zeichenzaehler-label>
      <input class="m3" id="kundeEditForm_ort" maxlength="50" type="text" v-model="entity.ort"></input>
    </div>
  </div>
  <div class="m1">
    <div class="m2m">
      <zeichenzaehler-label :elem="entity.telefon" :forid="'kundeEditForm_telefon'" :label="'Telefon'" :maxlength="'50'"></zeichenzaehler-label>
      <input class="m2" id="kundeEditForm_telefon" maxlength="50" type="text" v-model="entity.telefon"></input>
    </div>
    <div class="m2">
      <zeichenzaehler-label :elem="entity.email" :forid="'kundeEditForm_email'" :label="'E-Mail'" :maxlength="'100'"></zeichenzaehler-label>
      <input class="m2" id="kundeEditForm_email" maxlength="100" type="text" v-model="entity.email"></input>
    </div>
  </div>
  <div class="m1">
    <zeichenzaehler-label :elem="entity.bemerkung" :forid="'kundeEditForm_bemerkung'" :label="'Bemerkungen'" :maxlength="'4000'"></zeichenzaehler-label>
    <textarea class="m1" v-model="entity.bemerkung"></textarea>
  </div>
      `),
  props: {
    initialEntity: Object,
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
      return this.entity && (this.entity.nachname || this.entity.firmenname);
    },
    loadEntity: function() {
      showLoader();
      this.getEntity()
        .then(this.setEntity)
        .then(this.initEntityIfEmpty)
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
    initEntityIfEmpty: function() {
      if (!hasAllProperties(this.entity, ['id'])) {
        this.entity = this.initialEntity || {};
      }
    },
    getEntity: function() {
      return axios.get(this.restUrlGet);
    },
    setEntity: function(response) {
      this.entity = response.data;
    },
  }
});

