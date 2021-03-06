var vm = new Vue({
  i18n,
  el: '#bestellungErstellen',
  data: {
    entity: {
      angebot: {},
      kunde: {},
      filiale: {}
    },
    einstellungDruckansichtNeuesFenster: true,
    nichtBezahlteRechnungen: {},
    result: {},
    resetErsteller: false,
    showDialog: false,
    showAngebotDialog: false,
    showKundeDialog: false,
    showNichtBezahlteRechnungDialog: false,
    wochentagdatum: ''
  },
  methods: {
    areRequiredFieldsNotEmpty: function() {
      return this.entity && this.entity.kunde && hasAllPropertiesAndNotEmpty(this.entity, ['kunde.id', 'kosten', 'beschreibung']) && this.entity.ersteller;
    },
    init: function() {
      showLoader();
      vm.resetErsteller = true;
      vm.getEntity()
        .then(vm.setEntity)
        .then(vm.getEinstellungDruckansichtNeuesFenster)
        .then(vm.setEinstellungDruckansichtNeuesFenster)
        .then(hideLoader);
    },
    saveFunc: function() {
      showLoader();
      vm.executeSave()
        .then(vm.openBestellung)
        .then(hideLoader);
    },
    executeSave: function() {
      return axios.put('/bestellung/', vm.entity);
    },
    changeAbholdatum: function() {
      this.getAbholdatum()
        .then(this.setAbholdatum);
    },
    handleAngebotResponse: function(angebot) {
      vm.showAngebotDialog = false;
      vm.entity.angebot = angebot.angebot;
      vm.entity.angebot.text = angebot.text;
      vm.entity.kunde = angebot.angebot.kunde;
      vm.checkProblemkunde(angebot.angebot.kunde);
    },
    handleKundeResponse: function(kunde) {
      vm.showKundeDialog = false;
      vm.entity.kunde = kunde;
      vm.checkProblemkunde(kunde);
    },
    checkProblemkunde: function(kunde) {
      if (kunde.problem) {
        var problemText = this.$t('kunde.problemStandard');
        if (kunde.bemerkung) {
          problemText = problemText + ':<br><br>' + kunde.bemerkung;
        }
        vm.result = { warning: problemText };
        vm.showDialog = true;
      }
      vm.getNichtBezahlteRechnungen(kunde.id)
      .then(vm.setNichtBezahlteRechnungen);
    },
    openBestellung: function(response) {
      var data = response.data;
      if (data.success || data.info) {
        var id = data.bestellung.id;
        if (vm.einstellungDruckansichtNeuesFenster) {
          window.open('/bestellung-drucken.html?id=' + id, '_blank', 'resizable=yes');
        } else {
          window.open('/bestellung-drucken.html?id=' + id);
        }
        vm.init();
      }
      vm.result = data;
      vm.showDialog = true;
    },
    getAbholdatum: function() {
      return axios.get('/bestellung/datum/');
    },
    setAbholdatum: function(response) {
      vm.wochentagdatum = formatDayOfWeek(response.data.datum);
      vm.entity.abholdatum = response.data.datum;
    },
    getEntity: function() {
      return axios.get('/bestellung/' + -1);
    },
    setEntity: function(response) {
      vm.wochentagdatum = formatDayOfWeek(response.data.datum);
      vm.entity = response.data;
    },
    getEinstellungDruckansichtNeuesFenster: function() {
      return axios.get('/mitarbeiter-profil');
    },
    setEinstellungDruckansichtNeuesFenster: function(response) {
      vm.einstellungDruckansichtNeuesFenster = response.data.druckansichtNeuesFenster;
    },
    setMitarbeiter: function(mitarbeiter) {
      vm.entity.ersteller = mitarbeiter;
    },
    getNichtBezahlteRechnungen: function(kundeId) {
      return axios.get('/rechnung/offen/' + kundeId);
    },
    setNichtBezahlteRechnungen: function(response) {
      var data = response.data;
      if (data && data.length > 0) {
        vm.nichtBezahlteRechnungen = data;
        vm.showNichtBezahlteRechnungDialog = true;
      }
    },
  }
});

vm.init();
