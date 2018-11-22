Vue.component('kunde-suchen-dialog', {
  template: createEditDialogTemplate(`
  <div class="m2m">
    <div class="m2m">
      <label for="searchFormKunde_nachname">Nachname</label>
      <input class="m2" id="searchFormKunde_nachname" type="text" v-model="grid.searchQuery.nachname"></input>
    </div>
    <div class="m2m">
      <label for="searchForm_vorname">Vorname</label>
      <input class="m2" id="searchForm_vorname" type="text" v-model="grid.searchQuery.vorname"></input>
    </div>
    <div class="m2">
      <button class="delete right" title="Suchfelder leeren" v-on:click="grid.searchQuery = {}; grid.reload = true;"></button>
      <button class="right" title="Suchen" v-on:click="grid.reload = true;">Suchen</button>
    </div>
  </div>
  <h5 class="m2">
    Gewählter Kunde *:
    <br>
    {{entity.nachname}} {{entity.vorname}}
    <br>
    {{entity.strasse}}
    <br>
    {{entity.plz}} {{entity.ort}}
    <br>
    {{entity.telefon}}
  </h5>
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
  <kunde-edit-dialog
    :initial-entity="grid.searchQuery"
    :rest-url-get="editRow.restUrlGet"
    :rest-url-save="editRow.restUrlSave"
    :title="editRow.title"
    v-if="showEditDialog"
    @close="showEditDialog = false"
    @saved="handleEditResponse"
  ></kunde-edit-dialog>
  <messages-box v-bind:text="result" v-if="showDialog" @close="showDialog = false"></messages-box>
      `),
  props: {
    kunde: Object,
  },
  data: function() {
    return {
      entity: this.kunde || {},
      showDialog: false,
      showEditDialog: false,
      editRow: {
        restUrlGet: '/reparatur/kunde/',
        restUrlSave: '/reparatur/kunde',
        title: 'Kunde bearbeiten',
      },
      grid: {
        actions: [
          { clazz: 'add', title: 'Kunde hinzufügen', clickFunc: this.addFunction }
        ],
        gridColumns:  [
          { name: 'functions',
            title: 'Funktionen',
            sortable: false,
            width: 80,
            formatter: [
            { clazz: 'ok', title: 'Diesen Kunden wählen', clickFunc: this.chooseFunction },
            { clazz: 'edit', title: 'Kunde bearbeiten', clickFunc: this.editFunction },
          ] },
          { name: 'nachname', title: 'Nachname', width: 150 },
          { name: 'vorname', title: 'Vorname', width: 100 },
          { name: 'strasse', title: 'Straße', width: 100 },
          { name: 'ort', title: 'Ort', width: 100 },
        ],
        reload: false,
        restUrl: 'reparatur/kunde',
        searchQuery: {
          nachname: this.kunde ? this.kunde.nachname : null,
          vorname: this.kunde ? this.kunde.vorname : null
        },
      },
      result: {},
      title: 'Kunde suchen'
    };
  },
  methods: {
    areRequiredFieldsNotEmpty: function() {
      return this.entity && hasAllProperties(this.entity, ['id']);
    },
    addFunction: function() {
      this.editRow.restUrlGet = '/reparatur/kunde/' + -1;
      this.editRow.title = 'Kunde hinzufügen';
      this.showEditDialog = true;
    },
    chooseFunction: function(row) {
      this.entity = row;
    },
    editFunction: function(row) {
      this.editRow.restUrlGet = '/reparatur/kunde/' + row.id;
      this.editRow.title = 'Kunde ' + row.nachname + ' bearbeiten';
      this.showEditDialog = true;
    },
    handleEditResponse: function(data) {
      if (data.success) {
        this.showEditDialog = false;
        this.grid.reload = true;
        this.entity = data.kunde;
      } 
      this.result = data;
      this.showDialog = true;
    },
    saveFunc: function() {
      this.closeAndReturnResponse();
    },
    closeAndReturnResponse: function() {
      this.$emit('saved', this.entity);
    },
  }
});

