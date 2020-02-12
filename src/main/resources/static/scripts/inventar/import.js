var vm = new Vue({
  i18n,
  el: '#import',
  created() {
    window.addEventListener('keydown', e => {
      if (e.key == 'Enter') {
        vm.search();
      }
    });
  },
  data: {
    file: null,
    produkte: [],
    rechte: {},
    result: {},
    showDialog: false,
  },
  methods: {

    importProdukte: function() {
      vm.executeImport().then(vm.handleImportResponse);
    },

    executeImport: function() {
      return axios.put('/inventar/import', vm.produkte);
    },

    handleImportResponse: function(response) {
      vm.result = response.data;
      vm.showDialog = true;
    },

    init: function() {
      vm.prepareRoles();
    },

    removeItem: function(index) {
      vm.produkte.splice(index, 1);
    },

    loadFile: function() {
      vm.file = this.$refs.produkteFile.files[0];
      vm.getProdukte().then(vm.setProdukte);
    },

    prepareRoles: function() {
      vm.getRecht('ROLE_INVENTAR_IMPORT');
    },

    hasNotRoleImport: function() {
      return !vm.rechte['ROLE_INVENTAR_IMPORT'];
    },

    getProdukte: function() {
      const config = { headers: { 'Content-Type': 'multipart/form-data' } };
      let fd = new FormData();
      fd.append('file', vm.file);
      return axios.post('inventar/import', fd, config);
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
