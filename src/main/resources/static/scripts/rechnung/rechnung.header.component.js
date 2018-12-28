Vue.component('page-header-rechnung', {
  props: ['marker'],
  template: 
  `<div id="navigation2">
     <a class="navigationLink2" href="rechnung.html" v-if="marker === 'Erstellen'">&#11089; Erstellen</a>
     <a class="navigationLink2" href="rechnung.html" v-if="marker !== 'Erstellen'">Erstellen</a>
     <a class="navigationLink2" href="rechnung-uebersicht.html" v-if="marker === 'Rechnungen'">&#11089; Rechnungen</a>
     <a class="navigationLink2" href="rechnung-uebersicht.html" v-if="marker !== 'Rechnungen'">Rechnungen</a>
  </div>`
});
