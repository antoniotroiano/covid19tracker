function loading(id) {
    let element = document.getElementById(id);
    element.classList.add("is-loading")
}

function chartGermany(datesG, confirmedList, recoveredList, deathsList) {
    const dates = datesG;
    const confirmed = confirmedList;
    const dateConfirmed = [];
    const ctx = document.getElementById('dataGermanyConfirmed').getContext('2d');
    const dataGermanyConfirmed = new Chart(ctx, {
        type: 'bar',
        data: {
            labels: dates,
            datasets: [{
                label: 'Confirmed Germany COVID-19',
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
                text: 'Confirmed Germany COVID-19'
            }
        }
    });

    const recovered = recoveredList;
    const dateRecovered = [];
    const ctx2 = document.getElementById('dataGermanyRecovered').getContext('2d');
    const dataGermanyRecovered = new Chart(ctx2, {
        type: 'bar',
        data: {
            labels: dates,
            datasets: [{
                label: 'Recovered Germany COVID-19',
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
                text: 'Recovered Germany COVID-19'
            }
        }
    });

    const deaths = deathsList;
    const dateDeath = [];
    const ctx3 = document.getElementById('dataGermanyDeaths').getContext('2d');
    const dataGermanyDeaths = new Chart(ctx3, {
        type: 'bar',
        data: {
            labels: dates,
            datasets: [{
                label: 'Death Germany COVID-19',
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
                text: 'Death Germany COVID-19'
            }
        }
    });
}