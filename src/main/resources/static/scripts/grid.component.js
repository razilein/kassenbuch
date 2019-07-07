Vue.component('grid', {
  template: `
    <table :class="clazz">
      <thead>
        <th class="tableNavi" :colspan="columns.length" style="line-height: 5px; font-size: 11px;">
          Anzahl Datensätze Seite: {{pageElements}}, Gesamt: {{totalElements}}
        </th>
      </thead>
      <thead>
        <tr>
          <th class="tableNavi">
            <button
            @click="clickFunc(null, action.clickFunc)"
            :class="getStringOrFuncResult(action.clazz)"
            :title="getStringOrFuncResult(action.title)"
            v-for="action in actions"
            v-if="isEnabled(action.disabled)"
            ></button>
          </th>
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
            :title="getEntryByKey(entry, key.name)"
            v-for="key in columns" :style="{ width: key.width }">
              <button
                @click="clickFunc(entry, form.clickFunc)"
                :class="getStringOrFuncResult(form.clazz, entry)"
                :title="getStringOrFuncResult(form.title, entry)"
                v-for="form in key.formatter"
                v-if="form.clickFunc && isEnabled(form.disabled, entry)"
              ></button>
              <span v-for="form in key.formatter" v-if="form === 'datetime'">{{formatDatetime(getEntryByKey(entry, key.name))}}</span>
              <span v-for="form in key.formatter" v-if="form === 'date'">{{formatDate(getEntryByKey(entry, key.name))}}</span>
              <span v-for="form in key.formatter" v-if="form === 'boolean'">{{formatBooleanJaNein(getEntryByKey(entry, key.name))}}</span>
              <span v-for="form in key.formatter" v-if="form === 'money'">{{formatMoney(getEntryByKey(entry, key.name))}}</span>
              <span v-if="!key.formatter">{{getEntryByKey(entry, key.name)}}</span>
          </td>
        </tr>
      </tbody>
    </table>
  `,
  props: {
    actions: Array,
    clazz: String,
    columns: Array,
    size: Number,
    sort: String,
    sortorder: String,
    filterKey: Object,
    reload: Boolean,
    restUrl: String,
    searchQuery: Object
  },
  data: function() {
    var order = {};
    this.columns.forEach(function(key) {
      order[key.name] = 1;
    });
    this.reloadTabledata();
    return {
      actions: [],
      data: this.data,
      page: 0,
      pageElements: 0,
      size: this.size || 10,
      sort: this.sort || '',
      sortOrders: order,
      sortorder: this.sortorder || 'asc',
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
          this.page = 0;
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
    getStringOrFuncResult: function(element, row) {
      return typeof element === 'function' ? element(row) : element;
    },
    isEnabled: function(disabled, row) {
      var result = true;
      if (typeof disabled === 'boolean') {
        result = !disabled;
      } else if (typeof disabled === 'function') {
        result = !disabled(row);
      }
      return result;
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
    getEntryByKey: function(entry, key) {
      var result = entry;
      key.split('.').forEach(function(item) {
        if (result) {
          result = result[item];
        }
      });
      return result;
    },
    getData: function() {
      var params = {
        conditions: this.searchQuery,
        data: {
          page: this.page,
          size: this.size,
          sort: this.sort,
          sortorder: this.sortorder
        }
      };
      return axios.post(this.restUrl, params);
    },
    setData: function(response) {
      this.data = response.data.content;
      this.pageElements = response.data.numberOfElements;
      this.totalElements = response.data.totalElements;
      this.totalPages = response.data.totalPages || 1;
    }
  }
});
