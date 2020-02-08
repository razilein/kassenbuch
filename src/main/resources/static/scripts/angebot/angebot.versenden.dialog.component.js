Vue.component('angebot-versenden-dialog', {
  template: `
    <div class="dialog-mask">
      <div class="dialog-wrapper">
        <div class="dialog-container info">
          <h3 class="dialog-header">Angebot {{row.nummer}} per E-Mail versenden</h3>
          <div class="dialog-body">
            Wollen Sie dieses Angebot als E-Mail an {{row.kunde.nameKomplett}} versenden?
            <br>
            <div class="m1">
              <zeichenzaehler-label :elem="anrede" :forid="'angebotversenden_anrede'" :label="'Briefanrede'" :maxlength="'1000'"></zeichenzaehler-label>
              <textarea class="m1" id="angebotversenden_anrede" maxlength="1000" type="text" v-model="anrede"></textarea>
            </div>
            <div class="m1">
              <input accept="application/pdf" class="m1" name="file" ref="angebotFile" type="file" @change="loadFile">
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
  data: function() {
    return {
      anrede: this.row.kunde.briefanrede
    };
  },
  methods: {
    loadFile: function() {
      this.file = this.$refs.angebotFile.files[0];
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
      fd.append('anrede', this.anrede);
      return axios.post('email/angebot', fd, config);
    },
    closeAndReturnResponse(response) {
      this.$emit('sended', response.data);
    }
  }
});

