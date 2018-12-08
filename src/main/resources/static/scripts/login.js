var vm = new Vue({
  el: '#login',
  data: {
    model: {},
    result: {},
    showDialog: false
  },
  methods: {
    login: function() {
      vm.executeLogin().then(vm.handleLoginResponse);
    },
    executeLogin: function() {
      return axios.post('/loginsecure', vm.model);
    },
    handleLoginResponse: function(response) {
      vm.result = response.data;
      vm.showDialog = true;
      if (response.data.success) {
        window.open('/index.html');
      }
    }
  }
});
