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

function formatDayOfWeek(date) {
  var dayOfWeek = moment(date, 'YYYY-MM-DD').format('d');
  switch (dayOfWeek) {
    case '0':
      return 'Sonntag';
    case '1':
      return 'Montag';
    case '2':
      return 'Dienstag';
    case '3':
      return 'Mittwoch';
    case '4':
      return 'Donnerstag';
    case '5':
      return 'Freitag';
    case '6':
      return 'Samstag';
    default:
      return '';
  }
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
        <button class="dialog-default-button info" @click="saveFunc()" v-if="areRequiredFieldsNotEmpty()">{{ $t("general.speichern") }}</button>
        <button
          class="dialog-default-button info disabled"
          :title="$t('general.pflichtfelder')"
          v-if="!areRequiredFieldsNotEmpty()"
        >{{ $t("general.speichern") }}</button>
        <button class="dialog-default-button info" @click="$emit('close')">{{ $t("general.abbrechen") }}</button>
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
        <button class="dialog-default-button info" @click="$emit('close')">{{ $t("general.abbrechen") }}</button>
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

function berechneEndpreisRechnung(entity) {
  return berechneEndpreis(entity.posten || [],
      entity.rechnung.mwst,
      Number(entity.rechnung.rabatt || 0).toFixed(2),
      Number(entity.rechnung.rabattP || 0).toFixed(2))
}

function berechneEndpreisAngebot(entity) {
  var ekBto = 0;
  var endpreisNto = 0;
  
  var mwst = entity.angebot.mwst + 100;
  entity.angebotsposten.forEach(function(element) {
    var postenPreis = ((element.menge || 1) * (element.preis || 0) - (element.rabatt || 0)) * 100 / mwst;
    endpreisNto = endpreisNto + Number((postenPreis).toFixed(2));

    var produkt = element.produkt || {};
    var postenEkPreis = (element.menge || 1) * (produkt.preisEkBrutto || 0);
    ekBto = ekBto + Number((postenEkPreis).toFixed(2));
  });
  ekBto = ekBto || 0;
  endpreisNto = endpreisNto || 0;
  var rabatt = Number(entity.angebot.rabatt || 0).toFixed(2);
  endpreisNto = endpreisNto - rabatt;
  if (entity.angebot.rabattP > 0) {
    var rabattP = 100.00 - Number(entity.angebot.rabattP).toFixed(2);
    endpreisNto = Number(endpreisNto * rabattP / 100.00).toFixed(2);
  }
  endpreisNto = endpreisNto < 0 ? 0 : endpreisNto;
  
  var endpreis = (endpreisNto - rabatt) * mwst / 100;
  return {
    endpreis: endpreis,
    endpreisNto: endpreisNto,
    ekBrutto: ekBto,
    endgewinn: endpreis - ekBto
  };
}

function berechneEndpreis(posten, mwst, rabatt, rabattP) {
  var ekBto = 0;
  var endpreis = 0;
  posten.forEach(function(element) {
    var produkt = element.produkt || {};
    var postenEkPreis = (element.menge || 1) * (produkt.preisEkBrutto || 0);
    ekBto = ekBto + postenEkPreis;

    var postenPreis = (element.menge || 1) * (element.preis || 0) - (element.rabatt || 0);
    endpreis = endpreis + Number((postenPreis).toFixed(2));
  });
  ekBto = ekBto || 0;
  endpreis = endpreis || 0;
  endpreis = endpreis - rabatt;
  if (rabattP > 0) {
    endpreis = Number((endpreis * (100.00 - rabattP) / 100.00).toFixed(2));
  }
  endpreis = endpreis < 0 ? 0 : endpreis;

  return {
    endpreis: endpreis,
    endpreisNto: endpreis * 100 / (mwst + 100),
    ekBrutto: ekBto,
    endgewinn: endpreis - ekBto
  };
}
