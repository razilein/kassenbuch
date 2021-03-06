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
    <div class="m3m">
      <label for="rechnungEditForm_lieferdatum">{{ $t("rechnung.lieferdatum") }}</label>
      <input class="m3" id="rechnungEditForm_lieferdatum" title="Wird das Feld leer gelassen, entspricht das Lieferdatum dem Rechnungsdatum" type="date" v-model="entity.rechnung.lieferdatum" />
    </div>
  </div>
  <div class="m1">
    <div class="m2m">
      <label for="rechnungEditForm_ersteller">{{ $t("general.ersteller") }}</label>
      <input class="m2" readonly type="text" v-model="entity.rechnung.ersteller" />
    </div>
  </div>
  <div class="m1">
    <div class="m5m">
      <label for="rechnungEditForm_ekBrutto">{{ $t("inventar.produkt.ekPreisB") }}</label>
      <input class="m5" readonly type="text" v-model="ekBrutto"  />
    </div>
    <div class="m6m">
      <label for="rechnungEditForm_gewinn">{{ $t("inventar.produkt.gewinn") }}</label>
      <input class="m6" readonly type="text" v-model="endgewinnAnzeige" :class="endgewinn < 0 ? 'error' : (endgewinn < 1 ? 'warning' : '')" />
    </div>
    <div class="m6m">
      <label for="rechnungEditForm_gesamtrabatt">{{ $t("general.rabattP") }}</label>
      <input class="m6" min="0.00" step="0.01" type="number" v-model="entity.rechnung.rabattP" v-on:change="berechneEndpreis" :readonly="entity.rechnung.rabatt > 0.00" />
    </div>
    <div class="m6m">
      <label for="rechnungEditForm_gesamtrabatt">{{ $t("general.rabattE") }}</label>
      <input class="m6" min="0.00" step="0.01" type="number" v-model="entity.rechnung.rabatt" v-on:change="berechneEndpreis" :readonly="entity.rechnung.rabattP > 0.00" />
    </div>
    <div class="m6">
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
    <div class="m1" v-if="entity.rechnung.angebot">
      <label for="rechnungEditForm_angebot">{{ $t("general.angebot") }}</label>
      <button class="angebot btnSmall" :title="$t('angebot.suchen')" @click="showAngebotDialog = true"></button>
      <button class="delete btnSmall" :title="$t('angebot.deselektieren')" @click="entity.rechnung.angebot = {};"></button>
      <textarea class="m1 big" id="rechnungEditForm_angebot" readonly v-model="entity.rechnung.angebot.text"></textarea>
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
<angebot-suchen-dialog
  :einstellung-druckansicht-neues-fenster="einstellungDruckansichtNeuesFenster"
  :angebot="entity.rechnung.angebot"
  :kunde="entity.rechnung.kunde"
  v-if="showAngebotDialog"
  @close="showAngebotDialog = false"
  @saved="handleAngebotResponse"
></angebot-suchen-dialog>
<bestellung-suchen-dialog
  :einstellung-druckansicht-neues-fenster="einstellungDruckansichtNeuesFenster"
  :bestellung="entity.rechnung.bestellung"
  :kunde="entity.rechnung.kunde"
  v-if="showBestellungDialog"
  @close="showBestellungDialog = false"
  @saved="handleBestellungResponse"
></bestellung-suchen-dialog>
<reparatur-suchen-dialog
  :einstellung-druckansicht-neues-fenster="einstellungDruckansichtNeuesFenster"
  :reparatur="entity.rechnung.reparatur"
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
      einstellungDruckansichtNeuesFenster: false,
      ekBrutto: 0.00,
      endpreis: 0.00,
      endgewinn: 0.00,
      endgewinnAnzeige: 0,
      entity: {
        rechnung: {
          kunde: {},
          reparatur: {},
        },
        posten: []
      },
      rabattEntity: {},
      showDialog: false,
      showAngebotDialog: false,
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
      this.entity.rechnung.rabatt = this.entity.rechnung.rabatt < 0 ? 0 : this.entity.rechnung.rabatt;
      var result = berechneEndpreisRechnung(this.entity);
      this.endpreis = formatMoney(result.endpreis);
      this.ekBrutto = formatMoney(result.ekBrutto);
      this.endgewinn = result.endpreis - result.ekBrutto;
      this.endgewinnAnzeige = formatMoney(this.endgewinn);
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
    handleAngebotResponse: function(angebot) {
      this.showAngebotDialog = false;
      this.entity.rechnung.angebot = angebot.angebot;
      this.entity.rechnung.angebot.text = angebot.text;
      this.entity.rechnung.kunde = angebot.angebot.kunde;
      this.entity.rechnung.rabatt = angebot.rabattBrutto;
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
        .then(this.getEinstellungDruckansichtNeuesFenster)
        .then(this.setEinstellungDruckansichtNeuesFenster)
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
    getEinstellungDruckansichtNeuesFenster: function() {
      return axios.get('/mitarbeiter-profil');
    },
    setEinstellungDruckansichtNeuesFenster: function(response) {
      this.einstellungDruckansichtNeuesFenster = response.data.druckansichtNeuesFenster;
    },
    getEntity: function() {
      return axios.get(this.restUrlGet);
    },
    setEntity: function(response) {
      var data = response.data;
      if (data.rechnung.angebot) {
        data.rechnung.angebot.text = data.rechnung.angebot.filiale.kuerzel + data.rechnung.angebot.nummerAnzeige;
      } else {
        data.rechnung.angebot = {};
      }
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

