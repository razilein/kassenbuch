var vm = new Vue({
  el: '#einstellungen',
  data: {
    einstellungen: {
      ablageverzeichnis: {},
      rechnungsverzeichnis: {}
    },
    result: {},
    showDialog: false,
  },
  methods: {

    init: function() {
      vm.getEinstellungen()
        .then(vm.setEinstellungen)
    },

    speichernEinstellungen: function() {
      vm.executeSpeichernEinstellungen()
        .then(vm.setMessages)
    },

    executeSpeichernEinstellungen: function() {
      return axios.put('einstellungen', vm.einstellungen);
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