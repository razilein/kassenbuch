Vue.component('edit-dialog', {
  template: createEditDialogTemplate(`
  <div class="m1">
    <label for="filialeEditForm_name">Name</label>
    <input class="m1" id="filialeEditForm_name" type="text" v-model="entity.name"></input>
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

