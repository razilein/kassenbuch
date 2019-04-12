Vue.component('edit-dialog', {
  template: createEditDialogTemplate(`
  <div class="m1">
    <label class="required" for="gruppeEditForm_kategorie">Kategorie</label>
    <select class="m1" v-model="entity.kategorie.id">
      <option :value="k.key" v-for="k in kategorien">{{k.value}}</option>
    </select>
  </div>
  <div class="m1">
    <zeichenzaehler-label :elem="entity.bezeichnung" :forid="'gruppeEditForm_bezeichnung'" :label="'Bezeichnung'" :maxlength="'100'" :required="true"></zeichenzaehler-label>
    <input class="m1" focus id="gruppeEditForm_bezeichnung" maxlength="100" type="text" v-model="entity.bezeichnung"></input>
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
        kategorie: {}
      },
      kategorien: []
    };
  },
  methods: {
    areRequiredFieldsNotEmpty: function() {
      return this.entity && this.entity.kategorie && hasAllProperties(this.entity, ['bezeichnung', 'kategorie.id']);
    },
    loadEntity: function() {
      showLoader();
      this.getEntity()
        .then(this.setEntity)
        .then(this.getKategorien)
        .then(this.setKategorien)
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
    getKategorien: function() {
      return axios.get('/inventar/gruppe/kategorie');
    },
    setKategorien: function(response) {
      this.kategorien = response.data;
    }
  }
});

