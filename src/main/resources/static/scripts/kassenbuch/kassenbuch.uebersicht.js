var vm = new Vue({
  i18n,
  el: '#kassenbuecher',
  created() {
    window.addEventListener('keydown', e => {
      var isDialogOpened = vm.showDialog;
      if (e.key == 'Enter' && !isDialogOpened) {
        vm.grid.reload = true;
      }
    });
  },
  data: {
    rechte: {},
    result: {},
    einstellungDruckansichtNeuesFenster: true,
    showDialog: false,
    showDeleteDialog: false,
    deleteRow: {
      id: null,
      restUrl: '/kassenbuch',
      title: '',
    },
    grid: {
      actions: [],
      gridColumns: [],
      reload: false,
      restUrl: 'kassenbuch',
      searchQuery: {},
    },
  },
  methods: {
    
    deleteFunction: function(row) {
      vm.deleteRow.id = row.id;
      vm.deleteRow.title = this.$t('kassenbuch.vom') + ' ' + row.datum + ' ' + this.$t('general.loeschen');
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
    
    init: function() {
      vm.prepareRoles()
        .then(vm.setGridColumns)
        .then(vm.getEinstellungDruckansichtNeuesFenster)
        .then(vm.setEinstellungDruckansichtNeuesFenster);
    },
    
    prepareRoles: function() {
      vm.getRecht('ROLE_KASSENBUCH');
      return vm.getRecht('ROLE_KASSENBUCH_VERWALTEN');
    },
    
    openFunction: function(row) {
      if (vm.einstellungDruckansichtNeuesFenster) {
        window.open('/kassenbuch-drucken.html?id=' + row.id, '_blank', 'resizable=yes');
      } else {
        window.open('/kassenbuch-drucken.html?id=' + row.id);
      }
    },
    
    setGridColumns: function() {
      vm.grid.gridColumns = [
        { name: 'functions',
          title: this.$t('general.funktionen'),
          sortable: false,
          width: 120,
          formatter: [
          { clazz: 'open-new-tab', disabled: vm.hasNotRoleRechnungAnzeigen, title: this.$t('kassenbuch.oeffnen'), clickFunc: vm.openFunction },
          { clazz: 'delete', disabled: vm.hasNotRoleVerwalten, title: this.$t('kassenbuch.loeschen'), clickFunc: vm.deleteFunction }
        ] },
        { name: 'datum', title: this.$t('general.datum'), width: 120, formatter: ['date'] },
        { name: 'ersteller', title: this.$t('general.ersteller'), width: 150 },
        { name: 'ausgangsbetrag', title: this.$t('kassenbuch.ausgangsbetrag'), width: 150, formatter: ['money'] }
      ];
    },
    
    hasNotRoleKassenbuchAnzeigen: function() {
      return !vm.rechte['ROLE_KASSENBUCH'];
    },
    
    hasNotRoleVerwalten: function() {
      return !vm.rechte['ROLE_KASSENBUCH_VERWALTEN'];
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
