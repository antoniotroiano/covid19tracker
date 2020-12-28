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

/*Search fields for tables*/
/*function searchFunctionProvince() {
    let input, filter, table, tr, td, i, txtValue;
    input = document.getElementById("searchProvince");
    filter = input.value.toUpperCase();
    table = document.getElementById("provinceTable");
    tr = table.getElementsByTagName("tr");
    for (i = 0; i < tr.length; i++) {
        td = tr[i].getElementsByTagName("td")[0];
        if (td) {
            txtValue = td.textContent || td.innerText;
            if (txtValue.toUpperCase().indexOf(filter) > -1) {
                tr[i].style.display = "";
            } else {
                tr[i].style.display = "none";
            }
        }
    }
}*/

/*Get selected country*/
function getCountryV2(country) {
    document.location.replace("/covid19/country/" + country);
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

/*Active buttons*/
$(function () {
    $('.btn').on("click", function () {
        $('.activeButton').not(this).removeClass('activeButton');
        $(this).toggleClass('activeButton');
    })
});

$(function () {
    $('.btnDailyTrend').on("click", function () {
        $('.activeButtonDailyTrend').not(this).removeClass('activeButtonDailyTrend');
        $(this).toggleClass('activeButtonDailyTrend');
    })
});

$(function () {
    $('.btnNumber').on("click", function () {
        $('.activeButtonNumber').not(this).removeClass('activeButtonNumber');
        $(this).toggleClass('activeButtonNumber');
    })
});

$(function () {
    $('.btnAll').on("click", function () {
        $('.activeButtonAll').not(this).removeClass('activeButtonAll');
        $(this).toggleClass('activeButtonAll');
    })
});

/*$(document).ready(function () {
    const loc = window.location.href;
    if (loc.search("=all") !== -1) {
        $('.all').addClass("activeButtonDays");
        $('.180').removeClass("activeButtonDays");
        $('.120').removeClass("activeButtonDays");
        $('.60').removeClass("activeButtonDays");
    }
    if (loc.search("=180") !== -1) {
        $('.all').removeClass("activeButtonDays");
        $('.180').addClass("activeButtonDays");
        $('.120').removeClass("activeButtonDays");
        $('.60').removeClass("activeButtonDays");
    }
    if (loc.search("=120") !== -1) {
        $('.all').removeClass("activeButtonDays");
        $('.180').removeClass("activeButtonDays");
        $('.120').addClass("activeButtonDays");
        $('.60').removeClass("activeButtonDays");
    }
    if (loc.search("=60") !== -1) {
        $('.all').removeClass("activeButtonDays");
        $('.180').removeClass("activeButtonDays");
        $('.120').removeClass("activeButtonDays");
        $('.60').addClass("activeButtonDays");
    }
});*/

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