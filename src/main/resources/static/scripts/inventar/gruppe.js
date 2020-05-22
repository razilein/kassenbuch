var vm = new Vue({
  i18n,
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
      title: '',
    },
    editRow: {
      restUrlGet: '/inventar/gruppe/',
      restUrlSave: '/inventar/gruppe',
      title: '',
    },
    grid: {
      actions: [],
      gridColumns: [],
      reload: false,
      restUrl: 'inventar/gruppe',
      searchQuery: {},
      sort: 'kategorie.bezeichnung,bezeichnung'
    },
  },
  methods: {
    
    addFunction: function() {
      vm.editRow.restUrlGet = '/inventar/gruppe/' + -1;
      vm.editRow.title = this.$t('inventar.gruppe.hinzufuegen');
      vm.showEditDialog = true;
    },
    
    editFunction: function(row) {
      vm.editRow.restUrlGet = '/inventar/gruppe/' + row.id;
      vm.editRow.title = this.$t('inventar.produkt.gruppe') + ' ' + row.bezeichnung + ' ' + this.$t('general.bearbeiten');
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
      vm.deleteRow.title = this.$t('inventar.produkt.gruppe') + ' ' + row.bezeichnung + ' ' + this.$t('general.loeschen');
      vm.showDeleteDialog = true;
    },
    
    handleDeleteResponse: function(data) {
      vm.grid.reload = true;
      vm.showDeleteDialog = false;
      vm.result = data;
      vm.showDialog = true;
    },
    
    init: function() {
      this.getKategorien()
        .then(this.setKategorien)
        .then(vm.prepareRoles)
        .then(vm.setGridActions)
        .then(vm.setGridColumns);
    },
    
    prepareRoles: function() {
      return vm.getRecht('ROLE_INVENTAR_GRUPPE_VERWALTEN');
    },
    
    setGridColumns: function() {
      vm.grid.gridColumns = [
        { name: 'functions',
          title: this.$t('general.funktionen'),
          sortable: false,
          width: 50,
          formatter: [
          { clazz: 'edit', disabled: vm.hasNotRoleVerwalten, title: this.$t('inventar.gruppe.bearbeiten'), clickFunc: vm.editFunction },
          { clazz: 'delete', disabled: vm.hasNotRoleVerwalten, title: this.$t('inventar.gruppe.loeschen'), clickFunc: vm.deleteFunction }
        ] },
        { name: 'kategorie.bezeichnung', title: this.$t('inventar.produkt.kategorie'), width: 200 },
        { name: 'bezeichnung', title: this.$t('general.bezeichnung'), width: 200 },
      ];
    },
    
    setGridActions: function() {
      vm.grid.actions = [
        { clazz: 'add', disabled: vm.hasNotRoleVerwalten, title: this.$t('inventar.gruppe.hinzufuegen'), clickFunc: vm.addFunction }
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
