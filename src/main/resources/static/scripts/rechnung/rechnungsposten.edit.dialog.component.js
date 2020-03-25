Vue.component('posten-edit-dialog', {
  i18n,
  template: createEditDialogTemplate(`
  <div class="m1">
    <div class="m6m">
      <label class="required" for="postenEditForm_menge">{{ $t("general.menge") }}</label>
      <input class="m6" id="postenEditForm_menge" type="number" v-model="entity.menge" />
    </div>
    <div class="m4m">
      <label class="required" for="postenEditForm_rabatt">{{ $t("general.rabatt") }}</label>
      <input class="m4" id="postenEditForm_rabatt" steps="0.01" type="number" v-model="entity.rabatt" />
    </div>
  </div>
  <div class="m1">
    <zeichenzaehler-label :elem="entity.bezeichnung" :forid="'postenEditForm_bezeichnung'" :label="$t('general.bezeichnung')" :maxlength="'500'"></zeichenzaehler-label>
    <textarea class="m1" id="postenEditForm_bezeichnung" maxlength="500" v-model="entity.bezeichnung"></textarea>
  </div>
  <div class="m1">
    <zeichenzaehler-label :elem="entity.seriennummer" :forid="'postenEditForm_seriennummer'" :label="$t('reparatur.seriennummer')" :maxlength="'100'"></zeichenzaehler-label>
    <input class="m1" id="postenEditForm_seriennummer" maxlength="100" v-on:blur="removeLastComma" v-on:keyup.enter="entity.seriennummer = entity.seriennummer += ', ';" type="text" v-model="entity.seriennummer" />
  </div>
  <div class="m1">
    <zeichenzaehler-label :elem="entity.hinweis" :forid="'postenEditForm_hinweis'" :label="$t('rechnung.hinweis')" :maxlength="'300'"></zeichenzaehler-label>
    <textarea class="m1" id="postenEditForm_hinweis" maxlength="300" type="text" v-model="entity.hinweis"></textarea>
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
      this.entity.menge = Number(this.entity.menge);
      this.entity.rabatt = Number(this.entity.rabatt);
      this.$emit('saved', this.entity);
    },
    removeLastComma: function() {
      var nummer = this.entity.seriennummer;
      if (nummer) {
        nummer = nummer.trim();
        var lastChar = nummer.substring(nummer.length - 1, nummer.length);
        if (lastChar === ',') {
          nummer = nummer.substring(0, nummer.length - 1);
        }
      }
      this.entity.seriennummer = nummer;
    },
  }
});

