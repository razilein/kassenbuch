Vue.component('reparatur-suchen-dialog', {
  template: createEditDialogTemplateWithoutSaveButton(`
  <div class="m1">
    <div class="m6m">
      <label for="searchFormReparatur_nummer">Nummer</label>
      <input class="m6" id="searchFormReparatur_nachname" type="text" v-model="grid.searchQuery.nummer"></input>
    </div>
    <div class="m2m">
      <label for="searchFormReparatur_nachname">Nachname</label>
      <input class="m2" id="searchFormReparatur_nachname" type="text" v-model="grid.searchQuery.nachname"></input>
    </div>
    <div class="m2">
      <button class="delete right" title="Suchfelder leeren" v-on:click="grid.searchQuery = {}; grid.reload = true;"></button>
      <button class="right" title="Suchen" v-on:click="grid.reload = true;">Suchen</button>
    </div>
  </div>
  <div id="gridDiv" v-if="grid.gridColumns.length > 0">
    <grid
      :actions="grid.actions"
      :columns="grid.gridColumns"
      :filter-key="grid.searchQuery"
      :rest-url="grid.restUrl"
      :reload="grid.reload"
      :search-query="grid.searchQuery"
      @reloaded="grid.reload = false"
    ></grid>
  </div>
  <messages-box v-bind:text="result" v-if="showDialog" @close="showDialog = false"></messages-box>
      `),
  props: {
    kunde: Object,
  },
  created: function() {
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
            { clazz: 'ok', title: 'Diesen Reparaturauftrag wählen', clickFunc: this.chooseFunction },
          ] },
          { name: 'nummer', title: 'Auftragsnummer', width: 150 },
          { name: 'kunde.nachname', title: 'Kunde', width: 100 },
          { name: 'geraet', title: 'Gerät', width: 100 },
          { name: 'kostenvoranschlag', title: 'Kosten', width: 90 },
        ],
        reload: false,
        restUrl: 'reparatur',
        searchQuery: {
          nachname: this.kunde ? this.kunde.nachname : null,
        },
      },
      result: {},
      title: 'Reparaturauftrag suchen'
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
