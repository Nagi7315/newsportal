$(document).ready(function() {
    if (window.location.href.indexOf("secure") > -1) {
        $.ajax({
            type: "GET",
            url: "/bin/newsportal/user-service",
            dataType: "json",
            success: function(result) {
                if (!result.isLoggedIn) {
                    //  localStorage.setItem('openModal', '#loginModal');                
                    //  window.location.replace("/content/newsportal/us/en.html");  
                    window.location.replace("/content/newsportal/us/en.html?loginModal=true");
                }
            },
            error: function(error) {
                console.error('Error updating data:', error);
            }
        });
    }
});
