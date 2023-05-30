/**
 * 
 */

const searchContact = () => {
	let query = $("#search-input").val();

	if (query === "") {
		$(".search-result").hide();
	} else {
		let url = `http://localhost:8080/search/${query}`

		fetch(url)
			.then((res) => {
				return res.json();
			})
			.then((data) => {

				let text = "<div class='list-group'>";

				data.forEach((c) => {
					text += `<a href="/contact/${c.id}" class="list-group-item list-group-action"> ${c.firstname} ${c.lastname} </a>`
				});

				text += "</div>";
				$(".search-result").html(text);
				$(".search-result").show();
			});
	}

}


const searchGroupContact = () => {

	$('.contact-group-search-result').show();


	let query = $("#search-input").val();

	if (query === "") {
		$(".search-result").hide();
	} else {

		let url = `http://localhost:8080/search/${query}`

		fetch(url)

			.then((res) => {
				return res.json();
			})
			.then((data) => {

				var groupSelect = $('#show-contacts-group');

				$('#show-contacts-group option:not(:selected)').remove();
				for (var c of data) {

					var id = c.id;

					var values = $("#show-contacts-group").val();

					if (values.includes(`${id}`)) {
						break;
					}

					var fullname = c.firstname + " " + c.lastname + ":         " + c.phone1;

					groupSelect.append(
						$('<option></option>').val(id).html(fullname)
					);

				}
			})
	}
}




// give a preview for the image 
const getImagePreview = (e) => {

	var image = URL.createObjectURL(e.target.files[0]);
	$(".image").attr("src", image);
	$(".image").show();
}

const getImagePreviewUpadate = (e) => {
	var image = URL.createObjectURL(e.target.files[0]);
	$(".image-upload").attr("src", image);
	$(".image-upload-profile").attr("src", image);

}

