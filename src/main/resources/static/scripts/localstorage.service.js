const SUCHE = {
  PRODUKT: {
    SORT_VERKAEUFE: 'SORT_VERKAEUFE'
  }
};

function saveInLocalstorage(key, value) {
  localStorage.setItem(key, value);
}

function getFromLocalstorage(key) {
  return localStorage.getItem(key);
}
