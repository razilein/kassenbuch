var vm = new Vue({
  i18n,
  el: '#reparatur-drucken',
  data: {
    currentDate: null,
    currentDay: null,
    entity: {
      filiale: {},
      kunde: {},
      mitarbeiter: {},
    },
    einstellungDruckansichtDruckdialog: true,
    filiale: {},
  },
  methods: {
    
    init: function() {
      moment.locale('de');
      this.currentDate = moment().format('DD.MM.YYYY');
      this.currentDay = moment().format('dddd');
      
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
      return axios.get('/reparatur/' + id);
    },
    
    setEntity: function(response) {
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
