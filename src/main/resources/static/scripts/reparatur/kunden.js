var vm = new Vue({
  el: '#kunden',
  data: {
    rechte: {},
    result: {},
    showDialog: false,
    showDeleteDialog: false,
    showEditDialog: false,
    deleteRow: {
      id: null,
      restUrl: '/reparatur/kunde',
      title: 'Kunde löschen',
    },
    editRow: {
      restUrlGet: '/reparatur/kunde/',
      restUrlSave: '/reparatur/kunde',
      title: 'Kunde bearbeiten',
    },
    grid: {
      actions: [],
      gridColumns: [],
      reload: false,
      restUrl: 'reparatur/kunde',
      searchQuery: {},
    },
  },
  methods: {
    
    addFunction: function() {
      vm.editRow.restUrlGet = '/reparatur/kunde/' + -1;
      vm.editRow.title = 'Kunde hinzufügen';
      vm.showEditDialog = true;
    },
    
    editFunction: function(row) {
      vm.editRow.restUrlGet = '/reparatur/kunde/' + row.id;
      vm.editRow.title = 'Kunde ' + row.nummer + ' bearbeiten';
      vm.showEditDialog = true;
    },
    
    handleEditResponse: function(data) {
      if (data.success) {
        vm.showEditDialog = false;
        vm.grid.reload = true;
      } 
      vm.result = data;
      vm.showDialog = true;
    },
    
    deleteFunction: function(row) {
      vm.deleteRow.id = row.id;
      vm.deleteRow.title = 'Kunde ' + row.nummer + ' löschen';
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

    init: function() {
      vm.prepareRoles();
      vm.setGridActions();
      vm.setGridColumns();
    },
    
    prepareRoles: function() {
      vm.getRecht('ROLE_KUNDEN_REPARATUR');
      vm.getRecht('ROLE_KUNDEN_VERWALTEN');
    },
    
    hasNotRoleReparaturAnzeigen: function() {
      return !vm.rechte['ROLE_KUNDEN_REPARATUR'];
    },
    
    hasNotRoleVerwalten: function() {
      return !vm.rechte['ROLE_KUNDEN_VERWALTEN'];
    },
    
    openReparaturFunction: function(row) {
      window.open('/reparatur-uebersicht.html?id=' + row.id);
    },
    
    setGridColumns: function() {
      vm.grid.gridColumns = [
        { name: 'functions',
          title: 'Funktionen',
          sortable: false,
          width: 120,
          formatter: [
          { clazz: 'zahnrad', disabled: vm.hasNotRoleReparaturAnzeigen, title: 'Reparaturaufträge anzeigen', clickFunc: vm.openReparaturFunction },
          { clazz: 'edit', disabled: vm.hasNotRoleVerwalten, title: 'Kunde bearbeiten', clickFunc: vm.editFunction },
          { clazz: 'delete', disabled: vm.hasNotRoleVerwalten, title: 'Kunde löschen', clickFunc: vm.deleteFunction }
        ] },
        { name: 'nummer', title: 'Nr.', width: 50 },
        { name: 'nachname', title: 'Nachname', width: 150 },
        { name: 'vorname', title: 'Vorname', width: 100 },
        { name: 'strasse', title: 'Straße', width: 100 },
        { name: 'plz', title: 'PLZ', width: 50 },
        { name: 'ort', title: 'Ort', width: 100 },
        { name: 'telefon', title: 'Telefonnummer', width: 140 },
        { name: 'bemerkung', title: 'Bemerkungen', width: 200 },
        { name: 'erstelltAm', title: 'Erstellt am', width: 100, formatter: ['datetime'] }
      ];
    },
    
    setGridActions: function() {
      vm.grid.actions = [
        { clazz: 'add', disabled: vm.hasNotRoleVerwalten, title: 'Kunde hinzufügen', clickFunc: vm.addFunction }
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
