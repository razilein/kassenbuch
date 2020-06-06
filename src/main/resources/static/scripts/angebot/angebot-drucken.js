var vm = new Vue({
  i18n,
  el: '#angebot-drucken',
  data: {
    currentDate: null,
    entity: {
      angebot: {
        erstelltAm: '',
        filiale: {},
        kunde: {}
      },
      angebotsposten: []
    },
    einstellungDruckansichtDruckdialog: true,
    gesamtNetto: 0.00,
    gesamtBrutto: 0.00,
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
      return axios.get('/angebot/' + id);
    },
    
    setEntity: function(response) {
      this.entity = response.data;
      this.entity.angebotsposten.forEach(function(element) {
        vm.gesamtBrutto = vm.gesamtBrutto + (element.preis * element.menge);
      });
      vm.gesamtNetto = vm.gesamtBrutto * 100 / (vm.entity.angebot.mwst + 100.00);
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
