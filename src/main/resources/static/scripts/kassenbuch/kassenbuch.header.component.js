Vue.component('page-header-kassenbuch', {
  i18n,
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
      <a class="tab1" href="kassenbuch.html">{{ $t("general.erstellen") }}</a>
      <a class="tab2" href="kassenbuch-uebersicht.html">{{ $t("general.kassenbuecher") }}</a>
      <a class="tab3" href="kassenstand.html">{{ $t("kassenbuch.kassenstand.titel") }}</a>
      <a class="tab4" href="statistiken.html">{{ $t("statistik.titel") }}</a>
    </nav>
    <figure></figure>
  </div>
  `
});
