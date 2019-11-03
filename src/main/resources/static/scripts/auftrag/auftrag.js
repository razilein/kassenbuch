var vm = new Vue({
  el: '#auftragErstellen',
  data: {
    entity: {
      kunde: {},
      filiale: {}
    },
    einstellungDruckansichtNeuesFenster: true,
    mitarbeiter: [],
    result: {},
    showDialog: false,
    showKundeDialog: false,
    wochentagdatum: ''
  },
  methods: {
    areRequiredFieldsNotEmpty: function() {
      return this.entity && this.entity.kunde && hasAllPropertiesAndNotEmpty(this.entity, ['kunde.id', 'kosten', 'beschreibung']);
    },
    init: function() {
      showLoader();
      vm.getEntity()
        .then(vm.setEntity)
        .then(vm.getEinstellungDruckansichtNeuesFenster)
        .then(vm.setEinstellungDruckansichtNeuesFenster)
        .then(hideLoader);
    },
    saveFunc: function() {
      showLoader();
      vm.executeSave()
        .then(vm.openAuftrag)
        .then(hideLoader);
    },
    executeSave: function() {
      return axios.put('/auftrag/', vm.entity);
    },
    changeAbholdatum: function() {
      this.getAbholdatum()
        .then(this.setAbholdatum);
    },
    handleKundeResponse: function(kunde) {
      vm.showKundeDialog = false;
      vm.entity.kunde = kunde;
    },
    openAuftrag: function(response) {
      var data = response.data;
      if (data.success || data.info) {
        var id = data.auftrag.id;
        if (vm.einstellungDruckansichtNeuesFenster) {
          window.open('/auftrag-drucken.html?id=' + id, '_blank', 'resizable=yes');
        } else {
          window.open('/auftrag-drucken.html?id=' + id);
        }
        vm.init();
      }
      vm.result = data;
      vm.showDialog = true;
    },
    getAbholdatum: function() {
      return axios.get('/auftrag/datum/');
    },
    setAbholdatum: function(response) {
      vm.wochentagdatum = formatDayOfWeek(response.data.datum);
      vm.entity.abholdatum = response.data.datum;
    },
    getEntity: function() {
      return axios.get('/auftrag/' + -1);
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
  }
});

vm.init();
