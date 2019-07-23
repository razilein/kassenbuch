var vm = new Vue({
  el: '#kassenbuch',
  created() {
    window.addEventListener('keydown', e => {
      var isDialogOpened = vm.showDialog || vm.showEditDialog;
      if (e.key == 'Enter' && !isDialogOpened) {
        vm.suchen();
      }
    });
  },
  data: {
    editEntity: {},
    gesamt: 0.00,
    model: {
      kassenbuch: {},
      posten: []
    },
    einstellungDruckansichtNeuesFenster: true,
    result: {},
    showDialog: false,
    showEditDialog: false,
  },
  methods: {
    
    openAddEintragungDialog: function() {
      vm.editEntity = {
        betrag: 0.00,
        eintragungsart: -1,
        verwendungszweck: null,
      };
      vm.showEditDialog = true;
    },
    
    areRequiredFieldsNotEmpty: function() {
      return this.model && this.model.posten.length > 0 && hasAllPropertiesAndNotEmpty(this.model, ['kassenbuch.ausgangsbetrag', 'kassenbuch.datum']);
    },
    
    berechneGesamt: function() {
      var gesamt = 0;
      vm.model.posten.forEach(function(element) {
        gesamt = gesamt + Number((element.betrag).toFixed(2));
      });
      var ausgangsbetrag = vm.model.kassenbuch.ausgangsbetrag || 0;
      gesamt = gesamt || 0;
      vm.gesamt = parseFloat(ausgangsbetrag) + gesamt;
    },
    
    executeSave: function() {
      return axios.put('/kassenbuch', vm.model);
    },
    
    handleEditResponse: function(response) {
      vm.editEntity = response;
      vm.editEntity.betrag = vm.editEntity.betrag * vm.editEntity.eintragungsart;
      vm.model.posten.push(vm.editEntity);
      vm.showEditDialog = false;
      vm.berechneGesamt();
    },
    
    openKassenbuch: function(response) {
      var data = response.data;
      if (data.success) {
        var id = data.kassenbuch.id;
        if (vm.einstellungDruckansichtNeuesFenster) {
          window.open('/kassenbuch-drucken.html?id=' + id, '_blank', 'resizable=yes');
        } else {
          window.open('/kassenbuch-drucken.html?id=' + id);
        }
        vm.init();
      }
      vm.result = data;
      vm.showDialog = true;
    },
    
    saveFunc: function() {
      showLoader();
      vm.executeSave()
        .then(vm.openKassenbuch)
        .then(hideLoader);
    },

    suchen: function() {
      vm.getPosten().then(vm.setPosten);
    },

    init: function() {
      showLoader();
      vm.getModel()
        .then(vm.setModel)
        .then(vm.getEinstellungDruckansichtNeuesFenster)
        .then(vm.setEinstellungDruckansichtNeuesFenster)
        .then(vm.berechneGesamt)
        .then(hideLoader);
    },

    getModel: function() {
      return axios.get('kassenbuch');
    },
    
    setModel: function(response) {
      vm.model = response.data;
    },
    
    getEinstellungDruckansichtNeuesFenster: function() {
      return axios.get('/mitarbeiter-profil');
    },
    
    setEinstellungDruckansichtNeuesFenster: function(response) {
      vm.einstellungDruckansichtNeuesFenster = response.data.druckansichtNeuesFenster;
    },
    
    getPosten: function() {
      return axios.get('kassenbuch/' + vm.model.kassenbuch.datum);
    },
    
    setPosten: function(response) {
      vm.model.posten = response.data;
    },

  }
});

vm.init();