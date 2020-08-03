Vue.component('rechnung-offen-dialog', {
  i18n,
  props: ['rechnungen', 'einstellungDruckansichtNeuesFenster', 'kundeId'],
  template: `
    <div class="dialog-mask">
      <div class="dialog-wrapper">
        <div class="dialog-container warning">
          <h3 class="dialog-header">{{ $t("rechnung.offen") }}</h3>
          <div class="dialog-body">
            {{ $t("rechnung.offenInfo") }}
            <div v-for="rechnung in rechnungen">
              <a href="#" @click="openRechnung(rechnung.key)">{{rechnung.value}}</a>
              <br />
            </div>
            <hr />
            <a href="#" @click="openAlleRechnungen(kundeId)">{{ $t("rechnung.alle") }}</a>
          </div>
          <div class="dialog-footer">
            <button class="dialog-default-button warning" @click="$emit('close')">{{ $t("general.ok") }}</button>
          </div>
        </div>
      </div>
    </div>
  `,
  methods: {
    openRechnung: function(id) {
      var params = '?id=' + id + '&exemplare=1';
      if (this.einstellungDruckansichtNeuesFenster) {
        window.open('/rechnung-drucken.html' + params, '_blank', 'resizable=yes');
      } else {
        window.open('/rechnung-drucken.html' + params);
      }
    },
    openAlleRechnungen: function(kundeId) {
      window.open('/rechnung-uebersicht.html?id=' + kundeId);
    },
  }
});
