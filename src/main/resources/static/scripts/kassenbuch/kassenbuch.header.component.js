Vue.component('page-header-kassenbuch', {
  data: {},
  props: ['marker'],
  template: 
  `<div id="navigation2">
     <a class="navigationLink2" href="kassenbuch.html" v-if="marker === 'Kassenbuch'">&#11089; Erstellen</a>
     <a class="navigationLink2" href="kassenbuch.html" v-if="marker !== 'Kassenbuch'">Erstellen</a>
     <a class="navigationLink2" href="kassenbuch-uebersicht.html" v-if="marker === 'Uebersicht'">&#11089; Kassenbücher</a>
     <a class="navigationLink2" href="kassenbuch-uebersicht.html" v-if="marker !== 'Uebersicht'">Kassenbücher</a>
     <a class="navigationLink2" href="kassenstand.html" v-if="marker === 'Kassenstand'">&#11089; Kassenstand</a>
     <a class="navigationLink2" href="kassenstand.html" v-if="marker !== 'Kassenstand'">Kassenstand</a>
     <a class="navigationLink2" href="statistiken.html" v-if="marker === 'Statistiken'">&#11089; Statistiken</a>
     <a class="navigationLink2" href="statistiken.html" v-if="marker !== 'Statistiken'">Statistiken</a>
  </div>`
});
