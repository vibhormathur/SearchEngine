var lastQuery = "";
var canUpdateAutoSuggest = true; 

$(document).ready(function() {
	lastQuery = $("#txtQuery").val(); // don't show autocomplete when page loads
	
	setInterval(function() {
		if (!canUpdateAutoSuggest)
			return;
		
		// Only submit request to server if query has changed
		var currentQuery = $("#txtQuery").val();
		if (lastQuery !== currentQuery) {
			lastQuery = currentQuery;
			if (currentQuery === "") { // hide autosuggest if no query entered
				hideSuggest();
				return;
			}
			
			// Request autocomplete list from server
			$.ajax({
				url : "services/autocomplete-service.jsp",
				data : {
					query : currentQuery
				},
				method : "GET",
				success : function(response) {
					response = $.trim(response);
					if (response !== "") {
						updateAndShowSuggest(response);
					}
					else {
						hideSuggest();
					}
				},
				error : function() {
					console.log("There was an error on the server");
				}
			});
		}
	}, 500);
	
	// Hide autocomplete box if user clicks outside of the textbox or autocomplete dropdown
	$(document).click(function(e) {
		// Check if mouse click was on search textbox or on the autosuggest dropdown
		var inSearchElements = $(e.target).is("#txtQuery") || $(e.target).is("#autocomplete") || $(e.target).is("#autocomplete *");
		
		// Hide the autosuggest
		if (!inSearchElements) {
			hideSuggest();
		}
	});
	
	// Allow user to use up and down arrow keys to navigate autocomplete suggestions
	$("#txtQuery").keyup(function(e) {
		var keyCode = e.keyCode || e.which;
		var currentSelected = $("#autocomplete .item.hover");
		var nextToSelect = null;
		
		if (keyCode === 40) { // Down arrow key
			if (currentSelected.length > 0) {
				// Find the next item in the list
				nextToSelect = currentSelected.next(".item");
			}
			else {
				// Select first one
				nextToSelect = $("#autocomplete .item:first");
			}
		}
		else if (keyCode === 38) { // Up arrow key
			if (currentSelected.length > 0) {
				// Find the previous item in the list
				nextToSelect = currentSelected.prev(".item");
			}
			else {
				// Select last one
				nextToSelect = $("#autocomplete .item:last");
			}
		}
		
		// Update highlight
		if (nextToSelect !== null && nextToSelect.length > 0) {
			highlight(nextToSelect);
			
			// Set text in query textbox to highlighted text
			var highlightedText = nextToSelect.text();
			canUpdateAutoSuggest = false; // don't refresh autosuggest (otherwise list will change while user navigates through it)			
			$("#txtQuery").val(highlightedText);
			lastQuery = highlightedText;
			canUpdateAutoSuggest = true; // update if user types anything new
		}
	});
});

function highlight(item) {
	$("#autocomplete .item").removeClass("hover"); // remove highlight from everything else
	$(item).addClass("hover");
}

function unhighlight(item) {
	$(item).removeClass("hover");
}

function updateAndShowSuggest(itemsHtml) {
	var txtQuery = $("#txtQuery");
	
	$("#autocomplete").html(itemsHtml)
	                  .width(txtQuery.outerWidth()) // match the length of the search box
	                  .css("top", txtQuery.offset().top + txtQuery.outerHeight()) // position right below
	                  .show() // show first because if the scrollbar appears, the left side of the textbox will change
	                  .css("left", txtQuery.offset().left); // align to the left side	                  
	
	// Attach events to items
	$("#autocomplete .item").click(function() {
		$("#autocomplete").hide();
		
		// Enter the selected query in the query-textbox
		$("#txtQuery").val($(this).text());
		
		// Submit the form (initiate search)
		$("#frmSearch").submit();
	});
	
	// Highlight events for mouse
	$("#autocomplete .item, #autocomplete .item *").mouseenter(function() {
		highlight($(this).closest(".item"));
	}).mouseout(function() {
		unhighlight($(this).closest(".item"));
	});
}

function hideSuggest() {
	$("#autocomplete").hide();
}