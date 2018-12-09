Vue.component('rechte-edit-dialog', {
  template: createEditDialogTemplate(`
  <div class="m1" v-for="e in entity.rollen">
    <label class="container checkbox">{{e.beschreibung}}
      <input id="rechteForm_recht_{{e.id}}" type="checkbox" v-model="e.right" />
      <span class="checkmark"></span>
    </label>
  </div>
      `),
  props: {
    restUrlGet: String,
    restUrlSave: String,
    title: String,
  },
  data: function() {
    this.load();
    return {
      entity: {},
      rechte: {}
    };
  },
  methods: {
    areRequiredFieldsNotEmpty: function() {
      return this.entity;
    },
    load: function() {
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
    }
  }
});

