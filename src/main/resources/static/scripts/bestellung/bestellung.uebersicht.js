var vm = new Vue({
  i18n,
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
      title: '',
    },
    editRow: {
      restUrlGet: '/bestellung/',
      restUrlSave: '/bestellung/',
      title: '',
    },
    grid: {
      actions: [],
      gridColumns: [],
      reload: false,
      restUrl: 'bestellung',
      searchQuery: {},
      sort: 'bestellungNr',
      sortorder: 'desc'
    },
  },
  methods: {
    
    editFunction: function(row) {
      vm.editRow.restUrlGet = '/bestellung/' + row.id;
      vm.editRow.title = this.$t('bestellung.titelK') + ' ' + row.nummer + ' ' + this.$t('general.bearbeiten');
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
      vm.deleteRow.title = this.$t('bestellung.titelK') + ' ' + row.nummer + ' ' + this.$t('general.loeschen');
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
      var art = row.erledigt ? this.$t('general.wiedereroeffnen') : this.$t('general.erledigen');
      vm.confirmDialog.text = this.$t('bestellung.erledigen') + art + '?';
      vm.confirmDialog.title = this.$t('bestellung.titelK') + ' ' + row.nummer + art;
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
          title: this.$t('general.funktionen'),
          sortable: false,
          width: 170,
          formatter: [
          { clazz: 'open-new-tab', disabled: vm.hasNotRoleBestellungAnzeigen, title: this.$t('bestellung.oeffnen'), clickFunc: vm.openFunction },
          { clazz: 'edit', disabled: vm.hasNotRoleVerwalten, title: this.$t('bestellung.bearbeiten'), clickFunc: vm.editFunction },
          { clazz: vm.getClazzErledigt, disabled: vm.hasNotRoleVerwalten, title: vm.getTitleErledigt, clickFunc: vm.erledigenFunction },
          { clazz: 'delete', disabled: vm.hasNotRoleVerwalten, title: this.$t('bestellung.loeschen'), clickFunc: vm.deleteFunction }
        ] },
        { name: 'bestellungNr', title: this.$t('general.nummer'), width: 100 },
        { name: 'angebotNr', title: this.$t('angebot.angebotNr'), width: 100 },
        { name: 'kundeNr', title: this.$t('kunde.kdNr'), width: 80 },
        { name: 'kunde.nameKomplett', sortable: false, title: this.$t('general.kunde'), width: 200 },
        { name: 'datum', title: this.$t('general.datum'), width: 120, formatter: ['date'] },
        { name: 'erstelltAm', title: this.$t('general.erstelltAm'), width: 100 },
        { name: 'ersteller', title: this.$t('general.ersteller'), width: 200 }
      ];
    },
    
    getClazzErledigt: function(row) {
      return row.erledigt ? 'good' : 'bad';
    },
    
    getTitleErledigt: function(row) {
      return row.erledigt ? this.$t('bestellung.wiedereroeffnen') : this.$t('bestellung.erledigt');
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
