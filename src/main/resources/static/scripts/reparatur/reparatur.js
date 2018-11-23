var vm = new Vue({
  el: '#reparaturErstellen',
  data: {
    entity: {
      kunde: {},
      mitarbeiter: {},
    },
    mitarbeiter: [],
    reparaturarten: [],
    result: {},
    showDialog: false,
    showKundeDialog: false,
  },
  methods: {
    areRequiredFieldsNotEmpty: function() {
      return this.entity && this.entity.kunde && hasAllProperties(this.entity, ['kunde.id', 'mitarbeiter.id']);
    },
    init: function() {
      showLoader();
      vm.getEntity()
        .then(vm.setEntity)
        .then(vm.getMitarbeiter)
        .then(vm.setMitarbeiter)
        .then(vm.getReparaturarten)
        .then(vm.setReparaturarten)
        .then(hideLoader);
    },
    saveFunc: function() {
      vm.executeSave()
        .then(vm.openReparatur);
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
    openDsgvoFile: function(kunde) {
      if (!kunde.dsgvo) {
        window.open('reparatur/kunde/download-dsgvo/' + kunde.id, '_blank');
      }
    },
    openReparatur: function(response) {
      var data = response.data;
      if (data.success) {
        vm.openDsgvoFile(data.reparatur.kunde);
        var id = data.reparatur.id;
        window.open('/reparatur-drucken.html?id=' + id);
        vm.init();
      }
      vm.result = data;
      vm.showDialog = true;
    },
    getAbholdatumZeit: function() {
      return axios.get('/reparatur/abholdatum/' + vm.entity.expressbearbeitung);
    },
    setAbholdatumZeit: function(response) {
      vm.entity.abholdatum = response.data.abholdatum;
      vm.entity.abholzeit = response.data.abholzeit;
    },
    getEntity: function() {
      return axios.get('/reparatur/' + -1);
    },
    setEntity: function(response) {
      vm.entity = response.data;
    },
    getMitarbeiter: function() {
      return axios.get('/reparatur/mitarbeiter');
    },
    setMitarbeiter: function(response) {
      vm.mitarbeiter = response.data;
    },
    getReparaturarten: function() {
      return axios.get('/reparatur/reparaturarten');
    },
    setReparaturarten: function(response) {
      vm.reparaturarten = response.data;
    },
  }
});

vm.init();
