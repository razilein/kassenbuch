var vm = new Vue({
  i18n,
  el: '#reparaturErstellen',
  data: {
    entity: {
      bestellung: {},
      kunde: {},
    },
    einstellungDruckansichtNeuesFenster: true,
    geraetepasswortarten: [],
    nichtBezahlteRechnungen: {},
    pruefstatus: [],
    reparaturarten: [],
    result: {},
    resetErsteller: false,
    showDialog: false,
    showBestellungDialog: false,
    showKundeDialog: false,
    showNichtBezahlteRechnungDialog: false,
    wochentagabholdatum: ''
  },
  methods: {
    areRequiredFieldsNotEmpty: function() {
      return this.entity && this.entity.kunde && hasAllPropertiesAndNotEmpty(this.entity, ['geraetepasswort', 'mitarbeiter', 'kunde.id', 'kostenvoranschlag']) &&
        this.entity.funktionsfaehig !== -1 && this.entity.geraet;
    },
    init: function() {
      showLoader();
      vm.resetErsteller = true;
      vm.getEntity()
        .then(vm.setEntity)
        .then(vm.getEinstellungDruckansichtNeuesFenster)
        .then(vm.setEinstellungDruckansichtNeuesFenster)
        .then(vm.getReparaturarten)
        .then(vm.setReparaturarten)
        .then(vm.getGeraetepasswortarten)
        .then(vm.setGeraetepasswortarten)
        .then(vm.getPruefstatus)
        .then(vm.setPruefstatus)
        .then(hideLoader);
    },
    saveFunc: function() {
      showLoader();
      vm.executeSave()
        .then(vm.openReparatur)
        .then(hideLoader);
    },
    executeSave: function() {
      return axios.put('/reparatur/', vm.entity);
    },
    changeAbholdatumZeit: function() {
      this.getAbholdatumZeit()
        .then(this.setAbholdatumZeit);
    },
    editKostenvoranschlag: function() {
      var kosten = this.entity.kostenvoranschlag || '';
      var isExpress = this.entity.expressbearbeitung;
      if (isExpress && !kosten.includes(this.$t('reparatur.expresspauschale'))) {
        kosten += ' ' + this.$t('reparatur.expresspauschale');
      } else if (!isExpress) {
        kosten = kosten.replace(this.$t('reparatur.expresspauschale'), '');
      }
      vm.entity.kostenvoranschlag = kosten.trim() || null;
    },
    handleKundeResponse: function(kunde) {
      vm.showKundeDialog = false;
      vm.entity.kunde = kunde;
      vm.getNichtBezahlteRechnungen(kunde.id)
        .then(vm.setNichtBezahlteRechnungen);
    },
    handleBestellungResponse: function(bestellung) {
      vm.showBestellungDialog = false;
      vm.entity.bestellung = bestellung;
      vm.entity.kunde = bestellung.kunde;
      vm.entity.kostenvoranschlag = bestellung.kosten;
      vm.editKostenvoranschlag();
    },
    openReparatur: function(response) {
      var data = response.data;
      if (data.success || data.info) {
        var id = data.reparatur.id;
        if (vm.einstellungDruckansichtNeuesFenster) {
          window.open('/reparatur-drucken.html?id=' + id, '_blank', 'resizable=yes');
        } else {
          window.open('/reparatur-drucken.html?id=' + id);
        }
        vm.init();
      }
      vm.result = data;
      vm.showDialog = true;
    },
    setGeraetepasswort: function() {
      if (vm.entity.geraetepasswortArt === 0) {
        vm.entity.geraetepasswort = null;
      } else {
        vm.entity.geraetepasswort = vm.geraetepasswortarten[vm.entity.geraetepasswortArt].value;
      }
    },
    getAbholdatumZeit: function() {
      return axios.get('/reparatur/abholdatum/' + vm.entity.expressbearbeitung);
    },
    setAbholdatumZeit: function(response) {
      vm.wochentagabholdatum = formatDayOfWeek(response.data.abholdatum);
      vm.entity.abholdatum = response.data.abholdatum;
      vm.entity.abholzeit = response.data.abholzeit;
    },
    getEntity: function() {
      return axios.get('/reparatur/' + -1);
    },
    setEntity: function(response) {
      vm.wochentagabholdatum = formatDayOfWeek(response.data.abholdatum);
      vm.entity = response.data;
    },
    getEinstellungDruckansichtNeuesFenster: function() {
      return axios.get('/mitarbeiter-profil');
    },
    setEinstellungDruckansichtNeuesFenster: function(response) {
      vm.einstellungDruckansichtNeuesFenster = response.data.druckansichtNeuesFenster;
    },
    getNichtBezahlteRechnungen: function(kundeId) {
      return axios.get('/rechnung/offen/' + kundeId);
    },
    setNichtBezahlteRechnungen: function(response) {
      if (response.data) {
        vm.nichtBezahlteRechnungen = response.data;
        vm.showNichtBezahlteRechnungDialog = true;
      }
    },
    getReparaturarten: function() {
      return axios.get('/reparatur/reparaturarten');
    },
    setReparaturarten: function(response) {
      vm.reparaturarten = response.data;
    },
    setMitarbeiter: function(mitarbeiter) {
      vm.entity.mitarbeiter = mitarbeiter;
    },
    getGeraetepasswortarten: function() {
      return axios.get('/reparatur/geraetepasswortarten');
    },
    setGeraetepasswortarten: function(response) {
      vm.geraetepasswortarten = response.data;
    },
    getPruefstatus: function() {
      return axios.get('/reparatur/pruefstatus');
    },
    setPruefstatus: function(response) {
      vm.pruefstatus = response.data;
    },
  }
});

vm.init();
