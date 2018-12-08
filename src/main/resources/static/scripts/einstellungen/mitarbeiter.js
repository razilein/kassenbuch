var vm = new Vue({
  el: '#mitarbeiter',
  data: {
    result: {},
    showConfirmDialog: false,
    showDialog: false,
    showEditDialog: false,
    showEditRechteDialog: false,
    confirmDialog: {
      text: 'Wollen Sie das Passwort des Benutzers zurücksetzen?',
    },
    editRechtDialog: {
      restUrlGet: '/einstellungen/mitarbeiter/rechte/',
      restUrlSave: '/einstellungen/mitarbeiter/rechte',
      title: 'Berechtigungen bearbeiten',
    },
    editRow: {
      restUrlGet: '/einstellungen/mitarbeiter/',
      restUrlSave: '/einstellungen/mitarbeiter',
      title: 'Mitarbeiter bearbeiten',
    },
    grid: {
      actions: [],
      gridColumns: [],
      reload: false,
      restUrl: 'einstellungen/mitarbeiter',
      searchQuery: {},
    },
  },
  methods: {
    
    addFunction: function() {
      vm.editRow.restUrlGet = '/einstellungen/mitarbeiter/' + -1;
      vm.editRow.title = 'Mitarbeiter hinzufügen';
      vm.showEditDialog = true;
    },
    
    editFunction: function(row) {
      vm.editRow.restUrlGet = '/einstellungen/mitarbeiter/' + row.id;
      vm.editRow.title = 'Mitarbeiter ' + row.completeName + ' bearbeiten';
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
    
    editRechteFunction: function(row) {
      vm.editRechtDialog.restUrlGet = '/einstellungen/mitarbeiter/rechte/' + row.id;
      vm.editRechtDialog.title = 'Berechtigungen für ' + row.completeName + ' bearbeiten';
      vm.showEditRechteDialog = true;
    },
    
    handleEditRechteResponse: function(data) {
      if (data.success) {
        vm.showEditRechteDialog = false;
      } 
      vm.result = data;
      vm.showDialog = true;
    },
    
    resetPasswordFunction: function(row) {
      vm.confirmDialog.title = 'Passwort für ' + row.completeName + ' zurücksetzen';
      vm.confirmDialog.func = vm.resetPassword;
      vm.confirmDialog.params = row;
      vm.showConfirmDialog = true;
    },
    
    resetPassword: function(row) {
      return axios.put('/einstellungen/mitarbeiter/reset', { id: row.id });
    },
    
    handleConfirmResponse: function(data) {
      if (data.success) {
        vm.showConfirmDialog = false;
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
          { clazz: 'edit', title: 'Mitarbeiter bearbeiten', clickFunc: vm.editFunction },
          { clazz: 'key', title: 'Passwort zurücksetzen', clickFunc: vm.resetPasswordFunction },
          { clazz: 'recht', title: 'Berechtigungen bearbeiten', clickFunc: vm.editRechteFunction },
        ] },
        { name: 'nachname', title: 'Nachname', width: '50%' },
        { name: 'vorname', title: 'Vorname', width: '50%' },
      ];
    },
    
    setGridActions: function() {
      vm.grid.actions = [
        { clazz: 'add', title: 'Mitarbeiter hinzufügen', clickFunc: vm.addFunction }
      ]
    },
    
  }
});

vm.init();
