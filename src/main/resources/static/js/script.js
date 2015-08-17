
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

function dummy() {}

function deleteAuthor(id) {
    $.post("/admin/a/author/get/", {'id': id}).success(function(author) {
        var a = author.lastName + " " + author.firstName + " " + author.middleName;
        swal(sw("Удалить автора?", "Удаляем автора " + a),
            function() {
                $.post('/admin/a/author/delete/', {'id': id})
                    .success(function(data) {
                        if (data.result === "success") {
                            dummy();
                            swal.close();
                        }
                        else (swal("Ошибка удаления", data.result, "error"));
                    });
            }
        );
    });
}