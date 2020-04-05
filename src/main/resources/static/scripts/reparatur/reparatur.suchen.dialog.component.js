Vue.component('reparatur-suchen-dialog', {
  i18n,
  template: createEditDialogTemplateWithoutSaveButton(`
  <div class="m1">
    <div class="m6m">
      <label for="searchFormReparatur_nummer">{{ $t("reparatur.repNr") }}</label>
      <input class="m6" id="searchFormReparatur_nummer" type="text" v-model="grid.searchQuery.nummer"></input>
    </div>
    <div class="m6m">
      <label for="searchForm_kundennummer">{{ $t("kunde.kdNr") }}</label>
      <input class="m6" id="searchForm_kundennummer" type="text" v-model="grid.searchQuery.kundennummer"></input>
    </div>
    <div class="m2m">
      <label for="searchFormReparatur_suchfeld_name">{{ $t("general.kunde") }}</label>
      <input class="m2" id="searchFormReparatur_suchfeld_name" :title="$t('general.suchfeldKunde')"
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
    reparatur: Object,
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
        reparatur: this.reparatur || {},
        kunde: this.kunde || {}
      },
      showDialog: false,
      grid: {
        actions: [],
        gridColumns:  [
          { name: 'functions',
            title: this.$t('general.funktionen'),
            sortable: false,
            width: 80,
            formatter: [
            { clazz: 'ok', title: this.$t('reparatur.waehlen'), clickFunc: this.chooseFunction },
          ] },
          { name: 'reparaturNr', title: this.$t('reparatur.repNr'), width: 80 },
          { name: 'geraet', title: this.$t('general.geraet'), width: 400 },
          { name: 'kunde.nameKomplett', title: this.$t('general.kunde'), width: 200 },
        ],
        reload: false,
        restUrl: 'reparatur',
        searchQuery: {
          nummer: this.reparatur && this.reparatur.nummer ? (this.reparatur.filiale.kuerzel + this.reparatur.nummer) : null,
          kundennummer: this.kunde ? this.kunde.nummer : null,
        },
        sort: 'reparaturNr',
        sortorder: 'desc'
      },
      result: {},
      title: this.$t('reparatur.suchen')
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

