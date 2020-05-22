var vm = new Vue({
  i18n,
  el: '#filiale',
  data: {
    rechte: {},
    result: {},
    showDialog: false,
    showEditDialog: false,
    editRow: {
      restUrlGet: '/einstellungen/filiale/',
      restUrlSave: '/einstellungen/filiale',
      title: '',
    },
    grid: {
      actions: [],
      gridColumns: [],
      reload: false,
      restUrl: 'einstellungen/filiale',
      searchQuery: {},
      sort: 'kuerzel'
    },
  },
  methods: {
    
    addFunction: function() {
      vm.editRow.restUrlGet = '/einstellungen/filiale/' + -1;
      vm.editRow.title = this.$t('einstellung.filiale.hinzufuegen');
      vm.showEditDialog = true;
    },
    
    editFunction: function(row) {
      vm.editRow.restUrlGet = '/einstellungen/filiale/' + row.id;
      vm.editRow.title = this.$t('general.filiale') + ' ' + row.name + ' ' + this.$t('general.bearbeiten');
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
      //TODO Remove Delete for Filiale
      vm.deleteRow.id = row.id;
      vm.deleteRow.title = this.$t('general.filiale') + ' ' + row.name + ' ' + this.$t('general.loeschen');
      vm.showDeleteDialog = true;
    },
    
    handleDeleteResponse: function(data) {
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
      return vm.getRecht('ROLE_FILIALEN_VERWALTEN');
    },
    
    hasNotRoleVerwalten: function() {
      return !vm.rechte['ROLE_FILIALEN_VERWALTEN'];
    },
    
    setGridColumns: function() {
      vm.grid.gridColumns = [
        { name: 'functions',
          title: this.$t('general.funktionen'),
          sortable: false,
          width: 120,
          formatter: [
          { clazz: 'edit', disabled: vm.hasNotRoleVerwalten, title: this.$t('einstellung.filiale.bearbeiten'), clickFunc: vm.editFunction },
        ] },
        { name: 'kuerzel', title: this.$t('general.kuerzel'), width: 80 },
        { name: 'name', title: this.$t('general.name'), width: 100 },
        { name: 'strasse', title: this.$t('general.strasse'), width: 60 },
        { name: 'plz', title: this.$t('general.plz'), width: 50 },
        { name: 'ort', title: this.$t('general.ort'), width: 100 },
      ];
    },
    
    setGridActions: function() {
      vm.grid.actions = [
        { clazz: 'add', disabled: vm.hasNotRoleVerwalten, title: this.$t('einstellung.filiale.hinzufuegen'), clickFunc: vm.addFunction }
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
