
function validateSecureKey() {
	var secureKey = document.getElementById('secureKey').value;
	if (secureKey === '!123') {
		document.getElementById('secureKeySection').classList.add('hidden');
		document.getElementById('buttonsSection').classList.remove('hidden');
	} else {
		alert('Invalid secure key. Please try again.');
	}
}

function resetState() {
	document.getElementById('secureKey').value = '';
	document.getElementById('secureKeySection').style.display = 'block';
	document.getElementById('buttonsSection').style.display = 'none';
}