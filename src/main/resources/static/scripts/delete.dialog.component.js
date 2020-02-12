Vue.component('delete-dialog', {
  i18n,
  template: `
    <div class="dialog-mask">
      <div class="dialog-wrapper">
        <div class="dialog-container error">
          <h3 class="dialog-header">{{title}}</h3>
          <div class="dialog-body">{{ $t("general.loeschenFrage") }}</div>
          <div class="dialog-footer">
            <button class="dialog-default-button error" @click="deleteFunc()">{{ $t("general.ok") }}</button>
            <button class="dialog-default-button error" @click="$emit('close')">{{ $t("general.abbrechen") }}</button>
          </div>
        </div>
      </div>
    </div>
  `,
  props: {
    id: Number,
    restUrl: String,
    title: String
  },
  methods: {
    deleteFunc: function() {
      this.executeDelete().then(this.closeAndReturnResponse);
    },
    executeDelete: function() {
      return axios.delete(this.restUrl, { data: { id: this.id } });
    },
    closeAndReturnResponse: function(response) {
      this.$emit('deleted', response.data);
    }
  }
});
