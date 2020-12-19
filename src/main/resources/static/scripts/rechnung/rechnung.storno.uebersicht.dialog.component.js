Vue.component('storno-uebersicht-dialog', {
  i18n,
  template: createEditDialogTemplateWithoutSaveButton(`
  <div id="gridDiv" v-if="grid.gridColumns.length > 0">
    <grid
      :columns="grid.gridColumns"
      :filter-key="grid.searchQuery"
      :rest-url="grid.restUrl"
      :reload="grid.reload"
      :search-query="grid.searchQuery"
      :size="100"
      :sort="grid.sort"
      :sortorder="grid.sortorder"
      @reloaded="grid.reload = false"
    ></grid>
  </div>
  <div class="m1" style="height: 10px;"></div>
  <delete-dialog
    :title="deleteRow.title"
    :rest-url="deleteRow.restUrl"
    :id="deleteRow.id"
    v-if="showDeleteDialog"
    @close="showDeleteDialog = false"
    @deleted="handleDeleteResponse"
  ></delete-dialog>
  <messages-box v-bind:text="result" v-if="showDialog" @close="showDialog = false"></messages-box>
      `, true),
  props: {
    rechnungId: Number,
    rechte: Object,
    title: String,
  },
  data: function() {
    return {
      deleteRow: {
        id: null,
        restUrl: '/rechnung/storno',
        title: '',
      },
      einstellungDruckansichtNeuesFenster: true,
      grid: {
        actions: [],
        gridColumns: [],
        reload: false,
        restUrl: 'rechnung/storno',
        searchQuery: { 'rechnung.id': this.rechnungId },
        sort: 'datum',
        sortorder: 'desc'
      },
      rechte: {},
      showDeleteDialog: false,
      showDialog: false,
    };
  },
  created: function() {
    this.init();
  },
  methods: {
    init: function() {
      this.setGridColumns();
      this.getEinstellungDruckansichtNeuesFenster()
        .then(this.setEinstellungDruckansichtNeuesFenster)
    },
    
    setGridColumns: function() {
      this.grid.gridColumns = [
        { name: 'functions',
          title: this.$t('general.funktionen'),
          sortable: false,
          width: 200,
          formatter: [
          { clazz: 'open-new-tab', disabled: this.hasNotRoleRechnungAnzeigen, title: this.$t('rechnung.oeffnen'), clickFunc: this.openFunction },
          { clazz: 'open-new-tab', disabled: this.hasNotRoleStornoAnzeigen, title: this.$t('rechnung.stornoBeleg'), clickFunc: this.openStornoFunction },
          { clazz: 'delete', disabled: this.hasNotRoleVerwalten, title: this.$t('rechnung.stornoLoeschen'), clickFunc: this.deleteFunction }
        ] },
        { name: 'nummerAnzeige', sortable: false, title: this.$t('general.nummer'), width: 150 },
        { name: 'vollstorno', title: this.$t('rechnung.vollstornoArt'), width: 150, formatter: ['boolean'] },
        { name: 'kunde.nameKomplett', title: this.$t('general.kunde'), sortable: false, width: 200 },
        { name: 'datum', title: this.$t('general.datum'), width: 120, formatter: ['date'] },
        { name: 'ersteller', title: this.$t('general.ersteller'), width: 150 },
      ];
    },
    
    deleteFunction: function(row) {
      this.deleteRow.id = row.id;
      this.deleteRow.title = this.$t('rechnung.storno') + ' ' + row.nummer + ' ' + this.$t('general.loeschen');
      this.showDeleteDialog = true;
    },
    
    handleDeleteResponse: function(data) {
      if (data.success) {
        this.showDeleteDialog = false;
        this.grid.reload = true;
      }
      this.result = data;
      this.showDialog = true;
    },
    
    openFunction: function(row) {
      var params = '?id=' + row.rechnung.id + '&exemplare=1&storno=true';
      if (this.einstellungDruckansichtNeuesFenster) {
        window.open('/rechnung-drucken.html' + params, '_blank', 'resizable=yes');
      } else {
        window.open('/rechnung-drucken.html' + params);
      }
    },
    
    openStornoFunction: function(row) {
      var params = '?id=' + row.id;
      if (this.einstellungDruckansichtNeuesFenster) {
        window.open('/rechnung-storno-drucken.html' + params, '_blank', 'resizable=yes');
      } else {
        window.open('/rechnung-storno-drucken.html' + params);
      }
    },
    
    getEinstellungDruckansichtNeuesFenster: function() {
      return axios.get('/mitarbeiter-profil');
    },
    
    setEinstellungDruckansichtNeuesFenster: function(response) {
      this.einstellungDruckansichtNeuesFenster = response.data.druckansichtNeuesFenster;
    },
    
    hasNotRoleStornoAnzeigen: function() {
      return !this.rechte['ROLE_RECHNUNG_STORNO'];
    },
    
    hasNotRoleRechnungAnzeigen: function(row) {
      return !this.rechte['ROLE_RECHNUNG'] || row.vollstorno;
    },
    
    hasNotRoleVerwalten: function() {
      return !this.rechte['ROLE_RECHNUNG_STORNO_VERWALTEN'];
    },
    
  }
});

