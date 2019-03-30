var vm = new Vue({
  el: '#bestellung',
  data: {
    bestellung: '',
    downloadBestellung: '',
    rechte: {},
    result: {},
    showDialog: false,
  },
  methods: {

    clearBestellung: function() {
      vm.bestellung = '';
      vm.executeClearBestellung().then(vm.handleResponse);
    },

    executeClearBestellung: function() {
      return axios.delete('/bestellung');
    },

    save: function() {
      vm.executeSave().then(vm.handleResponse);
    },

    executeSave: function() {
      return axios.put('/bestellung', vm.downloadBestellung, { headers: { "Content-Type": "text/plain" } });
    },

    handleResponse: function(response) {
      vm.result = response.data;
      vm.showDialog = true;
    },

    init: function() {
      vm.getBestellung()
        .then(vm.setBestellung)
        .then(vm.getDownloadBestellung)
        .then(vm.setDownloadBestellung);
    },

    getBestellung: function() {
      return axios.get('/bestellung');
    },

    setBestellung: function(response) {
      vm.bestellung = response.data;
    },

    getDownloadBestellung: function() {
      return axios.get('/bestellung/download');
    },

    setDownloadBestellung: function(response) {
      vm.downloadBestellung = response.data;
    },

  }
});

vm.init();
