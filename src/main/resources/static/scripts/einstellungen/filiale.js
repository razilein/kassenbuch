var vm = new Vue({
  el: '#filiale',
  data: {
    rechte: {},
    result: {},
    showDialog: false,
    showEditDialog: false,
    editRow: {
      restUrlGet: '/einstellungen/filiale/',
      restUrlSave: '/einstellungen/filiale',
      title: 'Filiale bearbeiten',
    },
    grid: {
      actions: [],
      gridColumns: [],
      reload: false,
      restUrl: 'einstellungen/filiale',
      searchQuery: {},
      sort: 'kuerzel'
    },
  },
  methods: {
    
    addFunction: function() {
      vm.editRow.restUrlGet = '/einstellungen/filiale/' + -1;
      vm.editRow.title = 'Filiale hinzufügen';
      vm.showEditDialog = true;
    },
    
    editFunction: function(row) {
      vm.editRow.restUrlGet = '/einstellungen/filiale/' + row.id;
      vm.editRow.title = 'Filiale ' + row.name + ' bearbeiten';
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
      //TODO Remove Delete for Filiale
      vm.deleteRow.id = row.id;
      vm.deleteRow.title = 'Filiale ' + row.name + ' löschen';
      vm.showDeleteDialog = true;
    },
    
    handleDeleteResponse: function(data) {
      vm.showDeleteDialog = false;
      vm.result = data;
      vm.showDialog = true;
    },

    init: function() {
      vm.prepareRoles();
      vm.setGridActions();
      vm.setGridColumns();
    },
    
    prepareRoles: function() {
      vm.getRecht('ROLE_FILIALEN_VERWALTEN');
    },
    
    hasNotRoleVerwalten: function() {
      return !vm.rechte['ROLE_FILIALEN_VERWALTEN'];
    },
    
    setGridColumns: function() {
      vm.grid.gridColumns = [
        { name: 'functions',
          title: 'Funktionen',
          sortable: false,
          width: 120,
          formatter: [
          { clazz: 'edit', disabled: vm.hasNotRoleVerwalten, title: 'Filiale bearbeiten', clickFunc: vm.editFunction },
        ] },
        { name: 'kuerzel', title: 'Kürzel', width: 80 },
        { name: 'name', title: 'Name', width: 100 },
        { name: 'strasse', title: 'Straße', width: 60 },
        { name: 'plz', title: 'PLZ', width: 50 },
        { name: 'ort', title: 'Ort', width: 100 },
      ];
    },
    
    setGridActions: function() {
      vm.grid.actions = [
        { clazz: 'add', disabled: vm.hasNotRoleVerwalten, title: 'Filiale hinzufügen', clickFunc: vm.addFunction }
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
