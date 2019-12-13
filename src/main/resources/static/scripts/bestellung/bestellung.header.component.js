Vue.component('page-header-bestellung', {
  props: ['marker'],
  template: 
    `
    <div class="tabbed">
      <input checked="checked" id="tab1" type="radio" name="tabs" v-if="marker === 'Erstellen'" />
      <input id="tab1" type="radio" name="tabs" v-if="marker !== 'Erstellen'" />
      <input checked="checked" id="tab2" type="radio" name="tabs" v-if="marker === 'Bestellungen'" />
      <input id="tab2" type="radio" name="tabs" v-if="marker !== 'Bestellungen'" />
      
      <nav>
        <a class="tab1" href="bestellung.html">Erstellen</a>
        <a class="tab2" href="bestellung-uebersicht.html">Bestellungen</a>
      </nav>
      <figure></figure>
    </div>
    `
});
