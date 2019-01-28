var vm = new Vue({
  el: '#reparatur-drucken',
  data: {
    currentDate: null,
    currentDay: null,
    entity: {
      filiale: {},
      kunde: {},
      mitarbeiter: {},
    },
    filiale: {},
  },
  methods: {
    
    init: function() {
      moment.locale('de');
      this.currentDate = moment().format('DD.MM.YYYY');
      this.currentDay = moment().format('dddd');
      
      this.getEntity().then(this.setEntity);
    },
    
    getEntity: function() {
      var id = getParamFromCurrentUrl('id');
      return axios.get('/reparatur/' + id);
    },
    
    setEntity: function(response) {
      this.entity = response.data;
    },
    
  }
});

vm.init();
