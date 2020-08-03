var vm = new Vue({
  i18n,
  el: '#rechnungErstellen',
  created() {
    window.addEventListener('keydown', e => {
      var isDialogOpened = vm.showDialog || vm.showEditDialog || vm.showRabattDialog;
      isDialogOpened = isDialogOpened || vm.showKundeDialog || vm.showRechnungspositionDialog || vm.showReparaturDialog;
      if (e.key == 'Enter' && !isDialogOpened) {
        vm.grid.reload = true;
      }
    });
  },
  data: {
    editEntity: {},
    ekBrutto: 0.00,
    endpreis: 0.00,
    endgewinn: 0.00,
    endgewinnAnzeige: 0,
    entity: {
      rechnung: {
        bestellung: {},
        kunde: {},
        reparatur: {},
      },
      posten: []
    },
    einstellungDruckansichtNeuesFenster: true,
    nichtBezahlteRechnungen: {},
    rabattEntity: {},
    result: {},
    showDialog: false,
    showAngebotDialog: false,
    showBestellungDialog: false,
    showEditDialog: false,
    showKundeDialog: false,
    showRabattDialog: false,
    showRechnungspositionDialog: false,
    showNichtBezahlteRechnungDialog: false,
    showReparaturDialog: false,
    zahlarten: []
  },
  methods: {
    addItem: function(index) {
      var posten = vm.entity.posten[index];
      posten.menge = posten.menge + 1;
      vm.entity.posten[index] = posten;
      vm.berechneEndpreis();
    },
    areRequiredFieldsNotEmpty: function() {
      var kundeRequired = (this.entity.rechnung.art === 2 || this.entity.rechnung.art === 3);
      return this.entity && this.entity.posten.length > 0 && (!kundeRequired || this.entity.rechnung.kunde.nummer) && this.entity.rechnung.art >= 0;
    },
    berechneEndpreis: function() {
      vm.entity.rechnung.rabatt = vm.entity.rechnung.rabatt < 0 ? 0 : vm.entity.rechnung.rabatt;
      var result = berechneEndpreisRechnung(vm.entity);
      vm.endpreis = formatMoney(result.endpreis);
      vm.ekBrutto = formatMoney(result.ekBrutto);
      vm.endgewinn = result.endpreis - result.ekBrutto;
      vm.endgewinnAnzeige = formatMoney(vm.endgewinn);
    },
    chooseFunction: function(row) {
      var bezeichnung = row.hersteller ? row.hersteller + '-' + row.bezeichnung : row.bezeichnung;
      var produkt = {
        position: vm.entity.posten.length + 1,
        produkt: row,
        menge: 1,
        bezeichnung: bezeichnung,
        seriennummer: null,
        hinweis: null,
        preis: row.preisVkBrutto,
        rabatt: 0
      };
      vm.entity.posten.push(produkt);
      vm.berechneEndpreis();
    },
    editPosten: function(index) {
      var posten = vm.entity.posten[index];
      vm.editEntity = {
        id: posten.id,
        position: posten.position,
        produkt: posten.produkt,
        menge: posten.menge,
        bezeichnung: posten.bezeichnung,
        seriennummer: posten.seriennummer,
        hinweis: posten.hinweis,
        preis: posten.preis,
        rabatt: posten.rabatt
      };
      vm.showEditDialog = true;
    },
    editFunction: function(row) {
      var bezeichnung = row.hersteller ? row.hersteller + '-' + row.bezeichnung : row.bezeichnung;
      vm.editEntity = {
        position: vm.entity.posten.length + 1,
        produkt: row,
        menge: 1,
        bezeichnung: bezeichnung,
        seriennummer: null,
        hinweis: null,
        preis: row.preisVkBrutto,
        rabatt: 0
      };
      vm.showEditDialog = true;
    },
    handleEditResponse: function(response) {
      vm.editEntity = response;
      vm.entity.posten[vm.editEntity.position - 1] = vm.editEntity;
      vm.showEditDialog = false;
      vm.berechneEndpreis();
    },
    openRabattDialog: function(index) {
      vm.rabattEntity = vm.entity.posten[index];
      vm.showRabattDialog = true;
    },
    handleRabattResponse: function(response) {
      vm.rabattEntity.rabatt = response;
      vm.entity.posten[vm.rabattEntity.position - 1] = vm.rabattEntity;
      vm.showRabattDialog = false;
      vm.berechneEndpreis();
    },
    init: function() {
      showLoader();
      vm.endpreis = 0.00;
      vm.getEntity()
        .then(vm.setEntity)
        .then(vm.getEinstellungDruckansichtNeuesFenster)
        .then(vm.setEinstellungDruckansichtNeuesFenster)
        .then(vm.getZahlarten)
        .then(vm.setZahlarten)
        .then(hideLoader);
    },
    saveFunc: function() {
      showLoader();
      vm.executeSave()
        .then(vm.openRechnung)
        .then(hideLoader);
    },
    executeSave: function() {
      return axios.put('/rechnung/', vm.entity);
    },
    handleKundeResponse: function(kunde) {
      vm.showKundeDialog = false;
      vm.entity.rechnung.kunde = kunde;
      vm.entity.rechnung.nameDrucken = true;
      vm.entity.rechnung.nameDruckenBeiFirma = vm.entity.rechnung.kunde.nameDruckenBeiFirma;
      vm.getNichtBezahlteRechnungen(kunde.id)
        .then(vm.setNichtBezahlteRechnungen);
    },
    handleReparaturResponse: function(reparatur) {
      vm.showReparaturDialog = false;
      vm.entity.rechnung.reparatur = reparatur;
      vm.entity.rechnung.kunde = reparatur.kunde;
      if (reparatur.kunde) {
        vm.entity.rechnung.nameDrucken = true;
        vm.entity.rechnung.nameDruckenBeiFirma = vm.entity.rechnung.kunde.nameDruckenBeiFirma;
      }
      if (reparatur.bestellung) {
        vm.entity.rechnung.bestellung = reparatur.bestellung;
      }
    },
    handleBestellungResponse: function(bestellung) {
      vm.showBestellungDialog = false;
      vm.entity.rechnung.bestellung = bestellung;
      vm.entity.rechnung.kunde = bestellung.kunde;
      if (bestellung.kunde) {
        vm.entity.rechnung.nameDrucken = true;
        vm.entity.rechnung.nameDruckenBeiFirma = vm.entity.rechnung.kunde.nameDruckenBeiFirma;
      }
    },
    handleAngebotResponse: function(angebot) {
      vm.showAngebotDialog = false;
      vm.entity.rechnung.angebot = angebot.angebot;
      vm.entity.rechnung.angebot.text = angebot.text;
      vm.entity.rechnung.kunde = angebot.angebot.kunde;
      vm.entity.rechnung.rabatt = angebot.rabattBrutto;

      angebot.angebotsposten.forEach(function(p) {
        var produkt = {
          position: vm.entity.posten.length + 1,
          produkt: p.produkt,
          menge: p.menge,
          bezeichnung: p.bezeichnung,
          seriennummer: null,
          hinweis: null,
          preis: p.preis,
          rabatt: 0
        };
        vm.entity.posten.push(produkt);
      });

      vm.berechneEndpreis();
    },
    openRechnung: function(response) {
      var data = response.data;
      if (data.success || data.info) {
        var exemplare = data.rechnung.art === 2 ? 1 : 2;
        var params = '?id=' + data.rechnung.id + '&exemplare=' + exemplare;
        if (vm.einstellungDruckansichtNeuesFenster) {
          window.open('/rechnung-drucken.html' + params, '_blank', 'resizable=yes');
        } else {
          window.open('/rechnung-drucken.html' + params);
        }
        vm.init();
      }
      vm.result = data;
      vm.showDialog = true;
    },
    removeItem: function(index) {
      var posten = vm.entity.posten[index];
      posten.menge = posten.menge - 1;
      if (posten.menge <= 0) {
        vm.entity.posten.splice(index, 1);
        vm.entity.posten.forEach(function(element, i) {
          var ele = element;
          ele.position = i + 1;
          vm.entity.posten[i] = ele;
        });
      } else {
        vm.entity.posten[index] = posten;
      }
      vm.berechneEndpreis();
    },
    getEntity: function() {
      return axios.get('/rechnung/' + -1);
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
    getZahlarten: function() {
      return axios.get('/rechnung/zahlarten');
    },
    setZahlarten: function(response) {
      response.data.unshift({key: -1, value: this.$t('general.auswaehlen')});
      vm.zahlarten = response.data;
    }
  }
});

vm.init();
