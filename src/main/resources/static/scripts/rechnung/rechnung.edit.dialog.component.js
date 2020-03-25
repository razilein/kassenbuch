Vue.component('edit-dialog', {
  i18n,
  template: createEditDialogTemplate(`
  <div class="m1">
    <div class="m6m">
      <label for="rechnungEditForm_nummer">{{ $t("general.nummer") }}</label>
      <input class="m6" readonly type="text" v-model="entity.rechnung.nummer" />
    </div>
    <div class="m4m">
      <label class="required" for="rechnungEditForm_datum">{{ $t("general.datum") }}</label>
      <input class="m4" type="date" v-model="entity.rechnung.datum" />
    </div>
    <div class="m4m">
      <label for="rechnungEditForm_lieferdatum">{{ $t("general.lieferdatum") }}</label>
      <input class="m4" id="rechnungEditForm_lieferdatum" title="Wird das Feld leer gelassen, entspricht das Lieferdatum dem Rechnungsdatum" type="date" v-model="entity.rechnung.lieferdatum" />
    </div>
  </div>
  <div class="m1">
    <div class="m2m">
      <label for="rechnungEditForm_ersteller">{{ $t("general.ersteller") }}</label>
      <input class="m2" readonly type="text" v-model="entity.rechnung.ersteller" />
    </div>
    <div class="m6m">
      <label for="rechnungEditForm_endpreis">{{ $t("general.endpreis") }}</label>
      <input class="m6" readonly type="text" v-model="endpreis" />
    </div>
  </div>
  <div style="width: 1000px;">
    <table>
      <thead>
        <th style="width: 100px;">{{ $t("general.funktionen") }}</th>
        <th style="width: 30px;">{{ $t("general.pos") }}</th>
        <th style="width: 550px;">{{ $t("general.bezeichnung") }}</th>
        <th style="width: 120px;">{{ $t("rechnung.sn") }}</th>
        <th style="width: 120px;">{{ $t("rechnung.hinweis") }}</th>
        <th style="width: 50px;">{{ $t("general.menge") }}</th>
        <th style="width: 100px;">{{ $t("general.preis") }}</th>
        <th style="width: 100px;">{{ $t("general.rabatt") }}</th>
        <th style="width: 100px;">{{ $t("general.gesamt") }}</th>
      </thead>
      <tbody>
        <tr v-for="(posten, index) in entity.posten">
          <td>
            <button class="edit" :title="$t('angebot.postenBearbeiten')" v-on:click="editPosten(index)"></button>
          </td>
          <td>{{posten.position}}</td>
          <td>{{posten.bezeichnung}}</td>
          <td>{{posten.seriennummer}}</td>
          <td>{{posten.hinweis}}</td>
          <td>{{posten.menge}}</td>
          <td>{{ formatMoney(posten.preis) }}</td>
          <td>{{ formatMoney(posten.rabatt) }}</td>
          <td>{{ formatMoney(posten.menge * posten.preis - posten.rabatt) }}</td>
        </tr>
      </tbody>
    </table>
  </div>
  <div class="m1" style="height: 10px;"></div>
  <div class="m1">
    <div class="m1">
      <label class="container radio" v-for="art in zahlarten">
        <input
          :checked="entity.rechnung.art === art.key"
          name="art"
          type="radio"
          :value="art.key"
          v-model="entity.rechnung.art"
        >{{art.value}}</input>
        <span class="checkmark"></span>
      </label>
    </div>
    <div class="m1" v-if="entity.rechnung.bestellung">
      <label for="rechnungEditForm_bestellung">{{ $t("bestellung.titelK") }}</label>
      <button class="bestellung btnSmall" :title="$t('bestellung.suchen')" @click="showBestellungDialog = true"></button>
      <button class="delete btnSmall" :title="$t('bestellung.deselektieren')" @click="entity.rechnung.bestellung = {}"></button>
      <textarea class="m1" id="rechnungEditForm_bestellung" readonly v-model="entity.rechnung.bestellung.beschreibung"></textarea>
    </div>
    <div class="m1" v-if="entity.rechnung.reparatur">
      <label for="rechnungEditForm_reparatur">{{ $t("reparatur.reparaturauftrag") }}</label>
      <button class="zahnrad btnSmall" :title="$t('reparatur.suchen')" @click="showReparaturDialog = true"></button>
      <button class="delete btnSmall" :title="$t('reparatur.deselektieren')" @click="entity.rechnung.reparatur = {}"></button>
      <input class="m1" id="rechnungEditForm_reparatur" readonly type="text" v-model="entity.rechnung.reparatur.nummer" />
    </div>
    <div class="m1">
      <label class="container checkbox">{{ $t("rechnung.nameDrucken") }}
        <input id="rechnungEditForm_name_drucken" type="checkbox" v-model="entity.rechnung.nameDrucken" />
        <span class="checkmark"></span>
      </label>
    </div>
    <div class="m1" v-if="entity.rechnung.kunde && entity.rechnung.kunde.firmenname && entity.rechnung.kunde.nachname">
      <label class="container checkbox">{{ $t("rechnung.nameDruckenFirma") }}
        <input id="rechnungEditForm_name_drucken_bei_firma" type="checkbox" v-model="entity.rechnung.nameDruckenBeiFirma" />
        <span class="checkmark"></span>
      </label>
    </div>
    <div class="m1" v-if="entity.rechnung.kunde">
      <label for="rechnungEditForm_kunde">{{ $t("general.kunde") }}</label>
      <button class="kunde btnSmall" :title="$t('kunde.suchen')" @click="showKundeDialog = true"></button>
      <button class="delete btnSmall" :title="$t('kunde.deselektieren')" @click="entity.rechnung.kunde = {}"></button>
      <textarea class="m1" id="rechnungEditForm_kunde" readonly v-model="entity.rechnung.kunde.completeWithAdress"></textarea>
    </div>
  </div>
<messages-box v-bind:text="result" v-if="showDialog" @close="showDialog = false"></messages-box>
<kunde-suchen-dialog
  :kunde="entity.rechnung.kunde"
  v-if="showKundeDialog"
  @close="showKundeDialog = false"
  @saved="handleKundeResponse"
></kunde-suchen-dialog>
<bestellung-suchen-dialog
  :kunde="entity.rechnung.kunde"
  v-if="showBestellungDialog"
  @close="showBestellungDialog = false"
  @saved="handleBestellungResponse"
></bestellung-suchen-dialog>
<reparatur-suchen-dialog
  :kunde="entity.rechnung.kunde"
  v-if="showReparaturDialog"
  @close="showReparaturDialog = false"
  @saved="handleReparaturResponse"
></reparatur-suchen-dialog>
<posten-edit-dialog
  :entity="editEntity"
  :title="$t('angebot.postenBearbeiten')"
  v-if="showEditDialog"
  @close="showEditDialog = false"
  @saved="handleEditResponse"
></posten-edit-dialog>
      `, true),
  props: {
    restUrlGet: String,
    restUrlSave: String,
    title: String,
  },
  data: function() {
    this.loadEntity();
    return {
      editEntity: {},
      endpreis: 0.00,
      entity: {
        rechnung: {
          kunde: {},
          reparatur: {},
        },
        posten: []
      },
      rabattEntity: {},
      showDialog: false,
      showBestellungDialog: false,
      showEditDialog: false,
      showKundeDialog: false,
      showRechnungspositionDialog: false,
      showReparaturDialog: false,
      zahlarten: []
    };
  },
  methods: {
    areRequiredFieldsNotEmpty: function() {
      var kundeRequired = (this.entity.rechnung.art === 2 || this.entity.rechnung.art === 3);
      return this.entity && this.entity.rechnung && hasAllPropertiesAndNotEmpty(this.entity, ['rechnung.datum']) && (!kundeRequired || this.entity.rechnung.kunde.nummer);
    },
    berechneEndpreis: function() {
      var endpreis = 0;
      this.entity.posten.forEach(function(element) {
        var postenPreis = (element.menge || 1) * (element.preis || 0) - (element.rabatt || 0);  
        endpreis = endpreis + Number((postenPreis).toFixed(2));
      });
      endpreis = endpreis || 0;
      endpreis = endpreis < 0 ? 0 : endpreis;
      this.endpreis = formatMoney(endpreis);
    },
    editPosten: function(index) {
      var posten = this.entity.posten[index];
      this.editEntity = {
        id: posten.id,
        position: posten.position,
        produkt: posten.row,
        menge: posten.menge,
        bezeichnung: posten.bezeichnung,
        seriennummer: posten.seriennummer,
        hinweis: posten.hinweis,
        preis: posten.preis,
        rabatt: posten.rabatt
      };
      this.showEditDialog = true;
    },
    handleEditResponse: function(response) {
      this.editEntity = response;
      this.entity.posten[this.editEntity.position - 1] = this.editEntity;
      this.showEditDialog = false;
      this.berechneEndpreis();
    },
    handleKundeResponse: function(kunde) {
      this.showKundeDialog = false;
      this.entity.rechnung.kunde = kunde;
      this.entity.rechnung.nameDruckenBeiFirma = this.entity.rechnung.kunde.nameDruckenBeiFirma;
    },
    handleReparaturResponse: function(reparatur) {
      this.showReparaturDialog = false;
      this.entity.rechnung.reparatur = reparatur;
      this.entity.rechnung.kunde = reparatur.kunde;
      this.entity.rechnung.nameDruckenBeiFirma = this.entity.rechnung.kunde.nameDruckenBeiFirma;
      if (reparatur.bestellung) {
        this.entity.rechnung.bestellung = reparatur.bestellung;
      }
    },
    handleBestellungResponse: function(bestellung) {
      this.showBestellungDialog = false;
      this.entity.rechnung.bestellung = bestellung;
      this.entity.rechnung.kunde = bestellung.kunde;
      this.entity.rechnung.nameDruckenBeiFirma = this.entity.rechnung.kunde.nameDruckenBeiFirma;
    },
    loadEntity: function() {
      showLoader();
      this.getEntity()
        .then(this.setEntity)
        .then(this.getZahlarten)
        .then(this.setZahlarten)
        .then(this.berechneEndpreis)
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
    getEntity: function() {
      return axios.get(this.restUrlGet);
    },
    setEntity: function(response) {
      var data = response.data;
      if (!data.rechnung.bestellung) {
        data.rechnung.bestellung = {};
      }
      if (!data.rechnung.reparatur) {
        data.rechnung.reparatur = {};
      }
      if (!data.rechnung.kunde) {
        data.rechnung.kunde = {};
      }
      this.entity = data;
    },
    getZahlarten: function() {
      return axios.get('/rechnung/zahlarten');
    },
    setZahlarten: function(response) {
      this.zahlarten = response.data;
    }
  }
});

