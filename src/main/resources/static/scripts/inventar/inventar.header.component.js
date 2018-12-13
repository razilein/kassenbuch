Vue.component('page-header-inventar', {
  props: ['marker'],
  template: 
  `<div id="navigation2">
     <a class="navigationLink2" href="produkt.html" v-if="marker === 'Produkt'">&#11089; Produkte</a>
     <a class="navigationLink2" href="produkt.html" v-if="marker !== 'Produkt'">Produkte</a>
     <a class="navigationLink2" href="gruppe.html" v-if="marker === 'Gruppe'">&#11089; Gruppen</a>
     <a class="navigationLink2" href="gruppe.html" v-if="marker !== 'Gruppe'">Gruppen</a>
     <a class="navigationLink2" href="kategorie.html" v-if="marker === 'Kategorie'">&#11089; Kategorien</a>
     <a class="navigationLink2" href="kategorie.html" v-if="marker !== 'Kategorie'">Kategorien</a>
  </div>`
});
