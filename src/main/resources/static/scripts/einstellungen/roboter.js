var vm = new Vue({
  i18n,
  el: '#roboter',
  data: {
    einstellungen: {
      roboterCron: {},
      roboterFiliale: {},
      roboterMailBodyReparaturauftrag : {},
    },
    filialen: [],
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
      return axios.put('einstellungen/roboter', vm.einstellungen);
    },

    getEinstellungen: function() {
      return axios.get('einstellungen/roboter');
    },

    setEinstellungen: function(response) {
      vm.einstellungen = response.data;
    },
    
    getFilialen: function() {
      return axios.get('einstellungen/filiale');
    },
    
    setFilialen: function(response) {
      vm.filialen = response.data;
    },

    setMessages: function(response) {
      vm.result = response.data;
      vm.showDialog = true;
    },

  }
});

vm.init();