document.addEventListener("DOMContentLoaded", function () {
  const $quantityInput = $("#quantity");
  const $plusButton = $("#plus-button");
  const $minusButton = $("#minus-button");
  const $addToCartButton = $("#add-to-cart-button");
  const $stockQuantity = $("#stockQuantity");
  const $productName = $(".product-name");
  const $popupMessage = $("#popup-message");

  let maxStock = parseInt($stockQuantity.text()) || 0;

  $plusButton.on("click", function () {
    let currentVal = parseInt($quantityInput.val()) || 1;
    if (currentVal < maxStock) {
      $quantityInput.val(currentVal + 1);
    }
  });

  $minusButton.on("click", function () {
    let currentVal = parseInt($quantityInput.val()) || 1;
    if (currentVal > 1) {
      $quantityInput.val(currentVal - 1);
    }
  });

  $quantityInput.on("change", function () {
    let val = parseInt($quantityInput.val()) || 1;
    if (val < 1) val = 1;
    if (val > maxStock) val = maxStock;
    $quantityInput.val(val);
  });

  $addToCartButton.on("click", function () {
    (async () => {
      const quantityToAdd = parseInt($quantityInput.val()) || 1;
      const productId = $("#productId").val() || "0";

      const result = await window.addProductToBasket(productId, quantityToAdd);
      if (result.success) {
        showPopupMessage(
          `Product "${$productName.text()}" (${quantityToAdd}) added to cart!`,
          "success"
        );
      } else {
        showPopupMessage(
          `Failed to add "${$productName.text()}" to cart.`,
          "error"
        );
      }
    })();
  });

  function showPopupMessage(message, type) {
    $popupMessage.removeClass("popup-success popup-error");
    $popupMessage.text(message);

    if (type === "success") {
      $popupMessage.css("background-color", "#4CAF50");
    } else {
      $popupMessage.css("background-color", "#f44336");
    }

    $popupMessage.fadeIn(200);

    setTimeout(function () {
      $popupMessage.fadeOut(200);
    }, 3000);
  }
});
