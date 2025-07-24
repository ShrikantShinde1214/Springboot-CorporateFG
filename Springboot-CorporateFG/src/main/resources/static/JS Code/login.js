let captchaCode = '';
function generateCaptcha() {
	const chars = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789';
	captchaCode = '';
	for (let i = 0; i < 6; i++) {
		captchaCode += chars.charAt(Math.floor(Math.random() * chars.length));
	}
	document.getElementById('captchaImage').innerHTML = `<span>${captchaCode}</span>`;
}

function validateLogin() {
	const captcha = document.getElementById('captcha').value.trim();
	const errorDiv = document.getElementById('loginError');

	if (captcha !== captchaCode) {
		errorDiv.style.display = 'block';
		errorDiv.textContent = 'Invalid Captcha.';
		return false;
	}
	return true;
}
window.onload = generateCaptcha;
