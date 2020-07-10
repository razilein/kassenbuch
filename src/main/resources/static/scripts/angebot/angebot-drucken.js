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
    gesamtrabattP: 0.00
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
      
      var mwst = (vm.entity.angebot.mwst + 100.00);
      this.entity.angebotsposten.forEach(function(element) {
        vm.gesamtNetto = vm.gesamtNetto + (element.preis * element.menge * 100 / mwst);
      });
      var rabatt = vm.entity.angebot.rabatt || 0;
      vm.gesamtrabattP = Number(vm.gesamtNetto * vm.entity.angebot.rabattP / 100).toFixed(2);
      vm.mwst = (vm.gesamtNetto - rabatt - vm.gesamtrabattP) * vm.entity.angebot.mwst / 100;
      vm.gesamtBrutto = vm.gesamtNetto - rabatt - vm.gesamtrabattP + vm.mwst;
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
