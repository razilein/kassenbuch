Vue.component('edit-dialog', {
  template: createEditDialogTemplate(`
  <div class="m1">
    <div class="m2m">
      <zeichenzaehler-label :elem="entity.name" :forid="'filialeEditForm_name'" :label="'Name'" :maxlength="'50'" :required="true"></zeichenzaehler-label>
      <input class="m2" id="filialeEditForm_name" maxlength="50" type="text" v-model="entity.name"></input>
    </div>
    <div class="m2">
      <zeichenzaehler-label :elem="entity.kuerzel" :forid="'filialeEditForm_kuerzel'" :label="'Kürzel für Aufträge'" :maxlength="'3'" :required="true"></zeichenzaehler-label>
      <input class="m2" id="filialeEditForm_kuerzel" maxlength="3" type="text" v-model="entity.kuerzel"></input>
    </div>
  </div>
  <div class="m1">
    <div class="m2m">
      <zeichenzaehler-label :elem="entity.strasse" :forid="'filialeEditForm_strasse'" :label="'Straße'" :maxlength="'100'" :required="true"></zeichenzaehler-label>
      <input class="m2" id="filialeEditForm_strasse" maxlength="100" type="text" v-model="entity.strasse"></input>
    </div>
    <div class="m6m">
      <zeichenzaehler-label :elem="entity.plz" :forid="'filialeEditForm_plz'" :label="'PLZ'" :maxlength="'8'" :required="true"></zeichenzaehler-label>
      <input class="m6" id="filialeEditForm_plz" maxlength="8" type="text" v-model="entity.plz"></input>
    </div>
    <div class="m3">
      <zeichenzaehler-label :elem="entity.ort" :forid="'filialeEditForm_ort'" :label="'Ort'" :maxlength="'50'" :required="true"></zeichenzaehler-label>
      <input class="m3" id="filialeEditForm_ort" maxlength="50" type="text" v-model="entity.ort"></input>
    </div>
  </div>
  <div class="m1">
    <div class="m2m">
      <zeichenzaehler-label :elem="entity.telefon" :forid="'filialeEditForm_telefon'" :label="'Telefon'" :maxlength="'50'" :required="true"></zeichenzaehler-label>
      <input class="m2" id="filialeEditForm_telefon" maxlength="50" type="text" v-model="entity.telefon"></input>
    </div>
    <div class="m2">
      <zeichenzaehler-label :elem="entity.email" :forid="'filialeEditForm_email'" :label="'E-Mail'" :maxlength="'50'" :required="true"></zeichenzaehler-label>
      <input class="m2" id="filialeEditForm_email" maxlength="50" type="text" v-model="entity.email"></input>
    </div>
  </div>
  <div class="m1">
    <zeichenzaehler-label :elem="entity.rechnungTextAndereFiliale" :forid="'filialeEditForm_rechnung_text_andere_filiale'" :label="'Rechnungstext: Besuchen Sie uns auch in unserer Filiale in'" :maxlength="'100'" :required="true"></zeichenzaehler-label>
    <input class="m1" id="filialeEditForm_rechnung_text_andere_filiale" maxlength="100" type="text" v-model="entity.rechnungTextAndereFiliale"></input>
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
      return this.entity && hasAllProperties(this.entity, ['name', 'kuerzel', 'strasse', 'plz', 'ort', 'telefon', 'email']);
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

