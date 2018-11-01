Vue.component('delete-dialog', {
  template: `
    <div class="dialog-mask">
      <div class="dialog-wrapper">
        <div class="dialog-container error">
          <h3 class="dialog-header">{{title}}</h3>
          <div class="dialog-body">Wollen Sie diesen Datensatz wirklich löschen?</div>
          <div class="dialog-footer">
            <button class="dialog-default-button error" @click="deleteFunc()">OK</button>
            <button class="dialog-default-button error" @click="$emit('close')">Abbrechen</button>
          </div>
        </div>
      </div>
    </div>
  `,
  props: {
    id: Number,
    restUrl: String,
    title: String,
  },
  methods: {
    deleteFunc: function() {
      axios.delete(this.restUrl, { data: { id: this.id } });
    },
  }
});

