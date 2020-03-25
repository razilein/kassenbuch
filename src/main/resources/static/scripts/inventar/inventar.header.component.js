Vue.component('page-header-inventar', {
  i18n,
  props: ['marker'],
  template: 
  `
  <div class="tabbed">
    <input checked="checked" id="tab1" type="radio" name="tabs" v-if="marker === 'Produkt'" />
    <input id="tab1" type="radio" name="tabs" v-if="marker !== 'Produkt'" />
    <input checked="checked" id="tab2" type="radio" name="tabs" v-if="marker === 'Gruppen'" />
    <input id="tab2" type="radio" name="tabs" v-if="marker !== 'Gruppen'" />
    <input checked="checked" id="tab3" type="radio" name="tabs" v-if="marker === 'Kategorien'" />
    <input id="tab3" type="radio" name="tabs" v-if="marker !== 'Kategorien'" />
    <input checked="checked" id="tab4" type="radio" name="tabs" v-if="marker === 'Inventur'" />
    <input id="tab4" type="radio" name="tabs" v-if="marker !== 'Inventur'" />
    <input checked="checked" id="tab5" type="radio" name="tabs" v-if="marker === 'Export'" />
    <input id="tab5" type="radio" name="tabs" v-if="marker !== 'Export'" />
    <input checked="checked" id="tab6" type="radio" name="tabs" v-if="marker === 'Import'" />
    <input id="tab6" type="radio" name="tabs" v-if="marker !== 'Import'" />
    
    <nav>
      <a class="tab1" href="produkt.html">{{ $t("inventar.produkte") }}</a>
      <a class="tab2" href="gruppe.html">{{ $t("inventar.gruppen") }}</a>
      <a class="tab3" href="kategorie.html">{{ $t("inventar.produkt.kategorien") }}</a>
      <a class="tab4" href="inventur.html">{{ $t("inventar.inventur.titel") }}</a>
      <a class="tab5" href="export.html">{{ $t("export.titel") }}</a>
      <a class="tab6" href="import.html">{{ $t("import.titel") }}</a>
    </nav>
    <figure></figure>
  </div>
  `
});
