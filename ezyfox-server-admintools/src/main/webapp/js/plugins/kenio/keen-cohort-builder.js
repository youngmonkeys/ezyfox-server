(function(env){

  // Define cohort builder bundle
  var CohortBuilder = {
    'fetchCohortDatasets'       : fetchCohortDatasets,
    'formatCohortDatasets'      : formatCohortDatasets,
    'generateDateMatrix'        : generateDateMatrix,
    'generateCohortQueryMatrix' : generateCohortQueryMatrix,
    'registerCohortTable'       : registerCohortTable
  };
  var Dataset, each;

  if (typeof module !== 'undefined' && module.exports) {
    module.exports = CohortBuilder;
  }
  else if (env && env.Keen) {
    env.CohortBuilder = CohortBuilder;
    env.Keen.CohortBuilder = CohortBuilder;
    if (env.Keen.Dataviz) {
      registerCohortTable(env.Keen.Dataviz);
    }
  }
  else {
    throw 'Keen Analysis and Dataviz SDKs are required for browser use';
    return;
  }

  Dataset = env.Keen.Dataset;
  each = env.Keen.utils.each;

  function generateDateMatrix(units, intervals) {
    var dateMatrix = [[]];
    var dateStart, dateEnd,
        stepIntervals = [],
        stepUnits = units;

    for (var i = 0; i < intervals; i++) {
      stepIntervals.push(i+1);
    }

    for (var i = stepIntervals.length-1; i > -1; i--) {
      dateStart = new Date();
      dateEnd = new Date();
      if (stepUnits === 'days') {
        dateStart.setDate(dateStart.getDate() - stepIntervals[i]);
        dateEnd.setDate(dateEnd.getDate() - stepIntervals[i] + 1);
      }
      else {
        dateStart.setDate(dateStart.getDate() - (7 * stepIntervals[i]));
        dateEnd.setDate(dateEnd.getDate() - (7 * stepIntervals[i]) + 7);
      }
      dateStart.setHours(0, 0, 0, 0);
      dateEnd.setHours(0, 0, 0, 0);
      dateMatrix[0].push({ 'start': dateStart.toISOString(), 'end': dateEnd.toISOString() });
    }
    for (var i = 1; i < dateMatrix[0].length; i++) {
      dateMatrix.push(dateMatrix[0].slice(i));
    }
    return dateMatrix;
  }

  function generateCohortQueryMatrix(dm, fn) {
    var matrix = new Array(dm.length);
    each(dm, function(cohort, i){
      matrix[i] = new Array();
      each(cohort, function(interval, j){
        var context = {
          'created_at': { start: cohort[0].start, end: cohort[0].end },
          'current_interval': { start: interval.start, end: interval.end }
        };
        matrix[i].push(fn(context));
      });
    });
    return matrix;
  }

  function fetchCohortDatasets(client, cohorts, callback) {
    var dataset = new Dataset();
    var inProgress = 0;
    each(cohorts, function(cohort, i){
      inProgress += cohort.length;
      client.run(cohort, function(err, cohortResult){
        cohortResult = (cohortResult instanceof Array) ? cohortResult : [cohortResult];
        each(cohortResult, function(res, j){
          var colLabel = cohortResult.length + ' {{unit}} ago';
          var rowLabel = '{{unit}} ' + (j+1);
          var value = (res.result[res.result.length-1] / res.result[0]) * 100;
          if (isNaN(value)) {
            value = 0;
          }
          else {
            value = value.toFixed(0);
          }
          // '<span style="background-color:rgba(0, 175, 215,' + (value/100) + ');">' + value.toFixed(1) + ' %</span>'
          dataset.set([colLabel, rowLabel], value);
          inProgress--;
          if (!inProgress) callback(formatCohortDatasets(dataset));
        });
      });
    });
  }

  function formatCohortDatasets(dataset){
    dataset
      .sortColumns('desc', function(set) {
        return parseInt(set[0]);
      })
      .updateColumn(0, function(value) {
        return value.replace('{{unit}}', 'week');
      })
      .updateRow(0, function(value, i) {
        var unit = parseInt(value[0]) > 1 ? 'weeks' : 'week';
        return i == 0 ? 'Cohort joined' : value.replace('{{unit}}', unit);
      });

    each(dataset.matrix, function(row, i) {
      dataset.updateRow(i, function(value, j) {
        return value !== null ? value : '-';
      })
    });

    return dataset;
  }

  function registerCohortTable(DV){
    DV.register('cohort-builder', {
      'table': {
        render: function() {
          var html = '<div class="keen-cohort-table"><table>';
          var data = this.data();

          // Build table head
          html += '<thead><tr>';
          for (var i = 0; i < data[0].length; i++) {
            html += '<th width="' + (100/data[0].length) + '">' + decodeURIComponent(data[0][i]) + '</th>';
          }
          html += '</tr></thead>';

          // Build table body
          html += '<tbody>';
          for (var i = 1; i < data.length; i++) {
            html += '<tr>';
            for (var j = 0; j < data[i].length; j++) {
              if (j === 0) {
                html += '<td>' + decodeURIComponent(data[i][j]) + '</td>';
              }
              else if (typeof data[i][j] === 'number') {
                var percent = Number(decodeURIComponent(data[i][j]));
                html += '<td style="background-color: rgba(0, 175, 215, ' + (percent / 100) + ');">' + percent.toFixed(2) + '%' + '</td>';
              }
              else {
                html += '<td>&nbsp;</td>';
              }
            }
            html += '</tr>';
          }
          html += '</tbody>';

          html += '</table></div>';
          this.el().innerHTML = html;
        },
        destroy: function(){
          this.el().innerHTML = '';
        }
      },
      'matrix': {
        render: function() {
          var html = '<div class="keen-cohort-matrix"><table>';
          var data = this.data();

          // Build table head
          var reverseColumn = this.dataset.selectColumn(0);

          html += '<thead><tr>';
          for (var i = 0; i < reverseColumn.length; i++) {
            html += '<th width="' + (100/reverseColumn.length) + '">' + decodeURIComponent(reverseColumn[i]) + '</th>';
          }
          html += '</tr></thead>';

          // Build table body
          html += '<tbody>';
          for (var i = 1; i < data[0].length; i++) {
            var reverseSeries = this.dataset.selectColumn(i);
            html += '<tr>';
            for (var j = 0; j < reverseSeries.length; j++) {
              if (j === 0) {
                html += '<td>' + decodeURIComponent(reverseSeries[j]) + '</td>';
              }
              else if (typeof reverseSeries[j] === 'number') {
                var percent = Number(decodeURIComponent(reverseSeries[j]));
                html += '<td style="background-color: rgba(0, 175, 215, ' + (percent / 100) + ');">' + percent.toFixed(2) + '%' + '</td>';
              }
              else {
                html += '<td>&nbsp;</td>';
              }
            }
            html += '</tr>';
          }
          html += '</tbody>';
          html += '</table></div>';
          this.el().innerHTML = html;
        },
        destroy(){
          this.el().innerHTML = '';
        }
      }
    });
  }

}).call(this, typeof window !== 'undefined' ? window : typeof global !== 'undefined' ? global : typeof self !== 'undefined' ? self : {});
