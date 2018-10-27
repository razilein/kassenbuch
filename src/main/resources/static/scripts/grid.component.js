Vue.component('grid', {
  template: `
    <table :class="clazz">
      <thead>
        <tr>
          <th class="tableNavi" :colspan="columns.length">
            Anzahl: {{data.length}}
          </th>
        </tr>
      </thead>
      <thead>
        <tr>
          <th v-for="key in columns"
            @click="sortBy(key.name)"
            :class="{ active: sortKey == key.name }"
            :style="{ width: key.width }">
            {{ key.title }}
            <span class="arrow" :class="sortOrders[key.name] > 0 ? 'asc' : 'dsc'"></span>
          </th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="entry in filteredData">
          <td
            :title="entry[key.name]"
            v-for="key in columns">
              <button
                @click="clickFunc(entry, form.clickFunc)"
                :class="form.clazz"
                :title="form.title"
                v-for="form in key.formatter"
              ></button>
            {{entry[key.name]}}
          </td>
        </tr>
      </tbody>
    </table>
  `,
  props: {
    clazz: String,
    data: Array,
    columns: Array,
    filterKey: String
  },
  data: function () {
    var sortOrders = {}
    this.columns.forEach(function (key) {
      sortOrders[key.name] = 1;
    })
    return {
      sortKey: '',
      sortOrders: sortOrders
    }
  },
  computed: {
    filteredData: function () {
      var sortKey = this.sortKey;
      var filterKeys = this.filterKey;
      var order = this.sortOrders[sortKey] || 1;
      var data = this.data;
      if (filterKeys) {
        data = data.filter(function (row) {
          return Object.keys(row).some(function (key) {
            var filterKey = filterKeys[key] || null;
            if (!filterKey) {
              return true;
            }
            var columnValue = String(row[key]).toLowerCase();
            return columnValue.indexOf(filterKey.toLowerCase()) > -1;
          })
        })
      }
      if (sortKey) {
        data = data.slice().sort(function (a, b) {
          a = a[sortKey];
          b = b[sortKey];
          return (a === b ? 0 : a > b ? 1 : -1) * order;
        })
      }
      return data;
    }
  },
  filters: {
    capitalize: function (str) {
      return str.charAt(0).toUpperCase() + str.slice(1);
    }
  },
  methods: {
    sortBy: function (key) {
      this.sortKey = key;
      this.sortOrders[key] = this.sortOrders[key] * -1;
    },
    clickFunc: function(row, func) {
      return typeof func === 'function' ? func(row) : null;
    }
  }
})

