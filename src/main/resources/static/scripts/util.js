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

// https://stackoverflow.com/questions/149055/how-can-i-format-numbers-as-dollars-currency-string-in-javascript
function formatMoney(amount, decimalCount = 2, decimal = ',', thousands = '.') {
  try {
    decimalCount = Math.abs(decimalCount);
    decimalCount = isNaN(decimalCount) ? 2 : decimalCount;

    const negativeSign = amount < 0 ? '-' : '';

    let i = parseInt(
      (amount = Math.abs(Number(amount) || 0).toFixed(decimalCount))
    ).toString();
    let j = i.length > 3 ? i.length % 3 : 0;

    return negativeSign + (j ? i.substr(0, j) + thousands : '') +
      i.substr(j).replace(/(\d{3})(?=\d)/g, '$1' + thousands) +
      (decimalCount ? decimal +
          Math.abs(amount - i)
            .toFixed(decimalCount)
            .slice(2)
        : '');
  } catch (e) {
    console.log(e);
  }
}

function hasMessages(result) {
  return (
    result && (result.success || result.info || result.warning || result.error)
  );
}

function showLoader() {
  var loader = document.getElementById('loader');
  if (loader) {
    loader.classList.remove('hide');
  }
}

function hideLoader() {
  var loader = document.getElementById('loader');
  if (loader) {
    loader.classList.add('hide');
  }
}

function createEditDialogTemplate(body, big) {
  var style = big ? 'style="width: 1000px !important;"' : '';
  return (
    `<div class="dialog-mask">
  <div class="dialog-wrapper">
    <div class="dialog-container info" ` +
      style +
    `>
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

function createEditDialogTemplateWithoutSaveButton(body, big) {
  var style = big ? 'style="width: 1000px !important;"' : '';
  return (
    `<div class="dialog-mask">
  <div class="dialog-wrapper">
    <div class="dialog-container info" ` +
    style +
    `>
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

function hasRole(role) {
  return axios.get('/mitarbeiter-profil/hasRole/' + role);
}
