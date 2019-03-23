var vm = new Vue({
  el: '#rechnungen',
  created() {
    window.addEventListener('keydown', e => {
      var isDialogOpened = vm.showDialog || vm.showEditDialog || vm.showDeleteDialog;
      if (e.key == 'Enter' && !isDialogOpened) {
        vm.grid.reload = true;
      }
    });
  },
  data: {
    kundeId: getParamFromCurrentUrl('id') || null,
    rechte: {},
    result: {},
    showDialog: false,
    showDeleteDialog: false,
    showEditDialog: false,
    deleteRow: {
      id: null,
      restUrl: '/rechnung',
      title: 'Rechnung löschen',
    },
    editRow: {
      restUrlGet: '/rechnung/',
      restUrlSave: '/rechnung/',
      title: 'Rechnung bearbeiten',
    },
    grid: {
      actions: [],
      gridColumns: [],
      reload: false,
      restUrl: 'rechnung',
      searchQuery: {},
      sort: 'nummer',
      sortorder: 'desc'
    },
  },
  methods: {
    
    editFunction: function(row) {
      vm.editRow.restUrlGet = '/rechnung/' + row.id;
      vm.editRow.title = 'Rechnung ' + row.nummer + ' bearbeiten';
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
      vm.deleteRow.title = 'Rechnung ' + row.nummer + ' löschen';
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
      vm.getRecht('ROLE_RECHNUNG');
      vm.getRecht('ROLE_RECHNUNG_VERWALTEN');
    },
    
    hasNotRoleRechnungAnzeigen: function() {
      return !vm.rechte['ROLE_RECHNUNG'];
    },
    
    hasNotRoleVerwalten: function() {
      return !vm.rechte['ROLE_RECHNUNG_VERWALTEN'];
    },
    
    openFunction: function(row) {
      window.open('/rechnung-drucken.html?id=' + row.id);
    },
    
    setGridColumns: function() {
      vm.grid.gridColumns = [
        { name: 'functions',
          title: 'Funktionen',
          sortable: false,
          width: 120,
          formatter: [
          { clazz: 'open-new-tab', disabled: vm.hasNotRoleRechnungAnzeigen, title: 'Rechnung öffnen', clickFunc: vm.openFunction },
          { clazz: 'edit', disabled: vm.hasNotRoleVerwalten, title: 'Rechnung bearbeiten', clickFunc: vm.editFunction },
          { clazz: 'delete', disabled: vm.hasNotRoleVerwalten, title: 'Rechnung löschen', clickFunc: vm.deleteFunction }
        ] },
        { name: 'nummer', title: 'Rechnungsnummer', width: 150 },
        { name: 'reparatur.nummer', title: 'Auftragsnummer', width: 150 },
        { name: 'kunde.nummer', title: 'Kundennummer', width: 100 },
        { name: 'kunde.nameKomplett', title: 'Kunde', width: 100 },
        { name: 'datum', title: 'Datum', width: 120, formatter: ['date'] },
        { name: 'ersteller', title: 'Ersteller', width: 150 },
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
