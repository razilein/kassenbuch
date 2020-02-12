Vue.component('edit-dialog', {
  i18n,
  template: createEditDialogTemplate(`
  <div class="m1">
    <div class="m2m">
      <label class="required" for="produktEditForm_kategorie">{{ $t("inventar.produkt.kategorie") }}</label>
      <select class="m2" v-model="entity.gruppe.kategorie.id" v-on:change="updateGruppen()">
        <option value=""></option>
        <option :value="k.key" v-for="k in kategorien">{{k.value}}</option>
      </select>
    </div>
    <div class="m2">
      <label class="required" for="produktEditForm_gruppe">{{ $t("inventar.produkt.gruppe") }}</label>
      <select class="m2" v-model="entity.gruppe.id">
        <option value=""></option>
        <option :value="g.key" v-for="g in gruppen">{{g.value}}</option>
      </select>
    </div>
  </div>
  <div class="m1">
    <zeichenzaehler-label :elem="entity.bezeichnung" :forid="'produktEditForm_bezeichnung'" :label="$t('general.bezeichnung')" :maxlength="'500'" :required="true"></zeichenzaehler-label>
    <textarea class="m1" id="gruppeEditForm_bezeichnung" maxlength="500" v-model="entity.bezeichnung"></textarea>
  </div>
  <div class="m1">
    <div class="m2m">
      <zeichenzaehler-label :elem="entity.hersteller" :forid="'produktEditForm_hersteller'" :label="$t('inventar.produkt.hersteller')" :maxlength="'100'"></zeichenzaehler-label>
      <input class="m2" id="produktEditForm_hersteller" maxlength="100" type="text" v-model="entity.hersteller" />
    </div>
    <div class="m2">
      <zeichenzaehler-label :elem="entity.ean" :forid="'produktEditForm_ean'" :label="$t('inventar.produkt.ean')" :maxlength="'100'"></zeichenzaehler-label>
      <input class="m2" id="produktEditForm_ean" maxlength="100" v-model="entity.ean" />
    </div>
  </div>
  <div class="m1">
    <div class="m4m" style="margin-top: 15px;">
      <label class="container checkbox">{{ $t("inventar.produkt.unbegrenzt") }}
        <input id="produktEditForm_bestandUnendlich" type="checkbox" v-model="entity.bestandUnendlich" v-on:change="if (entity.bestandUnendlich) { entity.bestand = 0; entity.preisEkNetto = null; entity.preisEkBrutto = null; }" />
        <span class="checkmark"></span>
      </label>
    </div>
    <div class="m4m">
      <label for="produktEditForm_bestand">{{ $t("inventar.produkt.bestand") }}</label>
      <input class="m4" id="produktEditForm_ean" min="0" step="1" type="number" v-model="entity.bestand" :readonly="entity.bestandUnendlich" />
    </div>
    <div class="m4m">
      <label for="produktEditForm_preisEkNetto">{{ $t("inventar.produkt.ekPreisN") }}</label>
      <input class="m4" id="produktEditForm_preisEkNetto" min="0.00" step="1.00" type="number" v-model="entity.preisEkNetto" :readonly="entity.bestandUnendlich" v-on:change="berechnePreisByEkNetto()" />
    </div>
    <div class="m4">
      <label for="produktEditForm_preisEkBrutto">{{ $t("inventar.produkt.ekPreisB") }}</label>
      <input class="m4" id="produktEditForm_preisEkBrutto" min="0.00" step="1.00" type="number" v-model="entity.preisEkBrutto" :readonly="entity.bestandUnendlich" v-on:change="berechnePreisByEkBrutto()" />
    </div>
  </div>
  <div class="m1">
    <div class="m2m">
      <div class="m3 right" v-if="!entity.bestandUnendlich">
        {{ $t("inventar.produkt.vorschlagVk") }}:<br>
        <span v-if="entity.preisEkBrutto">{{ $t("inventar.produkt.ekPreisB10") }}: {{formatMoney(entity.preisEkBrutto * 1.10)}}<br></span>
        <span v-if="entity.preisEkBrutto">{{ $t("inventar.produkt.aufgerundet") }}: {{formatMoney(Math.ceil(entity.preisEkBrutto * 1.10))}}</span>
      </div>
    </div>
    <div class="m2">
      <div class="m4m">
        <label for="produktEditForm_preisVkNetto">{{ $t("inventar.produkt.vkPreisN") }}</label>
        <input class="m4" id="produktEditForm_preisVkNetto" min="0.00" step="1.00" type="number" v-model="entity.preisVkNetto" v-on:change="berechnePreisByVkNetto()" />
      </div>
      <div class="m4">
        <label for="produktEditForm_preisVkBrutto">{{ $t("inventar.produkt.vkPreisB") }}</label>
        <input class="m4" id="produktEditForm_preisVkBrutto" min="0.00" step="1.00" type="number" v-model="entity.preisVkBrutto" v-on:change="berechnePreisByVkBrutto()" />
      </div>
    </div>
  </div>
      `),
  props: {
    duplicate: Boolean,
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
    },
    berechnePreisByVkBrutto: function() {
      var preisVkbrutto = this.entity.preisVkBrutto;
      this.entity.preisVkNetto = Number((preisVkbrutto / 1.19).toFixed(2));
    },
    berechnePreisByEkNetto: function() {
      this.entity.preisEkBrutto = Number((this.entity.preisEkNetto * 1.19).toFixed(2));
    },
    berechnePreisByEkBrutto: function() {
      var preisEkBrutto = this.entity.preisEkBrutto;
      this.entity.preisEkNetto = Number((preisEkBrutto / 1.19).toFixed(2));
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
      showLoader();
      this.executeSave()
        .then(this.closeAndReturnResponse)
        .then(hideLoader);
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
      if (this.duplicate) {
        response.data.id = null;
      }
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

