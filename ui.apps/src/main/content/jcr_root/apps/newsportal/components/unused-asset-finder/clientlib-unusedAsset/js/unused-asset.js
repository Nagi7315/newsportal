document.addEventListener("DOMContentLoaded", function () {
    document.getElementById("unusedAssetsBtn").addEventListener("click", function () {
   const btn = document.getElementById("unusedAssetsBtn");
   let path = btn.getAttribute('data-path');

        // Send table data to the AEM servlet via AJAX (POST)
        $.ajax({
            type: "GET",
            url: path,

            xhrFields: {
                responseType: 'blob' // Receive the response as a blob (CSV file)
            },
            success: function (response, status, xhr) {
                // Create a link element to trigger the file download
                let blob = response;
                let link = document.createElement('a');
                let url = URL.createObjectURL(blob);
                link.href = url;
                link.download = 'unuesd_assets.csv'; // Set the file name
                link.click(); // Trigger the download
                URL.revokeObjectURL(url); // Clean up the URL object
            },
            error: function (xhr, status, error) {
                alert('Error: ' + error);
            }
        });
    });
});

