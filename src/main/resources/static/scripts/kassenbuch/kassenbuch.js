var vm = new Vue({
  el: '#kassenbuch',
  data: {
    manuell: {
      csvDatei: null,
      eintragungen: [],
    },
    model: {},
    result: {},
    showDialog: false,
  },
  methods: {

    addEintragung: function() {
      vm.manuell.eintragungen.push({
        betrag: 0,
        datum: getDateAsString(),
        eintragungsart: -1,
        gespeichert: false,
        verwendungszweck: null,
      });
    },

    anzeigen: function() {
      window.open('kassenbuch/download/');
    },

    erstellen: function() {
      showLoader();
      vm.erstellenExecute()
        .then(vm.setMessages)
        .then(hideLoader);
    },

    erstellenExecute: function() {
      return axios.post('kassenbuch/erstellen', vm.model);
    },

    downloadFile: function(response) {
      var newBlob = new Blob([response.data], {type: 'application/pdf'});
      const data = window.URL.createObjectURL(newBlob);
      var link = document.createElement('a');
      link.href = data;
      link.download='kassenbuch.pdf';
      link.click();
    },

    init: function() {
      showLoader();
      vm.getAusgangsbetrag()
        .then(vm.setAusgangsbetrag)
        .then(vm.getCsvDatei)
        .then(vm.setCsvDatei)
        .then(hideLoader);
    },

    initModel: function() {
      var today = getDateAsString();
      vm.model = {
        ausgangsbetrag : null,
        ausgangsbetragDatum: today,
        zeitraumBis: today,
        zeitraumVon: today
      }
    },

    saveEintragungen: function() {
      showLoader();
      vm.saveEintragungenExecute()
        .then(vm.markSavedEintragungen)
        .then(hideLoader);
    },

    saveEintragungenExecute: function() {
      return axios.post('kassenbuch/eintragungen/erstellen', vm.manuell);
    },

    markSavedEintragungen: function(response) {
      vm.setMessages(response);
      if (response.data.success) {
        for (var i = 0; i < vm.manuell.eintragungen.length; i++) { 
          vm.manuell.eintragungen[i].gespeichert = true;
        }
      }
    },

    removeEintragung: function(index) {
      vm.manuell.eintragungen.splice(index, 1);
    },

    getAusgangsbetrag: function() {
      return axios.get('kassenbuch/ausgangsbetrag');
    },

    setAusgangsbetrag: function(response) {
      vm.model.ausgangsbetrag = response.data;
    },

    getCsvDatei: function() {
      return axios.get('kassenbuch/csv');
    },

    setCsvDatei: function(response) {
      vm.manuell.csvDatei = response.data;
    },

    setMessages: function(response) {
      vm.result = response.data;
      vm.init();
      vm.showDialog = true;
    },

  }
});

vm.initModel();
vm.init();