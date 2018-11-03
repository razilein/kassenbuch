var vm = new Vue({
  el: '#mitarbeiter',
  data: {
    result: {},
    showDialog: false,
    showEditDialog: false,
    editRow: {
      restUrlGet: '/einstellungen/mitarbeiter/',
      restUrlSave: '/einstellungen/mitarbeiter',
      title: 'Mitarbeiter bearbeiten',
    },
    grid: {
      actions: [],
      gridColumns: [],
      reload: false,
      restUrl: 'einstellungen/mitarbeiter',
      searchQuery: {},
    },
  },
  methods: {
    
    addFunction: function() {
      vm.editRow.restUrlGet = '/einstellungen/mitarbeiter/' + -1;
      vm.editRow.title = 'Mitarbeiter hinzufügen';
      vm.showEditDialog = true;
    },
    
    editFunction: function(row) {
      vm.editRow.restUrlGet = '/einstellungen/mitarbeiter/' + row.id;
      vm.editRow.title = 'Mitarbeiter ' + row.completeName + ' bearbeiten';
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
    
    init: function() {
      vm.setGridActions();
      vm.setGridColumns();
    },
    
    setGridColumns: function() {
      vm.grid.gridColumns = [
        { name: 'functions',
          title: 'Funktionen',
          sortable: false,
          width: 120,
          formatter: [
          { clazz: 'edit', title: 'Mitarbeiter bearbeiten', clickFunc: vm.editFunction },
        ] },
        { name: 'nachname', title: 'Nachname', width: '50%' },
        { name: 'vorname', title: 'Vorname', width: '50%' },
      ];
    },
    
    setGridActions: function() {
      vm.grid.actions = [
        { clazz: 'add', title: 'Mitarbeiter hinzufügen', clickFunc: vm.addFunction }
      ]
    },
    
  }
});

vm.init();
