Vue.component('page-header-einstellungen', {
  i18n,
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
    <input checked="checked" id="tab4" type="radio" name="tabs" v-if="marker === 'Roboter'" />
    <input id="tab4" type="radio" name="tabs" v-if="marker !== 'Roboter'" />
    
    <nav>
      <a class="tab1" href="einstellungen.html">{{ $t("einstellung.profil.allgemein") }}</a>
      <a class="tab2" href="filiale.html">{{ $t("general.filiale") }}</a>
      <a class="tab3" href="mitarbeiter.html">{{ $t("einstellung.mitarbeiter") }}</a>
      <a class="tab4" href="roboter.html">{{ $t("einstellung.roboter") }}</a>
    </nav>
    <figure></figure>
  </div>
  `
});
