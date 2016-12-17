function swdelete(title, text) {
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

function swconfirm(title, text) {
    return {
        title: title,
        allowOutsideClick: false,
        closeOnConfirm: false,
        closeOnCancel: true,
        text: text,
        type: "info",
        showCancelButton: true,
        showLoaderOnConfirm: true,
        confirmButtonText: "Да",
        confirmButtonColor: "#2960f8",
        cancelButtonText: "Отмена"
    };
}


function regenerate(id) {
    $.post("/admin/a/music/get/", {'id': id}, function (music) {
        swal(swconfirm("Перегенерировать файлы?", "Будет произведена перегенерация файлов для произведения \"" + music.name + "\"?"), function () {
            $.post("/admin/a/music/regenerate/", {'id': id}, function () {
                swal.close();
            });
        })
    })
}

function deleteAuthor(id) {
    $.post("/admin/a/author/get/", {'id': id}, function(author) {
        var a = author.lastName + " " + author.firstName + " " + author.middleName;
        swal(swdelete("Удалить автора?", "Удаляем автора " + a), function() {
            $.post('/admin/a/author/delete/', {'id': id}, function(data) {
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
    $.post("/admin/a/instrument/get/", {id: id}, function(instrument) {
        swal(swdelete("Удалить инструмент?", "Удаляем инструмент " + instrument.name), function () {
            $.post("/admin/a/instrument/delete/", {id: id}, function(data) {
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
    $.post("/admin/a/difficulty/get/", {id: id}, function(diff) {
        swal(swdelete("Удалить сложность?", "Удаляем сложность " + diff.name), function() {
            $.post("/admin/a/difficulty/delete/", {id: id}, function(data) {
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
    $.post("/admin/a/music/get/", {id: id}, function(music) {
        swal(swdelete("Удалить партитуру?", "Удаляем партитуру " + music.name), function() {
            $.post("/admin/a/music/delete/", {id: id}, function(data) {
                if (data.result === "success") {
                    swal.close();
                    location.reload();
                }
                else swal("Ошибка удаления", data.result, "error");
            });
        });
    });
}