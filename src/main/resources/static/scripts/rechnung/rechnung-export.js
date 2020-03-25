var vm = new Vue({
  i18n,
  el: '#rechnungen-export',
  data: {
    monate: [],
    rechte: {},
    result: {},
    searchQuery: {
      monat: moment().month() + 1,
      jahr: moment().year(),
    },
    showDialog: false,
  },
  methods: {

    areRequiredFieldsNotEmpty: function() {
      return this.searchQuery && hasAllProperties(this.searchQuery, ['jahr', 'monat']);
    },

    exportRechnungen: function() {
      vm.executeExport().then(vm.handleExportResponse);
    },

    executeExport: function() {
      return axios.put('/rechnung/export', vm.searchQuery);
    },

    handleExportResponse: function(response) {
      vm.result = response.data;
      vm.showDialog = true;
    },

    init: function() {
      vm.prepareRoles();
      vm.getMonate()
        .then(vm.setMonate);
    },

    prepareRoles: function() {
      vm.getRecht('ROLE_RECHNUNG_EXPORT');
    },

    hasNotRoleExport: function() {
      return !vm.rechte['ROLE_RECHNUNG_EXPORT'];
    },

    getMonate: function() {
      return axios.get('/rechnung/monate');
    },

    setMonate: function(response) {
      vm.monate = response.data;
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
