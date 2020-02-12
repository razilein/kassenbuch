Vue.component('ersteller-select', {
  i18n,
  template: `
  <div class="m1">
    <label class="required" for="editForm_mitarbeiter">{{ $t("general.ersteller") }}</label>
    <select class="m1" id="editForm_mitarbeiter" v-model="selection" v-on:change="changeMitarbeiter()">
      <option value=""></option>
      <option :value="m.value" v-for="m in mitarbeiter">{{m.value}}</option>
    </select>
  </div>
  `,
  props: {
    reset: Boolean,
  },
  data: function() {
    this.loadEntity();
    return {
      mitarbeiter: [],
      selection: '',
    };
  },
  mounted() {
    this.$watch(
      function() {
        return this.reset;
      },
      function(newVal, oldVal) {
        if (newVal === true && oldVal === false) {
          this.selection = '';
          this.$emit('resetted');
        }
      },
      { deep: true }
    );
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

