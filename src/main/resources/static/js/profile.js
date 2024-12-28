document.addEventListener("DOMContentLoaded", () => {
  const profileButton = document.getElementById("profile-button");
  const profileDropdown = document.getElementById("profile-dropdown");
  const backofficeLink = document.getElementById("backoffice-link");
  const consoleLink = document.getElementById("console-link");
  const logoutButton = document.getElementById("logout-button");
  const closeProfileDropdown = document.getElementById("close-profile-dropdown");
  const loginModal = document.getElementById("login-modal");
  const loginError = document.getElementById("login-error");

  let userProfile = null;
  let userLoggedIn = false;

  const fetchUserProfile = async () => {
    try {
      const response = await fetch("/api/profile/getUserProfile");
      userProfile = await response.json();
      updateProfileButton(userProfile);
      updateUIBasedOnProfile();
    } catch (error) {
      console.error("Error fetching user profile:", error);
    }
  };

  const updateProfileButton = (profile) => {
    if (profile.anonymous) {
      profileButton.textContent = "← Sign In";
      profileButton.classList.add("sign-in-button");
      profileButton.classList.remove("profile-icon-button");
      userLoggedIn = false;
      profileButton.addEventListener("click", () => {
        loginError.style.display = "none";
        loginModal.style.display = loginModal.style.display === "flex" ? "none" : "flex";
      });
    } else {
      profileButton.textContent = "👤 " + profileIconTxt;
      profileButton.classList.add("profile-icon-button");
      profileButton.classList.remove("sign-in-button");
      userLoggedIn = true;
    }
  };

  const updateUIBasedOnProfile = () => {
    if (!userLoggedIn || !userProfile) return;

    if (userProfile.manager) {
      backofficeLink.style.display = "block";
    }
    if (userProfile.admin) {
      consoleLink.style.display = "block";
    }

    profileButton.addEventListener("click", () => {
      profileDropdown.style.display = profileDropdown.style.display === "block" ? "none" : "block";
    });

    if (closeProfileDropdown) {
      closeProfileDropdown.addEventListener("click", () => {
        profileDropdown.style.display = "none";
      });
    }

    logoutButton.addEventListener("click", async () => {
      try {
        const response = await fetch("/api/auth/logout", {
          method: "POST",
          credentials: "include",
        });

        if (response.ok) {
            window.location.href = "/";
        } else {
          console.error("Failed to log out.");
        }
      } catch (error) {
        console.error("Error during logout:", error);
      }
    });

  };

  fetchUserProfile();
});
