Vue.component('zeichenzaehler-label', {
  template: `
    <label :class="required ? 'required' : ''" :for="forid">{{label}} ({{elem ? elem.length : 0}}/{{maxlength}})</label>
  `,
  props: {
    elem: Object,
    forid: String,
    label: String,
    maxlength: String,
    required: Boolean
  }
});
