Vue.component('edit-dialog', {
  template: createEditDialogTemplate(`
  <div class="m1">
    <div class="m2m">
      <zeichenzaehler-label :elem="entity.nachname" :forid="'mitarbeiterEditForm_name'" :label="'Nachname'" :maxlength="'50'" :required="true"></zeichenzaehler-label>
      <input class="m2" id="mitarbeiterEditForm_name" maxlength="50" type="text" v-model="entity.nachname"></input>
    </div>
    <div class="m2">
      <zeichenzaehler-label :elem="entity.vorname" :forid="'mitarbeiterEditForm_vorname'" :label="'Vorname'" :maxlength="'50'" :required="true"></zeichenzaehler-label>
      <input class="m2" id="mitarbeiterEditForm_vorname" maxlength="50" type="text" v-model="entity.vorname"></input>
    </div>
  </div>
  <div class="m1">
    <div class="m2m">
      <zeichenzaehler-label :elem="entity.email" :forid="'mitarbeiterEditForm_email'" :label="'E-Mail (geschäftlich)'" :maxlength="'50'"></zeichenzaehler-label>
      <input class="m2" id="mitarbeiterEditForm_email" maxlength="50" type="text" v-model="entity.email"></input>
    </div>
    <div class="m2">
      <zeichenzaehler-label :elem="entity.emailPrivat" :forid="'mitarbeiterEditForm_email_privat'" :label="'E-Mail (privat)'" :maxlength="'50'"></zeichenzaehler-label>
      <input class="m2" id="mitarbeiterEditForm_email_privat" maxlength="50" type="text" v-model="entity.emailPrivat"></input>
    </div>
  </div>
  <div class="m1">
    <div class="m2m">
      <zeichenzaehler-label :elem="entity.telefon" :forid="'mitarbeiterEditForm_telefon'" :label="'Telefon (privat)'" :maxlength="'50'"></zeichenzaehler-label>
      <input class="m2" id="mitarbeiterEditForm_telefon" maxlength="50" type="text" v-model="entity.telefon"></input>
    </div>
  </div>
  <div class="m1">
    <div class="m2m">
      <label class="required" for="mitarbeiterEditForm_filiale">Filiale</label>
      <select class="m2" id="mitarbeiterEditForm_filiale" v-model="entity.filiale.id">
        <option :value="f.key" v-for="f in filialen">{{f.value}}</option>
      </select>
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
      entity: {
        filiale: {},
      },
      filialen: [],
    };
  },
  methods: {
    areRequiredFieldsNotEmpty: function() {
      return this.entity && hasAllProperties(this.entity, ['nachname', 'vorname']);
    },
    loadEntity: function() {
      showLoader();
      this.getEntity()
        .then(this.setEntity)
        .then(this.getFilialen)
        .then(this.setFilialen)
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
    getFilialen: function() {
      return axios.get('einstellungen/filiale');
    },
    setFilialen: function(response) {
      this.filialen = response.data;
    },
  }
});

