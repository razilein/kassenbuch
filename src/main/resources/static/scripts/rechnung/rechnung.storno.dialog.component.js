Vue.component('storno-dialog', {
  i18n,
  template: createEditDialogTemplate(`
  <div class="m1">
    <ersteller-select
      :reset="resetErsteller"
      @changed="setMitarbeiter"
      @resetted="resetErsteller = false;"
    ></ersteller-select>
    <div class="m4m">
      <label class="required" for="stornoForm_datum">{{ $t("general.datum") }}</label>
      <input class="m4" type="date" v-model="entity.storno.datum" />
    </div>
  </div>
  <div class="m1">
    <zeichenzaehler-label :elem="entity.storno.grund" :forid="'stornoForm_grund'" :label="$t('rechnung.stornoGrund')" :maxlength="'1000'"></zeichenzaehler-label>
    <textarea class="m1" v-model="entity.storno.grund"></textarea>
  </div>
  <div class="m1">
    <label class="container checkbox">{{ $t("rechnung.vollstorno") }}
      <input id="stornoForm_vollstorno" type="checkbox" v-model="entity.storno.vollstorno" />
      <span class="checkmark"></span>
    </label>
  </div>
  <div style="width: 1000px;" v-if="!entity.storno.vollstorno">
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
            <button class="add" :title="$t('rechnung.postenStornieren')" v-on:click="addPosten(index)" v-if="!posten.storno"></button>
            <button class="minus" :title="$t('rechnung.postenNichtStornieren')" v-on:click="removePosten(index)" v-if="posten.storno"></button>
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
    <div class="m1" v-if="entity.storno.kunde">
      <label for="stornoForm_kunde">{{ $t("general.kunde") }}</label>
      <button class="kunde btnSmall" :title="$t('kunde.suchen')" @click="showKundeDialog = true"></button>
      <button class="delete btnSmall" :title="$t('kunde.deselektieren')" @click="entity.storno.kunde = {}"></button>
      <textarea class="m1" id="stornoForm_kunde" readonly v-model="entity.storno.kunde.completeWithAdress"></textarea>
    </div>
  </div>
<messages-box v-bind:text="result" v-if="showDialog" @close="showDialog = false"></messages-box>
<kunde-suchen-dialog
  :kunde="entity.storno.kunde"
  v-if="showKundeDialog"
  @close="showKundeDialog = false"
  @saved="handleKundeResponse"
></kunde-suchen-dialog>
      `, true),
  props: {
    restUrlGet: String,
    restUrlSave: String,
    title: String,
  },
  data: function() {
    this.loadEntity();
    this.resetErsteller = true;
    return {
      editEntity: {},
      entity: {
        rechnung: {
          kunde: {},
          reparatur: {},
        },
        posten: [],
        storno: {
          kunde: {}
        }
      },
      resetErsteller: false,
      showDialog: false,
      showKundeDialog: false,
    };
  },
  methods: {
    areRequiredFieldsNotEmpty: function() {
      return this.entity && this.entity.storno && hasAllPropertiesAndNotEmpty(this.entity, ['storno.datum', 'storno.grund', 'storno.ersteller']);
    },
    addPosten: function(index) {
      this.entity.posten[index].storno = true;
    },
    removePosten: function(index) {
      this.entity.posten[index].storno = false;
    },
    handleKundeResponse: function(kunde) {
      this.showKundeDialog = false;
      this.entity.storno.kunde = kunde;
    },
    loadEntity: function() {
      showLoader();
      this.getEntity()
        .then(this.setEntity)
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
      if (!data.storno.kunde) {
        data.storno.kunde = {};
      }
      this.entity = data;
    },
    setMitarbeiter: function(mitarbeiter) {
      this.entity.storno.ersteller = mitarbeiter;
    },
  }
});

