Vue.component('kunde-edit-dialog', {
  template: createEditDialogTemplate(`
  <div class="m1">
      <label class="required" for="kundeEditForm_firmenname">Firmenname</label>
      <input class="m1" id="kundeEditForm_firmenname" maxlength="200" type="text" v-model="entity.firmenname"></input>
  </div>
  <div class="m1">
    <div class="m2m">
      <label class="required" for="kundeEditForm_nachname">Nachname</label>
      <input class="m2" id="kundeEditForm_nachname" maxlength="100" type="text" v-model="entity.nachname"></input>
    </div>
    <div class="m2">
      <label for="kundeEditForm_vorname">Vorname</label>
      <input class="m2" id="kundeEditForm_vorname" maxlength="50" type="text" v-model="entity.vorname"></input>
    </div>
  </div>
  <div class="m1">
    <div class="m2m">
      <label for="kundeEditForm_strasse">Straße</label>
      <input class="m2" id="kundeEditForm_strasse" maxlength="100" type="text" v-model="entity.strasse"></input>
    </div>
    <div class="m6m">
      <label for="kundeEditForm_plz">PLZ</label>
      <input class="m6" id="kundeEditForm_plz" maxlength="8" type="text" v-model="entity.plz"></input>
    </div>
    <div class="m3">
      <label for="kundeEditForm_ort">Ort</label>
      <input class="m3" id="kundeEditForm_ort" maxlength="50" type="text" v-model="entity.ort"></input>
    </div>
  </div>
  <div class="m1">
    <div class="m2m">
      <label for="kundeEditForm_telefon">Telefon</label>
      <input class="m2" id="kundeEditForm_telefon" maxlength="50" type="text" v-model="entity.telefon"></input>
    </div>
    <div class="m2">
      <label for="kundeEditForm_email">E-Mail</label>
      <input class="m2" id="kundeEditForm_email" maxlength="100" type="text" v-model="entity.email"></input>
    </div>
  </div>
  <div class="m1">
    <label for="kundeEditForm_bemerkung" maxlength="4000">Bemerkungen</label>
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

