Vue.component('edit-dialog', {
  i18n,
  template: createEditDialogTemplate(`
  <div class="m1">
    <zeichenzaehler-label :elem="entity.bezeichnung" :forid="'kategorieEditForm_bezeichnung'" :label="$t('general.bezeichnung')" :maxlength="'100'" :required="true"></zeichenzaehler-label>
    <input class="m1" focus id="kategorieEditForm_bezeichnung" maxlength="100" type="text" v-model="entity.bezeichnung"></input>
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
      return this.entity && hasAllProperties(this.entity, ['bezeichnung']);
    },
    loadEntity: function() {
      showLoader();
      this.getEntity()
        .then(this.setEntity)
        .then(hideLoader);
    },
    saveFunc: function() {
      showLoader();
      this.executeSave()
        .then(this.closeAndReturnResponse)
        .then(hideLoader);
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

