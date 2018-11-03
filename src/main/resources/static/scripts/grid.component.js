Vue.component('grid', {
  template: `
    <table :class="clazz">
      <thead>
        <tr>
          <th class="tableNavi" :colspan="columns.length">
            Zeige:
            <select
              @change="changePageSize()"
              v-model="size"
            >
              <option v-for="val in pageSizes" :value="val">{{val}}</option>
            </select>
            <button class="pageLeft disabled" title="Eine Seite zurück" v-if="page === 0"></button>
            <button class="pageLeft" @click="previousPage()" title="Eine Seite zurück" v-if="page !== 0"></button>
            Seite {{ page + 1 }} von {{ totalPages }}
            <button class="pageRight disabled" title="Zur nächsten Seite" v-if="page + 1 === totalPages"></button>
            <button class="pageRight" @click="nextPage()" title="Zur nächsten Seite" v-if="page + 1 !== totalPages"></button>
          </th>
        </tr>
      </thead>
      <thead>
        <tr>
          <th v-for="key in columns"
            @click="sortBy(key)"
            :class="{ active: sort == key.name }"
            :style="{ width: key.width }"
          >
            {{ key.title }}
            <span class="arrow" :class="sortOrders[key.name] > 0 ? 'asc' : 'desc'" v-if="key.sortable !== false"></span>
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
    reload: Boolean,
    restUrl: String
  },
  data: function() {
    var order = {};
    this.columns.forEach(function(key) {
      order[key.name] = 1;
    });
    this.reloadTabledata();
    return {
      data: this.data,
      page: 0,
      size: 10,
      sort: '',
      sortOrders: order,
      sortorder: 'asc',
      totalElements: 0,
      totalPages: 0
    };
  },
  computed: {
    pageSizes: function() {
      return [10, 20, 50, 100, 200, 500, 1000];
    }
  },
  mounted() {
    this.$watch(
      function() {
        return this.reload;
      },
      function(newVal, oldVal) {
        if (newVal === true && oldVal === false) {
          this.reloadTabledata();
          this.$emit('reloaded');
        }
      },
      { deep: true }
    );
  },
  methods: {
    sortBy: function(key) {
      if (key.sortable !== false) {
        this.sort = key.name;
        this.sortOrders[key.name] = this.sortOrders[key.name] * -1;
        this.sortorder = this.sortorder === 'asc' ? 'desc' : 'asc';
        this.reloadTabledata();
      }
    },
    changePageSize: function() {
      this.reloadTabledata();
    },
    clickFunc: function(row, func) {
      return typeof func === 'function' ? func(row) : null;
    },
    nextPage: function() {
      this.page = this.page + 1;
      this.reloadTabledata();
    },
    previousPage: function() {
      this.page = this.page - 1;
      this.reloadTabledata();
    },
    reloadTabledata: function() {
      showLoader();
      this.getData()
        .then(this.setData)
        .then(hideLoader);
    },
    getData: function() {
      var params = {
        page: this.page,
        size: this.size,
        sort: this.sort,
        sortorder: this.sortorder
      };
      return axios.post(this.restUrl, params);
    },
    setData: function(response) {
      this.data = response.data.content;
      this.totalElements = response.data.totalElements;
      this.totalPages = response.data.totalPages;
    }
  }
});
