Vue.component('page-header-auftrag', {
  props: ['marker'],
  template: 
    `
    <div class="tabbed">
      <input checked="checked" id="tab1" type="radio" name="tabs" v-if="marker === 'Erstellen'" />
      <input id="tab1" type="radio" name="tabs" v-if="marker !== 'Erstellen'" />
      <input checked="checked" id="tab2" type="radio" name="tabs" v-if="marker === 'Auftraege'" />
      <input id="tab2" type="radio" name="tabs" v-if="marker !== 'Auftraege'" />
      
      <nav>
        <a class="tab1" href="auftrag.html">Erstellen</a>
        <a class="tab2" href="auftrag-uebersicht.html">Aufträge</a>
      </nav>
      <figure></figure>
    </div>
    `
});
