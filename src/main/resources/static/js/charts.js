function loading(id) {
    let element = document.getElementById(id);
    element.classList.add("is-loading")
}

function chartGermany(confCanvas, recCanvas, deaCanvas, confLabel, recLabel, deaLabel, datesA, confList, recList, deaList) {
    const dates = datesA;
    const confirmed = confList;
    const dateConfirmed = [];
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
                minBarLength: 200,
                data: confirmed
            }]
        },
        options: {
            scales: {
                xAxes: [{
                    display: true,
                    label: 'Date',
                    ticks: {
                        callback: function (dataLabel) {
                            dateConfirmed.push(dataLabel);
                            if (dateConfirmed.filter(d => d === dataLabel).length >= 2) {
                                return '';
                            } else {
                                return dataLabel + '.2020';
                            }
                            // Hide the label of every 2nd dataset. return null to hide the grid line too
                            //return index % 2 === 0 ? dataLabel : '';
                        }
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
            layout: {
                padding: {
                    left: 10,
                    right: 10,
                    top: 10,
                    bottom: 10
                }
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
                text: confLabel
            }
        }
    });

    const recovered = recList;
    const dateRecovered = [];
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
                minBarLength: 200,
                data: recovered
            }]
        },
        options: {
            scales: {
                xAxes: [{
                    display: true,
                    label: 'Date',
                    ticks: {
                        callback: function (dataLabel) {
                            dateRecovered.push(dataLabel);
                            if (dateRecovered.filter(d => d === dataLabel).length >= 2) {
                                return '';
                            } else {
                                return dataLabel + '.2020';
                            }
                        }
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
            layout: {
                padding: {
                    left: 10,
                    right: 10,
                    top: 10,
                    bottom: 10
                }
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
                text: recLabel
            }
        }
    });

    const deaths = deaList;
    const dateDeath = [];
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
                minBarLength: 200,
                data: deaths
            }]
        },
        options: {
            scales: {
                xAxes: [{
                    display: true,
                    label: 'Date',
                    ticks: {
                        callback: function (dataLabel) {
                            dateDeath.push(dataLabel);
                            if (dateDeath.filter(d => d === dataLabel).length >= 2) {
                                return '';
                            } else {
                                return dataLabel + '.2020';
                            }
                        }
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
            layout: {
                padding: {
                    left: 10,
                    right: 10,
                    top: 10,
                    bottom: 10
                }
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
                text: deaLabel
            }
        }
    });
}