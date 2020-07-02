var vm = new Vue({
  i18n,
  el: '#rechnung-drucken',
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
    exemplare: parseInt(getParamFromCurrentUrl('exemplare') || 2),
    gesamtnetto: 0.00,
    gesamtmwst: 0.00,
    gesamtrabatt: 0.00,
    gesamtrabattP: 0.00,
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
        .then(vm.setZahlart)
        .then(vm.openPrint);
    },
    
    openPrint: function() {
      if (vm.einstellungDruckansichtDruckdialog) {
        window.print();
      }
    },
    
    setZahlart: function() {
      switch(vm.entity.rechnung.art) {
        case 0:
          vm.art = this.$t('rechnung.zahlungsarten.bar');
          break;
        case 1:
          vm.art = this.$t('rechnung.zahlungsarten.ec');
          break;
        case 2:
          vm.art = this.$t('rechnung.zahlungsarten.ueberweisung');
          break;
        case 3:
          vm.art = this.$t('rechnung.zahlungsarten.paypal');
          break;
        default:
          vm.art = this.$t('rechnung.zahlungsarten.undefiniert');
      }
      vm.entity.posten.forEach(function(element) {
        vm.gesamtsumme = vm.gesamtsumme + element.gesamt;
        vm.gesamtrabatt = vm.gesamtrabatt + element.rabatt;
      });
      vm.gesamtsumme = vm.gesamtsumme - vm.entity.rechnung.rabatt;
      if (vm.entity.rechnung.rabattP) {
        vm.gesamtrabattP = vm.gesamtsumme * vm.entity.rechnung.rabattP / 100;
        vm.gesamtsumme = vm.gesamtsumme - vm.gesamtrabattP;
      }
      vm.gesamtnetto = vm.gesamtsumme * 100 / (vm.entity.rechnung.mwst + 100.0);
      vm.gesamtmwst = vm.gesamtsumme - vm.gesamtnetto;
      vm.entity.rechnung.datum = formatDate(vm.entity.rechnung.datum);
      vm.entity.rechnung.lieferdatum = formatDate(vm.entity.rechnung.lieferdatum);
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
