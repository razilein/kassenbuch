Vue.component('page-header-rechnung', {
  props: ['marker'],
  template: 
  `
  <div class="tabbed">
    <input checked="checked" id="tab1" type="radio" name="tabs" v-if="marker === 'Erstellen'" />
    <input id="tab1" type="radio" name="tabs" v-if="marker !== 'Erstellen'" />
    <input checked="checked" id="tab2" type="radio" name="tabs" v-if="marker === 'Rechnungen'" />
    <input id="tab2" type="radio" name="tabs" v-if="marker !== 'Rechnungen'" />
    <input checked="checked" id="tab3" type="radio" name="tabs" v-if="marker === 'Rechnungen-Export'" />
    <input id="tab3" type="radio" name="tabs" v-if="marker !== 'Rechnungen-Export'" />
    
    <nav>
      <a class="tab1" href="rechnung.html">Erstellen</a>
      <a class="tab2" href="rechnung-uebersicht.html">Rechnungen</a>
      <a class="tab3" href="rechnung-export.html">Export</a>
    </nav>
    <figure></figure>
  </div>
  `
});
