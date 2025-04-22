// customer.js
$(document).ready(function () {
    // Fetch customers
    fetchCustomers();

    // Add customer
    $('#addCustomerForm').submit(function (event) {
        event.preventDefault();
        const name = $('#customerName').val();
        const email = $('#customerEmail').val();
        const phone = $('#customerPhone').val();

        $.ajax({
            url: 'http://localhost:8080/api/v1/customer/save', // Your API endpoint
            type: 'POST',
            data: JSON.stringify({ name, email, phone }),
            contentType: 'application/json',
            success: function (response) {
                $('#addCustomerModal').modal('hide');
                fetchCustomers();  // Refresh customer list
            },
            error: function (error) {
                alert('Failed to add customer!');
            }
        });
    });
});

// Fetch and display customers
function fetchCustomers() {
    $.ajax({
        url: 'http://localhost:8080/api/v1/customer',
        type: 'GET',
        success: function (response) {
            $('#customerTableBody').empty();
            response.forEach(function (customer) {
                $('#customerTableBody').append(`
                    <tr>
                        <td>${customer.name}</td>
                        <td>${customer.email}</td>
                        <td>${customer.phone}</td>
                        <td>
                            <button class="btn btn-info" onclick="editCustomer(${customer.id})">Edit</button>
                            <button class="btn btn-danger" onclick="deleteCustomer(${customer.id})">Delete</button>
                        </td>
                    </tr>
                `);
            });
        },
        error: function (error) {
            alert('Failed to fetch customers!');
        }
    });
}

// Edit customer (implement as needed)
// function editCustomer(customerId) {}

// Delete customer
function deleteCustomer(customerId) {
    $.ajax({
        url: `/api/customers/${customerId}`,
        type: 'DELETE',
        success: function (response) {
            fetchCustomers();  // Refresh customer list
        },
        error: function (error) {
            alert('Failed to delete customer!');
        }
    });
}
