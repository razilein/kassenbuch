Vue.component('page-header-rechnung', {
  i18n,
  props: ['marker'],
  template: 
  `
  <div class="tabbed">
    <input checked="checked" id="tab1" type="radio" name="tabs" v-if="marker === 'Erstellen'" />
    <input id="tab1" type="radio" name="tabs" v-if="marker !== 'Erstellen'" />
    <input checked="checked" id="tab2" type="radio" name="tabs" v-if="marker === 'Rechnungen'" />
    <input id="tab2" type="radio" name="tabs" v-if="marker !== 'Rechnungen'" />
    <input checked="checked" id="tab3" type="radio" name="tabs" v-if="marker === 'Vorlagen'" />
    <input id="tab3" type="radio" name="tabs" v-if="marker !== 'Vorlagen'" />
    <input checked="checked" id="tab4" type="radio" name="tabs" v-if="marker === 'Rechnungen-Export'" />
    <input id="tab4" type="radio" name="tabs" v-if="marker !== 'Rechnungen-Export'" />
    
    <nav>
      <a class="tab1" href="rechnung.html">{{ $t("general.erstellen") }}</a>
      <a class="tab2" href="rechnung-uebersicht.html">{{ $t("general.rechnungen") }}</a>
      <a class="tab3" href="rechnung-uebersicht.html?vorlage=true">{{ $t("general.vorlagen") }}</a>
      <a class="tab4" href="rechnung-export.html">{{ $t("export.titel") }}</a>
    </nav>
    <figure></figure>
  </div>
  `
});
