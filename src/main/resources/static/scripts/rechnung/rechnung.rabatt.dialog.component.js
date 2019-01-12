Vue.component('rabatt-dialog', {
  template: createEditDialogTemplate(`
  <div class="m6">
    <label for="rabattForm_menge">Menge</label>
    <input class="m6" id="rabattForm_menge" readonly type="number" v-model="entity.menge">
  </div>
  <div class="m1">
    <div class="m4m">
      <label for="rabattForm_preisEk">Preis EK</label>
      <input class="m4" id="rabattForm_preisEkBrutto" readonly step="0.01" type="number" v-model="entity.produkt.preisEkBrutto"></input>
    </div>
    <div class="m4">
      <label for="rabattForm_preisVk">Preis VK</label>
      <input class="m4" id="rabattForm_preisVkBrutto" readonly step="0.01" type="number" v-model="entity.produkt.preisVkBrutto"></input>
    </div>
  </div>
  <div class="m1" v-if="entity.menge > 1">
    <div class="m4m">
      <label for="rabattForm_gesamt_preisEk">Gesamtpreis EK</label>
      <input class="m4" id="rabattForm_gesamt_preisEk" readonly step="0.01" type="number" v-model="calculatedEntity.preisEk"></input>
    </div>
    <div class="m4">
      <label for="rabattForm_gesamt_preisVk">Gesamtpreis VK</label>
      <input class="m4" id="rabattForm_gesamt_preisVk" readonly step="0.01" type="number" v-model="calculatedEntity.preisVk"></input>
    </div>
  </div>
  <div class="m1">
    <div class="m4m">
      <label for="rabattForm_rabatt_prozent">Rabatt in %</label>
      <input class="m4" id="rabattForm_rabatt_prozent" min="0" max="100" step="0.01" type="number" v-model="calculatedEntity.rabattProzent" v-on:change="berechneRabatt()"></input>
    </div>
    <div class="m4">
      <label class="required" for="rabattForm_rabatt">Rabatt</label>
      <input class="m4" id="rabattForm_rabatt" step="0.01" type="number" v-model="calculatedEntity.rabatt" v-on:change="berechneRabattProzent()"></input>
    </div>
  </div>
  <div class="m1">
    <div class="m4m">
      <label for="rabattForm_gewinn">Gewinn</label>
      <input class="m4" id="rabattForm_gewinn" readonly step="0.01" type="number" v-model="calculatedEntity.gewinn"></input>
    </div>
    <div class="m4">
      <label for="rabattForm_gesamtpreis">Gesamtpreis</label>
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
      title: 'Rabattrechner'
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
    },
    berechneRabattProzent: function() {
      var rabatt = this.calculatedEntity.rabatt;
      if (rabatt < 0.01) {
        this.calculatedEntity.rabattProzent = 0;
      } else {
        this.calculatedEntity.rabattProzent = Number((rabatt * 100 / this.calculatedEntity.preisVk).toFixed(2));
      }
      this.berechneGewinn();
    },
    berechneGewinn: function() {
      this.calculatedEntity.gewinn = Number((this.calculatedEntity.gesamt - this.calculatedEntity.rabatt - this.calculatedEntity.preisEk).toFixed(2));
    },
    saveFunc: function() {
      this.$emit('saved', this.calculatedEntity.rabatt);
    },
  }
});

