var vm = new Vue({
  i18n,
  el: '#einkauf',
  data: {
    einkauf: '',
    downloadEinkauf: '',
    rechte: {},
    result: {},
    showDialog: false,
  },
  methods: {

    clearEinkauf: function() {
      vm.einkauf = '';
      vm.executeClearEinkauf().then(vm.handleResponse);
    },

    executeClearEinkauf: function() {
      return axios.delete('/einkauf');
    },

    save: function() {
      showLoader();
      vm.executeSave()
        .then(vm.handleResponse)
        .then(hideLoader);
    },

    executeSave: function() {
      return axios.put('/einkauf', vm.downloadEinkauf, { headers: { "Content-Type": "text/plain" } });
    },

    handleResponse: function(response) {
      vm.result = response.data;
      vm.showDialog = true;
    },

    init: function() {
      showLoader();
      vm.getEinkauf()
        .then(vm.setEinkauf)
        .then(vm.getDownloadEinkauf)
        .then(vm.setDownloadEinkauf)
        .then(hideLoader);
    },

    getEinkauf: function() {
      return axios.get('/einkauf');
    },

    setEinkauf: function(response) {
      vm.einkauf = response.data;
    },

    getDownloadEinkauf: function() {
      return axios.get('/einkauf/download');
    },

    setDownloadEinkauf: function(response) {
      vm.downloadEinkauf = response.data;
    },

  }
});

vm.init();
