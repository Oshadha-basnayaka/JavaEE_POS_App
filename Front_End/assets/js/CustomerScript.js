getAllCustomers();

$("#btnGetAll").click(function () {
    getAllCustomers();
});

function getAllCustomers() {
    $("#tblCustomer").empty();

    $.ajax({
        url: 'http://localhost:8080/app/pages/customer',
        dataType: "json",
        method: "GET",
        success: function (customers) {
            for (let i in customers) {
                let cus = customers[i];
                let id = cus.id;
                let name = cus.name;
                let address = cus.address;
                let row = `<tr><td>${id}</td><td>${name}</td><td>${address}</td></tr>`;
                $("#tblCustomer").append(row);
            }
        },
        error: function (error) {
            alert(error.responseJSON.message);
        }
    });
}

// add
$("#btnCustomer").click(function () {
    let formData = $("#customerForm").serialize();

    $.ajax({
        url: "http://localhost:8080/app/pages/customer",
        method: "POST",
        data: formData,
        success: function (res) {
            alert(res.message);
            getAllCustomers();
        },
        error: function (error) {
            alert(error.responseJSON.message);
        }
    });
});

// delete
$("#btnCusDelete").click(function () {
    let id = $('#txtCustomerID').val();

    $.ajax({
        url: 'http://localhost:8080/app/pages/customer?cusID=' + id,
        method: 'DELETE',

        success: function (res) {
            alert(res.message);
            getAllCustomers();
        },
        error: function (error) {
            alert(error.responseJSON.message);
        }
    });
});

// update
$("#btnUpdate").click(function () {
    let id = $('#txtCustomerID').val();
    let name = $('#txtCustomerName').val();
    let address = $('#txtCustomerAddress').val();

    let customer = {
        "cusID": id,
        "cusName": name,
        "cusAddress": address
    }

    $.ajax({
        url: 'http://localhost:8080/app/pages/customer',
        method: 'PUT',
        contentType: "application/json",
        data: JSON.stringify(customer),

        success: function (res) {
            alert(res.message);
            getAllCustomers();
        },
        error: function (error) {
            alert(error.responseJSON.message);
        }
    });
});