Vue.component('page-header', {
  template: 
  `<div id="header">
     <div class="loading hide" id="loader">Loading&#8230;</div>
     <img src="themes/icons/logo.png" height="100" />
     <div id="navigation">
       <a class="navigationLink" href="kassenbuch.html">
         <img src="themes/icons/kasse.png" height="20" /> Kassenbuch
       </a>
       <a class="navigationLink" href="reparatur.html">
         <img src="themes/icons/zahnrad.png" height="20" /> Reparaturen
       </a>
       <a class="navigationLink" href="einstellungen.html">
         <img src="themes/icons/einstellungen.png" height="20" /> Einstellungen
       </a>
      </div>
    </div>`
});
