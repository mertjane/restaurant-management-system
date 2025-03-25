document.addEventListener("DOMContentLoaded", function () {
  const token = sessionStorage.getItem("token");

  // If no client is found, redirect to auth.html
  if (!token) {
    window.location.href = "auth.html";
  } else {
    document.body.style.display = "block"; // Show page only if logged in
  }

  // Element selectors for dropdown menus
  const openDropdown = document.getElementById("toggleDropdown"); // Profile icon button
  const notifDropdown = document.getElementById("notif-toggle"); // Notification icon button
  const notif = document.getElementById("notif-dropdown"); // Notification dropdown menu
  const dropdown = document.getElementById("dropdown"); // Profile dropdown menu

  // Toggle notification dropdown and close profile dropdown
  notifDropdown.addEventListener("click", function () {
    event.stopPropagation(); // Prevent click from propagating to document
    dropdown.classList.remove("active"); // Close profile dropdown
    notif.classList.toggle("active"); // Toggle notification dropdown
  });

  // Toggle profile dropdown and close notification dropdown
  openDropdown.addEventListener("click", function () {
    event.stopPropagation(); // Prevent click from propagating to document
    notif.classList.remove("active"); // Close notification dropdown
    dropdown.classList.toggle("active"); // Toggle profile dropdown
  });

  // Close notification dropdown when clicking outside
  document.addEventListener("click", function (event) {
    if (
      !notif.contains(event.target) &&
      !notifDropdown.contains(event.target)
    ) {
      notif.classList.remove("active");
    }
  });

  // Close profile dropdown when clicking outside
  document.addEventListener("click", function (event) {
    if (
      !dropdown.contains(event.target) &&
      !openDropdown.contains(event.target)
    ) {
      dropdown.classList.remove("active");
    }
  });

  // Sidebar toggle
  document
    .getElementById("toggleSidebar")
    .addEventListener("click", function () {
      document.querySelector(".sidebar").classList.toggle("active");
      document.querySelector(".site-header").classList.toggle("active");
      document.querySelector(".main-content").classList.toggle("active");
    });

  // Logout function
  document.getElementById("logout").addEventListener("click", function () {
    sessionStorage.removeItem("token"); // Clear stored login data
    window.location.href = "auth.html"; // Redirect to login
  });
});
