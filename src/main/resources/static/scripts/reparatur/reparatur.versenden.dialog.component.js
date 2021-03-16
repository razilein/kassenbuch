Vue.component('reparatur-versenden-dialog', {
  i18n,
  template: `
    <div class="dialog-mask">
      <div class="dialog-wrapper">
        <div class="dialog-container info">
          <h3 class="dialog-header">{{ $t("reparatur.reparaturauftrag") }} {{row.nummer}} {{ $t("reparatur.emailInformation") }}</h3>
          <div class="dialog-body">
            {{ $t("reparatur.emailSenden1") }} {{row.kunde.nameKomplett}} {{ $t("reparatur.emailSenden2") }}
            <br>
            <div class="m1">
              <zeichenzaehler-label :elem="text" :forid="'rechnungversenden_text'" :label="$t('general.mailtext')" :maxlength="'5000'"></zeichenzaehler-label>
              <textarea class="m1" id="rechnungversenden_text" maxlength="5000" style="height: 500px" type="text" v-model="text"></textarea>
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
    loadText: function() {
      this.getText().then(this.setText);
    },
    sendFunc: function() {
      showLoader();
      this.executeSend().then(this.closeAndReturnResponse);
    },
    executeSend: function() {
      return axios.post('email/reparatur', {id: this.row.id, text: this.text});
    },
    closeAndReturnResponse(response) {
      this.$emit('sended', response.data);
    },
    getText: function() {
      return axios.get('email/reparatur/' + this.row.id);
    },
    setText: function(response) {
      this.text = response.data.success;
    }
  }
});

