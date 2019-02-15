Vue.component('page-header-inventar', {
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
    
    <nav>
      <a class="tab1" href="produkt.html">Produkte</a>
      <a class="tab2" href="gruppe.html">Gruppen</a>
      <a class="tab3" href="kategorie.html">Kategorien</a>
      <a class="tab4" href="inventur.html">Inventur</a>
    </nav>
    <figure></figure>
  </div>
  `
});
