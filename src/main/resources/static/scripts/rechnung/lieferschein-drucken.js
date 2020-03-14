var vm = new Vue({
  i18n,
  el: '#lieferschein-drucken',
  data: {
    art: '',
    currentDate: null,
    currentDay: null,
    entity: {
      rechnung: {
        filiale: {},
        kunde: {},
        reparatur: {
          filiale: {}
        },
      },
      posten: []
    },
    einstellungDruckansichtDruckdialog: true,
  },
  methods: {
    
    init: function() {
      moment.locale('de');
      vm.currentDate = moment().format('DD.MM.YYYY');
      vm.currentDay = moment().format('dddd');
      
      vm.getEntity()
        .then(vm.setEntity)
        .then(vm.getEinstellungDruckansichtDruckdialog)
        .then(vm.setEinstellungDruckansichtDruckdialog)
        .then(vm.openPrint);
    },
    
    openPrint: function() {
      if (vm.einstellungDruckansichtDruckdialog) {
        window.print();
      }
    },
    
    getEntity: function() {
      var id = getParamFromCurrentUrl('id');
      return axios.get('/rechnung/' + id);
    },
    
    setEntity: function(response) {
      vm.entity = response.data;
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
