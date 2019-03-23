Vue.component('page-header-kassenbuch', {
  data: {},
  props: ['marker'],
  template: 
  `
  <div class="tabbed">
    <input checked="checked" id="tab1" type="radio" name="tabs" v-if="marker === 'Kassenbuch'" />
    <input id="tab1" type="radio" name="tabs" v-if="marker !== 'Kassenbuch'" />
    <input checked="checked" id="tab2" type="radio" name="tabs" v-if="marker === 'Uebersicht'" />
    <input id="tab2" type="radio" name="tabs" v-if="marker !== 'Uebersicht'" />
    <input checked="checked" id="tab3" type="radio" name="tabs" v-if="marker === 'Kassenstand'" />
    <input id="tab3" type="radio" name="tabs" v-if="marker !== 'Kassenstand'" />
    <input checked="checked" id="tab4" type="radio" name="tabs" v-if="marker === 'Statistiken'" />
    <input id="tab4" type="radio" name="tabs" v-if="marker !== 'Statistiken'" />
    
    <nav>
      <a class="tab1" href="kassenbuch.html">Erstellen</a>
      <a class="tab2" href="kassenbuch-uebersicht.html">Kassenbücher</a>
      <a class="tab3" href="kassenstand.html">Kassenstand</a>
      <a class="tab4" href="statistiken.html">Statistiken</a>
    </nav>
    <figure></figure>
  </div>
  `
});
