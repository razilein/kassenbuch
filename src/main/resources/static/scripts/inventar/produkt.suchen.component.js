Vue.component('produkt-suchen', {
  i18n,
  template: `<div v-if="loaded">
  <div class="form m1">
    <div class="m1">
      <div class="m2m">
        <label for="searchForm_kategorie">{{ $t("inventar.produkt.kategorie") }}</label>
        <select class="m2" id="searchForm_kategorie" v-model="grid.searchQuery.kategorie" v-on:change="updateGruppen()" :readonly="isReadonlySuche1()">
          <option value=""></option>
          <option :value="k.key" v-for="k in kategorien">{{k.value}}</option>
        </select>
      </div>
      <div class="m2">
        <label for="searchForm_gruppe">{{ $t("inventar.produkt.gruppe") }}</label>
        <select class="m2" id="searchForm_gruppe" v-model="grid.searchQuery.gruppe" :readonly="isReadonlySuche1()">
          <option value=""></option>
          <option :value="g.key" v-for="g in gruppen">{{g.value}}</option>
        </select>
      </div>
    </div>
    <div class="m2m">
      <label for="searchForm_bezeichnung">{{ $t("general.bezeichnung") }}</label>
      <input class="m2" id="searchForm_bezeichnung" type="text" v-model="grid.searchQuery.bezeichnung" :readonly="isReadonlySuche1()" />
    </div>
    <div class="m2">
      <label for="searchForm_hersteller">{{ $t("inventar.produkt.hersteller") }}</label>
      <input class="m2" id="searchForm_hersteller" type="text" v-model="grid.searchQuery.hersteller" :readonly="isReadonlySuche1()" ></input>
    </div>
    <hr>
    <div class="m1">
      <div class="m2m">
        <label for="searchForm_ean">{{ $t("inventar.produkt.ean") }}</label>
        <input class="m2" id="searchForm_ean" type="text" v-model="grid.searchQuery.ean" onclick="this.select()" />
      </div>
    </div>
    <div class="m1">
      <div class="m2m">
        <label class="container checkbox">{{ $t("inventar.produkt.sortierung") }}
          <input id="searchForm_sortierung" type="checkbox" v-model="grid.searchQuery.sortierung" v-on:change="saveSearchQuerySortierung" />
          <span class="checkmark"></span>
        </label>
      </div>
      <div class="m2 right">
        <button class="delete" :title="$t('general.suchfelderLeeren')"  v-on:click="grid.searchQuery = {}; setGridSearch(); grid.reload = true;"></button>
        <button class="right" :title="$t('general.suchen')" v-on:click="grid.reload = true;">{{ $t("general.suchen") }}</button>
      </div>
    </div>
  </div>
  <div style="height: 250px;"></div>
  <div id="gridDiv" v-if="grid.gridColumns.length > 0">
    <grid
      :actions="grid.actions"
      :columns="grid.gridColumns"
      :filter-key="grid.searchQuery"
      :rest-url="grid.restUrl"
      :reload="grid.reload"
      :search-query="grid.searchQuery"
      :size="10"
      :sort="grid.sort"
      @reloaded="grid.reload = false"
    ></grid>
  </div>
</div>
`,
  props: {
    showDialog: Boolean
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
    showLoader();
    this.getKategorien()
      .then(this.setKategorien)
      .then(this.setGridColumns)
      .then(this.setGridSearch)
      .then(this.hideLoader);

    return {
      rechte: this.rechte || {},
      entity: {},
      loaded: false,
      showDialog: false,
      grid: {
        actions: [],
        gridColumns:  [],
        reload: false,
        restUrl: 'rechnung/produkt',
        searchQuery: {},
        sort: 'gruppe.kategorie.bezeichnung,gruppe.bezeichnung,bezeichnung'
      },
      gruppen: [],
      kategorien: [],
      result: {}
    };
  },
  methods: {
    areRequiredFieldsNotEmpty: function() {
      return this.entity && hasAllProperties(this.entity, ['id']);
    },
    chooseFunction: function(row) {
      this.getProdukt(row)
        .then(this.setProdukt)
        .then(this.closeAndReturnResponse);
    },
    editFunction: function(row) {
      this.getProdukt(row)
        .then(this.setProdukt)
        .then(this.closeAndEditResponse);
    },
    gridReload: function() {
      this.grid.searchQuery = {};
      this.setGridSearch();
      this.grid.reload = true;
    },
    setGridColumns: function() {
      this.grid.gridColumns = [
        { name: 'functions',
          title: this.$t('general.funktionen'),
          sortable: false,
          width: 50,
          formatter: [
          { clazz: 'ok2', title: this.$t('rechnung.produktWaehlen'), clickFunc: this.chooseFunction },
          { clazz: 'ok', title: this.$t('rechnung.hinweisErgaenzen'), clickFunc: this.editFunction },
        ] },
        { name: 'hersteller', title: this.$t('inventar.produkt.hersteller'), width: 150 },
        { name: 'bezeichnung', title: this.$t('general.bezeichnung'), width: 500 },
        { name: 'ean', title: this.$t('inventar.produkt.ean'), width: 120 },
        { name: 'bestand', title: this.$t('inventar.produkt.bestand'), width: 100 },
        { name: 'preise', title: this.$t('inventar.produkt.preise'), width: 100 }
      ];
    },
    setGridSearch: function() {
      this.grid.searchQuery.sortierung = getFromLocalstorage(SUCHE.PRODUKT.SORT_VERKAEUFE) === 'true';
    },
    saveSearchQuerySortierung: function() {
      saveInLocalstorage(SUCHE.PRODUKT.SORT_VERKAEUFE, this.grid.searchQuery.sortierung);
    },
    updateGruppen: function() {
      this.getGruppen().then(this.setGruppen);
    },
    getKategorien: function() {
      return axios.get('/rechnung/kategorie');
    },
    setKategorien: function(response) {
      this.kategorien = response.data;
    },
    getGruppen: function() {
      var kategorieId = this.grid.searchQuery.kategorie;
      if (kategorieId !== undefined) {
        return axios.get('/rechnung/gruppe/' + kategorieId);
      } else {
        return { data: [] };
      }
    },
    setGruppen: function(response) {
      this.gruppen = response.data;
    },
    getProdukt: function(row) {
      return axios.get('/inventar/produkt/' + row.id);
    },
    setProdukt: function(response) {
      this.entity = response.data;
    },
    isReadonlySuche1: function() {
      if (this.grid && this.grid.searchQuery) {
        var query = this.grid.searchQuery;
        return query.ean ? query.ean.length > 0 : false;
      } else {
        return false;
      }
    },
    hideLoader: function() {
      this.loaded = true;
      hideLoader();
    },
    closeAndEditResponse: function() {
      this.$emit('edit', this.entity);
    },
    closeAndReturnResponse: function() {
      this.$emit('return', this.entity);
    },
  }
});

