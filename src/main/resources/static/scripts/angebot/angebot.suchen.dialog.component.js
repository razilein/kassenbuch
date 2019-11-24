Vue.component('angebot-suchen-dialog', {
  template: createEditDialogTemplateWithoutSaveButton(`
  <div class="m1">
    <div class="m6m">
      <label for="searchFormAngebot_nummer">Angebot-Nr.</label>
      <input class="m6" id="searchFormAngebot_nummer" type="text" v-model="grid.searchQuery.nummer"></input>
    </div>
    <div class="m6m">
      <label for="searchForm_kundennummer">Kd.-Nr.</label>
      <input class="m6" id="searchForm_kundennummer" type="text" v-model="grid.searchQuery.kundennummer"></input>
    </div>
    <div class="m2">
      <label for="searchForm_suchfeld_name">Kunde</label>
      <input class="m2" id="searchForm_suchfeld_name" title="Ermöglicht die Suche nach Firmenname, Vorname oder Nachname"
        type="text" v-model="grid.searchQuery.suchfeld_name"></input>
    </div>
    <div class="m2">
      <button class="delete right" title="Suchfelder leeren" v-on:click="grid.searchQuery = {}; grid.reload = true;"></button>
      <button class="right" title="Suchen" v-on:click="grid.reload = true;">Suchen</button>
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
  },
  created: function() {
    window.addEventListener('keydown', e => {
      var isDialogOpened = this.showDialog;
      if (e.key == 'Enter' && !isDialogOpened) {
        this.grid.reload = true;
      }
    });
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
            { clazz: 'ok', title: 'Dieses Angebot wählen', clickFunc: this.chooseFunction },
          ] },
          { name: 'nummer', title: 'Angebot-Nr.', width: 80 },
          { name: 'kunde.nummer', sortable: false, title: 'Kd.-Nr.', width: 80 },
          { name: 'kunde.nameKomplett', sortable: false, title: 'Kunde', width: 200 },
          { name: 'gesamtbetragNetto', title: 'Gesamtbetrag (Nto)', width: 120, formatter: ['money'] },
          { name: 'gesamtbetrag', title: 'Gesamtbetrag (Bto)', width: 120, formatter: ['money'] },
          { name: 'erstelltAm', sortable: false, title: 'Erstellt am', width: 100 },
        ],
        reload: false,
        restUrl: 'angebot',
        searchQuery: {
          nummer: this.angebot && this.angebot.nummer ? this.angebot.nummer : null,
          kundennummer: this.kunde ? this.kunde.nummer : null,
        },
        sort: 'nummer',
        sortorder: 'desc'
      },
      result: {},
      title: 'Angebot suchen'
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
  }
});

