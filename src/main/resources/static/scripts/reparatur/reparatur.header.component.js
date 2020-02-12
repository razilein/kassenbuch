Vue.component('page-header-reparatur', {
  i18n,
  props: ['marker'],
  template: 
    `
    <div class="tabbed">
      <input checked="checked" id="tab1" type="radio" name="tabs" v-if="marker === 'Erstellen'" />
      <input id="tab1" type="radio" name="tabs" v-if="marker !== 'Erstellen'" />
      <input checked="checked" id="tab2" type="radio" name="tabs" v-if="marker === 'Reparaturen'" />
      <input id="tab2" type="radio" name="tabs" v-if="marker !== 'Reparaturen'" />
      
      <nav>
        <a class="tab1" href="reparatur.html">{{ $t("general.erstellen") }}</a>
        <a class="tab2" href="reparatur-uebersicht.html">{{ $t("general.reparaturen") }}</a>
      </nav>
      <figure></figure>
    </div>
    `
});
