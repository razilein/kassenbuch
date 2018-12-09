var vm = new Vue({
  el: '#mitarbeiter',
  data: {
    rechte: {},
    result: {},
    showConfirmDialog: false,
    showDialog: false,
    showDeleteDialog: false,
    showEditDialog: false,
    showEditRechteDialog: false,
    confirmDialog: {
      text: 'Wollen Sie das Passwort des Benutzers zurücksetzen?',
    },
    deleteRow: {
      id: null,
      restUrl: '/einstellungen/mitarbeiter',
      title: 'Mitarbeiter löschen',
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
    
    deleteFunction: function(row) {
      vm.deleteRow.id = row.id;
      vm.deleteRow.title = 'Mitarbeiter ' + row.completeName + ' löschen';
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
      vm.getRecht('ROLE_MITARBEITER_RECHTE');
      vm.getRecht('ROLE_MITARBEITER_RESET');
      vm.getRecht('ROLE_MITARBEITER_VERWALTEN');
    },
    
    hasNotRoleEditRecht: function() {
      return !vm.rechte['ROLE_MITARBEITER_RECHTE'];
    },
    
    hasNotRoleResetPassword: function() {
      return !vm.rechte['ROLE_MITARBEITER_RESET'];
    },
    
    hasNotRoleVerwalten: function() {
      return !vm.rechte['ROLE_MITARBEITER_VERWALTEN'];
    },

    setGridColumns: function() {
      vm.grid.gridColumns = [
        { name: 'functions',
          title: 'Funktionen',
          sortable: false,
          width: 150,
          formatter: [
          { clazz: 'edit', disabled: vm.hasNotRoleVerwalten, title: 'Mitarbeiter bearbeiten', clickFunc: vm.editFunction },
          { clazz: 'key', disabled: vm.hasNotRoleResetPassword, title: 'Passwort zurücksetzen', clickFunc: vm.resetPasswordFunction },
          { clazz: 'recht', disabled: vm.hasNotRoleEditRecht, title: 'Berechtigungen bearbeiten', clickFunc: vm.editRechteFunction },
          { clazz: 'delete', disabled: vm.hasNotRoleVerwalten, title: 'Mitarbeiter löschen', clickFunc: vm.deleteFunction }
        ] },
        { name: 'nachname', title: 'Nachname', width: 200 },
        { name: 'vorname', title: 'Vorname', width: 150 },
      ];
    },
    
    setGridActions: function() {
      vm.grid.actions = [
        { clazz: 'add', disabled: vm.hasNotRoleVerwalten, title: 'Mitarbeiter hinzufügen', clickFunc: vm.addFunction }
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
