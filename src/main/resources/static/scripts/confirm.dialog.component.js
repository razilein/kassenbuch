Vue.component('confirm-dialog', {
  template: `
    <div class="dialog-mask">
      <div class="dialog-wrapper">
        <div class="dialog-container info">
          <h3 class="dialog-header">{{title}}</h3>
          <div class="dialog-body">{{text}}</div>
          <div class="dialog-footer">
            <button class="dialog-default-button info" @click="confirmFunc()">OK</button>
            <button class="dialog-default-button info" @click="$emit('close')">Abbrechen</button>
          </div>
        </div>
      </div>
    </div>
  `,
  props: {
    func: Object,
    params: Object,
    text: String,
    title: String
  },
  methods: {
    confirmFunc: function() {
      this.func(this.params).then(this.closeAndReturnResponse);
    },
    closeAndReturnResponse: function(response) {
      this.$emit('confirmed', response.data);
    }
  }
});
