var vm = new Vue({
  el: '#gruppe',
  created() {
    window.addEventListener('keydown', e => {
      var isDialogOpened = vm.showDialog || vm.showEditDialog || vm.showDeleteDialog;
      if (e.key == 'Enter' && !isDialogOpened) {
        vm.grid.reload = true;
      }
    });
  },
  data: {
    kategorien: [],
    rechte: {},
    result: {},
    showDeleteDialog: false,
    showEditDialog: false,
    showDialog: false,
    deleteRow: {
      id: null,
      restUrl: '/inventar/gruppe',
      title: 'Gruppe löschen',
    },
    editRow: {
      restUrlGet: '/inventar/gruppe/',
      restUrlSave: '/inventar/gruppe',
      title: 'Gruppe bearbeiten',
    },
    grid: {
      actions: [],
      gridColumns: [],
      reload: false,
      restUrl: 'inventar/gruppe',
      searchQuery: {},
    },
  },
  methods: {
    
    addFunction: function() {
      vm.editRow.restUrlGet = '/inventar/gruppe/' + -1;
      vm.editRow.title = 'Gruppe hinzufügen';
      vm.showEditDialog = true;
    },
    
    editFunction: function(row) {
      vm.editRow.restUrlGet = '/inventar/gruppe/' + row.id;
      vm.editRow.title = 'Gruppe ' + row.bezeichnung + ' bearbeiten';
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
      vm.deleteRow.title = 'Gruppe ' + row.bezeichnung + ' löschen';
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
      vm.getRecht('ROLE_INVENTAR_GRUPPE_VERWALTEN');
    },
    
    setGridColumns: function() {
      vm.grid.gridColumns = [
        { name: 'functions',
          title: 'Funktionen',
          sortable: false,
          width: 50,
          formatter: [
          { clazz: 'edit', disabled: vm.hasNotRoleVerwalten, title: 'Gruppe bearbeiten', clickFunc: vm.editFunction },
          { clazz: 'delete', disabled: vm.hasNotRoleVerwalten, title: 'Gruppe löschen', clickFunc: vm.deleteFunction }
        ] },
        { name: 'kategorie.bezeichnung', title: 'Kategorie', width: 200 },
        { name: 'bezeichnung', title: 'Bezeichnung', width: 200 },
      ];
    },
    
    setGridActions: function() {
      vm.grid.actions = [
        { clazz: 'add', disabled: vm.hasNotRoleVerwalten, title: 'Gruppe hinzufügen', clickFunc: vm.addFunction }
      ]
    },
    
    getKategorien: function() {
      return axios.get('/inventar/gruppe/kategorie');
    },
    
    setKategorien: function(response) {
      this.kategorien = response.data;
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
