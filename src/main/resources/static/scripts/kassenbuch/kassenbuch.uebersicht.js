var vm = new Vue({
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
    grid: {
      actions: [],
      gridColumns: [],
      reload: false,
      restUrl: 'kassenbuch',
      searchQuery: {},
    },
  },
  methods: {
    
    init: function() {
      vm.prepareRoles();
      vm.setGridColumns();
      vm.getEinstellungDruckansichtNeuesFenster().then(vm.setEinstellungDruckansichtNeuesFenster);
    },
    
    prepareRoles: function() {
      vm.getRecht('ROLE_KASSENBUCH');
    },
    
    hasNotRoleKassenbuchAnzeigen: function() {
      return !vm.rechte['ROLE_KASSENBUCH'];
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
          title: 'Funktionen',
          sortable: false,
          width: 120,
          formatter: [
          { clazz: 'open-new-tab', disabled: vm.hasNotRoleRechnungAnzeigen, title: 'Kassenbuch öffnen', clickFunc: vm.openFunction },
        ] },
        { name: 'datum', title: 'Datum', width: 120, formatter: ['date'] },
        { name: 'ersteller', title: 'Ersteller', width: 150 },
        { name: 'ausgangsbetrag', title: 'Ausgangsbetrag', width: 150 }
      ];
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
