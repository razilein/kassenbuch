var vm = new Vue({
  el: '#kassenstand',
  data: {
    model: [
      { anzahl: 0, betrag: 0, multi: 500 },
      { anzahl: 0, betrag: 0, multi: 200 },
      { anzahl: 0, betrag: 0, multi: 100 },
      { anzahl: 0, betrag: 0, multi: 50 },
      { anzahl: 0, betrag: 0, multi: 20 },
      { anzahl: 0, betrag: 0, multi: 10 },
      { anzahl: 0, betrag: 0, multi: 5 },
      { anzahl: 0, betrag: 0, multi: 2 },
      { anzahl: 0, betrag: 0, multi: 1 },
//      { anzahl: 0, betrag: 0, multi: 0.5 },
//      { anzahl: 0, betrag: 0, multi: 0.2 },
//      { anzahl: 0, betrag: 0, multi: 0.1 },
//      { anzahl: 0, betrag: 0, multi: 0.05 },
//      { anzahl: 0, betrag: 0, multi: 0.02 },
//      { anzahl: 0, betrag: 0, multi: 0.01 },
    ],
    showDialog: false,
  },
  methods: {

    init: function() {
    },
    
    initModel: function() {
    },

  }
});

vm.init();
vm.initModel();