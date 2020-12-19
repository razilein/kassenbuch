var vm = new Vue({
  i18n,
  el: '#rechnung-storno-drucken',
  data: {
    art: '',
    currentDate: null,
    currentDay: null,
    entity: {
      storno: {
        filiale: {},
        kunde: {}
      },
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
    gesamtnetto: 0.00,
    gesamtmwst: 0.00,
    gesamtrabatt: 0.00,
    gesamtsumme: 0.00,
  },
  methods: {
    
    init: function() {
      vm.art = this.$t('rechnung.zahlungsarten.undefiniert');
      moment.locale('de');
      vm.currentDate = moment().format('DD.MM.YYYY');
      vm.currentDay = moment().format('dddd');
      
      vm.getEntity()
        .then(vm.setEntity)
        .then(vm.getEinstellungDruckansichtDruckdialog)
        .then(vm.setEinstellungDruckansichtDruckdialog)
        .then(vm.initPosten)
        .then(vm.openPrint);
    },
    
    openPrint: function() {
      if (vm.einstellungDruckansichtDruckdialog) {
        window.print();
      }
    },
    
    initPosten: function() {
      vm.entity.posten.forEach(function(element) {
        vm.gesamtsumme = vm.gesamtsumme + element.gesamt;
        vm.gesamtrabatt = vm.gesamtrabatt + element.rabatt;
      });
      vm.gesamtsumme = vm.gesamtsumme - vm.entity.rechnung.rabatt;
      vm.gesamtnetto = vm.gesamtsumme * 100 / (vm.entity.rechnung.mwst + 100.0);
      vm.gesamtmwst = vm.gesamtsumme - vm.gesamtnetto;
      vm.entity.storno.datum = formatDate(vm.entity.storno.datum);
    },
    
    getEntity: function() {
      var id = getParamFromCurrentUrl('id');
      return axios.get('/rechnung/' + id + '/stornobeleg');
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
