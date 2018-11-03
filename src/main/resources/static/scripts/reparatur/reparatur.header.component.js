Vue.component('page-header-reparatur', {
  props: ['marker'],
  template: 
  `<div id="navigation2">
     <a class="navigationLink2" href="reparatur.html" v-if="marker === 'Erstellen'">&#11089; Erstellen</a>
     <a class="navigationLink2" href="reparatur.html" v-if="marker !== 'Erstellen'">Erstellen</a>
     <a class="navigationLink2" href="kunden.html" v-if="marker === 'Kunden'">&#11089; Kunden</a>
     <a class="navigationLink2" href="kunden.html" v-if="marker !== 'Kunden'">Kunden</a>
  </div>`
});
