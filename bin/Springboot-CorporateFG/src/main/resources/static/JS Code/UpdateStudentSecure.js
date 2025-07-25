
function showPasswordModal() {
	var passwordModal = new bootstrap.Modal(document.getElementById('passwordModal'));
	passwordModal.show();
}

function validatePassword() {
	var password = document.getElementById('passwordInput').value;
	var errorMessage = document.getElementById('passwordError');
	if (password === "!123") {
		document.getElementById('emailForm').submit();
	} else {
		errorMessage.style.display = 'block';
	}
}