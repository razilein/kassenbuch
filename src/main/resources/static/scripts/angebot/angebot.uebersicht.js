var vm = new Vue({
  el: '#angebote',
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
      restUrl: '/angebot',
      title: 'Angebot löschen',
    },
    editRow: {
      restUrlGet: '/angebot/',
      restUrlSave: '/angebot/',
      title: 'Angebot bearbeiten',
    },
    grid: {
      actions: [],
      gridColumns: [],
      reload: false,
      restUrl: 'angebot',
      searchQuery: {},
      sort: 'nummer',
      sortorder: 'desc'
    },
  },
  methods: {
    
    editFunction: function(row) {
      vm.editRow.restUrlGet = '/angebot/' + row.id;
      vm.editRow.title = 'Angebot ' + row.nummer + ' bearbeiten';
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
      vm.deleteRow.title = 'Angebot ' + row.nummer + ' löschen';
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
      return axios.put('/angebot/erledigen', { id: row.id });
    },
    
    erledigenFunction: function(row) {
      var art = row.erledigt ? ' wiedereröffnen' : ' erledigen';
      vm.confirmDialog.text = 'Wollen Sie dieses Angebot' + art + '?';
      vm.confirmDialog.title = 'Angebot ' + row.nummer + art;
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
      vm.getRecht('ROLE_ANGEBOT');
      vm.getRecht('ROLE_ANGEBOT_VERWALTEN');
    },
    
    hasNotRoleAngebotAnzeigen: function() {
      return !vm.rechte['ROLE_ANGEBOT'];
    },
    
    hasNotRoleVerwalten: function() {
      return !vm.rechte['ROLE_ANGEBOT_VERWALTEN'];
    },
    
    openFunction: function(row) {
      if (vm.einstellungDruckansichtNeuesFenster) {
        window.open('/angebot-drucken.html?id=' + row.id, '_blank', 'resizable=yes');
      } else {
        window.open('/angebot-drucken.html?id=' + row.id);
      }
    },
    
    setGridColumns: function() {
      vm.grid.gridColumns = [
        { name: 'functions',
          title: 'Funktionen',
          sortable: false,
          width: 170,
          formatter: [
          { clazz: 'open-new-tab', disabled: vm.hasNotRoleAngebotAnzeigen, title: 'Angebot öffnen', clickFunc: vm.openFunction },
          { clazz: 'edit', disabled: vm.hasNotRoleVerwalten, title: 'Angebot bearbeiten', clickFunc: vm.editFunction },
          { clazz: vm.getClazzErledigt, disabled: vm.hasNotRoleVerwalten, title: vm.getTitleErledigt, clickFunc: vm.erledigenFunction },
          { clazz: 'delete', disabled: vm.hasNotRoleVerwalten, title: 'Angebot löschen', clickFunc: vm.deleteFunction }
        ] },
        { name: 'nummer', title: 'Angebot-Nr.', width: 80 },
        { name: 'kunde.nummer', sortable: false, title: 'Kd.-Nr.', width: 80 },
        { name: 'kunde.nameKomplett', sortable: false, title: 'Kunde', width: 200 },
        { name: 'gesamtbetragNetto', title: 'Gesamtbetrag (Nto)', width: 120, formatter: ['money'] },
        { name: 'gesamtbetrag', title: 'Gesamtbetrag (Bto)', width: 120, formatter: ['money'] },
        { name: 'erstelltAm', sortable: false, title: 'Erstellt am', width: 100 },
        { name: 'ersteller', title: 'Ersteller', width: 200 }
      ];
    },
    
    getClazzErledigt: function(row) {
      return row.erledigt ? 'good' : 'bad';
    },
    
    getTitleErledigt: function(row) {
      return row.erledigt ? 'Das Angebot wurde erledigt. Angebot wiedereröffnen?' : 'Das Angebot wurde noch nicht erledigt. Jetzt erledigen?';
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
