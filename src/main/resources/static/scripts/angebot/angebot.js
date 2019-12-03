var vm = new Vue({
  el: '#angebotErstellen',
  data: {
    entity: {
      angebot: {
        kunde: {},
        filiale: {}
      },
      angebotsposten: [],
    },
    endpreisNto: 0.00,
    endpreis: 0.00,
    einstellungDruckansichtNeuesFenster: true,
    editEntity: {},
    mitarbeiter: [],
    result: {},
    showDialog: false,
    showEditDialog: false,
    showKundeDialog: false,
  },
  methods: {
    addNewItem: function() {
      vm.entity.angebotsposten.push({
        menge: 1,
        position: vm.entity.angebotsposten.length + 1,
        preis: 0.00
      });
      vm.editPosten(vm.entity.angebotsposten.length - 1);
    },
    addItem: function(index) {
      var posten = vm.entity.angebotsposten[index];
      posten.menge = posten.menge + 1;
      vm.entity.angebotsposten[index] = posten;
      vm.berechneEndpreis();
    },
    areRequiredFieldsNotEmpty: function() {
      return this.entity && this.entity.angebot.kunde && hasAllPropertiesAndNotEmpty(this.entity, ['angebot.kunde.id']) && this.entity.angebotsposten.length > 0;
    },
    berechneEndpreis: function() {
      var endpreis = 0;
      vm.entity.angebotsposten.forEach(function(element) {
        var postenPreis = (element.menge || 1) * (element.preis || 0) - (element.rabatt || 0);  
        endpreis = endpreis + postenPreis;
      });
      endpreis = endpreis || 0;
      endpreis = endpreis < 0 ? 0 : endpreis;
      vm.endpreis = formatMoney(endpreis);
      vm.endpreisNto = formatMoney(endpreis / 1.19);
    },
    editPosten: function(index) {
      var posten = vm.entity.angebotsposten[index];
      vm.editEntity = {
        id: posten.id,
        menge: posten.menge,
        bezeichnung: posten.bezeichnung,
        position: posten.position,
        preis: posten.preis
      };
      vm.showEditDialog = true;
    },
    handleEditResponse: function(response) {
      vm.editEntity = response;
      vm.entity.angebotsposten[vm.editEntity.position - 1] = vm.editEntity;
      vm.showEditDialog = false;
      vm.berechneEndpreis();
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
        .then(vm.openAngebot)
        .then(hideLoader);
    },
    executeSave: function() {
      return axios.put('/angebot/', vm.entity);
    },
    changeAbholdatum: function() {
      this.getAbholdatum()
        .then(this.setAbholdatum);
    },
    handleKundeResponse: function(kunde) {
      vm.showKundeDialog = false;
      vm.entity.angebot.kunde = kunde;
    },
    openAngebot: function(response) {
      var data = response.data;
      if (data.success || data.info) {
        var id = data.angebot.id;
        if (vm.einstellungDruckansichtNeuesFenster) {
          window.open('/angebot-drucken.html?id=' + id, '_blank', 'resizable=yes');
        } else {
          window.open('/angebot-drucken.html?id=' + id);
        }
        vm.init();
      }
      vm.result = data;
      vm.showDialog = true;
    },
    removeItem: function(index) {
      var posten = vm.entity.angebotsposten[index];
      posten.menge = posten.menge - 1;
      if (posten.menge <= 0) {
        vm.entity.angebotsposten.splice(index, 1);
        vm.entity.angebotsposten.forEach(function(element, i) {
          var ele = element;
          ele.position = i + 1;
          vm.entity.angebotsposten[i] = ele;
        });
      } else {
        vm.entity.angebotsposten[index] = posten;
      }
      vm.berechneEndpreis();
    },
    getEntity: function() {
      return axios.get('/angebot/' + -1);
    },
    setEntity: function(response) {
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