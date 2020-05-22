var vm = new Vue({
  i18n,
  el: '#kunden',
  created() {
    window.addEventListener('keydown', e => {
      var isDialogOpened = vm.showDialog || vm.showEditDialog || vm.showDeleteDialog;
      if (e.key == 'Enter' && !isDialogOpened) {
        vm.grid.reload = true;
      }
    });
  },
  data: {
    rechte: {},
    result: {},
    showDialog: false,
    showDeleteDialog: false,
    showDuplicateDialog: false,
    showEditDialog: false,
    deleteRow: {
      id: null,
      restUrl: '/kunde',
      title: '',
    },
    duplicateRow: {
      kunde: {},
      title: ''
    },
    editRow: {
      restUrlGet: '/kunde/',
      restUrlSave: '/kunde',
      title: '',
    },
    grid: {
      actions: [],
      gridColumns: [],
      reload: false,
      restUrl: 'kunde',
      searchQuery: {},
      sort: 'firmenname,nachname,vorname'
    },
  },
  methods: {
    
    addFunction: function() {
      vm.editRow.restUrlGet = '/kunde/' + -1;
      vm.editRow.title = this.$t('kunde.hinzufuegen');
      vm.showEditDialog = true;
    },
    
    editFunction: function(row) {
      vm.editRow.restUrlGet = '/kunde/' + row.id;
      vm.editRow.title = this.$t('general.kunde') + ' ' + row.nummer + ' ' + this.$t('general.bearbeiten');
      vm.showEditDialog = true;
    },
    
    handleEditResponse: function(data) {
      if (data.success) {
        vm.showEditDialog = false;
        vm.grid.searchQuery.firmenname = data.kunde.firmenname;
        vm.grid.searchQuery.nachname = data.kunde.nachname;
        vm.grid.searchQuery.vorname = data.kunde.vorname;
        vm.grid.reload = true;
      } 
      vm.result = data;
      vm.showDialog = true;
    },

    deleteFunction: function(row) {
      vm.deleteRow.id = row.id;
      vm.deleteRow.title = this.$t('general.kunde') + ' ' + row.nummer + ' ' + this.$t('general.loeschen');
      vm.showDeleteDialog = true;
    },
    
    handleDeleteResponse: function(data) {
      if (data.success) {
        vm.showDeleteDialog = false;
        vm.grid.reload = true;
      }
      vm.result = data;
      vm.showDialog = true;
    },
    
    duplicateFunction: function(row) {
      vm.duplicateRow.kunde = row;
      vm.duplicateRow.title = this.$t('kunde.duplikat1') + ' (' + row.nummer + ') ' + this.$t('kunde.duplikat2');
      vm.showDuplicateDialog = true;
    },
    
    duplikateZusammenfuehren: function(data) {
      var dto = {
        kunde: vm.duplicateRow.kunde,
        duplikat: data
      };
      vm.executeDuplikateZusammenfuehren(dto).then(vm.handleDuplicateResponse);
    },
    
    executeDuplikateZusammenfuehren: function(data) {
      return axios.put('/kunde/duplikat', data);
    },
    
    handleDuplicateResponse: function(response) {
      var data = response.data;
      if (data.success) {
        vm.grid.reload = true;
        vm.showDuplicateDialog = false;
      }
      vm.result = data;
      vm.showDialog = true;
    },
    
    init: function() {
      vm.prepareRoles()
        .then(vm.setGridActions)
        .then(vm.setGridColumns);
    },
    
    prepareRoles: function() {
      vm.getRecht('ROLE_KUNDEN_ANGEBOTE');
      vm.getRecht('ROLE_KUNDEN_BESTELLUNGEN');
      vm.getRecht('ROLE_KUNDEN_REPARATUR');
      vm.getRecht('ROLE_KUNDEN_RECHNUNG');
      return vm.getRecht('ROLE_KUNDEN_VERWALTEN');
    },
    
    hasNotRoleAngeboteAnzeigen: function(row) {
      return !vm.rechte['ROLE_KUNDEN_ANGEBOTE'];
    },
    
    hasNotRoleBestellungenAnzeigen: function(row) {
      return !vm.rechte['ROLE_KUNDEN_BESTELLUNGEN'];
    },
    
    hasNotRoleReparaturAnzeigen: function(row) {
      return !vm.rechte['ROLE_KUNDEN_REPARATUR'];
    },
    
    hasNotRoleRechnungAnzeigen: function(row) {
      return !vm.rechte['ROLE_KUNDEN_RECHNUNG'];
    },
    
    hasNotRoleVerwalten: function() {
      return !vm.rechte['ROLE_KUNDEN_VERWALTEN'];
    },
    
    openAngebotFunction: function(row) {
      if (row.anzahlAngebote > 0) {
        window.open('/angebot-uebersicht.html?id=' + row.id);
      }
    },
    
    openBestellungFunction: function(row) {
      if (row.anzahlBestellungen > 0) {
        window.open('/bestellung-uebersicht.html?id=' + row.id);
      }
    },
    
    openReparaturFunction: function(row) {
      if (row.anzahlReparaturen > 0) {
        window.open('/reparatur-uebersicht.html?id=' + row.id);
      }
    },
    
    openRechnungFunction: function(row) {
      if (row.anzahlRechnungen > 0) {
        window.open('/rechnung-uebersicht.html?id=' + row.id);
      }
    },
    
    getAngebotClass: function(row) {
      return row.anzahlAngebote === 0 ? 'angebot disabled' : 'angebot';
    },
    
    getBestellungClass: function(row) {
      return row.anzahlBestellungen === 0 ? 'bestellung disabled' : 'bestellung';
    },
    
    getReparaturClass: function(row) {
      return row.anzahlReparaturen === 0 ? 'zahnrad disabled' : 'zahnrad';
    },
    
    getRechnungClass: function(row) {
      return row.anzahlRechnungen === 0 ? 'euro disabled' : 'euro';
    },
    
    setGridColumns: function() {
      vm.grid.gridColumns = [
        { name: 'functions',
          title: this.$t('general.funktionen'),
          sortable: false,
          width: 170,
          formatter: [
          { clazz: vm.getReparaturClass, disabled: vm.hasNotRoleReparaturAnzeigen, title: this.$t('kunde.anzeigen.reparatur'), clickFunc: vm.openReparaturFunction },
          { clazz: vm.getRechnungClass, disabled: vm.hasNotRoleRechnungAnzeigen, title: this.$t('kunde.anzeigen.rechnung'), clickFunc: vm.openRechnungFunction },
          { clazz: vm.getAngebotClass, disabled: vm.hasNotRoleAngeboteAnzeigen, title: this.$t('kunde.anzeigen.angebot'), clickFunc: vm.openAngebotFunction },
          { clazz: vm.getBestellungClass, disabled: vm.hasNotRoleBestellungenAnzeigen, title: this.$t('kunde.anzeigen.bestellung'), clickFunc: vm.openBestellungFunction },
          { clazz: 'edit', disabled: vm.hasNotRoleVerwalten, title: this.$t('kunde.bearbeiten'), clickFunc: vm.editFunction },
          { clazz: 'kunden', disabled: vm.hasNotRoleVerwalten, title: this.$t('kunde.duplikat'), clickFunc: vm.duplicateFunction },
          { clazz: 'delete', disabled: vm.hasNotRoleVerwalten, title: this.$t('kunde.loeschen'), clickFunc: vm.deleteFunction }
        ] },
        { name: 'nummer', title: this.$t('kunde.kdNr'), width: 70 },
        { name: 'firmenname', title: this.$t('kunde.firmenname'), width: 150 },
        { name: 'nachname', title: this.$t('kunde.nachname'), width: 150 },
        { name: 'vorname', title: this.$t('kunde.vorname'), width: 100 },
        { name: 'strasse', title: this.$t('general.strasse'), width: 100 },
        { name: 'plz', title: this.$t('general.plz'), width: 50 },
        { name: 'ort', title: this.$t('general.ort'), width: 100 },
        { name: 'suchfeldTelefon', title: this.$t('kunde.suchfeldTelefon'), width: 140 },
        { name: 'bemerkung', title: this.$t('general.bemerkung'), width: 200 },
        { name: 'erstelltAm', title: this.$t('general.erstelltAm'), width: 100, formatter: ['datetime'] }
      ];
    },
    
    setGridActions: function() {
      vm.grid.actions = [
        { clazz: 'add', disabled: vm.hasNotRoleVerwalten, title: this.$t('kunde.hinzufuegen'), clickFunc: vm.addFunction }
      ]
    },
    
    getRecht: function(role) {
      return hasRole(role).then(vm.setRecht(role));
    },
    
    setRecht: function(role) {
      return function(response) {
        vm.rechte[role] = response.data;
      }
    }
    
  }
});

vm.init();
