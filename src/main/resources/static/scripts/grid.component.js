Vue.component('grid', {
  template: `
    <table :class="clazz">
      <thead>
        <tr>
          <th class="tableNavi" :colspan="columns.length">
            Anzahl: {{data ? data.length : 0}}
          </th>
        </tr>
      </thead>
      <thead>
        <tr>
          <th v-for="key in columns"
            @click="sortBy(key)"
            :class="{ active: sort == key.name }"
            :style="{ width: key.width }">
            {{ key.title }}
            <span class="arrow" :class="sortOrders[key.name] > 0 ? 'asc' : 'desc'"></span>
          </th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="entry in data">
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
    columns: Array,
    filterKey: Object,
    restUrl: String,
  },
  data: function () {
    var order = {}
    this.columns.forEach(function (key) {
      order[key.name] = 1;
    });
    console.log(order);
    this.getData()
      .then(this.setData);
    return {
      data: this.data,
      sort: '',
      sortOrders: order,
      sortorder: 'asc',
    }
  },
  computed: {},
  filters: {
    capitalize: function (str) {
      return str.charAt(0).toUpperCase() + str.slice(1);
    }
  },
  methods: {
    sortBy: function (key) {
      this.sort = key.name;
      this.sortOrders[key.name] = this.sortOrders[key.name] * -1;
      console.log(this.sortOrders);
      this.sortorder = this.sortorder === 'asc' ? 'desc' : 'asc';
      this.getData()
        .then(this.setData);
    },
    clickFunc: function(row, func) {
      return typeof func === 'function' ? func(row) : null;
    },
    getData: function() {
      var params = {
        page: 0,
        size: 10,
        sort: this.sort,
        sortorder: this.sortorder
      };
      return axios.post(this.restUrl, params);
    },
    setData: function(response) {
      this.data = response.data.content;
    },
  }
});

