Vue.component('ersteller-select', {
  template: `
  <div class="m1">
    <label class="required" for="editForm_mitarbeiter">Ersteller</label>
    <select class="m1" id="editForm_mitarbeiter" v-model="selection" v-on:change="changeMitarbeiter()">
      <option value=""></option>
      <option :value="m.value" v-for="m in mitarbeiter">{{m.value}}</option>
    </select>
  </div>
  `,
  props: {
    entity: String,
  },
  data: function() {
    this.loadEntity();
    return {
      mitarbeiter: [],
      selection: '',
    };
  },
  methods: {
    loadEntity: function() {
      showLoader();
      this.getMitarbeiter()
        .then(this.setMitarbeiter)
        .then(hideLoader);
    },
    getMitarbeiter: function() {
      return axios.get('/reparatur/mitarbeiter');
    },
    setMitarbeiter: function(response) {
      this.mitarbeiter = response.data;
    },
    changeMitarbeiter: function() {
      this.$emit('changed', this.selection);
    },
  }
});

