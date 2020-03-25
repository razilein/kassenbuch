var vm = new Vue({
  i18n,
  el: '#statistiken',
  data: {
    model: {
      posten: '',
      zeitraumBis: null,
      zeitraumVon: null,
    },
    result: {},
    showDialog: false,
  },
  methods: {

    init: function() {
      vm.initModel();
    },

    erstelleStatistikZahlungsarten: function() {
      showLoader();
      vm.executeErstelleStatistikZahlungsarten()
        .then(vm.setMessages)
        .then(hideLoader)
    },

    executeErstelleStatistikZahlungsarten: function() {
      return axios.put('kassenbuch/statistiken/zahlungsarten', vm.model);
    },
    
    erstelleStatistikUeberweisungen: function() {
      showLoader();
      vm.executeErstelleStatistikUeberweisungen()
        .then(vm.setMessages)
        .then(hideLoader)
    },
    
    executeErstelleStatistikUeberweisungen: function() {
      return axios.put('kassenbuch/statistiken/ueberweisungen', vm.model);
    },

    initModel: function() {
      var today = getDateAsString();
      vm.model.zeitraumBis = today;
      vm.model.zeitraumVon = today;
    },

    setMessages: function(response) {
      vm.result = response.data;
      vm.showDialog = true;
    },

  }
});

vm.init();

