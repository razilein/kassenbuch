var vm = new Vue({
  i18n,
  el: '#export',
  created() {
    window.addEventListener('keydown', e => {
      if (e.key == 'Enter') {
        vm.search();
      }
    });
  },
  data: {
    produkte: [],
    rechte: {},
    result: {},
    searchQuery: {
      datum_bis: null,
      datum_von: null,
    },
    showDialog: false,
  },
  methods: {

    exportProdukte: function() {
      vm.executeExport().then(vm.handleExportResponse);
    },

    executeExport: function() {
      return axios.put('/inventar/export', vm.produkte);
    },

    handleExportResponse: function(response) {
      vm.result = response.data;
      vm.showDialog = true;
    },

    init: function() {
      vm.prepareRoles().then(vm.search);
    },

    removeItem: function(index) {
      vm.produkte.splice(index, 1);
    },

    search: function() {
      vm.getProdukte().then(vm.setProdukte);
    },

    prepareRoles: function() {
      return vm.getRecht('ROLE_INVENTAR_EXPORT');
    },

    hasNotRoleExport: function() {
      return !vm.rechte['ROLE_INVENTAR_EXPORT'];
    },

    getProdukte: function() {
      return axios.post('inventar/export', vm.searchQuery);
    },

    setProdukte: function(response) {
      vm.produkte = response.data;
    },

    getRecht: function(role) {
      return hasRole(role).then(vm.setRecht(role));
    },

    setRecht: function(role) {
      return function(response) {
        vm.rechte[role] = response.data;
      }
    },

  }
});

vm.init();
