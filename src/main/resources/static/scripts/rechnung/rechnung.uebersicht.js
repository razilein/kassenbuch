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
    showConfirmDialog: false,
    showDeleteDialog: false,
    showEditDialog: false,
    showVersendenDialog: false,
    confirmDialog: {},
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
    versendenDialog: {},
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
    
    canSendEmail: function(row) {
      return !row.kunde || !row.kunde.email;
    },
    
    sendMailFunction: function(row) {
      vm.versendenDialog.row = row;
      vm.showVersendenDialog = true;
      vm.openFunction(row);
    },
    
    handleSendResponse: function(data) {
      hideLoader();
      if (data.success) {
        vm.showVersendenDialog = false;
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
    
    bezahlt: function(row) {
      return axios.put('/rechnung/bezahlt', { id: row.id });
    },
    
    bezahltFunction: function(row) {
      var art = row.bezahlt ? ' noch nicht bezahlt' : ' wurde bezahlt';
      vm.confirmDialog.text = 'Wollen Sie diese Rechnung als' + art + ' markieren?';
      vm.confirmDialog.title = 'Rechnungsnummer ' + row.nummer + art;
      vm.confirmDialog.func = vm.bezahlt;
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
      window.open('/rechnung-drucken.html?id=' + row.id, '_blank', 'resizable=yes');
    },
    
    setGridColumns: function() {
      vm.grid.gridColumns = [
        { name: 'functions',
          title: 'Funktionen',
          sortable: false,
          width: 200,
          formatter: [
          { clazz: 'open-new-tab', disabled: vm.hasNotRoleRechnungAnzeigen, title: 'Rechnung öffnen', clickFunc: vm.openFunction },
          { clazz: 'edit', disabled: vm.hasNotRoleVerwalten, title: 'Rechnung bearbeiten', clickFunc: vm.editFunction },
          { clazz: vm.getClazzErledigt, disabled: vm.hasNotRoleVerwalten, title: vm.getTitleErledigt, clickFunc: vm.bezahltFunction },
          { clazz: 'email', disabled: vm.canSendEmail, title: 'Rechnung per E-Mail an den Kunden versenden', clickFunc: vm.sendMailFunction },
          { clazz: 'delete', disabled: vm.hasNotRoleVerwalten, title: 'Rechnung löschen', clickFunc: vm.deleteFunction }
        ] },
        { name: 'nummer', title: 'Rechn.-Nr.', width: 80 },
        { name: 'reparatur.nummer', title: 'Rep.-Nr.', width: 80 },
        { name: 'kunde.nummer', title: 'Kd.-Nr.', width: 80 },
        { name: 'kunde.nameKomplett', title: 'Kunde', width: 200 },
        { name: 'datum', title: 'Datum', width: 120, formatter: ['date'] },
        { name: 'ersteller', title: 'Ersteller', width: 150 },
      ];
    },
    
    getClazzErledigt: function(row) {
      return row.bezahlt ? 'good' : 'bad';
    },
    
    getTitleErledigt: function(row) {
      return row.bezahlt ? 'Die Rechnung wurde bezahlt. Rechnung als nicht bezahlt markieren?' : 'Die Rechnung wurde noch nicht bezahlt. Jetzt als bezahlt markieren?';
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
