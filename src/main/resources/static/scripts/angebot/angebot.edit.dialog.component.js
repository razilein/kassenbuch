Vue.component('edit-dialog', {
  template: createEditDialogTemplate(`
  <div class="m1">
    <div class="m6m">
      <label for="angebotEditForm_nummer">Nummer</label>
      <input class="m6" readonly type="text" v-model="entity.angebot.nummer" />
    </div>
    <div class="m3m">
      <label class="required" for="angebotEditForm_datum">Erstellt am</label>
      <input class="m3" type="text" readonly v-model="entity.angebot.erstelltAm" />
    </div>
    <div class="m2">
      <label for="angebotEditForm_ersteller">Ersteller</label>
      <input class="m2" readonly type="text" v-model="entity.angebot.ersteller" />
    </div>
  </div>
  <div class="m1">
    <div class="m6m">
      <label for="angebotEditForm_endpreis">Endpreis (Nto)</label>
      <input class="m6" readonly type="text" v-model="endpreisNto" />
    </div>
    <div class="m6">
      <label for="angebotEditForm_endpreis">Endpreis (Bto)</label>
      <input class="m6" readonly type="text" v-model="endpreis" />
    </div>
  </div>
  <div>
    <table>
      <thead v-if="!entity.angebot.erledigt">
        <tr>
          <th class="tableNavi">
            <button
              class="add"
              title="Posten hinzufügen"
              v-on:click="addNewItem()"
            ></button>
          </th>
        </tr>
      </thead>
      <thead>
        <th style="width: 100px;" v-if="!entity.angebot.erledigt">Funktionen</th>
        <th style="width: 30px;">Pos.</th>
        <th style="width: 200px;">Bezeichnung</th>
        <th style="width: 50px;">Menge</th>
        <th style="width: 100px;">Preis (Nto)</th>
        <th style="width: 100px;">Gesamt (Nto)</th>
        <th style="width: 100px;">Preis (Bto)</th>
        <th style="width: 100px;">Gesamt (Bto)</th>
      </thead>
      <tbody>
        <tr v-for="(posten, index) in entity.angebotsposten">
          <td v-if="!entity.angebot.erledigt">
            <button class="add" title="Menge um 1 erhöhen" v-on:click="addItem(index)"></button>
            <button class="minus" title="Menge um 1 verringern" v-on:click="removeItem(index)"></button>
            <button class="edit" title="Posten bearbeiten" v-on:click="editPosten(index)"></button>
          </td>
          <td>{{posten.position}}</td>
          <td>{{posten.bezeichnung}}</td>
          <td>{{posten.menge}}</td>
          <td>{{ formatMoney(posten.preis / 1.19) }}</td>
          <td>{{ formatMoney(posten.menge * posten.preis / 1.19) }}</td>
          <td>{{ formatMoney(posten.preis) }}</td>
          <td>{{ formatMoney(posten.menge * posten.preis) }}</td>
        </tr>
      </tbody>
    </table>
  </div>
  <br>
  <div class="m1" v-if="entity.angebot.kunde">
    <label class="required" for="angebotEditForm_angebot_kunde">Kunde</label>
    <button class="kunde btnSmall" title="Kunde suchen" @click="showKundeDialog = true"></button>
    <textarea class="m1" id="angebotEditForm_angebot_kunde" readonly v-model="entity.angebot.kunde.completeWithAdress"></textarea>
  </div>
  <kunde-suchen-dialog
    :kunde="entity.kunde"
    v-if="showKundeDialog"
    @close="showKundeDialog = false"
    @saved="handleKundeResponse"
  ></kunde-suchen-dialog>
  <posten-edit-dialog
    :entity="editEntity"
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
      entity: {
        angebot: {
          kunde: {},
          filiale: {}
        },
        angebotsposten: [],
      },
      editEntity: {},
      endpreisNto: 0.00,
      endpreis: 0.00,
      showEditDialog: false,
      showKundeDialog: false,
    };
  },
  methods: {
    addNewItem: function() {
      this.entity.angebotsposten.push({
        menge: 1,
        position: this.entity.angebotsposten.length + 1,
        preis: 0.00
      });
      this.editPosten(this.entity.angebotsposten.length - 1);
    },
    addItem: function(index) {
      var posten = this.entity.angebotsposten[index];
      posten.menge = posten.menge + 1;
      this.entity.angebotsposten[index] = posten;
      this.berechneEndpreis();
    },
    areRequiredFieldsNotEmpty: function() {
      return this.entity && this.entity.angebot.kunde && hasAllPropertiesAndNotEmpty(this.entity, ['angebot.kunde.id']) &&
        this.entity.angebotsposten.length > 0 && !this.entity.angebot.erledigt;
    },
    berechneEndpreis: function() {
      var endpreis = 0;
      this.entity.angebotsposten.forEach(function(element) {
        var postenPreis = (element.menge || 1) * (element.preis || 0) - (element.rabatt || 0);  
        endpreis = endpreis + postenPreis;
      });
      endpreis = endpreis || 0;
      endpreis = endpreis < 0 ? 0 : endpreis;
      this.endpreis = formatMoney(endpreis);
      this.endpreisNto = formatMoney(endpreis / 1.19);
    },
    editPosten: function(index) {
      var posten = this.entity.angebotsposten[index];
      this.editEntity = {
        id: posten.id,
        menge: posten.menge,
        bezeichnung: posten.bezeichnung,
        position: posten.position,
        preis: posten.preis
      };
      this.showEditDialog = true;
    },
    handleEditResponse: function(response) {
      this.editEntity = response;
      this.entity.angebotsposten[this.editEntity.position - 1] = this.editEntity;
      this.showEditDialog = false;
      this.berechneEndpreis();
    },
    removeItem: function(index) {
      var posten = this.entity.angebotsposten[index];
      posten.menge = posten.menge - 1;
      if (posten.menge <= 0) {
        this.entity.angebotsposten.splice(index, 1);
        this.entity.angebotsposten.forEach(function(element, i) {
          var ele = element;
          ele.position = i + 1;
          this.entity.angebotsposten[i] = ele;
        });
      } else {
        this.entity.angebotsposten[index] = posten;
      }
      this.berechneEndpreis();
    },
    loadEntity: function() {
      showLoader();
      this.getEntity()
        .then(this.setEntity)
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
    handleKundeResponse: function(kunde) {
      this.showKundeDialog = false;
      this.entity.angebot.kunde = kunde;
    },
    getEntity: function() {
      return axios.get(this.restUrlGet);
    },
    setEntity: function(response) {
      this.wochentagdatum = formatDayOfWeek(response.data.datum);
      this.entity = response.data;
    },
  }
});

