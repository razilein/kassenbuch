var vm = new Vue({
  el: '#rechnung-drucken',
  data: {
    art: 'UNDEFINIERT',
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
    gesamtnetto: 0.00,
    gesamtmwst: 0.00,
    gesamtrabatt: 0.00,
    gesamtsumme: 0.00,
  },
  methods: {
    
    init: function() {
      moment.locale('de');
      vm.currentDate = moment().format('DD.MM.YYYY');
      vm.currentDay = moment().format('dddd');
      
      vm.getEntity()
        .then(vm.setEntity)
        .then(vm.setZahlart);
    },
    
    setZahlart: function() {
      switch(vm.entity.rechnung.art) {
        case 0:
          vm.art = 'BAR';
          break;
        case 1:
          vm.art = 'EC';
          break;
        case 2:
          vm.art = 'ÜBERWEISUNG';
          break;
        case 3:
          vm.art = 'PAYPAL';
          break;
        default:
          vm.art = 'UNDEFINIERT';
      }
      vm.entity.posten.forEach(function(element) {
        vm.gesamtsumme = vm.gesamtsumme + element.gesamt;
        vm.gesamtrabatt = vm.gesamtrabatt + element.rabatt;
      });
      vm.gesamtnetto = vm.gesamtsumme / 1.19;
      vm.gesamtmwst = vm.gesamtsumme - vm.gesamtnetto;
      vm.entity.rechnung.datum = formatDate(vm.entity.rechnung.datum);
    },
    
    getEntity: function() {
      var id = getParamFromCurrentUrl('id');
      return axios.get('/rechnung/' + id);
    },
    
    setEntity: function(response) {
      vm.entity = response.data;
    },
    
  }
});

vm.init();
