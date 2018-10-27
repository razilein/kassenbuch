var vm = new Vue({
  el: '#filiale',
  data: {
    result: {},
    showDialog: false,
    searchQuery: {},
    gridColumns: [],
    gridData: [
      { nachname: 'Kind', vorname: 'Sebastian' },
      { nachname: 'Weiske', vorname: 'Stefan' },
      { nachname: 'Dernoscheck', vorname: 'Peer' }
    ],
  },
  methods: {
    
    editFunction: function(row) {
      alert('It works 2!');
    },
    
    deleteFunction: function(row) {
      alert('It works!');
    },

    init: function() {
      vm.setGridColumns()
    },
    
    setGridColumns: function() {
      vm.gridColumns = [
        { name: 'functions',
          title: 'Funktionen',
          width: '10%',
          formatter: [
          { clazz: 'delete', title: 'Filiale bearbeiten', clickFunc: vm.editFunction },
          { clazz: 'delete', title: 'Filiale löschen', clickFunc: vm.deleteFunction }
        ] },
        { name: 'nachname', title: 'Nachname', width: '50%' },
        { name: 'vorname', title: 'Vorname', width: '40%' }
      ];
    }

  }
});

vm.init();
