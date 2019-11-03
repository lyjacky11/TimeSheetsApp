var counter = 1;

function calculateHours() {
	var pay_rate = document.getElementById("pay_rate").value;
	var rate_x = document.getElementById("rate_x").value;
	var start_shift = document.getElementById("start_shift").value;
	var end_shift = document.getElementById("end_shift").value;
	var hours_worked = document.getElementById("hours_worked");
	var total_breaks = document.getElementById("total_breaks");
	var gross_pay = document.getElementById("gross_pay");
	var results = document.getElementById("results");

	if (start_shift != "" && end_shift != "") {

		// Shift Times
		var start_hour = start_shift.split(":")[0];
		var start_minute = start_shift.split(":")[1];
		var end_hour = end_shift.split(":")[0];
		var end_minute = end_shift.split(":")[1];

		// Calculate shift hours
		var net_start = Number(start_hour) + start_minute / 60;
		var net_end = Number(end_hour) + end_minute / 60;
		var total_shift_hours = net_end - net_start;
		var totalb_hours = 0;

		for (var i = 1; i <= counter; i++) {
			var start_id = "start_break" + i;
			var end_id = "end_break" + i;
			var start_break = document.getElementById(start_id).value;
			var end_break = document.getElementById(end_id).value;
			if (start_break != "" && end_break != "") {
				// Break Times
				var startb_hour = start_break.split(":")[0];
				var startb_minute = start_break.split(":")[1];
				var endb_hour = end_break.split(":")[0];
				var endb_minute = end_break.split(":")[1];

				// Calculate break hours
				var netb_start = Number(startb_hour) + startb_minute / 60;
				var netb_end = Number(endb_hour) + endb_minute / 60;
				if (netb_end < netb_start || netb_start < net_start || netb_end > net_end) {
					alert("Invalid break times!\nPlease check the information and try again!");
					return false;
				}
				var sum = netb_end - netb_start;
				totalb_hours += sum;
			}
		}

		// Calculate total hours
		var total_hours = total_shift_hours - totalb_hours;
		var total_amount = pay_rate * rate_x * total_hours;

		var hour_netdiff = Math.floor(total_hours);
		var minute_netdiff = Math.round((total_hours % 1) * 60);

		var hour_bdiff = Math.floor(totalb_hours);
		var minute_bdiff = Math.round((totalb_hours % 1) * 60);

		totalb_hours = totalb_hours.toFixed(2);
		total_hours = total_hours.toFixed(2);
		total_amount = total_amount.toFixed(2);

		if (total_hours < 0 || totalb_hours < 0 || net_end < net_start) {
			alert("Invalid shift/break times!\nPlease check the information and try again!");
			return false;
		}
		hours_worked.innerHTML = "Hours Worked: " + hour_netdiff + "h " + minute_netdiff + "m | " + total_hours + " hours";
		total_breaks.innerHTML = "Total Breaks: " + hour_bdiff + "h " + minute_bdiff + "m | " + totalb_hours + " hours";
		gross_pay.innerHTML = "Gross Pay: $" + total_amount;
		results.style.display = "block";
	}
}

function addBreak() {
	var breaks = document.getElementById("breaks");
	var currentID = "break" + counter;
	var break_num = document.getElementById(currentID);
	var removeButton = document.getElementById("removeButton");

	clone = break_num.cloneNode(true);
	counter++;
	var newID = "break" + counter;
	clone.id = newID;

	var start_break = clone.getElementsByTagName("input")[0];
	var end_break = clone.getElementsByTagName("input")[1];
	start_break.id = "start_break" + counter;
	end_break.id = "end_break" + counter;

	breaks.appendChild(clone);
	removeButton.style.visibility = "visible";
}

function removeBreak() {
	var removeButton = document.getElementById("removeButton");
	if (counter > 1) {
		var currentID = "break" + counter;
		var break_num = document.getElementById(currentID);
		break_num.remove();
		counter--;
		if (counter == 1) {
			removeButton.style.visibility = "hidden";
		}
	}
}

function clearForm() {
	var hours_worked = document.getElementById("hours_worked");
	var total_breaks = document.getElementById("total_breaks");
	var gross_pay = document.getElementById("gross_pay");
	var shift_times = document.getElementById("shift_times");

	shift_times.reset();
	while (counter != 1) {
		removeBreak();
	}
	hours_worked.innerHTML = "Hours Worked: 0h 0m | 0.00 hours";
	total_breaks.innerHTML = "Total Breaks: 0h 0m | 0.00 hours";
	gross_pay.innerHTML = "Gross Pay: $0.00";
}