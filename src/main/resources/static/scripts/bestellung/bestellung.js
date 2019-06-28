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
      showLoader();
      vm.executeSave()
        .then(vm.handleResponse)
        .then(hideLoader);
    },

    executeSave: function() {
      return axios.put('/bestellung', vm.downloadBestellung, { headers: { "Content-Type": "text/plain" } });
    },

    handleResponse: function(response) {
      vm.result = response.data;
      vm.showDialog = true;
    },

    init: function() {
      showLoader();
      vm.getBestellung()
        .then(vm.setBestellung)
        .then(vm.getDownloadBestellung)
        .then(vm.setDownloadBestellung)
        .then(hideLoader);
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
