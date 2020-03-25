Vue.component('messages-box', {
  i18n,
  props: ['text'],
  template: `
    <div class="dialog-mask" @mousemove="if (!!text.success) $emit('close');">
      <div class="dialog-wrapper">
        <div class="dialog-container success" v-if="text.success">
            <h3 class="dialog-header">{{ $t("general.meldung.erfolg") }}</h3>
            <div class="dialog-body">{{text.success}}</div>
            <div class="dialog-footer">
              <button class="dialog-default-button success" @click="$emit('close')">{{ $t("general.ok") }}</button>
            </div>
        </div>
        <div class="dialog-container info" v-if="text.info">
            <h3 class="dialog-header">{{ $t("general.meldung.information") }}</h3>
            <div class="dialog-body">{{text.info}}</div>
            <div class="dialog-footer">
              <button class="dialog-default-button info" @click="$emit('close')">{{ $t("general.ok") }}</button>
            </div>
        </div>
        <div class="dialog-container warning" v-if="text.warning">
            <h3 class="dialog-header">{{ $t("general.meldung.warnung") }}</h3>
            <div class="dialog-body">{{text.warning}}</div>
            <div class="dialog-footer">
              <button class="dialog-default-button warning" @click="$emit('close')">{{ $t("general.ok") }}</button>
            </div>
        </div>
        <div class="dialog-container error" v-if="text.error">
            <h3 class="dialog-header">{{ $t("general.meldung.fehler") }}</h3>
            <div class="dialog-body" v-html="text.error"></div>
            <div class="dialog-footer">
              <button class="dialog-default-button error" @click="$emit('close')">{{ $t("general.ok") }}</button>
            </div>
        </div>
      </div>
    </div>
  `
});
