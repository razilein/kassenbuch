var vm = new Vue({
  el: '#bestellung-drucken',
  data: {
    currentDate: null,
    entity: {
      filiale: {},
      kunde: {},
    },
    einstellungDruckansichtDruckdialog: true,
    filiale: {},
  },
  methods: {
    
    init: function() {
      moment.locale('de');
      vm.currentDate = moment().format('DD.MM.YYYY');

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
