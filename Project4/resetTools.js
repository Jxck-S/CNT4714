function resetForm() {
    document.getElementById("commands").value = "";
}

function resetResults() {
    var resultsContainer = document.getElementById("results_container");
    while (resultsContainer.firstChild) {
        resultsContainer.removeChild(resultsContainer.firstChild);
    }
}

function resetFormById(formId) {
    resetResults();
    var form = document.getElementById(formId);
    var inputs = form.getElementsByTagName("input");
    for (var i = 0; i < inputs.length; i++) {
        if (inputs[i].type == "text") {
            inputs[i].value = "";
        }
    }
}

