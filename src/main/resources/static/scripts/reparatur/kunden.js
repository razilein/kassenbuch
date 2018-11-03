var vm = new Vue({
  el: '#kunden',
  data: {
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
      vm.editRow.title = 'Kunde ' + row.nachname + ' bearbeiten';
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
      vm.deleteRow.title = 'Kunde ' + row.nachname + ' löschen';
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
      vm.setGridActions();
      vm.setGridColumns();
    },
    
    setGridColumns: function() {
      vm.grid.gridColumns = [
        { name: 'functions',
          title: 'Funktionen',
          sortable: false,
          width: 120,
          formatter: [
          { clazz: 'edit', title: 'Kunde bearbeiten', clickFunc: vm.editFunction },
          { clazz: 'delete', title: 'Kunde löschen', clickFunc: vm.deleteFunction }
        ] },
        { name: 'nachname', title: 'Nachname', width: 150 },
        { name: 'vorname', title: 'Vorname', width: 100 },
        { name: 'strasse', title: 'Straße', width: 100 },
        { name: 'plz', title: 'PLZ', width: 50 },
        { name: 'ort', title: 'Ort', width: 100 },
        { name: 'telefon', title: 'Telefonnummer', width: 140 },
        { name: 'bemerkung', title: 'Bemerkungen', width: 200 },
      ];
    },
    
    setGridActions: function() {
      vm.grid.actions = [
        { clazz: 'add', title: 'Kunde hinzufügen', clickFunc: vm.addFunction }
      ]
    },
    
  }
});

vm.init();
