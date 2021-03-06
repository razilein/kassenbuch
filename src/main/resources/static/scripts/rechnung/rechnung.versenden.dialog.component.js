Vue.component('rechnung-versenden-dialog', {
  i18n,
  template: `
    <div class="dialog-mask">
      <div class="dialog-wrapper">
        <div class="dialog-container info">
          <h3 class="dialog-header">{{ $t("general.rechnung") }} {{row.nummer}} {{ $t("general.emailSenden") }}</h3>
          <div class="dialog-body">
            {{ $t("rechnung.emailSenden") }} {{row.kunde.nameKomplett}} {{ $t("general.versenden") }}
            <br>
            <div class="m1">
              <zeichenzaehler-label :elem="text" :forid="'rechnungversenden_text'" :label="$t('general.mailtext')" :maxlength="'5000'"></zeichenzaehler-label>
              <textarea class="m1" id="rechnungversenden_text" maxlength="5000" style="height: 500px" type="text" v-model="text"></textarea>
            </div>
            <div class="m1">
              <input accept="application/pdf" class="m1" name="file" ref="rechnungFile" type="file" @change="loadFile">
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
      this.file = this.$refs.rechnungFile.files[0];
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
      fd.append('file', this.file);
      fd.append('id', this.row.id);
      fd.append('text', this.text);
      return axios.post('email/rechnung', fd, config);
    },
    closeAndReturnResponse(response) {
      this.$emit('sended', response.data);
    },
    getText: function() {
      return axios.get('email/rechnung/' + this.row.id);
    },
    setText: function(response) {
      this.text = response.data.success;
    }
  }
});

