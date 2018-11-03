function getDateAsString() {
  var today = new Date();

  var day = today.getDate();
  day = day < 10 ? '0' + day : day;

  var month = today.getMonth() + 1;
  month = month < 10 ? '0' + month : month;
  return today.getFullYear() + '-' + month + '-' + day;
}

function hasMessages(result) {
  return (
    result && (result.success || result.info || result.warning || result.error)
  );
}

function showLoader() {
  document.getElementById('loader').classList.remove('hide');
}

function hideLoader() {
  document.getElementById('loader').classList.add('hide');
}

function createEditDialogTemplate(body) {
  return (
    `<div class="dialog-mask">
  <div class="dialog-wrapper">
    <div class="dialog-container info">
      <h3 class="dialog-header">{{title}}</h3>
      <div class="dialog-body">` +
    body +
    `</div>
      <div class="dialog-footer">
        <button class="dialog-default-button info" @click="saveFunc()" v-if="areRequiredFieldsNotEmpty()">Speichern</button>
        <button
          class="dialog-default-button info disabled"
          title="Bitte füllen Sie alle mit * gekennzeichneten Pflichtfelder aus."
          v-if="!areRequiredFieldsNotEmpty()"
        >Speichern</button>
        <button class="dialog-default-button info" @click="$emit('close')">Abbrechen</button>
      </div>
    </div>
  </div>
</div>`
  );
}

function hasAllProperties(object, properties) {
  var result = true;
  properties.forEach(function(val) {
    result = result && object[val];
  });
  return result;
}
