var vm = new Vue({
  el: '#reparaturen',
  data: {
    kundeId: getParamFromCurrentUrl('id') || null,
    rechte: {},
    result: {},
    showDialog: false,
    showDeleteDialog: false,
    showEditDialog: false,
    deleteRow: {
      id: null,
      restUrl: '/reparatur',
      title: 'Reparaturauftrag löschen',
    },
    editRow: {
      restUrlGet: '/reparatur/',
      restUrlSave: '/reparatur/',
      title: 'Reparaturauftrag bearbeiten',
    },
    grid: {
      actions: [],
      gridColumns: [],
      reload: false,
      restUrl: 'reparatur',
      searchQuery: {},
    },
  },
  methods: {
    
    editFunction: function(row) {
      vm.editRow.restUrlGet = '/reparatur/' + row.id;
      vm.editRow.title = 'Reparaturauftrag ' + row.nummer + ' bearbeiten';
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
      vm.deleteRow.title = 'Reparaturauftrag ' + row.nummer + ' löschen';
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
      if (vm.kundeId) {
        vm.grid.searchQuery['kunde.id'] = vm.kundeId;
        vm.grid.reload = true;
      }
      vm.setGridColumns();
    },
    
    prepareRoles: function() {
      vm.getRecht('ROLE_REPARATUR');
      vm.getRecht('ROLE_REPARATUR_VERWALTEN');
    },
    
    hasNotRoleReparaturAnzeigen: function() {
      return !vm.rechte['ROLE_REPARATUR'];
    },
    
    hasNotRoleVerwalten: function() {
      return !vm.rechte['ROLE_REPARATUR_VERWALTEN'];
    },
    
    openFunction: function(row) {
      window.open('/reparatur-drucken.html?id=' + row.id);
    },
    
    setGridColumns: function() {
      vm.grid.gridColumns = [
        { name: 'functions',
          title: 'Funktionen',
          sortable: false,
          width: 120,
          formatter: [
          { clazz: 'open-new-tab', disabled: vm.hasNotRoleReparaturAnzeigen, title: 'Reparaturauftrag öffnen', clickFunc: vm.openFunction },
          { clazz: 'edit', disabled: vm.hasNotRoleVerwalten, title: 'Reparaturauftrag bearbeiten', clickFunc: vm.editFunction },
          { clazz: 'delete', disabled: vm.hasNotRoleVerwalten, title: 'Reparaturauftrag löschen', clickFunc: vm.deleteFunction }
        ] },
        { name: 'nummer', title: 'Auftragsnummer', width: 150 },
        { name: 'kunde.nachname', title: 'Kunde', width: 100 },
        { name: 'geraet', title: 'Gerät', width: 100 },
        { name: 'expressbearbeitung', title: 'Express', width: 90, formatter: ['boolean'] },
        { name: 'abholdatum', title: 'Abholdatum', width: 120, formatter: ['date'] },
        { name: 'kostenvoranschlag', title: 'Kosten', width: 90 },
        { name: 'erledigt', title: 'Erledigt', width: 90, formatter: ['boolean'] },
        { name: 'erstelltAm', title: 'Erstellt am', width: 100 }
      ];
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
