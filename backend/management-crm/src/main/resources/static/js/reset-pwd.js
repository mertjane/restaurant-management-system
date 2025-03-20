document.addEventListener("DOMContentLoaded", function () {
  const resetForm = document.getElementById("reset-form");
  const confirmResetPwdBtn = document.getElementById("reset-btn");

  resetForm.addEventListener("submit", async (e) => {
    e.preventDefault();

    const token = document.getElementById("token").value;
    const newPassword = document.getElementById("newPassword").value;
    const confirmPassword = document.getElementById("confirmPassword").value;

    confirmResetPwdBtn.style.cursor = "not-allowed";
    confirmResetPwdBtn.textContent = "Loading...";

    const result = await api.resetMyPassword({
      token,
      newPassword,
      confirmPassword,
    });

    if (result.success) {
      console.log(result.message || "Password reset successfully.");

      setTimeout(() => {
        window.location.href = "/password-reset-success.html";
      }, 1500);
    } else {
      console.log(
        result.message || "Failed to reset password. Please try again later."
      );
    }
  });
});
