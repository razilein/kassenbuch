Vue.component('page-header-angebot', {
  props: ['marker'],
  template: 
    `
    <div class="tabbed">
      <input checked="checked" id="tab1" type="radio" name="tabs" v-if="marker === 'Erstellen'" />
      <input id="tab1" type="radio" name="tabs" v-if="marker !== 'Erstellen'" />
      <input checked="checked" id="tab2" type="radio" name="tabs" v-if="marker === 'Angebote'" />
      <input id="tab2" type="radio" name="tabs" v-if="marker !== 'Angebote'" />
      
      <nav>
        <a class="tab1" href="angebot.html">Erstellen</a>
        <a class="tab2" href="angebot-uebersicht.html">Angebote</a>
      </nav>
      <figure></figure>
    </div>
    `
});
