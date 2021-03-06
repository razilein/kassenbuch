var vm = new Vue({
  i18n,
  el: '#angebote',
  created() {
    window.addEventListener('keydown', e => {
      var isDialogOpened = vm.showDialog || vm.showConfirmDialog || vm.showDeleteDialog || vm.showEditDialog || vm.showVersendenDialog;
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
      title: '',
    },
    editRow: {
      restUrlGet: '/angebot/',
      restUrlSave: '/angebot/',
      title: '',
    },
    versendenDialog: {},
    grid: {
      actions: [],
      gridColumns: [],
      reload: false,
      restUrl: 'angebot',
      searchQuery: {},
      sort: 'angebotNr',
      sortorder: 'desc'
    },
  },
  methods: {
    
    editFunction: function(row) {
      vm.editRow.restUrlGet = '/angebot/' + row.id;
      vm.editRow.title = this.$t('general.angebot') + ' ' + row.nummer + ' ' + this.$t('general.bearbeiten');
      vm.editRow.duplicate = false;
      vm.showEditDialog = true;
    },
    
    duplicateFunction: function(row) {
      vm.editRow.restUrlGet = '/angebot/' + row.id;
      vm.editRow.title = this.$t('general.angebot') + ' ' + row.nummer + ' ' + this.$t('general.duplizieren');
      vm.editRow.duplicate = true;
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
      vm.deleteRow.title = this.$t('general.angebot') + ' ' + row.nummer + ' ' + this.$t('general.loeschen');
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
      var art = row.erledigt ? this.$t('general.wiedereroeffnen') : this.$t('general.erledigen');
      vm.confirmDialog.text = this.$t('angebot.erledigen') + art + '?';
      vm.confirmDialog.title = this.$t('general.angebot') + ' ' + row.nummer + art;
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
      if (vm.kundeId) {
        vm.grid.searchQuery['kunde.id'] = vm.kundeId;
        vm.grid.reload = true;
      }
      vm.prepareRoles()
        .then(vm.getEinstellungDruckansichtNeuesFenster)
        .then(vm.setEinstellungDruckansichtNeuesFenster)
        .then(vm.setGridColumns);
    },
    
    prepareRoles: function() {
      vm.getRecht('ROLE_ANGEBOT');
      return vm.getRecht('ROLE_ANGEBOT_VERWALTEN');
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
          title: this.$t('general.funktionen'),
          sortable: false,
          width: 170,
          formatter: [
          { clazz: 'open-new-tab', disabled: vm.hasNotRoleAngebotAnzeigen, title: this.$t('angebot.oeffnen'), clickFunc: vm.openFunction },
          { clazz: 'edit', disabled: vm.hasNotRoleVerwalten, title: this.$t('angebot.bearbeiten'), clickFunc: vm.editFunction },
          { clazz: vm.getClazzErledigt, disabled: vm.hasNotRoleVerwalten, title: vm.getTitleErledigt, clickFunc: vm.erledigenFunction },
          { clazz: 'duplicate', disabled: vm.hasNotRoleVerwalten, title: this.$t('angebot.duplizieren'), clickFunc: vm.duplicateFunction },
          { clazz: 'email', disabled: vm.canSendEmail, title: this.$t('angebot.email'), clickFunc: vm.sendMailFunction },
          { clazz: 'delete', disabled: vm.hasNotRoleVerwalten, title: this.$t('angebot.loeschen'), clickFunc: vm.deleteFunction }
        ] },
        { name: 'angebotNr', title: this.$t('angebot.angebotNr'), width: 100 },
        { name: 'kundeNr', title: this.$t('kunde.kdNr'), width: 80 },
        { name: 'kunde.nameKomplett', sortable: false, title: this.$t('general.kunde'), width: 200 },
        { name: 'gesamtbetragNetto', title: this.$t('general.gesamtNto'), width: 120, formatter: ['money'] },
        { name: 'gesamtbetrag', title: this.$t('general.gesamtBto'), width: 120, formatter: ['money'] },
        { name: 'erstelltAm', sortable: false, title: this.$t('general.erstelltAm'), width: 100 },
        { name: 'ersteller', title: this.$t('general.ersteller'), width: 200 }
      ];
    },
    
    getClazzErledigt: function(row) {
      return row.erledigt ? 'good' : 'bad';
    },
    
    getTitleErledigt: function(row) {
      return row.erledigt ? this.$t('angebot.wiedereroeffnen') : this.$t('angebot.erledigt');
    },
    
    getRecht: function(role) {
      return hasRole(role).then(vm.setRecht(role));
    },
    
    setRecht: function(role) {
      return function(response) {
        vm.rechte[role] = response.data;
      }
    },
    
    isReadonlySuche1: function() {
      if (this.grid && this.grid.searchQuery) {
        var query = this.grid.searchQuery;
        return query.bezeichnung ? query.bezeichnung.length > 0 : false;
      } else {
        return false;
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
