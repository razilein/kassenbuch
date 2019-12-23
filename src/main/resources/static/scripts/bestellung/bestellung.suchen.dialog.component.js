Vue.component('bestellung-suchen-dialog', {
  template: createEditDialogTemplateWithoutSaveButton(`
  <div class="m1">
    <div class="m6m">
      <label for="searchForm_nummer">Bestell-Nr.</label>
      <input class="m6" id="searchForm_nummer" type="text" v-model="grid.searchQuery.nummer"></input>
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
            { clazz: 'ok', title: 'Diese Bestellung wählen', clickFunc: this.chooseFunction },
          ] },
          { name: 'nummer', title: 'Nummer', width: 80 },
          { name: 'beschreibung', title: 'Bestellung', width: 400 },
          { name: 'kunde.nameKomplett', title: 'Kunde', width: 200 },
        ],
        reload: false,
        restUrl: 'bestellung',
        searchQuery: {
          nachname: this.kunde ? (this.kunde.nachname || this.kunde.firmenname) : null,
        },
        sort: 'erstelltAm',
        sortorder: 'desc'
      },
      result: {},
      title: 'Bestellung suchen'
    };
  },
  methods: {
    areRequiredFieldsNotEmpty: function() {
      return this.entity && hasAllProperties(this.entity, ['id']);
    },
    chooseFunction: function(row) {
      this.entity = row;
      this.saveFunc();
    },
    saveFunc: function() {
      this.closeAndReturnResponse();
    },
    closeAndReturnResponse: function() {
      this.$emit('saved', this.entity);
    },
  }
});

