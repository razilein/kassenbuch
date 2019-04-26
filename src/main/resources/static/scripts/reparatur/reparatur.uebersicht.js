var vm = new Vue({
  el: '#reparaturen',
  created() {
    window.addEventListener('keydown', e => {
      var isDialogOpened = vm.showDialog || vm.showConfirmDialog || vm.showDeleteDialog || vm.showEditDialog;
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
    showConfirmDialog: false,
    showDeleteDialog: false,
    showEditDialog: false,
    confirmDialog: {},
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
      sort: 'nummer',
      sortorder: 'desc'
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
    
    erledigen: function(row) {
      return axios.put('/reparatur/erledigen', { id: row.id });
    },
    
    erledigenFunction: function(row) {
      var art = row.erledigt ? ' wiedereröffnen' : ' erledigen';
      vm.confirmDialog.text = 'Wollen Sie diesen Auftrag' + art + '?';
      vm.confirmDialog.title = 'Reparaturauftrag ' + row.nummer + art;
      vm.confirmDialog.func = vm.erledigen;
      vm.confirmDialog.params = row;
      vm.showConfirmDialog = true;
    },
    
    handleConfirmResponse: function(data) {
      if (data.success) {
        vm.showConfirmDialog = false;
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
      window.open('/reparatur-drucken.html?id=' + row.id, '_blank', 'resizable=yes');
    },
    
    setGridColumns: function() {
      vm.grid.gridColumns = [
        { name: 'functions',
          title: 'Funktionen',
          sortable: false,
          width: 170,
          formatter: [
          { clazz: 'open-new-tab', disabled: vm.hasNotRoleReparaturAnzeigen, title: 'Reparaturauftrag öffnen', clickFunc: vm.openFunction },
          { clazz: 'edit', disabled: vm.hasNotRoleVerwalten, title: 'Reparaturauftrag bearbeiten', clickFunc: vm.editFunction },
          { clazz: vm.getClazzErledigt, disabled: vm.hasNotRoleVerwalten, title: vm.getTitleErledigt, clickFunc: vm.erledigenFunction },
          { clazz: 'delete', disabled: vm.hasNotRoleVerwalten, title: 'Reparaturauftrag löschen', clickFunc: vm.deleteFunction }
        ] },
        { name: 'nummer', title: 'Rep.-Nr.', width: 80 },
        { name: 'kunde.nummer', title: 'Kd.-Nr.', width: 80 },
        { name: 'kunde.nameKomplett', sortable: false, title: 'Kunde', width: 200 },
        { name: 'geraet', title: 'Gerät', width: 350 },
        { name: 'expressbearbeitung', title: 'Express', width: 90, formatter: ['boolean'] },
        { name: 'abholdatum', title: 'Abholdatum', width: 120, formatter: ['date'] },
        { name: 'erstelltAm', title: 'Erstellt am', width: 100 }
      ];
    },
    
    getClazzErledigt: function(row) {
      return row.erledigt ? 'good' : 'bad';
    },
    
    getTitleErledigt: function(row) {
      return row.erledigt ? 'Der Auftrag wurde erledigt. Auftrag wiedereröffnen?' : 'Der Auftrag wurde noch nicht erledigt. Jetzt erledigen?';
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
