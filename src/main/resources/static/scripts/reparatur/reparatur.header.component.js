Vue.component('page-header-reparatur', {
  props: ['marker'],
  template: 
    `
    <div class="tabbed">
      <input checked="checked" id="tab1" type="radio" name="tabs" v-if="marker === 'Erstellen'" />
      <input id="tab1" type="radio" name="tabs" v-if="marker !== 'Erstellen'" />
      <input checked="checked" id="tab2" type="radio" name="tabs" v-if="marker === 'Reparaturen'" />
      <input id="tab2" type="radio" name="tabs" v-if="marker !== 'Reparaturen'" />
      
      <nav>
        <a class="tab1" href="reparatur.html">Erstellen</a>
        <a class="tab2" href="reparatur-uebersicht.html">Reparaturen</a>
      </nav>
      <figure></figure>
    </div>
    `
});
