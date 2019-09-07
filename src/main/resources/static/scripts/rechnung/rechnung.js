var vm = new Vue({
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
    endpreis: 0.00,
    entity: {
      rechnung: {
        kunde: {},
        reparatur: {},
      },
      posten: []
    },
    einstellungDruckansichtNeuesFenster: true,
    grid: {
      actions: [],
      gridColumns: [],
      reload: false,
      restUrl: 'rechnung/produkt',
      searchQuery: {},
      sort: 'gruppe.kategorie.bezeichnung,gruppe.bezeichnung,bezeichnung'
    },
    gruppen: [],
    kategorien: [],
    rabattEntity: {},
    result: {},
    showDialog: false,
    showEditDialog: false,
    showKundeDialog: false,
    showRabattDialog: false,
    showRechnungspositionDialog: false,
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
      var endpreis = 0;
      vm.entity.posten.forEach(function(element) {
        var postenPreis = (element.menge || 1) * (element.preis || 0) - (element.rabatt || 0);  
        endpreis = endpreis + postenPreis;
      });
      endpreis = endpreis || 0;
      endpreis = endpreis < 0 ? 0 : endpreis;
      vm.endpreis = formatMoney(endpreis, 2, '.', ',');
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
      vm.setGridColumns();
      vm.getEntity()
        .then(vm.setEntity)
        .then(vm.getEinstellungDruckansichtNeuesFenster)
        .then(vm.setEinstellungDruckansichtNeuesFenster)
        .then(vm.getZahlarten)
        .then(vm.setZahlarten)
        .then(vm.getKategorien)
        .then(vm.setKategorien)
        .then(vm.gridReload)
        .then(hideLoader);
    },
    updateGruppen: function() {
      vm.getGruppen().then(vm.setGruppen);
    },
    saveSearchQuerySortierung: function() {
      saveInLocalstorage(SUCHE.PRODUKT.SORT_VERKAEUFE, vm.grid.searchQuery.sortierung);
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
    },
    handleReparaturResponse: function(reparatur) {
      vm.showReparaturDialog = false;
      vm.entity.rechnung.reparatur = reparatur;
      vm.entity.rechnung.kunde = reparatur.kunde;
      if (reparatur.kunde) {
        vm.entity.rechnung.nameDrucken = true;
      }
    },
    gridReload: function() {
      vm.grid.searchQuery = {};
      vm.setGridSearch();
      vm.grid.reload = true;
    },
    openRechnung: function(response) {
      var data = response.data;
      if (data.success || data.info) {
        var id = data.rechnung.id;
        if (vm.einstellungDruckansichtNeuesFenster) {
          window.open('/rechnung-drucken.html?id=' + id, '_blank', 'resizable=yes');
        } else {
          window.open('/rechnung-drucken.html?id=' + id);
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
    setGridColumns: function() {
      vm.grid.gridColumns = [
        { name: 'functions',
          title: 'Funktionen',
          sortable: false,
          width: 50,
          formatter: [
          { clazz: 'ok2', title: 'Dieses Produkt wählen', clickFunc: vm.chooseFunction },
          { clazz: 'ok', title: 'Seriennummer und Hinweise ergänzen und wählen', clickFunc: vm.editFunction },
        ] },
        { name: 'hersteller', title: 'Hersteller', width: 150 },
        { name: 'bezeichnung', title: 'Bezeichnung', width: 500 },
        { name: 'ean', title: 'EAN', width: 120 },
        { name: 'bestand', title: 'Bestand', width: 100 },
        { name: 'preise', title: 'Preise', width: 100 }
      ];
    },
    setGridSearch: function() {
      vm.grid.searchQuery.sortierung = getFromLocalstorage(SUCHE.PRODUKT.SORT_VERKAEUFE) === 'true';
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
    getKategorien: function() {
      return axios.get('/rechnung/kategorie');
    },
    setKategorien: function(response) {
      vm.kategorien = response.data;
    },
    getGruppen: function() {
      var kategorieId = this.grid.searchQuery.kategorie;
      if (kategorieId !== undefined) {
        return axios.get('/rechnung/gruppe/' + kategorieId);
      } else {
        return { data: [] };
      }
    },
    setGruppen: function(response) {
      vm.gruppen = response.data;
    },
    getZahlarten: function() {
      return axios.get('/rechnung/zahlarten');
    },
    setZahlarten: function(response) {
      response.data.unshift({key: -1, value: 'Bitte auswählen'});
      vm.zahlarten = response.data;
    }
  }
});

vm.init();
