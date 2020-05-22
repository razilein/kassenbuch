var vm = new Vue({
  i18n,
  el: '#kategorie',
  created() {
    window.addEventListener('keydown', e => {
      var isDialogOpened = vm.showDialog || vm.showEditDialog || vm.showDeleteDialog;
      if (e.key == 'Enter' && !isDialogOpened) {
        vm.grid.reload = true;
      }
    });
  },
  data: {
    rechte: {},
    result: {},
    showDeleteDialog: false,
    showEditDialog: false,
    showDialog: false,
    deleteRow: {
      id: null,
      restUrl: '/inventar/kategorie',
      title: '',
    },
    editRow: {
      restUrlGet: '/inventar/kategorie/',
      restUrlSave: '/inventar/kategorie',
      title: '',
    },
    grid: {
      actions: [],
      gridColumns: [],
      reload: false,
      restUrl: 'inventar/kategorie',
      searchQuery: {},
      sort: 'bezeichnung'
    },
  },
  methods: {
    
    addFunction: function() {
      vm.editRow.restUrlGet = '/inventar/kategorie/' + -1;
      vm.editRow.title = this.$t('inventar.kategorie.hinzufuegen');
      vm.showEditDialog = true;
    },
    
    editFunction: function(row) {
      vm.editRow.restUrlGet = '/inventar/kategorie/' + row.id;
      vm.editRow.title = this.$t('inventar.produkt.kategorie') + ' ' + row.bezeichnung + ' ' + this.$t('general.bearbeiten');
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
      vm.deleteRow.title = this.$t('inventar.produkt.kategorie') + ' ' + row.bezeichnung + ' ' + this.$t('general.loeschen');
      vm.showDeleteDialog = true;
    },
    
    handleDeleteResponse: function(data) {
      vm.grid.reload = true;
      vm.showDeleteDialog = false;
      vm.result = data;
      vm.showDialog = true;
    },
    
    init: function() {
      vm.prepareRoles()
        .then(vm.setGridActions)
        .then(vm.setGridColumns);
    },
    
    prepareRoles: function() {
      return vm.getRecht('ROLE_INVENTAR_KATEGORIE_VERWALTEN');
    },
    
    hasNotRoleVerwalten: function() {
      return !vm.rechte['ROLE_INVENTAR_KATEGORIE_VERWALTEN'];
    },
    
    setGridColumns: function() {
      vm.grid.gridColumns = [
        { name: 'functions',
          title: this.$t('general.funktionen'),
          sortable: false,
          width: 50,
          formatter: [
          { clazz: 'edit', disabled: vm.hasNotRoleVerwalten, title: this.$t('inventar.kategorie.bearbeiten'), clickFunc: vm.editFunction },
          { clazz: 'delete', disabled: vm.hasNotRoleVerwalten, title: this.$t('inventar.kategorie.loeschen'), clickFunc: vm.deleteFunction }
        ] },
        { name: 'bezeichnung', title: this.$t('general.bezeichnung'), width: 200 },
      ];
    },
    
    setGridActions: function() {
      vm.grid.actions = [
        { clazz: 'add', disabled: vm.hasNotRoleVerwalten, title: this.$t('inventar.kategorie.hinzufuegen'), clickFunc: vm.addFunction }
      ]
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
