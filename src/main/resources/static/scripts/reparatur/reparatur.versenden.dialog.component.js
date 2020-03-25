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
              <zeichenzaehler-label :elem="anrede" :forid="'rechnungversenden_anrede'" :label="$t('general.briefanrede')" :maxlength="'1000'"></zeichenzaehler-label>
              <textarea class="m1" id="rechnungversenden_anrede" maxlength="1000" type="text" v-model="anrede"></textarea>
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
    return {
      anrede: this.row.kunde.briefanrede
    };
  },
  methods: {
    sendFunc: function() {
      showLoader();
      this.executeSend().then(this.closeAndReturnResponse);
    },
    executeSend: function() {
      return axios.post('email/reparatur', {id: this.row.id, anrede: this.anrede});
    },
    closeAndReturnResponse(response) {
      this.$emit('sended', response.data);
    }
  }
});

