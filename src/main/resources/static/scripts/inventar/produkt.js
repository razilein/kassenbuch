var vm = new Vue({
  el: '#produkt',
  created() {
    window.addEventListener('keydown', e => {
      var isDialogOpened = vm.showDialog || vm.showEditDialog || vm.showDeleteDialog;
      if (e.key == 'Enter' && !isDialogOpened) {
        vm.grid.reload = true;
      }
      if (e.key == 'Delete' && !isDialogOpened && vm.grid.searchQuery.schnellerfassung) {
        vm.grid.searchQuery.ean = null;
      }
    });
  },
  data: {
    gruppen: [],
    kategorien: [],
    rechte: {},
    result: {},
    showDeleteDialog: false,
    showEditDialog: false,
    showDialog: false,
    deleteRow: {
      id: null,
      restUrl: '/inventar/produkt',
      title: 'Produkt löschen',
    },
    editRow: {
      restUrlGet: '/inventar/produkt/',
      restUrlSave: '/inventar/produkt',
      title: 'Produkt bearbeiten',
    },
    grid: {
      actions: [],
      gridColumns: [],
      reload: false,
      restUrl: 'inventar/produkt',
      searchQuery: {},
      sort: 'gruppe.kategorie.bezeichnung,gruppe.bezeichnung,bezeichnung',
    },
  },
  methods: {
    
    addFunction: function() {
      vm.editRow.restUrlGet = '/inventar/produkt/' + -1;
      vm.editRow.title = 'Produkt hinzufügen';
      vm.showEditDialog = true;
    },
    
    editFunction: function(row) {
      vm.editRow.restUrlGet = '/inventar/produkt/' + row.id;
      vm.editRow.title = 'Produkt ' + row.bezeichnung + ' bearbeiten';
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
    
    deleteFunction: function(row) {
      vm.deleteRow.id = row.id;
      vm.deleteRow.title = 'Produkt ' + row.bezeichnung + ' löschen';
      vm.showDeleteDialog = true;
    },
    
    handleDeleteResponse: function(data) {
      vm.grid.reload = true;
      vm.showDeleteDialog = false;
      vm.result = data;
      vm.showDialog = true;
    },
    
    init: function() {
      vm.prepareRoles();
      vm.setGridActions();
      vm.setGridColumns();
      this.getKategorien().then(this.setKategorien);
    },
    
    prepareRoles: function() {
      vm.getRecht('ROLE_INVENTAR_PRODUKT_VERWALTEN');
    },

    hasNotRoleVerwalten: function() {
      return !vm.rechte['ROLE_INVENTAR_PRODUKT_VERWALTEN'];
    },

    updateGruppen: function() {
      vm.getGruppen().then(vm.setGruppen);
    },
    
    setGridColumns: function() {
      vm.grid.gridColumns = [
        { name: 'functions',
          title: 'Funktionen',
          sortable: false,
          width: 50,
          formatter: [
          { clazz: 'edit', disabled: vm.hasNotRoleVerwalten, title: 'Produkt bearbeiten', clickFunc: vm.editFunction },
          { clazz: 'delete', disabled: vm.hasNotRoleVerwalten, title: 'Produkt löschen', clickFunc: vm.deleteFunction }
        ] },
        { name: 'gruppe.kategorie.bezeichnung', title: 'Kategorie', width: 120 },
        { name: 'gruppe.bezeichnung', title: 'Gruppe', width: 150 },
        { name: 'bezeichnung', title: 'Bezeichnung', width: 500 },
        { name: 'ean', title: 'EAN', width: 120 },
        { name: 'bestand', title: 'Bestand', width: 100 },
        { name: 'preise', sortable: false, title: 'Preise', width: 110 },
        { name: 'hersteller', title: 'Hersteller', width: 150 },
      ];
    },
    
    setGridActions: function() {
      vm.grid.actions = [
        { clazz: 'add', disabled: vm.hasNotRoleVerwalten, title: 'Produkt hinzufügen', clickFunc: vm.addFunction }
      ]
    },
    
    getKategorien: function() {
      return axios.get('/inventar/produkt/kategorie');
    },
    
    setKategorien: function(response) {
      vm.kategorien = response.data;
    },
    
    getGruppen: function() {
      var kategorieId = this.grid.searchQuery.kategorie;
      if (kategorieId !== undefined) {
        return axios.get('/inventar/produkt/gruppe/' + kategorieId);
      } else {
        return { data: [] };
      }
    },
    
    setGruppen: function(response) {
      vm.gruppen = response.data;
    },
    
    getRecht: function(role) {
      return hasRole(role).then(vm.setRecht(role));
    },
    
    setRecht: function(role) {
      return function(response) {
        vm.rechte[role] = response.data;
      }
    }
    
  }
});

vm.init();
