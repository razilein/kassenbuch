Vue.component('angebot-versenden-dialog', {
  i18n,
  template: `
    <div class="dialog-mask">
      <div class="dialog-wrapper">
        <div class="dialog-container info">
          <h3 class="dialog-header">{{ $t("general.angebot") }} {{row.nummer}} {{ $t("general.emailSenden") }}</h3>
          <div class="dialog-body">
            {{ $t("angebot.emailSenden") }} {{row.kunde.nameKomplett}} {{ $t("general.versenden") }}
            <br>
            <div class="m1">
              <zeichenzaehler-label :elem="text" :forid="'angebotversenden_text'" :label="$t('general.mailtext')" :maxlength="'5000'"></zeichenzaehler-label>
              <textarea class="m1" id="angebotversenden_text" maxlength="5000" style="height: 500px" type="text" v-model="text"></textarea>
            </div>
            <div class="m1">
              <input accept="application/pdf" class="m1" name="file" multiple ref="angebotFile" type="file" @change="loadFile">
            </div>
          </div>
          <div class="dialog-footer">
            <button class="dialog-default-button info" @click="sendFunc()">{{ $t("general.senden") }}</button>
            <button class="dialog-default-button info" @click="$emit('close')">{{ $t("general.abbrechen") }}</button>
          </div>
        </div>
      </div>
    </div>
  `,
  props: {
    row: Object,
  },
  data: function() {
    this.loadText();
    return {
      text: ''
    };
  },
  methods: {
    loadFile: function() {
      this.files = this.$refs.angebotFile.files;
    },
    loadText: function() {
      this.getText().then(this.setText);
    },
    sendFunc: function() {
      showLoader();
      this.executeSend().then(this.closeAndReturnResponse);
    },
    executeSend: function() {
      const config = { headers: { 'Content-Type': 'multipart/form-data' } };
      let fd = new FormData();
      for (var i = 0; i < this.files.length; i++) {
        var file = this.files[i];
        fd.append('files', file);
      }
      fd.append('id', this.row.id);
      fd.append('text', this.text);
      return axios.post('email/angebot', fd, config);
    },
    closeAndReturnResponse(response) {
      this.$emit('sended', response.data);
    },
    getText: function() {
      return axios.get('email/angebot/' + this.row.id);
    },
    setText: function(response) {
      this.text = response.data.success;
    }
  }
});

