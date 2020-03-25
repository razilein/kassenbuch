var vm = new Vue({
  i18n,
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
      title: '',
    },
    editRow: {
      restUrlGet: '/inventar/produkt/',
      restUrlSave: '/inventar/produkt',
      title: '',
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
      vm.editRow.title = this.$t('inventar.produkt.hinzufuegen');
      vm.showEditDialog = true;
    },
    
    editFunction: function(row) {
      vm.editRow.restUrlGet = '/inventar/produkt/' + row.id;
      vm.editRow.title = this.$t('general.produkt') + ' ' + row.bezeichnung + ' ' + this.$t('general.bearbeiten');
      vm.editRow.duplicate = false;
      vm.showEditDialog = true;
    },
    
    duplicateFunction: function(row) {
      vm.editRow.restUrlGet = '/inventar/produkt/' + row.id;
      vm.editRow.title = this.$t('general.produkt') + ' ' + row.bezeichnung + ' ' + this.$t('general.duplizieren');
      vm.editRow.duplicate = true;
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
      vm.deleteRow.title = this.$t('general.produkt') + ' ' + row.bezeichnung + ' ' + this.$t('general.loeschen');
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
      vm.setGridSearch();
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
    
    saveSearchQuerySortierung: function() {
      saveInLocalstorage(SUCHE.PRODUKT.SORT_VERKAEUFE, vm.grid.searchQuery.sortierung);
    },
    
    setGridColumns: function() {
      vm.grid.gridColumns = [
        { name: 'functions',
          title: this.$t('general.funktionen'),
          sortable: false,
          width: 130,
          formatter: [
          { clazz: 'edit', disabled: vm.hasNotRoleVerwalten, title: this.$t('inventar.produkt.bearbeiten'), clickFunc: vm.editFunction },
          { clazz: 'duplicate', disabled: vm.hasNotRoleVerwalten, title: this.$t('inventar.produkt.duplizieren'), clickFunc: vm.duplicateFunction },
          { clazz: 'delete', disabled: vm.hasNotRoleVerwalten, title: this.$t('inventar.produkt.loeschen'), clickFunc: vm.deleteFunction }
        ] },
        { name: 'gruppe.kategorie.bezeichnung', title: this.$t('inventar.produkt.kategorie'), width: 120 },
        { name: 'gruppe.bezeichnung', title: this.$t('inventar.produkt.gruppe'), width: 150 },
        { name: 'hersteller', title: this.$t('inventar.produkt.hersteller'), width: 150 },
        { name: 'bezeichnung', title: this.$t('general.bezeichnung'), width: 500 },
        { name: 'ean', title: this.$t('inventar.produkt.ean'), width: 120 },
        { name: 'bestand', title: this.$t('inventar.produkt.bestand'), width: 100 },
        { name: 'preise', sortable: false, title: this.$t('inventar.produkt.preise'), width: 110 },
      ];
    },
    
    setGridActions: function() {
      vm.grid.actions = [
        { clazz: 'add', disabled: vm.hasNotRoleVerwalten, title: this.$t('inventar.produkt.hinzufuegen'), clickFunc: vm.addFunction }
      ]
    },
    
    setGridSearch: function() {
      vm.grid.searchQuery.sortierung = getFromLocalstorage(SUCHE.PRODUKT.SORT_VERKAEUFE) === 'true';
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
