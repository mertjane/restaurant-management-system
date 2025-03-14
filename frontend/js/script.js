document.addEventListener("DOMContentLoaded", function () {
  const form = document.querySelector("form");

  const username = document.querySelector("#username");
  const password = document.querySelector("#password");
  const usernameError = document.querySelector("#username-error");
  const pwdError = document.querySelector("#pwd-error");
  const btn = document.querySelector("button");
  isLoggedin = false;

  const client = JSON.parse(sessionStorage.getItem("client"));

  // If client exists and is logged in, redirect to dashboard
  if (client && client.isLoggedin) {
    window.location.href = "dashboard.html";
  } else {
    document.body.style.display = "block"; // Show page only if not logged in
  }

  function validateUsername() {
    let isUsernameValid =
      username.value.length >= 5 && username.value.length <= 20;

    if (!isUsernameValid) {
      usernameError.textContent =
        "Username must be between 5 and 20 characters";
      usernameError.style.display = "block";
    } else {
      usernameError.style.display = "none";
    }

    // Disable button if any field is invalid
    btn.disabled = !isUsernameValid;
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

    btn.disabled = !isPasswordValid;
  }

  username.addEventListener("input", validateUsername);
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

  form.addEventListener("submit", function (event) {
    event.preventDefault(); // Stop form submission if invalid
    validateUsername();
    validatePwd();

    const client = {
      userID: Math.floor(Math.random() * 1000),
      username: username.value,
      isLoggedin: true,
    };

    if (btn.disabled) {
      console.log("Form is invalid");
      return;
    }
    console.log(client);
    showSuccessMessage();

    // Disable inputs and button while processing
    username.disabled = true;
    password.disabled = true;
    btn.disabled = true;
    btn.style.cursor = "not-allowed";
    btn.textContent = "Logging in..."; // Show a loading state

    setTimeout(() => {
      window.sessionStorage.setItem("client", JSON.stringify(client));
      window.location.href = "dashboard.html"; // Redirect after timeout
    }, 2500);

    form.reset();
  });
});
