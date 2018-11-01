var vm = new Vue({
  el: '#filiale',
  data: {
    result: {},
    showDialog: false,
    showDeleteDialog: false,
    deleteRow: {
      id: null,
      restUrl: '/einstellungen/filiale',
      title: 'Filiale löschen',
    },
    grid: {
      gridColumns: [],
      restUrl: 'einstellungen/filiale',
      searchQuery: {},
    }
  },
  methods: {
    
    editFunction: function(row) {
      alert('It works 2!');
    },
    
    deleteFunction: function(row) {
      vm.deleteRow.id = row.id;
      vm.deleteRow.title = 'Filiale ' + row.name + ' löschen';
      vm.showDeleteDialog = true;
    },

    init: function() {
      vm.setGridColumns();
    },
    
    setGridColumns: function() {
      vm.grid.gridColumns = [
        { name: 'functions',
          title: 'Funktionen',
          width: '10%',
          formatter: [
          { clazz: 'delete', title: 'Filiale bearbeiten', clickFunc: vm.editFunction },
          { clazz: 'delete', title: 'Filiale löschen', clickFunc: vm.deleteFunction }
        ] },
        { name: 'kuerzel', title: 'Kürzel', width: '5%' },
        { name: 'name', title: 'Name', width: '40%' },
        { name: 'strasse', title: 'Straße', width: '40%' },
        { name: 'plz', title: 'PLZ', width: '40%' },
        { name: 'ort', title: 'Ort', width: '40%' },
      ];
    },
    
  }
});

vm.init();
