document.addEventListener("DOMContentLoaded", function () {
    document.getElementById("exportCsvBtn").addEventListener("click", function () {
        // Extract table data
        let tableData = [];
        let table = document.querySelector("table");

        // Get headers
        let headers = [];
        table.querySelectorAll("thead tr th").forEach(th => {
            headers.push(th.innerText.trim());
        });
        tableData.push(headers); // Add headers as first row

        // Get rows data
        table.querySelectorAll("tbody tr").forEach(row => {
            let rowData = [];
            row.querySelectorAll("td").forEach(td => {
                rowData.push(td.innerText.trim());
            });
            tableData.push(rowData);
        });

        let jsonData = JSON.stringify({ data: tableData });

        // Send table data to the AEM servlet via AJAX (POST)
        $.ajax({
            type: "POST",
            url: "/bin/exportcsv",
            data: jsonData,
            contentType: "application/json",
            xhrFields: {
                responseType: 'blob' // Receive the response as a blob (CSV file)
            },
            success: function (response, status, xhr) {
                // Create a link element to trigger the file download
                let blob = response;
                let link = document.createElement('a');
                let url = URL.createObjectURL(blob);
                link.href = url;
                link.download = 'table_data.csv'; // Set the file name
                link.click(); // Trigger the download
                URL.revokeObjectURL(url); // Clean up the URL object
            },
            error: function (xhr, status, error) {
                alert('Error: ' + error);
            }
        });
    });
});

