Vue.component('kunde-suchen-dialog', {
  template: createEditDialogTemplateWithoutSaveButton(`
  <div class="m2m">
    <div class="m1">
      <label for="searchForm_suchfeld_name">Name</label>
      <input class="m1" id="searchForm_suchfeld_name" type="text" v-model="grid.searchQuery.suchfeld_name"></input>
    </div>
    <div class="m2">
      <button class="delete right" title="Suchfelder leeren" v-on:click="grid.searchQuery = {}; grid.reload = true;"></button>
      <button class="right" title="Suchen" v-on:click="grid.reload = true;">Suchen</button>
    </div>
  </div>
  <h5 class="m2 right">
    Gewählter Kunde *:
    <span v-if="entity.firmenname">
      <br>
      {{entity.firmenname}}
    </span>
    <br>
    {{entity.nachname}} {{entity.vorname}}
    <br>
    {{entity.strasse}}
    <br>
    {{entity.plz}} {{entity.ort}}
    <br>
    {{entity.telefon}}
  </h5>
  <div style="width: 150px;">&nbsp;</div>
  <div id="gridDiv" v-if="grid.gridColumns.length > 0">
    <grid
      :actions="grid.actions"
      :columns="grid.gridColumns"
      :filter-key="grid.searchQuery"
      :rest-url="grid.restUrl"
      :reload="grid.reload"
      :size="20"
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
      `, true),
  props: {
    kunde: Object,
    dialogTitel: String,
  },
  created: function() {
    this.prepareRoles();

    window.addEventListener('keydown', e => {
      var isDialogOpened = this.showDialog || this.showEditDialog;
      if (e.key == 'Enter' && !isDialogOpened) {
        this.grid.reload = true;
      }
    });
  },
  data: function() {
    var dialogTitle = this.dialogTitel || 'Kunde suchen';
    var suchfeldName = this.buildSuchfeldName();
    return {
      rechte: this.rechte || {},
      entity: this.kunde || {},
      showDialog: false,
      showEditDialog: false,
      editRow: {
        restUrlGet: '/kunde/',
        restUrlSave: '/kunde',
        title: 'Kunde bearbeiten',
      },
      grid: {
        actions: [
          { clazz: 'add', disabled: this.hasNotRoleVerwalten, title: 'Kunde hinzufügen', clickFunc: this.addFunction }
        ],
        gridColumns:  [
          { name: 'functions',
            title: 'Funktionen',
            sortable: false,
            width: 80,
            formatter: [
            { clazz: 'ok', title: 'Diesen Kunden wählen', clickFunc: this.chooseFunction },
            { clazz: 'edit', disabled: this.hasNotRoleVerwalten, title: 'Kunde bearbeiten', clickFunc: this.editFunction },
          ] },
          { name: 'telefon', title: 'Telefon', width: 100 },
          { name: 'firmenname', title: 'Firma', width: 150 },
          { name: 'nachname', title: 'Nachname', width: 150 },
          { name: 'vorname', title: 'Vorname', width: 100 },
          { name: 'strasse', title: 'Straße', width: 100 },
          { name: 'ort', title: 'Ort', width: 100 },
          { name: 'nummer', title: 'Nr.', width: 50 },
        ],
        reload: false,
        restUrl: 'kunde',
        searchQuery: {
          suchfeld_name: suchfeldName
        },
      },
      result: {},
      title: dialogTitle
    };
  },
  methods: {
    areRequiredFieldsNotEmpty: function() {
      return this.entity && hasAllProperties(this.entity, ['id']);
    },
    addFunction: function() {
      this.editRow.restUrlGet = '/kunde/' + -1;
      this.editRow.title = 'Kunde hinzufügen';
      this.showEditDialog = true;
    },
    buildSuchfeldName: function() {
      var suchfeld = '';
      if (this.kunde) {
        if (this.kunde.firmenname) {
          suchfeld += this.kunde.firmenname;
        }
        if (this.kunde.vorname) {
          suchfeld += ' ' + this.kunde.vorname;
        }
        if (this.kunde.nachname) {
          suchfeld += ' ' + this.kunde.nachname;
        }
      }
      return suchfeld;
    },
    chooseFunction: function(row) {
      this.entity = row;
      this.saveFunc();
    },
    editFunction: function(row) {
      this.editRow.restUrlGet = '/kunde/' + row.id;
      this.editRow.title = 'Kunde ' + row.nummer + ' bearbeiten';
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
    hasNotRoleVerwalten: function() {
      return !this.rechte['ROLE_KUNDEN_VERWALTEN'];
    },
    prepareRoles: function() {
      hasRole('ROLE_KUNDEN_VERWALTEN').then(this.setRecht);
    },
    setRecht: function(response) {
      this.rechte['ROLE_KUNDEN_VERWALTEN'] = response.data;
    }
  }
});

