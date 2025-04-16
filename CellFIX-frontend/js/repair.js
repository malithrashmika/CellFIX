$(document).ready(function() {
    // Fetch and display customer phone numbers in the dropdown
    fetchCustomers();

    // Fetch repair jobs and display them in the table
    fetchRepairJobs();

    // Handle form submission to add a new repair job
    $('#addRepairForm').submit(function(event) {
        event.preventDefault();

        const deviceModel = $('#deviceModel').val();
        const issueDescription = $('#issueDescription').val();
        const status = $('#status').val();
        const customerPhone = $('#customerPhone').val();  // Get selected customer phone
        const technicianId = $('#technicianId').val();
        const devicePhoto = $('#devicePhoto')[0].files[0];

        let formData = new FormData();
        formData.append('deviceModel', deviceModel);
        formData.append('issueDescription', issueDescription);
        formData.append('status', status);
        formData.append('customerPhone', customerPhone);  // Pass selected customer phone
        formData.append('technicianId', technicianId);
        if (devicePhoto) {
            formData.append('devicePhoto', devicePhoto);
        }

        $.ajax({
            url: '/api/repairs',
            type: 'POST',
            data: formData,
            contentType: false,
            processData: false,
            success: function(response) {
                alert('Repair job created successfully!');
                $('#addRepairModal').modal('hide');  // Close modal
                fetchRepairJobs();  // Refresh the repair job list
            },
            error: function() {
                alert('Error creating repair job!');
            }
        });
    });

    // Function to fetch customers from the backend and populate the dropdown
    function fetchCustomers() {
        $.ajax({
            url: '/api/customers',  // Adjust the API endpoint based on your backend
            type: 'GET',
            success: function(response) {
                let customerDropdown = $('#customerPhone');
                customerDropdown.empty();  // Clear any existing options

                response.forEach(function(customer) {
                    let option = `<option value="${customer.phoneNumber}">${customer.phoneNumber}</option>`;
                    customerDropdown.append(option);  // Add each customer's phone number as an option
                });
            },
            error: function() {
                alert('Error fetching customer data!');
            }
        });
    }

    // Function to fetch repair jobs from the backend and display them
    function fetchRepairJobs() {
        $.ajax({
            url: '/api/repairs',
            type: 'GET',
            success: function(response) {
                let repairJobsTable = $('#repairJobsTable tbody');
                repairJobsTable.empty(); // Clear the existing table data

                response.forEach(function(job, index) {
                    let row = `
                        <tr>
                            <td>${job.id}</td>
                            <td>${job.deviceModel}</td>
                            <td>${job.issueDescription}</td>
                            <td>${job.status}</td>
                            <td>${job.customer.phoneNumber}</td>
                            <td>${job.technician.name}</td>
                            <td>
                                <button class="btn btn-warning btn-sm" onclick="updateRepairStatus(${job.id})">Update Status</button>
                            </td>
                        </tr>`;
                    repairJobsTable.append(row);
                });
            },
            error: function() {
                alert('Error fetching repair jobs!');
            }
        });
    }

    // Function to update repair job status
    window.updateRepairStatus = function(repairId) {
        const newStatus = prompt('Enter new status (Pending, In Progress, Completed):');
        if (newStatus) {
            $.ajax({
                url: `/api/repairs/${repairId}/status`,
                type: 'PUT',
                data: { status: newStatus },
                success: function(response) {
                    alert('Repair job status updated!');
                    fetchRepairJobs(); // Refresh the repair job list
                },
                error: function() {
                    alert('Error updating repair job status!');
                }
            });
        }
    };
});
