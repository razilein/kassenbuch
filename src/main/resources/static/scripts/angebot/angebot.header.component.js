Vue.component('page-header-angebot', {
  i18n,
  props: ['marker'],
  template: 
    `
    <div class="tabbed">
      <input checked="checked" id="tab1" type="radio" name="tabs" v-if="marker === 'Erstellen'" />
      <input id="tab1" type="radio" name="tabs" v-if="marker !== 'Erstellen'" />
      <input checked="checked" id="tab2" type="radio" name="tabs" v-if="marker === 'Angebote'" />
      <input id="tab2" type="radio" name="tabs" v-if="marker !== 'Angebote'" />
      
      <nav>
        <a class="tab1" href="angebot.html">{{ $t("general.erstellen") }}</a>
        <a class="tab2" href="angebot-uebersicht.html">{{ $t("general.angebote") }}</a>
      </nav>
      <figure></figure>
    </div>
    `
});
