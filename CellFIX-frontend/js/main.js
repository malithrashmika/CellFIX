$(document).ready(function() {
    // Load default dashboard content
    loadDashboard();

    // Event listeners for sidebar links
    $('#dashboardLink').click(function() {
        loadDashboard();
    });

    $('#customersLink').click(function() {
        loadCustomersPage();
    });

    $('#repairsLink').click(function() {
        loadRepairsPage();
    });

    $('#paymentsLink').click(function() {
        loadPaymentsPage();
    });

    // Function to load dashboard content
    function loadDashboard() {
        $('#mainContent').load('dashboard.html');
    }

    // Function to load customers page content
    function loadCustomersPage() {
        $('#mainContent').load('customer.html');
    }

    // Function to load repairs page content
    function loadRepairsPage() {
        $('#mainContent').load('repairs.html');
    }

    // Function to load payments page content
    function loadPaymentsPage() {
        $('#mainContent').load('payments.html');
    }
});
