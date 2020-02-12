Vue.component('rabatt-dialog', {
  i18n,
  template: createEditDialogTemplate(`
  <div class="m6">
    <label for="rabattForm_menge">{{ $t("general.menge") }}</label>
    <input class="m6" id="rabattForm_menge" readonly type="number" v-model="entity.menge">
  </div>
  <div class="m1">
    <div class="m4m">
      <label for="rabattForm_preisEk">{{ $t("inventar.produkt.ekPreisB") }}</label>
      <input class="m4" id="rabattForm_preisEkBrutto" readonly step="0.01" type="number" v-model="entity.produkt.preisEkBrutto"></input>
    </div>
    <div class="m4">
      <label for="rabattForm_preisVk">{{ $t("inventar.produkt.vkPreisB") }}</label>
      <input class="m4" id="rabattForm_preisVkBrutto" readonly step="0.01" type="number" v-model="entity.produkt.preisVkBrutto"></input>
    </div>
  </div>
  <div class="m1" v-if="entity.menge > 1">
    <div class="m4m">
      <label for="rabattForm_gesamt_preisEk">{{ $t("inventar.produkt.ekPreisBGesamt") }}</label>
      <input class="m4" id="rabattForm_gesamt_preisEk" readonly step="0.01" type="number" v-model="calculatedEntity.preisEk"></input>
    </div>
    <div class="m4">
      <label for="rabattForm_gesamt_preisVk">{{ $t("inventar.produkt.vkPreisBGesamt") }}</label>
      <input class="m4" id="rabattForm_gesamt_preisVk" readonly step="0.01" type="number" v-model="calculatedEntity.preisVk"></input>
    </div>
  </div>
  <div class="m1">
    <div class="m4m">
      <label for="rabattForm_rabatt_prozent">{{ $t("inventar.produkt.rabattProzent") }}</label>
      <input class="m4" id="rabattForm_rabatt_prozent" min="0" max="100" step="0.01" type="number" v-model="calculatedEntity.rabattProzent" v-on:change="berechneRabatt()"></input>
    </div>
    <div class="m4">
      <label class="required" for="rabattForm_rabatt">{{ $t("general.rabatt") }}</label>
      <input class="m4" id="rabattForm_rabatt" step="0.01" type="number" v-model="calculatedEntity.rabatt" v-on:change="berechneRabattProzent()"></input>
    </div>
  </div>
  <div class="m1">
    <div class="m4m">
      <label for="rabattForm_gewinn">{{ $t("inventar.produkt.gewinn") }}</label>
      <input class="m4" id="rabattForm_gewinn" readonly step="0.01" type="number" v-model="calculatedEntity.gewinn"></input>
    </div>
    <div class="m4">
      <label for="rabattForm_gesamtpreis">{{ $t("general.gesamtpreis") }}</label>
      <input class="m4" id="rabattForm_gesamtpreis" readonly step="0.01" type="number" v-model="calculatedEntity.gesamt"></input>
    </div>
  </div>
      `),
  props: {
    entity: Object,
  },
  data: function() {
    var gesamt = this.entity.produkt.preisVkBrutto * this.entity.menge - this.entity.rabatt;
    var preisEk = this.entity.produkt.preisEkBrutto * this.entity.menge;
    var preisVk = this.entity.produkt.preisVkBrutto * this.entity.menge;
    var rabatt = this.entity.rabatt || 0;
    var gewinn = Number((gesamt - rabatt - preisEk).toFixed(2));
    return {
      calculatedEntity: {
        gesamt: gesamt,
        gewinn: gewinn,
        preisEk: preisEk,
        preisVk: preisVk,
        rabatt: rabatt,
        rabattProzent: rabatt * 100 / preisVk
      },
      title: this.$t('rechnung.rabattrechner')
    };
  },
  methods: {
    areRequiredFieldsNotEmpty: function() {
      return this.entity && hasAllPropertiesAndNotEmpty(this.calculatedEntity, ['rabatt']);
    },
    berechneRabatt: function() {
      var rabattProzent = this.calculatedEntity.rabattProzent;
      if (rabattProzent < 0.01) {
        this.calculatedEntity.rabatt = 0;
      } else {
        this.calculatedEntity.rabatt = Number((this.calculatedEntity.gesamt * rabattProzent / 100).toFixed(2));
      }
      this.berechneGewinn();
      this.berechneGesamt();
    },
    berechneRabattProzent: function() {
      var rabatt = this.calculatedEntity.rabatt;
      if (rabatt < 0.01) {
        this.calculatedEntity.rabattProzent = 0;
      } else {
        this.calculatedEntity.rabattProzent = Number((rabatt * 100 / this.calculatedEntity.preisVk).toFixed(2));
      }
      this.berechneGewinn();
      this.berechneGesamt();
    },
    berechneGewinn: function() {
      var gesamt = this.calculatedEntity.preisVk;
      this.calculatedEntity.gewinn = Number((gesamt - this.calculatedEntity.rabatt - this.calculatedEntity.preisEk).toFixed(2));
    },
    berechneGesamt: function() {
      var gesamt = this.calculatedEntity.preisVk;
      this.calculatedEntity.gesamt = Number((gesamt - this.calculatedEntity.rabatt).toFixed(2));
    },
    saveFunc: function() {
      this.$emit('saved', this.calculatedEntity.rabatt);
    },
  }
});

