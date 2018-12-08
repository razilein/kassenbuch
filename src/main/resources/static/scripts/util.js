function getDateAsString() {
  var today = new Date();

  var day = today.getDate();
  day = day < 10 ? '0' + day : day;

  var month = today.getMonth() + 1;
  month = month < 10 ? '0' + month : month;
  return today.getFullYear() + '-' + month + '-' + day;
}

function formatDateForInput(date) {
  return formatDatetimeByFormatter(date, 'YYYY-MM-DD');
}

function formatDate(date) {
  return formatDatetimeByFormatter(date, 'DD.MM.YYYY');
}

function formatDatetime(datetime) {
  return formatDatetimeByFormatter(datetime, 'DD.MM.YYYY HH:mm');
}

function formatTime(time) {
  return formatDatetimeByFormatter(time, 'HH:mm');
}

function formatDatetimeByFormatter(datetime, formatter) {
  if (datetime) {
    var local = moment.utc(datetime).local();
    return local.format(formatter);
  }
  return datetime;
}

function formatBooleanJaNein(value) {
  return value ? 'Ja' : 'Nein';
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

function createEditDialogTemplateWithoutSaveButton(body) {
  return (
    `<div class="dialog-mask">
  <div class="dialog-wrapper">
    <div class="dialog-container info">
      <h3 class="dialog-header">{{title}}</h3>
      <div class="dialog-body">` +
    body +
    `</div>
      <div class="dialog-footer">
        <button class="dialog-default-button info" @click="$emit('close')">Abbrechen</button>
      </div>
    </div>
  </div>
</div>`
  );
}

function hasAllProperties(object, properties, checkempty) {
  var result = true;
  properties.forEach(function(val) {
    var obj = object;
    val.split('.').forEach(function(v) {
      obj = obj[v];
    });
    result = result && obj !== null && obj !== undefined;
    if (checkempty) {
      result = result && obj !== '';
    }
  });
  return result;
}

function hasAllPropertiesAndNotEmpty(object, properties) {
  return hasAllProperties(object, properties, true);
}

function getParamFromCurrentUrl(param) {
  return getParamFromUrl(window.location.href, param);
}

function getParamFromUrl(url, param) {
  return new URL(url).searchParams.get(param);
}
