var vm = new Vue({
  el: '#rechnung-drucken',
  data: {
    art: 'UNDEFINIERT',
    currentDate: null,
    currentDay: null,
    entity: {
      rechnung: {
        kunde: {},
        reparatur: {},
      },
      posten: []
    },
    filiale: {},
    gesamtnetto: 0.00,
    gesamtmwst: 0.00,
    gesamtsumme: 0.00,
  },
  methods: {
    
    init: function() {
      moment.locale('de');
      vm.currentDate = moment().format('DD.MM.YYYY');
      vm.currentDay = moment().format('dddd');
      
      vm.getEntity()
        .then(vm.setEntity)
        .then(vm.getFiliale)
        .then(vm.setFiliale)
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
      });
      vm.gesamtmwst = vm.gesamtsumme * 19 / 100;
      vm.gesamtnetto = vm.gesamtsumme - vm.gesamtmwst;
      vm.entity.rechnung.datum = formatDate(vm.entity.rechnung.datum);
    },
    
    getEntity: function() {
      var id = getParamFromCurrentUrl('id');
      return axios.get('/rechnung/' + id);
    },
    
    setEntity: function(response) {
      vm.entity = response.data;
    },
    
    getFiliale: function() {
      return axios.get('/einstellungen/standardfiliale');
    },
    
    setFiliale: function(response) {
      vm.filiale = response.data;
    },
    
  }
});

vm.init();
