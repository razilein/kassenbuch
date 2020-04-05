var vm = new Vue({
  i18n,
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
    einstellungDruckansichtNeuesFenster: true,
    showDialog: false,
    showConfirmDialog: false,
    showDeleteDialog: false,
    showEditDialog: false,
    showVersendenDialog: false,
    confirmDialog: {},
    deleteRow: {
      id: null,
      restUrl: '/rechnung',
      title: '',
    },
    editRow: {
      restUrlGet: '/rechnung/',
      restUrlSave: '/rechnung/',
      title: '',
    },
    versendenDialog: {},
    grid: {
      actions: [],
      gridColumns: [],
      reload: false,
      restUrl: 'rechnung',
      searchQuery: {},
      sort: 'rechnungNr',
      sortorder: 'desc'
    },
    zahlarten: [],
  },
  methods: {
    
    editFunction: function(row) {
      vm.editRow.restUrlGet = '/rechnung/' + row.id;
      vm.editRow.title = this.$t('general.rechnung') + ' ' + row.nummer + ' ' + this.$t('general.bearbeiten');
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
      vm.openFunction(row, 1);
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
      vm.deleteRow.title = this.$t('general.rechnung') + ' ' + row.nummer + ' ' + this.$t('general.loeschen');
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
      var art = row.bezahlt ? this.$t('rechnung.nichtBezahlt') : this.$t('rechnung.wurdeBezahlt');
      vm.confirmDialog.text = this.$t('rechnung.bezahlen') + art + ' ' + this.$t('rechnung.markieren');
      vm.confirmDialog.title = this.$t('einstellung.filiale.rechnungsnummer') + ' ' + row.nummer + art;
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
      vm.getZahlarten()
        .then(vm.setZahlarten)
        .then(vm.getEinstellungDruckansichtNeuesFenster)
        .then(vm.setEinstellungDruckansichtNeuesFenster);
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
    
    openFunction: function(row, anzahl) {
      var exemplare = anzahl ? anzahl : (row.art === 2 ? 1 : 2);
      var params = '?id=' + row.id + '&exemplare=' + exemplare;
      if (vm.einstellungDruckansichtNeuesFenster) {
        window.open('/rechnung-drucken.html' + params, '_blank', 'resizable=yes');
      } else {
        window.open('/rechnung-drucken.html' + params);
      }
    },
    
    openLieferscheinFunction: function(row, anzahl) {
      if (vm.einstellungDruckansichtNeuesFenster) {
        window.open('/lieferschein-drucken.html?id=' + row.id, '_blank', 'resizable=yes');
      } else {
        window.open('/lieferschein-drucken.html?id=' + row.id);
      }
    },
    
    setGridColumns: function() {
      vm.grid.gridColumns = [
        { name: 'functions',
          title: this.$t('general.funktionen'),
          sortable: false,
          width: 200,
          formatter: [
          { clazz: 'open-new-tab', disabled: vm.hasNotRoleRechnungAnzeigen, title: this.$t('rechnung.oeffnen'), clickFunc: vm.openFunction },
          { clazz: 'edit', disabled: vm.hasNotRoleVerwalten, title: this.$t('rechnung.bearbeiten'), clickFunc: vm.editFunction },
          { clazz: vm.getClazzErledigt, disabled: vm.hasNotRoleVerwalten, title: vm.getTitleErledigt, clickFunc: vm.bezahltFunction },
          { clazz: 'email', disabled: vm.canSendEmail, title: this.$t('rechnung.email'), clickFunc: vm.sendMailFunction },
          { clazz: 'lieferschein', disabled: vm.hasNotRoleRechnungAnzeigen, title: this.$t('rechnung.lieferschein.drucken'), clickFunc: vm.openLieferscheinFunction },
          { clazz: 'delete', disabled: vm.hasNotRoleVerwalten, title: this.$t('rechnung.loeschen'), clickFunc: vm.deleteFunction }
        ] },
        { name: 'rechnungNr', title: this.$t('rechnung.rechnNr'), width: 80 },
        { name: 'reparaturNr', title: this.$t('reparatur.repNr'), width: 80 },
        { name: 'angebotNr', title: this.$t('angebot.angebotNr'), width: 100 },
        { name: 'bestellungNr', title: this.$t('bestellung.nummerKurz'), width: 80 },
        { name: 'kundeNr', title: this.$t('kunde.kdNr'), width: 80 },
        { name: 'kunde.nameKomplett', title: this.$t('general.kunde'), width: 200 },
        { name: 'rechnungsbetrag', title: this.$t('kassenbuch.betrag'), width: 100, formatter: ['money'] },
        { name: 'datum', title: this.$t('general.datum'), width: 120, formatter: ['date'] },
        { name: 'ersteller', title: this.$t('general.ersteller'), width: 150 },
        { name: 'erstelltAm', title: this.$t('general.erstelltAm'), width: 150 },
      ];
    },
    
    getClazzErledigt: function(row) {
      return row.bezahlt ? 'good' : 'bad';
    },
    
    getTitleErledigt: function(row) {
      return row.bezahlt ? this.$t('rechnung.bezahlt') : this.$t('rechnung.wiedereroeffnen');
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
    
    isReadonlySuche1: function() {
      if (this.grid && this.grid.searchQuery) {
        var query = this.grid.searchQuery;
        var readonly = query.posten ? query.posten.length > 0 : false;
        return readonly || query.mitangebot || query.mitbestellung || query.mitreparatur;
      } else {
        return false;
      }
    },
    
    isReadonlySuche2: function() {
      if (this.grid && this.grid.searchQuery) {
        var query = this.grid.searchQuery;
        return query.posten ? query.posten.length > 0 : false;
      } else {
        return false;
      }
    },
    
    getZahlarten: function() {
      return axios.get('/rechnung/zahlarten');
    },
    
    setZahlarten: function(response) {
      vm.zahlarten = response.data;
    },
    
  }
});

vm.init();
