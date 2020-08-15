Vue.component('kunde-edit-dialog', {
  i18n,
  template: createEditDialogTemplate(`
  <div class="m1">
    <div class="m2m">
      <label for="kundeEditForm_anrede">{{ $t("kunde.anrede") }}</label>
      <select class="m2" id="kundeEditForm_anrede" v-model="entity.anrede">
        <option :value="a.key" v-for="a in anreden">{{a.value}}</option>
      </select>
    </div>
    <div class="m2">
      <zeichenzaehler-label :elem="entity.akademischerTitel" :forid="'kundeEditForm_akademischer_titel'" :label="$t('kunde.akademischerTitel')" :maxlength="'50'"></zeichenzaehler-label>
      <input class="m2" id="kundeEditForm_akademischerTitel" maxlength="50" type="text" v-model="entity.akademischerTitel"></input>
    </div>
  </div>
  <div class="m1">
      <zeichenzaehler-label :elem="entity.firmenname" :forid="'kundeEditForm_firmenname'" :label="$t('kunde.firmenname')" :maxlength="'200'" :required="true"></zeichenzaehler-label>
      <input class="m1" id="kundeEditForm_firmenname" maxlength="200" type="text" v-model="entity.firmenname"></input>
  </div>
  <div class="m1">
    <div class="m2m">
      <zeichenzaehler-label :elem="entity.nachname" :forid="'kundeEditForm_nachname'" :label="$t('kunde.nachname')" :maxlength="'100'" :required="true"></zeichenzaehler-label>
      <input class="m2" id="kundeEditForm_nachname" maxlength="100" type="text" v-model="entity.nachname"></input>
    </div>
    <div class="m2">
      <zeichenzaehler-label :elem="entity.vorname" :forid="'kundeEditForm_vorname'" :label="$t('kunde.vorname')" :maxlength="'50'"></zeichenzaehler-label>
      <input class="m2" id="kundeEditForm_vorname" maxlength="50" type="text" v-model="entity.vorname"></input>
    </div>
  </div>
  <div class="m1" v-if="entity.firmenname && entity.nachname">
    <label class="container checkbox">{{ $t("rechnung.nameDruckenFirma") }}
      <input id="kundeEditForm_name_drucken_bei_firma" type="checkbox" v-model="entity.nameDruckenBeiFirma" />
      <span class="checkmark"></span>
    </label>
  </div>
  <div class="m1">
    <div class="m2m">
      <zeichenzaehler-label :elem="entity.strasse" :forid="'kundeEditForm_strasse'" :label="$t('general.strasse')" :maxlength="'100'"></zeichenzaehler-label>
      <input class="m2" id="kundeEditForm_strasse" maxlength="100" type="text" v-model="entity.strasse"></input>
    </div>
    <div class="m6m">
      <zeichenzaehler-label :elem="entity.plz" :forid="'kundeEditForm_plz'" :label="$t('general.plz')" :maxlength="'8'"></zeichenzaehler-label>
      <input class="m6" id="kundeEditForm_plz" maxlength="8" type="text" v-model="entity.plz"></input>
    </div>
    <div class="m3">
      <zeichenzaehler-label :elem="entity.ort" :forid="'kundeEditForm_ort'" :label="$t('general.ort')" :maxlength="'50'"></zeichenzaehler-label>
      <input class="m3" id="kundeEditForm_ort" maxlength="50" type="text" v-model="entity.ort"></input>
    </div>
  </div>
  <div class="m1"><hr /></div>
  <div class="m1">
    <div class="m2m">
      <zeichenzaehler-label :elem="entity.telefon" :forid="'kundeEditForm_telefon'" :label="$t('general.telefon')" :maxlength="'50'"></zeichenzaehler-label>
      <input class="m2" id="kundeEditForm_telefon" maxlength="50" type="text" v-model="entity.telefon"></input>
    </div>
    <div class="m2">
      <zeichenzaehler-label :elem="entity.mobiltelefon" :forid="'kundeEditForm_mobiltelefon'" :label="$t('kunde.mobiltelefon')" :maxlength="'50'"></zeichenzaehler-label>
      <input class="m2" id="kundeEditForm_mobiltelefon" maxlength="50" type="text" v-model="entity.mobiltelefon"></input>
    </div>
  </div>
  <div class="m1">
    <zeichenzaehler-label :elem="entity.telefonBemerkung" :forid="'kundeEditForm_telefonBemerkung'" :label="$t('kunde.mobiltelefonBemerkung')" :maxlength="'50'"></zeichenzaehler-label>
    <input class="m1" id="kundeEditForm_telefonBemerkung" maxlength="100" type="text" v-model="entity.telefonBemerkung"></input>
  </div>
  <div class="m1"><hr /></div>
  <div class="m1">
    <zeichenzaehler-label :elem="entity.email" :forid="'kundeEditForm_email'" :label="$t('general.email')" :maxlength="'100'"></zeichenzaehler-label>
    <input class="m1" id="kundeEditForm_email" maxlength="100" type="text" v-model="entity.email"></input>
  </div>
  <div class="m1"><hr /></div>
  <div class="m1">
    <label class="container checkbox" :title="$t('kunde.problemInfo')">{{ $t("kunde.problem") }}
      <input id="kundeEditForm_problem" type="checkbox" v-model="entity.problem" />
      <span class="checkmark"></span>
    </label>
  </div>
  <div class="m1">
    <zeichenzaehler-label :elem="entity.bemerkung" :forid="'kundeEditForm_bemerkung'" :label="$t('general.bemerkung')" :maxlength="'4000'"></zeichenzaehler-label>
    <textarea class="m1" v-model="entity.bemerkung"></textarea>
  </div>
      `),
  props: {
    initialEntity: Object,
    restUrlGet: String,
    restUrlSave: String,
    title: String,
  },
  data: function() {
    this.loadEntity();
    return {
      anreden: [],
      entity: {},
    };
  },
  methods: {
    areRequiredFieldsNotEmpty: function() {
      return this.entity && (this.entity.nachname || this.entity.firmenname);
    },
    loadEntity: function() {
      showLoader();
      this.getAnrede()
        .then(this.setAnrede)
        .then(this.getEntity)
        .then(this.setEntity)
        .then(this.initEntityIfEmpty)
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
    initEntityIfEmpty: function() {
      if (!hasAllProperties(this.entity, ['id'])) {
        this.entity
        if (this.initialEntity && this.initialEntity.suchfeld_name) {
          this.entity = {
            firmenname: this.initialEntity.suchfeld_name
          };
        } else {
          this.entity = this.initialEntity || {};
        }
      }
    },
    getAnrede: function() {
      return axios.get('/kunde/anreden');
    },
    setAnrede: function(response) {
      this.anreden = response.data;
    },
    getEntity: function() {
      return axios.get(this.restUrlGet);
    },
    setEntity: function(response) {
      this.entity = response.data;
    },
  }
});

