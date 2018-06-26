Vue.component('messages-box', {
  props: ['text'],
  template: `
    <div class="dialog-mask">
      <div class="dialog-wrapper">
        <div class="dialog-container success" v-if="text.success">
            <h3 class="dialog-header">Erfolg</h3>
            <div class="dialog-body">{{text.success}}</div>
            <div class="dialog-footer">
              <button class="dialog-default-button success" @click="$emit('close')">OK</button>
            </div>
        </div>
        <div class="dialog-container info" v-if="text.info">
            <h3 class="dialog-header">Information</h3>
            <div class="dialog-body">{{text.info}}</div>
            <div class="dialog-footer">
              <button class="dialog-default-button info" @click="$emit('close')">OK</button>
            </div>
        </div>
        <div class="dialog-container warning" v-if="text.warning">
            <h3 class="dialog-header">Warnung</h3>
            <div class="dialog-body">{{text.warning}}</div>
            <div class="dialog-footer">
              <button class="dialog-default-button warning" @click="$emit('close')">OK</button>
            </div>
        </div>
        <div class="dialog-container error" v-if="text.error">
            <h3 class="dialog-header">Fehler</h3>
            <div class="dialog-body">{{text.error}}</div>
            <div class="dialog-footer">
              <button class="dialog-default-button error" @click="$emit('close')">OK</button>
            </div>
        </div>
      </div>
    </div>
  `
})
