var vm = new Vue({
  el: '#rechnungErstellen',
  created() {
    window.addEventListener('keydown', e => {
      if (e.key == 'Enter') {
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
    grid: {
      actions: [],
      gridColumns: [],
      reload: false,
      restUrl: 'rechnung/produkt',
      searchQuery: {},
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
      return this.entity && this.entity.posten.length > 0;
    },
    berechneEndpreis: function() {
      var endpreis = 0;
      vm.entity.posten.forEach(function(element) {
        var postenPreis = (element.menge || 1) * (element.preis || 0) - (element.rabatt || 0);  
        endpreis = endpreis + Number((postenPreis).toFixed(2));
      });
      endpreis = endpreis || 0;
      endpreis = endpreis < 0 ? 0 : endpreis;
      vm.endpreis = endpreis;
    },
    chooseFunction: function(row) {
      var produkt = {
        position: vm.entity.posten.length + 1,
        produkt: row,
        menge: 1,
        bezeichnung: row.bezeichnung,
        seriennummer: null,
        hinweis: null,
        preis: row.preisVk,
        rabatt: 0
      };
      vm.entity.posten.push(produkt);
      vm.berechneEndpreis();
    },
    editPosten: function(index) {
      var posten = vm.entity.posten[index];
      vm.editEntity = {
        position: posten.position,
        produkt: posten.row,
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
      vm.editEntity = {
        position: vm.entity.posten.length + 1,
        produkt: row,
        menge: 1,
        bezeichnung: row.bezeichnung,
        seriennummer: null,
        hinweis: null,
        preis: row.preisVk,
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
      vm.setGridColumns();
      vm.getEntity()
        .then(vm.setEntity)
        .then(vm.getZahlarten)
        .then(vm.setZahlarten)
        .then(vm.getKategorien)
        .then(vm.setKategorien)
        .then(hideLoader);
    },
    updateGruppen: function() {
      vm.getGruppen().then(vm.setGruppen);
    },
    saveFunc: function() {
      vm.executeSave()
        .then(vm.openRechnung);
    },
    executeSave: function() {
      return axios.put('/rechnung/', vm.entity);
    },
    handleKundeResponse: function(kunde) {
      vm.showKundeDialog = false;
      vm.entity.rechnung.kunde = kunde;
    },
    handleReparaturResponse: function(reparatur) {
      vm.showReparaturDialog = false;
      vm.entity.rechnung.reparatur = reparatur;
      vm.entity.rechnung.kunde = reparatur.kunde;
    },
    openDsgvoFile: function(kunde) {
      if (kunde && !kunde.dsgvo) {
        window.open('reparatur/kunde/download-dsgvo/' + kunde.id);
      }
    },
    openRechnung: function(response) {
      var data = response.data;
      if (data.success) {
        var kunde = data.reparatur ? data.reparatur.kunde : data.kunde;
        vm.openDsgvoFile(kunde);
        var id = data.rechnung.id;
        window.open('/rechnung-drucken.html?id=' + id);
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
        { name: 'gruppe.kategorie.bezeichnung', title: 'Kategorie', width: 120 },
        { name: 'gruppe.bezeichnung', title: 'Gruppe', width: 150 },
        { name: 'bezeichnung', title: 'Bezeichnung', width: 500 },
        { name: 'ean', title: 'EAN', width: 120 },
        { name: 'hersteller', title: 'Hersteller', width: 150 },
      ];
    },
    getEntity: function() {
      return axios.get('/rechnung/' + -1);
    },
    setEntity: function(response) {
      vm.entity = response.data;
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
      vm.zahlarten = response.data;
    }
  }
});

vm.init();