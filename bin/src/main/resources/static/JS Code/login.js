let captchaCode = '';
<<<<<<< HEAD

function generateCaptcha() {
	const chars = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
=======
function generateCaptcha() {
	const chars = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789';
>>>>>>> 8e36f906150296c4c01735ee631e0252f928074c
	captchaCode = '';
	for (let i = 0; i < 6; i++) {
		captchaCode += chars.charAt(Math.floor(Math.random() * chars.length));
	}
	document.getElementById('captchaImage').innerHTML = `<span>${captchaCode}</span>`;
}

function validateLogin() {
<<<<<<< HEAD
	const enteredCaptcha = document.getElementById('captcha').value.trim();
	const errorDiv = document.getElementById('loginError');

	if (enteredCaptcha !== captchaCode) {
		errorDiv.style.display = 'block';
		errorDiv.textContent = '❌ Invalid Captcha (Case Sensitive).';
		return false; // stop form submission
	}

	errorDiv.style.display = 'none';
	return true; // allow redirection (form submission)
}

=======
	const captcha = document.getElementById('captcha').value.trim();
	const errorDiv = document.getElementById('loginError');

	if (captcha !== captchaCode) {
		errorDiv.style.display = 'block';
		errorDiv.textContent = 'Invalid Captcha.';
		return false;
	}
	return true;
}
>>>>>>> 8e36f906150296c4c01735ee631e0252f928074c
window.onload = generateCaptcha;
