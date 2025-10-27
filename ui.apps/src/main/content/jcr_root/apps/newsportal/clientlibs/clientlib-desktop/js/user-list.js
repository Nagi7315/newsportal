$(document).ready(function() {
	$('#modal').on('show.bs.modal', function(event) {
        let button = $(event.relatedTarget);
        let fname = button.data('firstName');
        console.log(fname);
        $('#firstName').val(fname);
    });

	$("#saveChangesBtn").click(function(e) {
        e.preventDefault();
		let id = $('#id').val();
        console.log(id);
		let userId = $('#userId').val();
		let title = $("#title").val();
		let body = $('#body').val();

		$("#modal").modal("hide");

		let userInfo;
		userInfo = {
			id,userId,title,body,
		};

		$.ajax({
			type: "POST",
			url: "/bin/news-portal/user-info",
			data: userInfo,
			success: function(result) {
				console.log('Data updated successfully');
			},
			error: function(error) {
				console.error('Error updating data:', error);
			}
		});

	});
});