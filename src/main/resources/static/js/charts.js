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
};

/*Bar chart for selected country*/
function barChartSelectedCountry(confCanvas, recCanvas, deaCanvas, confLabel, recLabel, deaLabel, datesA, confList, recList, deaList) {
    const dates = datesA;
    const ctxConfirmed = confCanvas.getContext('2d');
    const dataConfirmed = new Chart(ctxConfirmed, {
        type: 'bar',
        data: {
            labels: dates,
            datasets: [{
                label: confLabel,
                backgroundColor: 'rgb(147,190,234, 0.6)',
                borderWidth: 1,
                borderColor: 'rgb(13,23,234)',
                hoverBackgroundColor: 'rgb(147,190,234, 0.7)',
                barPercentage: 0.9,
                barThickness: 10,
                maxBarThickness: 15,
                data: confList
            }]
        },
        options: {
            responsive: true,
            scales: {
                xAxes: [{
                    display: true,
                    label: 'Date',
                    ticks: {
                        autoSkip: true,
                        maxTicksLimit: 30
                        /*callback: function (dataLabel) {
                            dateDeath.push(dataLabel);
                            if (dateDeath.filter(d => d === dataLabel).length >= 2) {
                                return '';
                            } else {
                                return dataLabel + '.2020';
                            }
                        }*/
                        // Hide the label of every 2nd dataset. return null to hide the grid line too
                        //return index % 2 === 0 ? dataLabel : '';
                    },
                    scaleLabel: {
                        display: true,
                        labelString: 'Date'
                    }
                }],
                yAxes: [{
                    display: true,
                    beginAtZero: false,
                    scaleLabel: {
                        display: true,
                        labelString: 'Confirmed'
                    }
                }]
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
                text: 'Confirmed'
            }
        }
    });

    const ctxRecovered = recCanvas.getContext('2d');
    const dataRecovered = new Chart(ctxRecovered, {
        type: 'bar',
        data: {
            labels: dates,
            datasets: [{
                label: recLabel,
                backgroundColor: 'rgba(167,234,122,0.6)',
                borderWidth: 1,
                borderColor: 'rgb(9,234,14)',
                hoverBackgroundColor: 'rgba(187,234,109,0.7)',
                barPercentage: 0.9,
                barThickness: 10,
                maxBarThickness: 15,
                data: recList
            }]
        },
        options: {
            responsive: true,
            scales: {
                xAxes: [{
                    display: true,
                    label: 'Date',
                    ticks: {
                        autoSkip: true,
                        maxTicksLimit: 30
                    },
                    scaleLabel: {
                        display: true,
                        labelString: 'Date'
                    }
                }],
                yAxes: [{
                    display: true,
                    beginAtZero: false,
                    scaleLabel: {
                        display: true,
                        labelString: 'Recovered'
                    }
                }]
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
                text: 'Recovered'
            }
        }
    });

    const ctxDeaths = deaCanvas.getContext('2d');
    const dataDeaths = new Chart(ctxDeaths, {
        type: 'bar',
        data: {
            labels: dates,
            datasets: [{
                label: deaLabel,
                backgroundColor: 'rgba(234,127,121,0.6)',
                borderWidth: 1,
                borderColor: 'rgb(234,3,12)',
                hoverBackgroundColor: 'rgba(234,81,77,0.7)',
                barPercentage: 0.9,
                barThickness: 10,
                maxBarThickness: 15,
                data: deaList
            }]
        },
        options: {
            responsive: true,
            scales: {
                xAxes: [{
                    display: true,
                    label: 'Date',
                    ticks: {
                        autoSkip: true,
                        maxTicksLimit: 30
                    },
                    scaleLabel: {
                        display: true,
                        labelString: 'Date'
                    }
                }],
                yAxes: [{
                    display: true,
                    beginAtZero: false,
                    scaleLabel: {
                        display: true,
                        labelString: 'Death'
                    }
                }]
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
                text: 'Death'
            }
        }
    });
}

/*Line chart for selected country*/
function lineChartDayOneToday(canvasLine, country, confirmedList, deathsList, recoveredList, dates) {

    const ctx = canvasLine.getContext('2d');
    const myChart = new Chart(ctx, {
        type: 'line',
        data: {
            labels: dates,
            datasets: [{
                label: 'Confirmed',
                borderColor: 'rgb(65, 133, 234, 0.6)',
                fill: false,
                pointRadius: 3,
                data: confirmedList
            }, {
                label: 'Deaths',
                borderColor: 'rgb(250, 2, 6, 0.6)',
                fill: false,
                pointRadius: 3,
                data: deathsList
            }, {
                label: 'Recovered',
                borderColor: 'rgb(27, 205, 4, 0.6)',
                fill: false,
                pointRadius: 3,
                data: recoveredList,
                showLine: true
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
                }],
                yAxes: [{
                    display: true,
                    gridLines: {
                        display: true
                    }
                }]
            },
            tooltips: {
                mode: 'point'
            },
            title: {
                display: true,
                fontSize: 20,
                lineHeight: 1,
                padding: 8,
                fontColor: '#69C8C8',
                text: country
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