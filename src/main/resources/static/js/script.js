function sw(title, text) {
    return {
        title: title,
        allowOutsideClick: false,
        closeOnConfirm: false,
        closeOnCancel: true,
        text: text,
        type: "warning",
        showCancelButton: true,
        showLoaderOnConfirm: true,
        confirmButtonText: "Удалить",
        confirmButtonColor: "#f04124",
        cancelButtonText: "Отмена"
    };
}

function deleteAuthor(id) {
    $.post("/admin/a/author/get/", {'id': id}).success(function(author) {
        var a = author.lastName + " " + author.firstName + " " + author.middleName;
        swal(sw("Удалить автора?", "Удаляем автора " + a), function() {
            $.post('/admin/a/author/delete/', {'id': id}).success(function(data) {
                if (data.result === "success") {
                    swal.close();
                    location.reload();
                }
                else swal("Ошибка удаления", data.result, "error");
            });
        });
    });
}

function deleteInstrument(id) {
    $.post("/admin/a/instrument/get/", {id: id}).success(function(instrument) {
        swal(sw("Удалить инструмент?", "Удаляем инструмент " + instrument.name), function () {
            $.post("/admin/a/instrument/delete/", {id: id}).success(function(data) {
                if (data.result === "success") {
                    swal.close();
                    location.reload();
                }
                else swal("Ошибка удаления", data.result, "error");
            });
        });
    });
}

function deleteDifficulty(id) {
    $.post("/admin/a/difficulty/get/", {id: id}).success(function(diff) {
        swal(sw("Удалить сложность?", "Удаляем сложность " + diff.name), function() {
            $.post("/admin/a/difficulty/delete/", {id: id}).success(function(data) {
                if (data.result === "success") {
                    swal.close();
                    location.reload();
                }
                else swal("Ошибка удаления", data.result, "error");
            });
        });
    });
}

function deleteMusic(id) {
    $.post("/admin/a/music/get/", {id: id}).success(function(music) {
        swal(sw("Удалить партитуру?", "Удаляем партитуру " + music.name), function() {
            $.post("/admin/a/music/delete/", {id: id}).success(function(data) {
                if (data.result === "success") {
                    swal.close();
                    location.reload();
                }
                else swal("Ошибка удаления", data.result, "error");
            });
        });
    });
}