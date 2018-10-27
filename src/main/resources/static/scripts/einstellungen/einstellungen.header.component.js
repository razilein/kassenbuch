Vue.component('page-header-einstellungen', {
  data: {},
  props: ['marker'],
  template: 
  `<div id="navigation2">
     <a class="navigationLink2" href="einstellungen.html" v-if="marker === 'Allgemein'">&#11089; Allgemein</a>
     <a class="navigationLink2" href="einstellungen.html" v-if="marker !== 'Allgemein'">Allgemein</a>
     <a class="navigationLink2" href="filiale.html" v-if="marker === 'Filiale'">&#11089; Filiale</a>
     <a class="navigationLink2" href="filiale.html" v-if="marker !== 'Filiale'">Filiale</a>
     <a class="navigationLink2" href="mitarbeiter.html" v-if="marker === 'Mitarbeiter'">&#11089; Mitarbeiter</a>
     <a class="navigationLink2" href="mitarbeiter.html" v-if="marker !== 'Mitarbeiter'">Mitarbeiter</a>
  </div>`
});
