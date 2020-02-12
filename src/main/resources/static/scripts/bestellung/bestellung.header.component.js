Vue.component('page-header-bestellung', {
  i18n,
  props: ['marker'],
  template: 
    `
    <div class="tabbed">
      <input checked="checked" id="tab1" type="radio" name="tabs" v-if="marker === 'Erstellen'" />
      <input id="tab1" type="radio" name="tabs" v-if="marker !== 'Erstellen'" />
      <input checked="checked" id="tab2" type="radio" name="tabs" v-if="marker === 'Bestellungen'" />
      <input id="tab2" type="radio" name="tabs" v-if="marker !== 'Bestellungen'" />
      
      <nav>
        <a class="tab1" href="bestellung.html">{{ $t("general.erstellen") }}</a>
        <a class="tab2" href="bestellung-uebersicht.html">{{ $t("general.bestellungen") }}</a>
      </nav>
      <figure></figure>
    </div>
    `
});
