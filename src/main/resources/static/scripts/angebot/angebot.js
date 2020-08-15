var vm = new Vue({
  i18n,
  el: '#angebotErstellen',
  data: {
    entity: {
      angebot: {
        kunde: {},
        filiale: {}
      },
      angebotsposten: [],
    },
    ekBrutto: 0.00,
    endpreisNto: 0.00,
    endpreis: 0.00,
    endgewinn: 0.00,
    endgewinnAnzeige: 0,
    einstellungDruckansichtNeuesFenster: true,
    editEntity: {},
    nichtBezahlteRechnungen: {},
    resetErsteller: false,
    result: {},
    showDialog: false,
    showEditDialog: false,
    showKundeDialog: false,
    showNichtBezahlteRechnungDialog: false,
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
    cancelAddNewItem: function() {
      vm.entity.angebotsposten.pop();
      vm.showEditDialog = false;
    },
    addItem: function(index) {
      var posten = vm.entity.angebotsposten[index];
      posten.menge = posten.menge + 1;
      vm.entity.angebotsposten[index] = posten;
      vm.berechneEndpreis();
    },
    areRequiredFieldsNotEmpty: function() {
      return this.entity && this.entity.angebot.kunde && hasAllPropertiesAndNotEmpty(this.entity, ['angebot.kunde.id']) &&
         this.entity.angebotsposten.length > 0 && this.entity.angebot.ersteller;
    },
    berechneEndpreis: function() {
      vm.entity.angebot.rabatt = vm.entity.angebot.rabatt < 0 ? 0 : vm.entity.angebot.rabatt;
      var result = berechneEndpreisAngebot(vm.entity);
      vm.endpreis = formatMoney(result.endpreis);
      vm.endpreisNto = formatMoney(result.endpreisNto);
      vm.ekBrutto = formatMoney(result.ekBrutto);
      vm.endgewinn = result.endgewinn;
      vm.endgewinnAnzeige = formatMoney(vm.endgewinn);
    },
    chooseFunction: function(row) {
      var bezeichnung = row.hersteller ? row.hersteller + '-' + row.bezeichnung : row.bezeichnung;
      var produkt = {
        position: vm.entity.angebotsposten.length + 1,
        produkt: row,
        menge: 1,
        bezeichnung: bezeichnung,
        preis: row.preisVkBrutto,
      };
      vm.entity.angebotsposten.push(produkt);
      vm.berechneEndpreis();
    },
    editFunction: function(row) {
      var bezeichnung = row.hersteller ? row.hersteller + '-' + row.bezeichnung : row.bezeichnung;
      vm.editEntity = {
        position: vm.entity.angebotsposten.length + 1,
        produkt: row,
        menge: 1,
        bezeichnung: bezeichnung,
        preis: row.preisVkBrutto,
      };
      vm.showEditDialog = true;
    },
    editPosten: function(index) {
      var posten = vm.entity.angebotsposten[index];
      vm.editEntity = {
        id: posten.id,
        menge: posten.menge,
        bezeichnung: posten.bezeichnung,
        position: posten.position,
        preis: posten.preis,
        produkt: posten.produkt
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
    setMitarbeiter: function(mitarbeiter) {
      vm.entity.angebot.ersteller = mitarbeiter;
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
