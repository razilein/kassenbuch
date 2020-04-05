var vm = new Vue({
  i18n,
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
    einstellungDruckansichtNeuesFenster: true,
    showDialog: false,
    showConfirmDialog: false,
    showDeleteDialog: false,
    showEditDialog: false,
    showVersendenDialog: false,
    confirmDialog: {},
    deleteRow: {
      id: null,
      restUrl: '/reparatur',
      title: '',
    },
    editRow: {
      restUrlGet: '/reparatur/',
      restUrlSave: '/reparatur/',
      title: '',
    },
    versendenDialog: {},
    grid: {
      actions: [],
      gridColumns: [],
      reload: false,
      restUrl: 'reparatur',
      searchQuery: {},
      sort: 'reparaturNr',
      sortorder: 'desc'
    },
  },
  methods: {
    
    editFunction: function(row) {
      vm.editRow.restUrlGet = '/reparatur/' + row.id;
      vm.editRow.title = this.$t('reparatur.reparaturauftrag') + ' ' + row.nummer + ' ' + this.$t('general.bearbeiten');
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
      vm.deleteRow.title = this.$t('reparatur.reparaturauftrag') + ' ' + row.nummer + ' ' + this.$t('general.loeschen');
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
      var art = row.erledigt ? this.$t('general.wiedereroeffnen') : this.$t('general.erledigen');
      vm.confirmDialog.text = this.$t('reparatur.erledigen') + art + '?';
      vm.confirmDialog.title = this.$t('reparatur.reparaturauftrag') + ' ' + row.nummer + art;
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
      if (vm.einstellungDruckansichtNeuesFenster) {
        window.open('/reparatur-drucken.html?id=' + row.id, '_blank', 'resizable=yes');
      } else {
        window.open('/reparatur-drucken.html?id=' + row.id);
      }
    },
    
    setGridColumns: function() {
      vm.grid.gridColumns = [
        { name: 'functions',
          title: this.$t('general.funktionen'),
          sortable: false,
          width: 170,
          formatter: [
          { clazz: 'open-new-tab', disabled: vm.hasNotRoleReparaturAnzeigen, title: this.$t('reparatur.oeffnen'), clickFunc: vm.openFunction },
          { clazz: 'edit', disabled: vm.hasNotRoleVerwalten, title: this.$t('reparatur.bearbeiten'), clickFunc: vm.editFunction },
          { clazz: vm.getClazzErledigt, disabled: vm.hasNotRoleVerwalten, title: vm.getTitleErledigt, clickFunc: vm.erledigenFunction },
          { clazz: 'email', disabled: vm.canSendEmail, title: this.$t('reparatur.email'), clickFunc: vm.sendMailFunction },
          { clazz: 'delete', disabled: vm.hasNotRoleVerwalten, title: this.$t('reparatur.loeschen'), clickFunc: vm.deleteFunction }
        ] },
        { name: 'reparaturNr', title: this.$t('reparatur.repNr'), width: 80 },
        { name: 'bestellungNr', title: this.$t('bestellung.nummerKurz'), width: 100 },
        { name: 'kundeNr', title: this.$t('kunde.kdNr'), width: 80 },
        { name: 'kunde.nameKomplett', sortable: false, title: this.$t('general.kunde'), width: 200 },
        { name: 'geraet', title: this.$t('general.geraet'), width: 350 },
        { name: 'expressbearbeitung', title: this.$t('reparatur.express'), width: 90, formatter: ['boolean'] },
        { name: 'abholdatum', title: this.$t('reparatur.abholdatum'), width: 120, formatter: ['date'] },
        { name: 'erstelltAm', title: this.$t('general.erstelltAm'), width: 100 }
      ];
    },
    
    getClazzErledigt: function(row) {
      return row.erledigt ? 'good' : 'bad';
    },
    
    getTitleErledigt: function(row) {
      return row.erledigt ? this.$t('reparatur.wiedereroeffnen') : this.$t('reparatur.erledigt');
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
