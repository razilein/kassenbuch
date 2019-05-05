Vue.component('rechnung-versenden-dialog', {
  template: `
    <div class="dialog-mask">
      <div class="dialog-wrapper">
        <div class="dialog-container info">
          <h3 class="dialog-header">Rechnung {{row.nummer}} per E-Mail versenden</h3>
          <div class="dialog-body">
            Wollen Sie diese Rechnung als E-Mail an {{row.kunde.nameKomplett}} versenden?
            <div class="m1">
              <input accept="application/pdf" class="m1" name="file" ref="rechnungFile" type="file" @change="loadFile">
            </div>
          </div>
          <div class="dialog-footer">
            <button class="dialog-default-button info" @click="sendFunc()">Senden</button>
            <button class="dialog-default-button info" @click="$emit('close')">Abbrechen</button>
          </div>
        </div>
      </div>
    </div>
  `,
  props: {
    row: Object,
  },
  methods: {
    loadFile: function() {
      this.file = this.$refs.rechnungFile.files[0];
    },
    sendFunc: function() {
      showLoader();
      this.executeSend().then(this.closeAndReturnResponse);
    },
    executeSend: function() {
      const config = { headers: { 'Content-Type': 'multipart/form-data' } };
      let fd = new FormData();
      fd.append('file', this.file);
      fd.append('id', this.row.id);
      return axios.post('email/rechnung', fd, config);
    },
    closeAndReturnResponse(response) {
      this.$emit('sended', response.data);
    }
  }
});

