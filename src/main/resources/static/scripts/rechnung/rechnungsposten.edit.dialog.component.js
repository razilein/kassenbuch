Vue.component('posten-edit-dialog', {
  template: createEditDialogTemplate(`
  <div class="m1">
    <div class="m6m">
      <label class="required" for="postenEditForm_menge">Menge</label>
      <input class="m6" id="postenEditForm_menge" type="number" v-model="entity.menge" />
    </div>
    <div class="m4m">
      <label class="required" for="postenEditForm_rabatt">Rabatt</label>
      <input class="m4" id="postenEditForm_rabatt" steps="0.01" type="number" v-model="entity.rabatt" />
    </div>
  </div>
  <div class="m1">
    <zeichenzaehler-label :elem="entity.bezeichnung" :forid="'postenEditForm_bezeichnung'" :label="'Bezeichnung'" :maxlength="'500'"></zeichenzaehler-label>
    <textarea class="m1" id="postenEditForm_bezeichnung" maxlength="500" v-model="entity.bezeichnung"></textarea>
  </div>
  <div class="m1">
    <zeichenzaehler-label :elem="entity.seriennummer" :forid="'postenEditForm_seriennummer'" :label="'Seriennummer'" :maxlength="'100'"></zeichenzaehler-label>
    <input class="m1" id="postenEditForm_seriennummer" maxlength="100" type="text" v-model="entity.seriennummer" />
  </div>
  <div class="m1">
    <zeichenzaehler-label :elem="entity.hinweis" :forid="'postenEditForm_hinweis'" :label="'Hinweis'" :maxlength="'100'"></zeichenzaehler-label>
    <input class="m1" id="postenEditForm_hinweis" maxlength="100" type="text" v-model="entity.hinweis" />
  </div>
      `),
  props: {
    entity: Object,
    title: String,
  },
  methods: {
    areRequiredFieldsNotEmpty: function() {
      return this.entity && hasAllPropertiesAndNotEmpty(this.entity, ['bezeichnung', 'menge', 'rabatt']) && this.entity.menge > 0 && this.entity.rabatt > -0.01;
    },
    saveFunc: function() {
      this.$emit('saved', this.entity);
    },
  }
});

