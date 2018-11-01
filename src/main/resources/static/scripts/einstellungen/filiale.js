var vm = new Vue({
  el: '#filiale',
  data: {
    result: {},
    showDialog: false,
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
      alert('It works!');
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
