/*Loading symbol in buttons*/
function loading(id) {
    let element = document.getElementById(id);
    element.classList.add("is-loading")
}

/*Dropdown menu*/
function myFunction() {
    document.getElementById("myDropdown").classList.toggle("show");
}

/*Filter for dropdown menu*/
function filterFunction() {
    let input, filter, div, a, i;
    input = document.getElementById("myInput");
    filter = input.value.toUpperCase();
    div = document.getElementById("myDropdown");
    a = div.getElementsByTagName("a");
    for (i = 0; i < a.length; i++) {
        let txtValue = a[i].textContent || a[i].innerText;
        if (txtValue.toUpperCase().indexOf(filter) > -1) {
            a[i].style.display = "";
        } else {
            a[i].style.display = "none";
        }
    }
}

/*Get selected country*/
function getCountry(country) {
    document.location.replace("/covid19/daily/" + country);
}

/*Modal with bulma*/
function openModal(region, confirmed, recovered, deaths, active) {
    let $activeElement = undefined;

    $(function () {
        $(".modal-bulma").click(function () {
            $activeElement = $(this);
            $("#region").text($activeElement.data(region));
            $("#confirmed").text($activeElement.data(confirmed));
            $("#recovered").text($activeElement.data(recovered));
            $("#deaths").text($activeElement.data(deaths));
            $("#active").text($activeElement.data(active));
            $(".modal").addClass("is-active");
        });
        $(".modal-close").click(function () {
            $(".modal").removeClass("is-active");
        });
        $(".closeBtn").click(function () {
            $(".modal").removeClass("is-active");
        });
    });
}

let backgroundColor = 'rgb(147,190,234, 0.6)';
let borderColor = 'rgb(13,23,234)';
let hoverBackgroundColor = 'rgb(147,190,234, 0.7)';
let typeChart = 'bar';
let booleanStacked = true;
let barChart;

/*Toggle between bar chart*/
function changeBarChart(canvas, dates, label, listConfirmed, listRecovered, listDeaths, type) {
    barChart.destroy();

    if (label === 'Recovered') {
        backgroundColor = 'rgba(167,234,122,0.6)';
        borderColor = 'rgb(9,234,14)';
        hoverBackgroundColor = 'rgba(187,234,109,0.7)';
        barChartSelectedCountry(canvas, dates, label, listConfirmed);
    } else if (label === 'Deaths') {
        backgroundColor = 'rgba(234,127,121,0.6)';
        borderColor = 'rgb(234,3,12)';
        hoverBackgroundColor = 'rgba(234,81,77,0.7)';
        barChartSelectedCountry(canvas, dates, label, listRecovered);
    } else if (label === 'Confirmed') {
        backgroundColor = 'rgb(147,190,234, 0.6)';
        borderColor = 'rgb(13,23,234)';
        hoverBackgroundColor = 'rgb(147,190,234, 0.7)';
        barChartSelectedCountry(canvas, dates, label, listDeaths);
    } else if (type === 'bar') {
        typeChart = 'bar';
        booleanStacked = true;
        barChartAll(canvas, dates, label, listConfirmed, listRecovered, listDeaths);
    } else {
        typeChart = 'line';
        booleanStacked = false;
        barChartAll(canvas, dates, label, listConfirmed, listRecovered, listDeaths)
    }
}

/*Bar chart for selected country*/
function barChartSelectedCountry(canvas, dates, label, listData) {
    barChart = new Chart(canvas, {
        type: 'bar',
        data: {
            labels: dates,
            datasets: [{
                label: label,
                backgroundColor: backgroundColor,
                borderWidth: 1,
                borderColor: borderColor,
                hoverBackgroundColor: hoverBackgroundColor,
                barThickness: 8,
                maxBarThickness: 10,
                data: listData
            }]
        },
        options: {
            responsive: true,
            responsiveAnimationDuration: 0,
            maintainAspectRatio: false,
            aspectRatio: 0.9,
            onResize: null,
            scales: {
                xAxes: [{
                    display: true,
                    gridLines: {
                        display: true
                    },
                    ticks: {
                        autoSkip: true,
                        maxTicksLimit: 17
                    }
                    /*callback: function (dataLabel) {
                        dateDeath.push(dataLabel);
                        if (dateDeath.filter(d => d === dataLabel).length >= 2) {
                            return '';
                        } else {
                            return dataLabel + '.2020';
                        }
                    }
                    //Hide the label of every 2nd dataset. return null to hide the grid line too
                    return index % 2 === 0 ? dataLabel : '';*/
                }],
                yAxes: [{
                    display: true,
                    gridLines: {
                        display: true
                    }
                }]
            },
            title: {
                display: true,
                fontSize: 20,
                lineHeight: 1,
                padding: 8,
                fontColor: '#69C8C8',
                text: label
            },
            legend: {
                display: true,
                position: 'bottom',
                align: 'center',
                labels: {
                    fontColor: 'rgb(000, 000, 000)',
                    fontSize: 12,
                    boxWidth: 35,
                    padding: 8
                }
            }
        }
    });
}

/*Bar chart for selected country with all values (confirmed, recovered, deaths)*/
function barChartAll(canvas, dates, label, confirmed, recovered, deaths) {
    barChart = new Chart(canvas, {
        type: typeChart,
        data: {
            labels: dates,
            datasets: [{
                label: 'Confirmed',
                backgroundColor: 'rgb(147,190,234, 0.6)',
                borderColor: 'rgb(13,23,234)',
                hoverBackgroundColor: 'rgb(147,190,234, 0.7)',
                borderWidth: 1,
                fill: false,
                pointRadius: 3,
                barThickness: 8,
                maxBarThickness: 10,
                data: confirmed
            }, {
                label: 'Recovered',
                backgroundColor: 'rgba(167,234,122,0.6)',
                borderColor: 'rgb(9,234,14)',
                hoverBackgroundColor: 'rgba(187,234,109,0.7)',
                borderWidth: 1,
                fill: false,
                pointRadius: 3,
                barThickness: 8,
                maxBarThickness: 10,
                data: recovered
            }, {
                label: 'Deaths',
                backgroundColor: 'rgba(234,127,121,0.6)',
                borderColor: 'rgb(234,3,12)',
                hoverBackgroundColor: 'rgba(234,81,77,0.7)',
                borderWidth: 1,
                fill: false,
                pointRadius: 3,
                barThickness: 8,
                maxBarThickness: 10,
                data: deaths
            }]
        },
        options: {
            responsive: true,
            responsiveAnimationDuration: 0,
            maintainAspectRatio: false,
            aspectRatio: 0.9,
            onResize: null,
            scales: {
                xAxes: [{
                    display: true,
                    stacked: booleanStacked,
                    gridLines: {
                        display: true
                    },
                    ticks: {
                        autoSkip: true,
                        maxTicksLimit: 17
                    }
                }],
                yAxes: [{
                    display: true,
                    stacked: booleanStacked,
                    gridLines: {
                        display: true
                    }
                }]
            },
            title: {
                display: true,
                fontSize: 20,
                lineHeight: 1,
                padding: 8,
                fontColor: '#69C8C8',
                text: label
            },
            legend: {
                display: true,
                position: 'bottom',
                align: 'center',
                labels: {
                    fontColor: 'rgb(000, 000, 000)',
                    fontSize: 12,
                    boxWidth: 35,
                    padding: 8
                }
            }
        }
    });
}

let stackedBoolean = false;
let charType = 'line';
let chartToggle;

/*Toggle line and bar chart*/
function change(newType, toggleCharts, dates, confirmed, recovered, deaths, countryToggle) {
    chartToggle.destroy();

    if (newType === 'bar') {
        charType = newType;
        stackedBoolean = true;
        toggleChartTypes(toggleCharts, dates, confirmed, recovered, deaths, countryToggle);
    } else {
        charType = 'line';
        stackedBoolean = false;
        toggleChartTypes(toggleCharts, dates, confirmed, recovered, deaths, countryToggle);
    }
}

/*Line or bar chart*/
function toggleChartTypes(toggleCharts, dates, confirmed, recovered, deaths, countryToggle) {
    chartToggle = new Chart(toggleCharts, {
        type: charType,
        data: {
            labels: dates,
            datasets: [{
                label: 'Confirmed',
                backgroundColor: 'rgb(147,190,234,0.6)',
                borderColor: 'rgb(65,133,234,0.6)',
                borderWidth: 2,
                fill: false,
                pointRadius: 3,
                barThickness: 8,
                maxBarThickness: 10,
                data: confirmed
            }, {
                label: 'Recovered',
                backgroundColor: 'rgba(167,234,122,0.6)',
                borderColor: 'rgb(27,205,4,0.6)',
                borderWidth: 2,
                fill: false,
                pointRadius: 3,
                barThickness: 8,
                maxBarThickness: 10,
                data: recovered
            }, {
                label: 'Deaths',
                backgroundColor: 'rgba(234,127,121,0.6)',
                borderColor: 'rgb(250,2,6,0.6)',
                borderWidth: 2,
                fill: false,
                pointRadius: 3,
                barThickness: 8,
                maxBarThickness: 10,
                data: deaths
            }]
        },
        options: {
            responsive: true,
            responsiveAnimationDuration: 0,
            maintainAspectRatio: false,
            aspectRatio: 0.9,
            onResize: null,
            scales: {
                xAxes: [{
                    display: true,
                    stacked: stackedBoolean,
                    gridLines: {
                        display: true
                    },
                    ticks: {
                        autoSkip: true,
                        maxTicksLimit: 17
                    }
                }],
                yAxes: [{
                    display: true,
                    stacked: stackedBoolean,
                    gridLines: {
                        display: true
                    }
                }]
            },
            title: {
                display: true,
                fontSize: 20,
                lineHeight: 1,
                padding: 8,
                fontColor: '#69C8C8',
                text: countryToggle
            },
            legend: {
                display: true,
                position: 'bottom',
                align: 'center',
                labels: {
                    fontColor: 'rgb(000, 000, 000)',
                    fontSize: 12,
                    boxWidth: 35,
                    padding: 8
                }
            }
        }
    });
}

/*Sir Model*/
function sirModelChart(sirModelCanvas, sus, inf, rec) {
    let days = [];
    for (let i = 1; i <= sus.length; i++) {
        days.push('Day ' + i);
    }

    let myChart = new Chart(sirModelCanvas, {
        type: 'line',
        data: {
            labels: days,
            datasets: [{
                label: 'Susceptible',
                borderColor: 'rgb(65, 133, 234, 0.6)',
                fill: false,
                pointRadius: 5,
                data: sus
            }, {
                label: 'Infected',
                borderColor: 'rgb(250, 2, 6, 0.6)',
                fill: false,
                pointRadius: 5,
                data: inf
            }, {
                label: 'Recovered',
                borderColor: 'rgb(27, 205, 4, 0.6)',
                fill: false,
                pointRadius: 5,
                data: rec,
                showLine: true
            }]
        },
        options: {
            responsive: true,
            responsiveAnimationDuration: 0,
            maintainAspectRatio: false,
            aspectRatio: 0.9,
            onResize: null,
            tooltips: {
                mode: 'point'
            },
            legend: {
                display: true,
                position: 'bottom',
                align: 'center',
                labels: {
                    boxWidth: 40,
                    fontColor: 'rgb(000, 000, 000)'
                }
            },
            title: {
                display: true,
                fontSize: 20,
                padding: 15,
                fontColor: '#69C8C8',
                text: 'SIR Model'
            }
        }
    });
}