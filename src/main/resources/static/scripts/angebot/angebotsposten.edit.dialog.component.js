Vue.component('posten-edit-dialog', {
  i18n,
  template: createEditDialogTemplate(`
  <div class="m1">
    <div class="m6m">
      <label class="required" for="postenEditForm_menge">{{ $t("general.menge") }}</label>
      <input class="m6" id="postenEditForm_menge" type="number" v-model="entity.menge" />
    </div>
    <div class="m4m">
      <label class="required" for="postenEditForm_preis">{{ $t("general.preis") }}</label>
      <input class="m4" id="postenEditForm_preis" steps="0.01" type="number" v-model="entity.preis" />
    </div>
  </div>
  <div class="m1">
    <zeichenzaehler-label :elem="entity.bezeichnung" :forid="'postenEditForm_bezeichnung'" :label="$t('general.bezeichnung')" :maxlength="'500'"></zeichenzaehler-label>
    <textarea class="m1" id="postenEditForm_bezeichnung" maxlength="500" v-model="entity.bezeichnung"></textarea>
  </div>
      `),
  props: {
    entity: Object,
    title: String,
  },
  methods: {
    areRequiredFieldsNotEmpty: function() {
      return this.entity && hasAllPropertiesAndNotEmpty(this.entity, ['bezeichnung', 'menge', 'preis']) && this.entity.menge > 0;
    },
    saveFunc: function() {
      this.entity.menge = Number(this.entity.menge);
      this.entity.preis = Number(this.entity.preis);
      this.$emit('saved', this.entity);
    },
  }
});

