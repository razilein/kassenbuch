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
        <input id="produktEditForm_bestandUnendlich" type="checkbox" v-model="entity.bestandUnendlich" v-on:change="if (entity.bestandUnendlich) { entity.bestand = 0; }" />
        <span class="checkmark"></span>
      </label>
    </div>
    <div class="m4m">
      <label for="produktEditForm_bestand">Bestand</label>
      <input class="m4" id="produktEditForm_ean" min="0" step="1" type="number" v-model="entity.bestand" :readonly="entity.bestandUnendlich" />
    </div>
    <div class="m4m">
      <label for="produktEditForm_preisEk">EK-Preis (brutto)</label>
      <input class="m4" id="produktEditForm_preisEk" min="0.00" step="1.00" type="number" v-model="entity.preisEk" />
    </div>
    <div class="m4">
      <label for="produktEditForm_preisVk">VK-Preis (brutto)</label>
      <input class="m4" id="produktEditForm_preisVk" min="0.00" step="1.00" type="number" v-model="entity.preisVk" />
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

