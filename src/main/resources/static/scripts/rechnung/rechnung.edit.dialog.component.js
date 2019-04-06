Vue.component('edit-dialog', {
  template: createEditDialogTemplate(`
  <div class="m1">
    <div class="m6m">
      <label for="rechnungEditForm_nummer">Nummer</label>
      <input class="m6" readonly type="text" v-model="entity.rechnung.nummer" />
    </div>
    <div class="m4m">
      <label class="required" for="rechnungEditForm_datum">Datum</label>
      <input class="m4" type="date" v-model="entity.rechnung.datum" />
    </div>
    <div class="m2">
      <label for="rechnungEditForm_ersteller">Ersteller</label>
      <input class="m2" readonly type="text" v-model="entity.rechnung.ersteller" />
    </div>
  </div>
  <div class="m1">
    <div class="m6m">
      <label for="rechnungEditForm_endpreis">Endpreis</label>
      <input class="m6" readonly type="number" v-model="endpreis" />
    </div>
  </div>
  <div style="width: 1000px;">
    <table>
      <thead>
        <th style="width: 100px;">Funktionen</th>
        <th style="width: 30px;">Pos.</th>
        <th style="width: 550px;">Bezeichnung</th>
        <th style="width: 120px;">S/N</th>
        <th style="width: 120px;">Hinweis</th>
        <th style="width: 50px;">Menge</th>
        <th style="width: 100px;">Preis</th>
        <th style="width: 100px;">Rabatt</th>
        <th style="width: 100px;">Gesamt</th>
      </thead>
      <tbody>
        <tr v-for="(posten, index) in entity.posten">
          <td>
            <button class="edit" title="Posten bearbeiten" v-on:click="editPosten(index)"></button>
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
    <div class="m1" v-if="entity.rechnung.reparatur">
      <label for="rechnungEditForm_reparatur">Reparaturauftrag</label>
      <button class="zahnrad btnSmall" title="Reparaturauftrag suchen" @click="showReparaturDialog = true"></button>
      <button class="delete btnSmall" title="Reparaturauftrag deselektieren" @click="entity.rechnung.reparatur = {}"></button>
      <input class="m1" id="rechnungEditForm_reparatur" readonly type="text" v-model="entity.rechnung.reparatur.nummer" />
    </div>
    <div class="m1">
      <label class="container checkbox">Soll der Name auf die Rechnung gedruckt werden?
        <input id="rechnungEditForm_name_drucken" type="checkbox" v-model="entity.rechnung.nameDrucken" />
        <span class="checkmark"></span>
      </label>
    </div>
    <div class="m1" v-if="entity.rechnung.kunde">
      <label for="rechnungEditForm_kunde">Kunde</label>
      <button class="kunde btnSmall" title="Kunde suchen" @click="showKundeDialog = true"></button>
      <button class="delete btnSmall" title="Kunde deselektieren" @click="entity.rechnung.kunde = {}"></button>
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
<reparatur-suchen-dialog
  :kunde="entity.rechnung.kunde"
  v-if="showReparaturDialog"
  @close="showReparaturDialog = false"
  @saved="handleReparaturResponse"
></reparatur-suchen-dialog>
<posten-edit-dialog
  :entity="editEntity"
  v-if="showEditDialog"
  @close="showEditDialog = false"
  @saved="handleEditResponse"
></posten-edit-dialog>
      `),
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
      this.endpreis = endpreis;
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
    },
    handleReparaturResponse: function(reparatur) {
      this.showReparaturDialog = false;
      this.entity.rechnung.reparatur = reparatur;
      this.entity.rechnung.kunde = reparatur.kunde;
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

