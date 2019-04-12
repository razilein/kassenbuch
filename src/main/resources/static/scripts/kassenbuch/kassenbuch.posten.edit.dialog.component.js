Vue.component('posten-edit-dialog', {
  template: createEditDialogTemplate(`
  <div class="m1">
    <zeichenzaehler-label :elem="entity.verwendungszweck" :forid="'kassenbuchEditForm_verwendungszweck'" :label="'Bezeichnung'" :maxlength="'200'" :required="true"></zeichenzaehler-label>
    <input class="m1" id="kassenbuchEditForm_verwendungszweck" maxlength="200" type="text" v-model="entity.verwendungszweck" />
  </div>
  <div class="m6">
    <div style="height: 15px;"></div>
    <label class="container radio" v-for="art in arten">
      <input
        :checked="entity.eintragungsart === art.key"
        name="art"
        type="radio"
        :value="art.key"
        v-model="entity.eintragungsart"
      >{{art.value}}</input>
      <span class="checkmark"></span>
    </label>
  </div>
  <div class="m4">
    <label class="required" for="kassenbuchEditForm_betrag">Betrag</label>
    <input class="m4" id="kassenbuchEditForm_betrag" type="number" step=".01" v-model="entity.betrag" />
  </div>
      `),
  props: {
    entity: Object,
  },
  data: function() {
    return {
      entity: {},
      arten: [
        { key: -1, value: '-' },
        { key: 1, value: '+' }
      ],
      title: 'Eintragung hinzufügen'
    };
  },
  methods: {
    areRequiredFieldsNotEmpty: function() {
      return this.entity && hasAllPropertiesAndNotEmpty(this.entity, ['betrag', 'verwendungszweck']);
    },
    saveFunc: function() {
      this.$emit('saved', this.entity);
    },
  }
});

