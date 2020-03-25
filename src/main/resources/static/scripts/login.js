var vm = new Vue({
  i18n,
  el: '#login',
  created() {
    window.addEventListener('keydown', e => {
      if (e.key === 'Enter') {
        vm.login();
      }
    });
  },
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
        window.location.replace('/index.html');
      }
    }
  }
});
