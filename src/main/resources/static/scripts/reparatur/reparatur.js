var vm = new Vue({
  el: '#reparaturErstellen',
  data: {
    entity: {
      kunde: {},
    },
    einstellungDruckansichtNeuesFenster: true,
    mitarbeiter: [],
    pruefstatus: [
      { key: 0, value: 'Gerät nicht geprüft' },
      { key: true, value: 'Gerät funktioniert' },
      { key: false, value: 'Gerät funktioniert nicht' }
    ],
    reparaturarten: [],
    result: {},
    showDialog: false,
    showKundeDialog: false,
    wochentagabholdatum: ''
  },
  methods: {
    areRequiredFieldsNotEmpty: function() {
      return this.entity && this.entity.kunde && hasAllPropertiesAndNotEmpty(this.entity, ['mitarbeiter', 'kunde.id', 'kostenvoranschlag']) && this.entity.funktionsfaehig !== 0;
    },
    init: function() {
      showLoader();
      vm.getEntity()
        .then(vm.setEntity)
        .then(vm.getEinstellungDruckansichtNeuesFenster)
        .then(vm.setEinstellungDruckansichtNeuesFenster)
        .then(vm.getReparaturarten)
        .then(vm.setReparaturarten)
        .then(vm.getMitarbeiter)
        .then(vm.setMitarbeiter)
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
      if (isExpress && !kosten.includes('+ 25,- Expresspauschale')) {
        kosten += ' + 25,- Expresspauschale';
      } else if (!isExpress) {
        kosten = kosten.replace('+ 25,- Expresspauschale', '');
      }
      vm.entity.kostenvoranschlag = kosten.trim() || null;
    },
    handleKundeResponse: function(kunde) {
      vm.showKundeDialog = false;
      vm.entity.kunde = kunde;
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
      response.data.funktionsfaehig = 0;
      vm.entity = response.data;
    },
    getEinstellungDruckansichtNeuesFenster: function() {
      return axios.get('/mitarbeiter-profil');
    },
    setEinstellungDruckansichtNeuesFenster: function(response) {
      vm.einstellungDruckansichtNeuesFenster = response.data.druckansichtNeuesFenster;
    },
    getReparaturarten: function() {
      return axios.get('/reparatur/reparaturarten');
    },
    setReparaturarten: function(response) {
      vm.reparaturarten = response.data;
    },
    getMitarbeiter: function() {
      return axios.get('/reparatur/mitarbeiter');
    },
    setMitarbeiter: function(response) {
      vm.mitarbeiter = response.data;
    },
  }
});

vm.init();
