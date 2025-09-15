
let captchaCode = '';

function generateCaptcha() {
    const chars = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
    captchaCode = '';
    for (let i = 0; i < 6; i++) {
        captchaCode += chars.charAt(Math.floor(Math.random() * chars.length));
    }
    document.getElementById('captchaImage').innerHTML = `<span>${captchaCode}</span>`;
    // Save captcha in hidden field so it can be sent to backend
    document.getElementById('captchaHidden').value = captchaCode;
}


// Validate captcha on form submit
function validateLogin() {
    const enteredCaptcha = document.getElementById('captcha').value.trim();
    const errorDiv = document.getElementById('loginError');

    if (enteredCaptcha === '') {
        showError('❌ Please enter the captcha.');
        return false;
    }

    if (enteredCaptcha !== captchaCode) {
        showError('❌ Invalid Captcha (Case Sensitive).');
        generateCaptcha(); // regenerate captcha
        document.getElementById('captcha').value = '';
        return false;
    }

    hideError();
    return true; // allow form submission
}

// Show error
function showError(message) {
    let errorDiv = document.getElementById('loginError');
    if (!errorDiv) {
        errorDiv = document.createElement('div');
        errorDiv.id = 'loginError';
        errorDiv.style.color = 'red';
        errorDiv.style.marginTop = '5px';
        document.getElementById('captchaContainer').appendChild(errorDiv);
    }
    errorDiv.style.display = 'block';
    errorDiv.textContent = message;
}

// Hide error
function hideError() {
    const errorDiv = document.getElementById('loginError');
    if (errorDiv) errorDiv.style.display = 'none';
}

// Generate captcha on page load
window.onload = generateCaptcha;

