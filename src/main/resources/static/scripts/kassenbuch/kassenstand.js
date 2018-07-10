var vm = new Vue({
  el: '#kassenstand',
  data: {
    kassenbuchbetrag: 0,
    kassenbetrag: 0,
    differenz: 0,
    model: [
      { anzahl: 0, betrag: 0, multi: 500, key: '500' },
      { anzahl: 0, betrag: 0, multi: 200, key: '200' },
      { anzahl: 0, betrag: 0, multi: 100, key: '100' },
      { anzahl: 0, betrag: 0, multi: 50, key: '50' },
      { anzahl: 0, betrag: 0, multi: 20, key: '20' },
      { anzahl: 0, betrag: 0, multi: 10, key: '10' },
      { anzahl: 0, betrag: 0, multi: 5, key: '5' },
      { anzahl: 0, betrag: 0, multi: 2, key: '2' },
      { anzahl: 0, betrag: 0, multi: 1, key: '1' },
      { anzahl: 0, betrag: 0, multi: 0.5, key: '050' },
      { anzahl: 0, betrag: 0, multi: 0.2, key: '020' },
      { anzahl: 0, betrag: 0, multi: 0.1, key: '010' },
      { anzahl: 0, betrag: 0, multi: 0.05, key: '005' },
      { anzahl: 0, betrag: 0, multi: 0.02, key: '002' },
      { anzahl: 0, betrag: 0, multi: 0.01, key: '001' },
    ],
    result: {},
    showDialog: false,
  },
  methods: {

    berechne: function() {
      vm.kassenbetrag = 0;
      for (var i = 0; i < vm.model.length; i++) {
        vm.model[i].anzahl = parseInt(vm.model[i].anzahl || 0);
        vm.model[i].betrag = parseFloat(vm.model[i].betrag || vm.model[i].anzahl * vm.model[i].multi);
        vm.kassenbetrag += vm.model[i].betrag;
      }
      vm.differenz = Number((vm.kassenbetrag - vm.kassenbuchbetrag).toFixed(2));
    },

    init: function() {
      vm.getKassenbuchbetrag()
        .then(vm.setKassenbuchBetrag)
        .then(vm.getKassenstand)
        .then(vm.setKassenstand)
        .then(vm.berechne);
    },

    save: function() {
      vm.executeSave()
        .then(vm.setMessages)
        .then(vm.init);
    },

    executeSave: function() {
      return axios.put('kassenbuch/kassenstand', vm.model);
    },

    getKassenstand: function() {
      return axios.get('kassenbuch/kassenstand');
    },

    setKassenstand: function(response) {
      var data = response.data;
      for (var i = 0; i < vm.model.length; i++) {
        for (var j = 0; j < data.length; j++) {
          if (data[j].key === vm.model[i].key) {
            vm.model[i].anzahl = data[j].anzahl;
            break;
          }
        }
      }
    },

    getKassenbuchbetrag: function() {
      return axios.get('kassenbuch/ausgangsbetrag');
    },

    setKassenbuchBetrag: function(response) {
      vm.kassenbuchbetrag = response.data;
    },

    setMessages: function(response) {
      vm.result = response.data;
      vm.init();
      vm.showDialog = true;
    },

  }
});

vm.init();
