Vue.component('bestellung-suchen-dialog', {
  i18n,
  template: createEditDialogTemplateWithoutSaveButton(`
  <div class="m1">
    <div class="m6m">
      <label for="searchForm_nummer">{{ $t("bestellung.nummerKurz") }}</label>
      <input class="m6" id="searchForm_nummer" type="text" v-model="grid.searchQuery.nummer"></input>
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
    bestellung: Object,
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
        bestellung: this.bestellung || {},
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
            { clazz: 'ok', title: this.$t('bestellung.waehlen'), clickFunc: this.chooseFunction },
          ] },
          { name: 'bestellungNr', title: this.$t('general.nummer'), width: 100 },
          { name: 'beschreibung', title: this.$t('bestellung.titelK'), width: 400 },
          { name: 'kunde.nameKomplett', title: this.$t('general.kunde'), width: 200 },
        ],
        reload: false,
        restUrl: 'bestellung',
        searchQuery: {
          nummer: this.bestellung && this.bestellung.nummer ? (this.bestellung.filiale.kuerzel + this.bestellung.nummerAnzeige) : null,
          kundennummer: this.kunde ? (this.kunde.nummer) : null,
        },
        sort: 'bestellungNr',
        sortorder: 'desc'
      },
      result: {},
      title: this.$t('bestellung.suchen')
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

