var vm = new Vue({
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
      vm.executeErstelleStatistikZahlungsarten()
        .then(vm.setMessages)
    },

    executeErstelleStatistikZahlungsarten: function() {
      return axios.put('kassenbuch/statistiken/zahlungsarten', vm.model);
    },
    
    erstelleStatistikUeberweisungen: function() {
      vm.executeErstelleStatistikUeberweisungen()
      .then(vm.setMessages)
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

