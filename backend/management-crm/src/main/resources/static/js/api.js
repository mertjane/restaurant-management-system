const API_BASE_URL = "http://localhost:8080";

const api = {
  // Forgot password trigger
  forgotPassword: async (email) => {
    try {
      const response = await fetch(`${API_BASE_URL}/auth/forgot-password`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ email }),
      });

      const data = await response.json();

      if (!response.ok) {
        throw new Error(data || "Failed to send reset link");
      }

      return {
        success: data.success,
        message: data.message,
      };
    } catch (error) {
      return {
        success: false,
        message: error.message,
      };
    }
  },

  // Reset Password Trigger
  resetMyPassword: async ({ token, newPassword, confirmPassword }) => {
    try {
      const response = await fetch(
        `${API_BASE_URL}/auth/reset-password?token=${token}`,
        {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify({ newPassword, confirmPassword }),
        }
      );

      const data = await response.json();

      if (!response.ok) {
        throw new Error(data || "Failed to reset password.");
      }

      return {
        success: data.success,
        message: data.message,
      };
    } catch (error) {
      return {
        success: false,
        message: error.message,
      };
    }
  },

  // Login
  login: async ({email, password}) => {
    try {
      const res = await fetch(`${API_BASE_URL}/auth/login`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ email, password }),
      });

      const data = await res.json();

      if (!res.ok) {
        throw new Error(data || "Login failed");
      }

      return {
        success: true,
        message: data.message || "Login successful",
        token: data.token, // only if using jwt version
      };
    } catch (err) {
      return {
        success: false,
        message: err.message,
      };
    }
  },
};
