Vue.component('page-header-einstellungen', {
  props: ['marker'],
  template: 
  `
  <div class="tabbed">
    <input checked="checked" id="tab1" type="radio" name="tabs" v-if="marker === 'Allgemein'" />
    <input id="tab1" type="radio" name="tabs" v-if="marker !== 'Allgemein'" />
    <input checked="checked" id="tab2" type="radio" name="tabs" v-if="marker === 'Filiale'" />
    <input id="tab2" type="radio" name="tabs" v-if="marker !== 'Filiale'" />
    <input checked="checked" id="tab3" type="radio" name="tabs" v-if="marker === 'Mitarbeiter'" />
    <input id="tab3" type="radio" name="tabs" v-if="marker !== 'Mitarbeiter'" />
    
    <nav>
      <a class="tab1" href="einstellungen.html">Allgemein</a>
      <a class="tab2" href="filiale.html">Filiale</a>
      <a class="tab3" href="mitarbeiter.html">Mitarbeiter</a>
    </nav>
    <figure></figure>
  </div>
  `
});
