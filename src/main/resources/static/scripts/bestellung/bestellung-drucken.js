var vm = new Vue({
  i18n,
  el: '#bestellung-drucken',
  data: {
    entity: {
      filiale: {},
      kunde: {},
    },
    einstellungDruckansichtDruckdialog: true,
    filiale: {},
  },
  methods: {
    
    init: function() {
      this.getEntity()
        .then(this.setEntity)
        .then(this.getEinstellungDruckansichtDruckdialog)
        .then(this.setEinstellungDruckansichtDruckdialog)
        .then(this.openPrint);
    },
    
    openPrint: function() {
      if (vm.einstellungDruckansichtDruckdialog) {
        window.print();
      }
    },
    
    getEntity: function() {
      var id = getParamFromCurrentUrl('id');
      return axios.get('/bestellung/' + id);
    },
    
    setEntity: function(response) {
      if (!response.data.kunde) {
        response.data.kunde = {};
      }
      response.data.erstelltAm = response.data.erstelltAm.substr(0, response.data.erstelltAm.indexOf(' '));
      this.entity = response.data;
    },
    
    getEinstellungDruckansichtDruckdialog: function() {
      return axios.get('/mitarbeiter-profil');
    },
    
    setEinstellungDruckansichtDruckdialog: function(response) {
      this.einstellungDruckansichtDruckdialog = response.data.druckansichtDruckdialog;
    },
    
  }
});

vm.init();
