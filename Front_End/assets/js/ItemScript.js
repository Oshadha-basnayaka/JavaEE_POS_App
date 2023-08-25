getAllItems();

$("#btnGetAllItems").click(function () {
    getAllItems();
});

function getAllItems() {
    $("#tblItem").empty();

    $.ajax({
        url: 'http://localhost:8080/app/pages/item',
        dataType: "json",
        method: "GET",
        success: function (items) {
            for (let i in items) {
                let item = items[i];
                let code = item.code;
                let description = item.itemName;
                let qtyOnHand = item.qty;
                let unitPrice = item.unitPrice;
                let row = `<tr><td>${code}</td><td>${description}</td><td>${qtyOnHand}</td><td>${unitPrice}</td></tr>`;
                $("#tblItem").append(row);
            }
        },
        error: function (error) {
            alert(error.responseJSON.message);
        }
    });
}

// add
$("#btnItem").click(function () {
    let formData = $("#itemForm").serialize();

    $.ajax({
        url: "http://localhost:8080/app/pages/item",
        method: "POST",
        data: formData,
        success: function (res) {
            alert(res.message);
            getAllItems();
        },
        error: function (error) {
            alert(error.responseJSON.message);
        }
    });
});

// delete
$("#btnItemDelete").click(function () {
    let code = $('#itemCode').val();

    $.ajax({
        url: "http://localhost:8080/app/pages/item?code=" + code,
        method: "DELETE",

        success: function (res) {
            alert(res.message);
            getAllItems();
        },
        error: function (error) {
            alert(error.responseJSON.message);
        }
    });
});

// update
$("#btnItemUpdate").click(function () {
    let code = $('#itemCode').val();
    let itemName = $('#itemName').val();
    let qty = $('#itemQty').val();
    let unitPrice = $('#itemPrice').val();

    let item = {
        "code": code,
        "itemName": itemName,
        "qty": qty,
        "unitPrice": unitPrice
    }

    $.ajax({
        url: "http://localhost:8080/app/pages/item",
        method: "PUT",
        contentType: "application/json",
        data: JSON.stringify(item),

        success: function (res) {
            alert(res.message);
            getAllItems();
        },
        error: function (error) {
            alert(error.responseJSON.message);
        }
    });
});