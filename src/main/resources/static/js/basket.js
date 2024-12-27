document.addEventListener("DOMContentLoaded", () => {
  const basketButton = document.getElementById("basket-button");
  const basketDropdown = document.getElementById("basket-dropdown");
  const basketCount = document.getElementById("basket-count");
  const closeBasket = document.getElementById("close-basket");
  const basketItemsContainer = document.getElementById("basket-items");
  const basketTotalPrice = document.getElementById("basket-total-price");

  window.fetchBasket = async function fetchBasket() {
    try {
      const response = await fetch("/api/basket/get");
      const data = await response.json();
      window.updateBasket(data);
    } catch (error) {
      console.error("Error fetching basket data:", error);
    }
  };

  window.updateBasket = function updateBasket(basket) {
    basketCount.textContent = basket.items.reduce(
      (sum, item) => sum + item.quantity,
      0
    );

    basketItemsContainer.innerHTML = basket.items
      .map(
        (item) => `
          <div class="basket-item" data-product-id="${item.product.id}">
              <span class="item-name">${item.product.name}</span>
              <span class="item-quantity">x${item.quantity}</span>
              <span class="item-price">$${
                (item.product.price * item.quantity).toFixed(2)
              }</span>
              <button class="remove-item-button">🗑️</button>
          </div>
        `
      )
      .join("");

    const totalPrice = basket.items.reduce(
      (sum, item) => sum + item.product.price * item.quantity,
      0
    );
    basketTotalPrice.textContent = `$${totalPrice.toFixed(2)}`;

    document.querySelectorAll(".remove-item-button").forEach((button) => {
      button.addEventListener("click", window.handleRemoveItem);
    });
  };

  window.handleRemoveItem = async function handleRemoveItem(event) {
    const productId = event.target.closest(".basket-item").dataset.productId;
    try {
      const response = await fetch(`/api/basket/remove?productId=${productId}`, {
        method: "DELETE",
      });
      if (response.ok) {
        await window.fetchBasket();
      } else {
        console.error("Failed to remove product from basket");
      }
    } catch (error) {
      console.error("Error removing product from basket:", error);
    }
  };

  window.addProductToBasket = async function addProductToBasket(productId, quantity) {
    try {
      const response = await fetch(`/api/basket/add?productId=${productId}&quantity=${quantity}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
      });

      if (!response.ok) {
        throw new Error("Failed to add product to basket");
      }

      await window.fetchBasket();

      return { success: true };
    } catch (error) {
      console.error("Error adding product to basket:", error);
      return { success: false };
    }
  };

  basketButton.addEventListener("click", () => {
    basketDropdown.style.display =
      basketDropdown.style.display === "block" ? "none" : "block";
  });

  closeBasket.addEventListener("click", () => {
    basketDropdown.style.display = "none";
  });

  window.fetchBasket();
});
