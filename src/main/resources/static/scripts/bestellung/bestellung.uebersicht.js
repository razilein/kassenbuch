var vm = new Vue({
  el: '#bestellungen',
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
    einstellungDruckansichtNeuesFenster: true,
    showDialog: false,
    showConfirmDialog: false,
    showDeleteDialog: false,
    showEditDialog: false,
    showVersendenDialog: false,
    confirmDialog: {},
    deleteRow: {
      id: null,
      restUrl: '/bestellung',
      title: 'Bestellung löschen',
    },
    editRow: {
      restUrlGet: '/bestellung/',
      restUrlSave: '/bestellung/',
      title: 'Bestellung bearbeiten',
    },
    grid: {
      actions: [],
      gridColumns: [],
      reload: false,
      restUrl: 'bestellung',
      searchQuery: {},
      sort: 'datum',
      sortorder: 'desc'
    },
  },
  methods: {
    
    editFunction: function(row) {
      vm.editRow.restUrlGet = '/bestellung/' + row.id;
      vm.editRow.title = 'Bestellung ' + row.nummer + ' bearbeiten';
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
      vm.deleteRow.title = 'Bestellung ' + row.nummer + ' löschen';
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
      return axios.put('/bestellung/erledigen', { id: row.id });
    },
    
    erledigenFunction: function(row) {
      var art = row.erledigt ? ' wiedereröffnen' : ' erledigen';
      vm.confirmDialog.text = 'Wollen Sie diese Bestellung' + art + '?';
      vm.confirmDialog.title = 'Bestellung ' + row.nummer + art;
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
      vm.getEinstellungDruckansichtNeuesFenster().then(vm.setEinstellungDruckansichtNeuesFenster);
      vm.setGridColumns();
    },
    
    prepareRoles: function() {
      vm.getRecht('ROLE_BESTELLUNG');
      vm.getRecht('ROLE_BESTELLUNG_VERWALTEN');
    },
    
    hasNotRoleBestellungAnzeigen: function() {
      return !vm.rechte['ROLE_BESTELLUNG'];
    },
    
    hasNotRoleVerwalten: function() {
      return !vm.rechte['ROLE_BESTELLUNG_VERWALTEN'];
    },
    
    openFunction: function(row) {
      if (vm.einstellungDruckansichtNeuesFenster) {
        window.open('/bestellung-drucken.html?id=' + row.id, '_blank', 'resizable=yes');
      } else {
        window.open('/bestellung-drucken.html?id=' + row.id);
      }
    },
    
    setGridColumns: function() {
      vm.grid.gridColumns = [
        { name: 'functions',
          title: 'Funktionen',
          sortable: false,
          width: 170,
          formatter: [
          { clazz: 'open-new-tab', disabled: vm.hasNotRoleBestellungAnzeigen, title: 'Bestellung öffnen', clickFunc: vm.openFunction },
          { clazz: 'edit', disabled: vm.hasNotRoleVerwalten, title: 'Bestellung bearbeiten', clickFunc: vm.editFunction },
          { clazz: vm.getClazzErledigt, disabled: vm.hasNotRoleVerwalten, title: vm.getTitleErledigt, clickFunc: vm.erledigenFunction },
          { clazz: 'delete', disabled: vm.hasNotRoleVerwalten, title: 'Bestellung löschen', clickFunc: vm.deleteFunction }
        ] },
        { name: 'nummer', title: 'Nummer', width: 80 },
        { name: 'kunde.nummer', title: 'Kd.-Nr.', width: 80 },
        { name: 'kunde.nameKomplett', sortable: false, title: 'Kunde', width: 200 },
        { name: 'datum', title: 'Datum', width: 120, formatter: ['date'] },
        { name: 'erstelltAm', title: 'Erstellt am', width: 100 },
        { name: 'ersteller', title: 'Ersteller', width: 200 }
      ];
    },
    
    getClazzErledigt: function(row) {
      return row.erledigt ? 'good' : 'bad';
    },
    
    getTitleErledigt: function(row) {
      return row.erledigt ? 'Die Bestellung wurde erledigt. Bestellung wiedereröffnen?' : 'Die Bestellung wurde noch nicht erledigt. Jetzt erledigen?';
    },
    
    getRecht: function(role) {
      return hasRole(role).then(vm.setRecht(role));
    },
    
    setRecht: function(role) {
      return function(response) {
        vm.rechte[role] = response.data;
      }
    },
    
    getEinstellungDruckansichtNeuesFenster: function() {
      return axios.get('/mitarbeiter-profil');
    },
    
    setEinstellungDruckansichtNeuesFenster: function(response) {
      vm.einstellungDruckansichtNeuesFenster = response.data.druckansichtNeuesFenster;
    },
    
  }
});

vm.init();
