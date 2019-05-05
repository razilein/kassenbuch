Vue.component('reparatur-versenden-dialog', {
  template: `
    <div class="dialog-mask">
      <div class="dialog-wrapper">
        <div class="dialog-container info">
          <h3 class="dialog-header">Reparaturauftrag {{row.nummer}} abgeschlossen - Kunde per E-Mail informieren</h3>
          <div class="dialog-body">
            Wollen Sie {{row.kunde.nameKomplett}} per E-Mail informieren, dass der Auftrag abgeschlossen ist?
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
    sendFunc: function() {
      showLoader();
      this.executeSend().then(this.closeAndReturnResponse);
    },
    executeSend: function() {
      return axios.post('email/reparatur', {id: this.row.id});
    },
    closeAndReturnResponse(response) {
      this.$emit('sended', response.data);
    }
  }
});

