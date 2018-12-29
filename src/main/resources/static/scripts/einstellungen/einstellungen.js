var vm = new Vue({
  el: '#einstellungen',
  data: {
    einstellungen: {
      ablageverzeichnis: {},
      filiale: {},
      rechnungsnummer: {},
      reparaturnummer: {}
    },
    filialen: {},
    result: {},
    showDialog: false,
  },
  methods: {

    init: function() {
      showLoader();
      vm.getFilialen()
        .then(vm.setFilialen)
        .then(vm.getEinstellungen)
        .then(vm.setEinstellungen)
        .then(hideLoader);
    },

    speichernEinstellungen: function() {
      showLoader();
      vm.executeSpeichernEinstellungen()
        .then(vm.setMessages)
        .then(hideLoader);
    },

    executeSpeichernEinstellungen: function() {
      return axios.put('einstellungen', vm.einstellungen);
    },
    
    getFilialen: function() {
      return axios.get('einstellungen/filiale');
    },
    
    setFilialen: function(response) {
      vm.filialen = response.data;
    },

    getEinstellungen: function() {
      return axios.get('einstellungen');
    },

    setEinstellungen: function(response) {
      vm.einstellungen = response.data;
    },

    setMessages: function(response) {
      vm.result = response.data;
      vm.showDialog = true;
    },

  }
});

vm.init();