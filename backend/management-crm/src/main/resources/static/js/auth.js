document.addEventListener("DOMContentLoaded", () => {
  const form = document.querySelector("form");
  const email = document.querySelector("#email");
  const password = document.querySelector("#password");
  const emailError = document.querySelector("#email-error");
  const pwdError = document.querySelector("#pwd-error");
  const forgotPwdBtn = document.querySelector("#forgot-pwd-btn");
  const loginBtn = document.querySelector("#login-btn");
  isLoggedin = false;

  const token = sessionStorage.getItem("token");

  // If client exists and is logged in, redirect to dashboard
  if (token) {
    window.location.href = "dashboard.html";
  } else {
    document.body.style.display = "block"; // Show page only if not logged in
  }

  function validateEmail() {
    let regex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
    let isEmailValid = regex.test(email.value);

    if (!isEmailValid) {
      emailError.textContent = "Invalid Email Address";
      emailError.style.display = "block";
    } else {
      emailError.style.display = "none";
    }

    // Disable button if any field is invalid
    loginBtn.disabled = !isEmailValid;
  }

  function validatePwd() {
    let isPasswordValid =
      password.value.length >= 8 && password.value.length <= 20;

    if (!isPasswordValid) {
      pwdError.textContent = "Password must be between 8 and 20 characters";
      pwdError.style.display = "block";
    } else {
      pwdError.style.display = "none";
    }

    loginBtn.disabled = !isPasswordValid;
  }

  email.addEventListener("input", validateEmail);
  password.addEventListener("input", validatePwd);

  function showSuccessMessage() {
    const successMessage = document.getElementById("success-message");

    successMessage.classList.add("show");

    setTimeout(() => {
      successMessage.classList.add("hide");

      setTimeout(() => {
        successMessage.classList.remove("show", "hide"); // Reset classes after animation
      }, 500); // Wait for fade-out to complete
    }, 3000); // Display message for 3 seconds
  }

  forgotPwdBtn.addEventListener("click", async (e) => {
    e.preventDefault();

    const emailInput = document.querySelector("#email");
    const email = emailInput.value.trim();

    if (!email) {
      emailError.style.display = "block";
      emailError.textContent = "Please enter your email";
      return;
    }

    // If email is valid, proceed with UI update and API call
    emailError.style.display = "none"; // Clear any previous error
    loginBtn.style.cursor = "not-allowed";
    loginBtn.textContent = "Loading...";

    // Call the API function
    const result = await api.forgotPassword(email);

    if (result.success) {
      console.log(result.message || "Password reset link sent to your email");
      setTimeout(() => {
        window.location.href = "email-sent-success.html";
      }, 2500);
    } else {
      console.log(result.message || "Failed to send reset link");
    }
  });

  // login function
  async function handleLogin(event) {
    event.preventDefault();

    const emailValue = email.value; // Use .value to get the input value
    const passwordValue = password.value;

    const result = await api.login({
      email: emailValue,
      password: passwordValue,
    });

    if (result.success) {
      console.log(result.message);
      if (result.token) {
        sessionStorage.setItem("token", result.token);
      }

      setTimeout(() => {
        window.location.href = "/dashboard.html"; // redirect to dashboard
      }, 1200);
    } else {
      console.log(result.message);
    }

    // Re-enable form elements after login attempt
    email.disabled = false;
    password.disabled = false;
    loginBtn.disabled = false;
    loginBtn.style.cursor = "pointer";
    loginBtn.textContent = "Login"; // Reset button text
  }

  form.addEventListener("submit", async function (event) {
    event.preventDefault(); // Stop form submission if invalid
    validateEmail();
    validatePwd();

    if (loginBtn.disabled) {
      console.log("Form is invalid");
      return;
    }

    showSuccessMessage();

    // Disable inputs and button while processing
    email.disabled = true;
    password.disabled = true;
    loginBtn.disabled = true;
    loginBtn.style.cursor = "not-allowed";
    loginBtn.textContent = "Logging in..."; // Show a loading state

    // Call handleLogin
    await handleLogin(event);

    form.reset();
  });
});
