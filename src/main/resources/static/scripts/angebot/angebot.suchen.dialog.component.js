Vue.component('angebot-suchen-dialog', {
  i18n,
  template: createEditDialogTemplateWithoutSaveButton(`
  <div class="m1">
    <div class="m6m">
      <label for="searchFormAngebot_nummer">{{ $t("angebot.angebotNr") }}</label>
      <input class="m6" id="searchFormAngebot_nummer" type="text" v-model="grid.searchQuery.nummer"></input>
    </div>
    <div class="m6m">
      <label for="searchForm_kundennummer">{{ $t("kunde.kdNr") }}</label>
      <input class="m6" id="searchForm_kundennummer" type="text" v-model="grid.searchQuery.kundennummer"></input>
    </div>
    <div class="m2">
      <label for="searchForm_suchfeld_name">{{ $t("general.kunde") }}</label>
      <input class="m2" id="searchForm_suchfeld_name" :title="$t('general.suchfeldKunde')"
        type="text" v-model="grid.searchQuery.suchfeld_name"></input>
    </div>
    <div class="m2">
      <button class="delete right" :title="$t('general.suchfelderLeeren')" v-on:click="grid.searchQuery = {}; grid.reload = true;"></button>
      <button class="right" :title="$t('general.suchen')" v-on:click="grid.reload = true;">{{ $t("general.suchen") }}</button>
    </div>
  </div>
  <div style="width: 1000px; height: 120px;"></div>
  <div id="gridDiv" v-if="grid.gridColumns.length > 0">
    <grid
      :actions="grid.actions"
      :columns="grid.gridColumns"
      :filter-key="grid.searchQuery"
      :rest-url="grid.restUrl"
      :reload="grid.reload"
      :search-query="grid.searchQuery"
      :size="20"
      :sort="grid.sort"
      :sortorder="grid.sortorder"
      @reloaded="grid.reload = false"
    ></grid>
  </div>
  <messages-box v-bind:text="result" v-if="showDialog" @close="showDialog = false"></messages-box>
      `, true),
  props: {
    angebot: Object,
    kunde: Object,
    einstellungDruckansichtNeuesFenster: Boolean
  },
  data: function() {
    return {
      rechte: this.rechte || {},
      entity: {
        angebot: this.angebot || {},
        kunde: this.kunde || {}
      },
      showDialog: false,
      grid: {
        actions: [],
        gridColumns:  [
          { name: 'functions',
            title: 'Funktionen',
            sortable: false,
            width: 80,
            formatter: [
            { clazz: 'ok', title: this.$t('angebot.waehlen'), clickFunc: this.chooseFunction },
          ] },
          { name: 'angebotNr', title: this.$t('angebot.angebotNr'), width: 100, link: [ this.openFunction ] },
          { name: 'kundeNr', title: this.$t('kunde.kdNr'), width: 80, link: [ this.getLinkKunde ] },
          { name: 'kunde.nameKomplett', sortable: false, title: this.$t('general.kunde'), width: 200, link: [ this.getLinkKunde ] },
          { name: 'gesamtbetragNetto', title: this.$t('general.gesamtNto'), width: 120, formatter: ['money'] },
          { name: 'gesamtbetrag', title: this.$t('general.gesamtBto'), width: 120, formatter: ['money'] },
          { name: 'erstelltAm', sortable: false, title: this.$t('general.erstelltAm'), width: 100 },
        ],
        reload: false,
        restUrl: 'angebot',
        searchQuery: {
          nummer: this.angebot && this.angebot.nummer ? (this.angebot.filiale.kuerzel + this.angebot.nummerAnzeige) : null,
          kundennummer: this.kunde ? this.kunde.nummer : null,
        },
        sort: 'angebotNr',
        sortorder: 'desc'
      },
      result: {},
      title: this.$t('angebot.suchen')
    };
  },
  methods: {
    areRequiredFieldsNotEmpty: function() {
      return this.entity && hasAllProperties(this.entity, ['id']);
    },
    chooseFunction: function(row) {
      this.getAngebot(row)
        .then(this.setAngebot)
        .then(this.saveFunc);
    },
    getAngebot: function(row) {
      return axios.get('/angebot/' + row.id);
    },
    setAngebot: function(response) {
      this.entity = response.data;
    },
    saveFunc: function() {
      this.closeAndReturnResponse();
    },
    closeAndReturnResponse: function() {
      this.$emit('saved', this.entity);
    },
    openFunction: function(row) {
      if (this.einstellungDruckansichtNeuesFenster) {
        window.open('/angebot-drucken.html?id=' + row.id, '_blank', 'resizable=yes');
      } else {
        window.open('/angebot-drucken.html?id=' + row.id);
      }
    },
    getLinkKunde: function(row) {
      if (row.kunde) {
        var params = '?id=' + row.kunde.id;
        if (this.einstellungDruckansichtNeuesFenster) {
          window.open('/kunden.html' + params, '_blank', 'resizable=yes');
        } else {
          window.open('/kunden.html' + params);
        }
      }
    },
  }
});

