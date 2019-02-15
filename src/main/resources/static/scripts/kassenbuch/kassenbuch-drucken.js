var vm = new Vue({
  el: '#kassenbuch-drucken',
  data: {
    entity: {
      kassenbuch: {},
      posten: []
    },
    gesamtAusgaben: 0.00,
    gesamtEinnahmen: 0.00
  },
  methods: {
    
    init: function() {
      this.getEntity().then(this.setEntity)
    },
    
    getEntity: function() {
      var id = getParamFromCurrentUrl('id');
      return axios.get('/kassenbuch/drucken/' + id);
    },
    
    setEntity: function(response) {
      var data = response.data;
      
      var betrag = data.kassenbuch.ausgangsbetrag;
      data.posten.forEach(function(element, index) {
        betrag = betrag + element.betrag;
        data.posten[index].gesamt = betrag;
        
        if (element.betrag < 0) {
          vm.gesamtAusgaben = vm.gesamtAusgaben + element.betrag;
        } else {
          vm.gesamtEinnahmen = vm.gesamtEinnahmen + element.betrag;
        }
      });
      this.entity = data;
    },
    
  }
});

vm.init();
