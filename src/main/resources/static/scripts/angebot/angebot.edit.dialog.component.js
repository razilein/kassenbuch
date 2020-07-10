Vue.component('edit-dialog', {
  i18n,
  template: createEditDialogTemplate(`
  <div class="m1">
    <div class="m6m">
      <label for="angebotEditForm_nummer">{{ $t("general.nummer") }}</label>
      <input class="m6" readonly type="text" v-model="entity.angebot.nummer" />
    </div>
    <div class="m3m">
      <label class="required" for="angebotEditForm_datum">{{ $t("general.erstelltAm") }}</label>
      <input class="m3" type="text" readonly v-model="entity.angebot.erstelltAm" />
    </div>
    <div class="m2">
      <label for="angebotEditForm_ersteller">{{ $t("general.ersteller") }}</label>
      <input class="m2" readonly type="text" v-model="entity.angebot.ersteller" />
    </div>
  </div>
  <div class="m1">
    <div class="m5m">
      <label for="angebotEditForm_ekBrutto">{{ $t("inventar.produkt.ekPreisB") }}</label>
      <input class="m5" readonly type="text" v-model="ekBrutto"  />
    </div>
    <div class="m6m">
      <label for="angebotEditForm_gewinn">{{ $t("inventar.produkt.gewinn") }}</label>
      <input class="m6" readonly type="text" v-model="endgewinnAnzeige" :class="endgewinn < 0 ? 'error' : (endgewinn < 1 ? 'warning' : '')" />
    </div>
  </div>
  <div class="m1">
    <div class="m6m">
      <label for="angebotEditForm_gesamtrabattP">{{ $t("general.rabattP") }}</label>
      <input class="m6" min="0.00" step="0.01" type="number" v-model="entity.angebot.rabattP" v-on:change="berechneEndpreis" :readonly="entity.angebot.rabatt > 0.00" />
    </div>
    <div class="m6m">
      <label for="angebotEditForm_gesamtrabatt">{{ $t("general.rabattE") }}</label>
      <input class="m6" min="0.00" step="0.01" type="number" v-model="entity.angebot.rabatt" v-on:change="berechneEndpreis" :readonly="entity.angebot.rabattP > 0.00" />
    </div>
    <div class="m6m">
      <label for="angebotEditForm_endpreis">{{ $t("general.endpreisNto") }}</label>
      <input class="m6" readonly type="text" v-model="endpreisNto" />
    </div>
    <div class="m6">
      <label for="angebotEditForm_endpreis">{{ $t("general.endpreisBto") }}</label>
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
              :title="$t('angebot.postenHinzufuegen')"
              v-on:click="addNewItem()"
            ></button>
          </th>
        </tr>
      </thead>
      <thead>
        <th style="width: 100px;" v-if="!entity.angebot.erledigt">{{ $t("general.funktionen") }}</th>
        <th style="width: 30px;">{{ $t("general.pos") }}</th>
        <th style="width: 200px;">{{ $t("general.bezeichnung") }}</th>
        <th style="width: 50px;">{{ $t("general.menge") }}</th>
        <th style="width: 100px;">{{ $t("angebot.preisNto") }}</th>
        <th style="width: 100px;">{{ $t("angebot.gesamtNto") }}</th>
        <th style="width: 100px;">{{ $t("angebot.preisBto") }}</th>
        <th style="width: 100px;">{{ $t("angebot.gesamtBto") }}</th>
      </thead>
      <tbody>
        <tr v-for="(posten, index) in entity.angebotsposten">
          <td v-if="!entity.angebot.erledigt">
            <button class="add" :title="$t('general.mengeErhoehen')" v-on:click="addItem(index)"></button>
            <button class="minus" :title="$t('general.mengeVerringern')" v-on:click="removeItem(index)"></button>
            <button class="edit" :title="$t('angebot.postenBearbeiten')" v-on:click="editPosten(index)"></button>
          </td>
          <td>{{posten.position}}</td>
          <td>{{posten.bezeichnung}}</td>
          <td>{{posten.menge}}</td>
          <td>{{ formatMoney(posten.preis * 100 / (entity.angebot.mwst + 100)) }}</td>
          <td>{{ formatMoney(posten.menge * posten.preis * 100 / (entity.angebot.mwst + 100)) }}</td>
          <td>{{ formatMoney(posten.preis) }}</td>
          <td>{{ formatMoney(posten.menge * posten.preis) }}</td>
        </tr>
      </tbody>
    </table>
  </div>
  <br>
  <div class="m1" v-if="entity.angebot.kunde">
    <label class="required" for="angebotEditForm_angebot_kunde">{{ $t("general.kunde") }}</label>
    <button class="kunde btnSmall" :title="$t('kunde.suchen')" @click="showKundeDialog = true"></button>
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
    :title="$t('angebot.postenBearbeiten')"
    v-if="showEditDialog"
    @close="showEditDialog = false"
    @saved="handleEditResponse"
  ></posten-edit-dialog>
      `, true),
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
        angebot: {
          kunde: {},
          filiale: {}
        },
        angebotsposten: [],
      },
      editEntity: {},
      ekBrutto: 0.00,
      endpreisNto: 0.00,
      endpreis: 0.00,
      endgewinn: 0.00,
      endgewinnAnzeige: 0,
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
      this.entity.angebot.rabatt = this.entity.angebot.rabatt < 0 ? 0 : this.entity.angebot.rabatt;
      var result = berechneEndpreisAngebot(this.entity);
      this.endpreis = formatMoney(result.endpreis);
      this.endpreisNto = formatMoney(result.endpreisNto);
      this.ekBrutto = formatMoney(result.ekBrutto);
      this.endgewinn = result.endgewinn;
      this.endgewinnAnzeige = formatMoney(this.endgewinn);
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
      if (this.duplicate) {
        response.data.angebot.id = null;
        
        var posten = []
        response.data.angebotsposten.forEach(function(value) {
          var p = value;
          p.id = null;
          posten.push(p);
        });
        response.data.angebotsposten = posten;
      }
      this.wochentagdatum = formatDayOfWeek(response.data.datum);
      this.entity = response.data;
    },
  }
});

