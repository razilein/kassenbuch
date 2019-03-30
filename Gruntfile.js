module.exports = function(grunt) {
  'use strict';
  grunt.initConfig({
    pkg: grunt.file.readJSON('package.json'),
    copy: {
      prod: {
        files: [
          {
            src: 'node_modules/axios/dist/axios.min.js',
            dest: 'src/main/resources/static/scripts/vendor/axios.min.js'
          },
          {
            src: 'node_modules/vue/dist/vue.min.js',
            dest: 'src/main/resources/static/scripts/vendor/vue.min.js'
          },
          {
            src: 'node_modules/moment/min/moment.min.js',
            dest: 'src/main/resources/static/scripts/vendor/moment.min.js'
          },
          {
            src: 'node_modules/moment/locale/de.js',
            dest: 'src/main/resources/static/scripts/vendor/de.js'
          }
        ]
      },
      dev: {
        files: [
          {
            src: 'node_modules/axios/dist/axios.js',
            dest: 'src/main/resources/static/scripts/vendor/axios.min.js'
          },
          {
            src: 'node_modules/vue/dist/vue.js',
            dest: 'src/main/resources/static/scripts/vendor/vue.min.js'
          },
          {
            src: 'node_modules/moment/moment.js',
            dest: 'src/main/resources/static/scripts/vendor/moment.min.js'
          },
          {
            src: 'node_modules/moment/locale/de.js',
            dest: 'src/main/resources/static/scripts/vendor/de.js'
          }
        ]
      }
    },
    eslint: {
      target: [
        'Gruntfile.js',
        'src/main/resources/static/scripts/*.js',
        'src/main/resources/static/themes/*.css',
        '!src/main/resources/static/scripts/util.js'
      ]
    }
  });
  grunt.loadNpmTasks('grunt-contrib-copy');
  grunt.loadNpmTasks('grunt-eslint');

  grunt.registerTask('default', ['eslint']);
  grunt.registerTask('dev', ['copy:dev']);
};
