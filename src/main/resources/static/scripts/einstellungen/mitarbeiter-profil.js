var vm = new Vue({
  el: '#mitarbeiterProfil',
  data: {
    filialen: [],
    model: {
      anmeldedaten: {},
      profildaten: {
        filiale: {}
      }
    },
    result: {},
    showDialog: false,
  },
  methods: {
    
    areRequiredFieldsNotEmptyAnmeldedaten: function() {
      return this.model &&
        this.model.anmeldedaten &&
        this.model.anmeldedaten.password === this.model.anmeldedaten.passwordRepeat &&
        hasAllPropertiesAndNotEmpty(this.model.anmeldedaten, ['username', 'passwordBefore']);
    },
    
    areRequiredFieldsNotEmptyProfildaten: function() {
      return this.model && this.model.profildaten && hasAllPropertiesAndNotEmpty(this.model.profildaten, ['nachname', 'vorname']);
    },
    
    init: function() {
      showLoader();
      vm.getAnmeldedaten()
        .then(vm.setAnmeldedaten)
        .then(vm.getProfildaten)
        .then(vm.setProfildaten)
        .then(vm.getFilialen)
        .then(vm.setFilialen)
        .then(hideLoader);
    },
    
    saveAnmeldedatenFunc: function() {
      vm.executeSaveAnmeldedaten()
        .then(vm.handleResponse);
    },
    
    executeSaveAnmeldedaten: function() {
      return axios.put('/mitarbeiter-profil/anmeldedaten', vm.model.anmeldedaten);
    },
    
    saveProfildatenFunc: function() {
      vm.executeSaveProfildaten()
      .then(vm.handleResponse);
    },
    
    executeSaveProfildaten: function() {
      return axios.put('/mitarbeiter-profil', vm.model.profildaten);
    },
    
    handleResponse: function(response) {
      vm.result = response.data;
      vm.showDialog = true;
    },
    
    getAnmeldedaten: function() {
      return axios.get('/mitarbeiter-profil/anmeldedaten');
    },
    
    setAnmeldedaten: function(response) {
      vm.model.anmeldedaten = response.data;
    },
    
    getFilialen: function() {
      return axios.get('einstellungen/filiale');
    },
    
    setFilialen: function(response) {
      vm.filialen = response.data;
    },
    
    getProfildaten: function() {
      return axios.get('/mitarbeiter-profil');
    },
    
    setProfildaten: function(response) {
      vm.model.profildaten = response.data;
    }
    
  }
});

vm.init();
