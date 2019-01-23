var vm = new Vue({
  el: '#inventur',
  data: {
    result: {},
    showDialog: false,
  },
  methods: {

    executeInventur: function() {
      showLoader();
      vm.execute()
        .then(vm.setMessages)
        .then(hideLoader);
    },

    execute: function() {
      return axios.post('inventar/inventur');
    },

    setMessages: function(response) {
      vm.result = response.data;
      vm.showDialog = true;
    },

  }
});
