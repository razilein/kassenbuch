Vue.component('edit-dialog', {
  template: createEditDialogTemplate(`
  <div class="m1">
    <div class="m2m">
      <label class="required" for="filialeEditForm_name">Name</label>
      <input class="m2" id="filialeEditForm_name" type="text" v-model="entity.name"></input>
    </div>
    <div class="m2">
      <label class="required" for="filialeEditForm_kuerzel">Kürzel für Aufträge</label>
      <input class="m2" id="filialeEditForm_kuerzel" type="text" v-model="entity.kuerzel"></input>
    </div>
  </div>
  <div class="m1">
    <div class="m2m">
      <label class="required" for="filialeEditForm_strasse">Straße</label>
      <input class="m2" id="filialeEditForm_strasse" type="text" v-model="entity.strasse"></input>
    </div>
    <div class="m6m">
      <label class="required" for="filialeEditForm_plz">PLZ</label>
      <input class="m6" id="filialeEditForm_plz" type="text" v-model="entity.plz"></input>
    </div>
    <div class="m3">
      <label class="required" for="filialeEditForm_ort">Ort</label>
      <input class="m3" id="filialeEditForm_ort" type="text" v-model="entity.ort"></input>
    </div>
  </div>
  <div class="m1">
    <div class="m2m">
      <label class="required" for="filialeEditForm_telefon">Telefon</label>
      <input class="m2" id="filialeEditForm_telefon" type="text" v-model="entity.telefon"></input>
    </div>
    <div class="m2">
      <label class="required" for="filialeEditForm_email">E-Mail</label>
      <input class="m2" id="filialeEditForm_email" type="text" v-model="entity.email"></input>
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

