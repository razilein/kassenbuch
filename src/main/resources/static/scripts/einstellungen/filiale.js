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
          sortable: false,
          width: 120,
          formatter: [
          { clazz: 'edit', title: 'Filiale bearbeiten', clickFunc: vm.editFunction },
          { clazz: 'delete', title: 'Filiale löschen', clickFunc: vm.deleteFunction }
        ] },
        { name: 'kuerzel', title: 'Kürzel', width: 80 },
        { name: 'name', title: 'Name', width: 100 },
        { name: 'strasse', title: 'Straße', width: 60 },
        { name: 'plz', title: 'PLZ', width: 50 },
        { name: 'ort', title: 'Ort', width: 100 },
      ];
    },
    
  }
});

vm.init();
