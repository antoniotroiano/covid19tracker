/*Loading symbol in buttons*/
function loading(id) {
    let element = document.getElementById(id);
    element.classList.add("is-loading")
}

/*Dropdown menu*/
function dropDownButton() {
    document.getElementById("dropDownCountry").classList.toggle("show");
}

/*Filter for dropdown menu*/
function filterFunction() {
    let input, filter, div, a, i;
    input = document.getElementById("dropDownInput");
    filter = input.value.toUpperCase();
    div = document.getElementById("dropDownCountry");
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
function getCountryV2(country) {
    document.location.replace("/covid19/timeSeries/country/" + country);
}

/*Modal with bulma for all country without US*/
/*function openModal(province, region, confirmed, recovered, deaths, active) {
    let $activeElement = undefined;

    $(function () {
        $(".modal-bulma").click(function () {
            $activeElement = $(this);
            $("#province").text($activeElement.data(province));
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
}*/

/*Modal with bulma for US*/
/*function openModalUs(province, confirmed, recovered, deaths, active, incidentRate, peopleTested, peopleHospitalized, mortalityRate, testingRate, hospitalizationRate) {
    let $activeElementUs = undefined;

    $(function () {
        $(".modal-bulma").click(function () {
            $activeElementUs = $(this);
            $("#region").text($activeElementUs.data(province));
            $("#confirmed").text($activeElementUs.data(confirmed));
            $("#recovered").text($activeElementUs.data(recovered));
            $("#deaths").text($activeElementUs.data(deaths));
            $("#active").text($activeElementUs.data(active));
            $("#incidentRate").text($activeElementUs.data(incidentRate));
            $("#peopleTested").text($activeElementUs.data(peopleTested));
            $("#peopleHospitalized").text($activeElementUs.data(peopleHospitalized));
            $("#mortalityRate").text($activeElementUs.data(mortalityRate));
            $("#testingRate").text($activeElementUs.data(testingRate));
            $("#hospitalizationRate").text($activeElementUs.data(hospitalizationRate));
            $(".modal").addClass("is-active");
        });
        $(".modal-close").click(function () {
            $(".modal").removeClass("is-active");
        });
        $(".closeBtn").click(function () {
            $(".modal").removeClass("is-active");
        });
    });
}*/

/*Active button*/
$(function () {
    $('.btn').on("click", function () {
        $('.activeButton').not(this).removeClass('activeButton');
        $(this).toggleClass('activeButton');
    })
});

/*Toggle between lists*/
$(function () {
    $("#buttonTotal").on("click", function () {
        $("#listTotal").css("display", "table")
        $("#listActive").css("display", "none")
        $("#listDeaths").css("display", "none")
    });

    $("#buttonActive").on("click", function () {
        $("#listTotal").css("display", "none")
        $("#listActive").css("display", "table")
        $("#listDeaths").css("display", "none")
    });

    $("#buttonDeaths").on("click", function () {
        $("#listTotal").css("display", "none")
        $("#listActive").css("display", "none")
        $("#listDeaths").css("display", "table")
    });
});

/*Collapsible in lists*/
$(function () {
    let coll = document.getElementsByClassName("collapsible");
    for (let i = 0; i < coll.length; i++) {
        coll[i].addEventListener("click", function () {
            this.classList.toggle("active");
            let content = this.nextElementSibling;
            if (content.style.maxHeight) {
                content.style.maxHeight = null;
            } else {
                content.style.maxHeight = content.scrollHeight + "px";
            }
        });
    }
});