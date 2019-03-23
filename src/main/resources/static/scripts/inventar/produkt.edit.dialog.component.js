Vue.component('edit-dialog', {
  template: createEditDialogTemplate(`
  <div class="m1">
    <div class="m2m">
      <label class="required" for="produktEditForm_kategorie">Kategorie</label>
      <select class="m2" v-model="entity.gruppe.kategorie.id" v-on:change="updateGruppen()">
        <option value=""></option>
        <option :value="k.key" v-for="k in kategorien">{{k.value}}</option>
      </select>
    </div>
    <div class="m2">
      <label class="required" for="produktEditForm_gruppe">Gruppe</label>
      <select class="m2" v-model="entity.gruppe.id">
        <option value=""></option>
        <option :value="g.key" v-for="g in gruppen">{{g.value}}</option>
      </select>
    </div>
  </div>
  <div class="m1">
    <label class="required" for="produktEditForm_bezeichnung">Bezeichnung</label>
    <textarea class="m1" id="gruppeEditForm_bezeichnung" maxlength="500" v-model="entity.bezeichnung"></textarea>
  </div>
  <div class="m1">
    <div class="m2m">
      <label for="produktEditForm_hersteller">Hersteller</label>
      <input class="m2" id="produktEditForm_hersteller" type="text" v-model="entity.hersteller" />
    </div>
    <div class="m2">
      <label for="produktEditForm_ean">EAN</label>
      <input class="m2" id="produktEditForm_ean" maxlength="100" v-model="entity.ean" />
    </div>
  </div>
  <div class="m1">
    <div class="m4m" style="margin-top: 15px;">
      <label class="container checkbox">Unbegrenzt
        <input id="produktEditForm_bestandUnendlich" type="checkbox" v-model="entity.bestandUnendlich" v-on:change="if (entity.bestandUnendlich) { entity.bestand = 0; entity.preisEkNetto = null; entity.preisEkBrutto = null; }" />
        <span class="checkmark"></span>
      </label>
    </div>
    <div class="m4m">
      <label for="produktEditForm_bestand">Bestand</label>
      <input class="m4" id="produktEditForm_ean" min="0" step="1" type="number" v-model="entity.bestand" :readonly="entity.bestandUnendlich" />
    </div>
    <div class="m4m">
      <label for="produktEditForm_preisEkNetto">EK-Preis (netto)</label>
      <input class="m4" id="produktEditForm_preisEkNetto" min="0.00" step="1.00" type="number" v-model="entity.preisEkNetto" :readonly="entity.bestandUnendlich" v-on:change="berechnePreisByEkNetto()" />
    </div>
    <div class="m4">
      <label for="produktEditForm_preisEkBrutto">EK-Preis (brutto)</label>
      <input class="m4" id="produktEditForm_preisEkBrutto" min="0.00" step="1.00" type="number" v-model="entity.preisEkBrutto" :readonly="entity.bestandUnendlich" v-on:change="berechnePreisByEkBrutto()" />
    </div>
  </div>
  <div class="m1">
    <div class="m2 right">
      <div class="m4m">
        <label for="produktEditForm_preisVkNetto">VK-Preis (netto)</label>
        <input class="m4" id="produktEditForm_preisVkNetto" min="0.00" step="1.00" type="number" v-model="entity.preisVkNetto" v-on:change="berechnePreisByVkNetto()" />
      </div>
      <div class="m4">
        <label for="produktEditForm_preisVkBrutto">VK-Preis (brutto)</label>
        <input class="m4" id="produktEditForm_preisVkBrutto" min="0.00" step="1.00" type="number" v-model="entity.preisVkBrutto" v-on:change="berechnePreisByVkBrutto()" />
      </div>
    </div>
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
      entity: {
        gruppe: {
          kategorie: {}
        }
      },
      gruppen: [],
      kategorien: []
    };
  },
  methods: {
    areRequiredFieldsNotEmpty: function() {
      return this.entity && this.entity.gruppe && hasAllPropertiesAndNotEmpty(this.entity, ['bezeichnung', 'gruppe.id']);
    },
    berechnePreisByVkNetto: function() {
      var preisVkNetto = this.entity.preisVkNetto;
      this.entity.preisVkBrutto = Number((preisVkNetto * 1.19).toFixed(2));
      
      if (!this.entity.bestandUnendlich) {
        this.entity.preisEkNetto = Number((preisVkNetto / 1.1).toFixed(2));
        this.entity.preisEkBrutto = Number((this.entity.preisEkNetto * 1.19).toFixed(2));
      }
    },
    berechnePreisByVkBrutto: function() {
      var preisVkbrutto = this.entity.preisVkBrutto;
      this.entity.preisVkNetto = Number((preisVkbrutto / 1.19).toFixed(2));
      
      if (!this.entity.bestandUnendlich) {
        this.entity.preisEkNetto = Number((this.entity.preisVkNetto / 1.1).toFixed(2));
        this.entity.preisEkBrutto = Number((this.entity.preisEkNetto * 1.19).toFixed(2));
      }
    },
    berechnePreisByEkNetto: function() {
      var preisEkNetto = this.entity.preisEkNetto;
      this.entity.preisEkBrutto = Number((this.entity.preisEkNetto * 1.19).toFixed(2));
      
      this.entity.preisVkNetto = Number((preisEkNetto * 1.1).toFixed(2));
      this.entity.preisVkBrutto = Number((this.entity.preisVkNetto * 1.19).toFixed(2));
    },
    berechnePreisByEkBrutto: function() {
      var preisEkBrutto = this.entity.preisEkBrutto;
      this.entity.preisEkNetto = Number((preisEkBrutto / 1.19).toFixed(2));
      
      this.entity.preisVkNetto = Number((this.entity.preisEkNetto * 1.1).toFixed(2));
      this.entity.preisVkBrutto = Number((this.entity.preisVkNetto * 1.19).toFixed(2));
    },
    loadEntity: function() {
      showLoader();
      this.getEntity()
        .then(this.setEntity)
        .then(this.getKategorien)
        .then(this.setKategorien)
        .then(this.getGruppen)
        .then(this.setGruppen)
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
    updateGruppen: function() {
      this.getGruppen().then(this.setGruppen);
    },
    getEntity: function() {
      return axios.get(this.restUrlGet);
    },
    setEntity: function(response) {
      this.entity = response.data;
    },
    getGruppen: function() {
      var kategorieId = this.entity.gruppe.kategorie.id;
      if (kategorieId !== null) {
        return axios.get('/inventar/produkt/gruppe/' + kategorieId);
      } else {
        return { data: [] };
      }
    },
    setGruppen: function(response) {
      this.gruppen = response.data;
    },
    getKategorien: function() {
      return axios.get('/inventar/produkt/kategorie');
    },
    setKategorien: function(response) {
      this.kategorien = response.data;
    }
  }
});

