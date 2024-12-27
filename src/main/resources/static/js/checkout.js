$(document).ready(function() {
    const $basketItems = $("#checkout-items");
    const $basketTotal = $("#basket-total");
    const $addressSelect = $("#addressSelect");
    const $newStreet = $("#newStreet");
    const $newCity = $("#newCity");
    const $newState = $("#newState");
    const $newCountry = $("#newCountry");

    const $cardHolder = $("#cardHolder");
    const $cardNumber = $("#cardNumber");
    const $cardExpiry = $("#cardExpiry");
    const $cardCVV = $("#cardCVV");

    const $checkoutButton = $("#checkout-button");
    const $successMessage = $("#success-message");
    const $errorMessage = $("#error-message");
    const $errorDetails = $("#error-details");

    fetchBasketData();

    fetchAddresses();

    $checkoutButton.on("click", async function() {
        $successMessage.hide();
        $errorMessage.hide();
        $errorDetails.empty();

        try {
            const validationError = validateFields();
            if (validationError) {
                showError(validationError);
                return;
            }

            let addressId = $addressSelect.val();
            if (addressId === "none") {
                addressId = null;
            }

            if (isNewAddressEntered()) {
                addressId = await addNewAddress();
                $addressSelect.val(addressId);
                $newStreet.val("");
                $newCity.val("");
                $newState.val("");
                $newCountry.val("");
            } else if (!addressId && !isNewAddressEntered()) {
                throw new Error("Select an existing address or fill in new address fields.");
            }

            await placeOrder(addressId);

            $successMessage.show();

            setTimeout(() => {
                window.location.href = "/";
            }, 3000);

        } catch (err) {
            console.error("Error during checkout:", err);
            showError("A server error occurred.");
        }
    });

    function fetchBasketData() {
        fetch("/api/basket/get")
            .then(response => response.json())
            .then(data => {
                renderBasket(data);
            })
            .catch(error => {
                console.error("Error fetching basket:", error);
            });
    }

    function renderBasket(basket) {
        let html = basket.items.map(item => `
            <div class="basket-item">
                <span>${item.product.name}</span>
                <span>x${item.quantity}</span>
                <span>$${(item.product.price * item.quantity).toFixed(2)}</span>
            </div>
        `).join("");

        $basketItems.html(html);

        let totalPrice = basket.items.reduce((acc, i) => acc + (i.product.price * i.quantity), 0);
        $basketTotal.text(`$${totalPrice.toFixed(2)}`);
    }

    function fetchAddresses() {
        fetch("/api/address/get")
            .then(response => response.json())
            .then(addressList => {
                renderAddressSelect(addressList);
            })
            .catch(error => {
                console.error("Error fetching addresses:", error);
            });
    }

    function renderAddressSelect(addresses) {
        $addressSelect.empty();
        $addressSelect.append(`<option value="none">-- Choose existing address --</option>`);
        addresses.forEach(addr => {
            const label = `${addr.street}, ${addr.city}, ${addr.state}, ${addr.country}`;
            $addressSelect.append(`<option value="${addr.id}">${label}</option>`);
        });
    }

    async function addNewAddress() {
        const addressDTO = {
            street: $newStreet.val(),
            city: $newCity.val(),
            state: $newState.val(),
            country: $newCountry.val()
        };

        const response = await fetch("/api/address/add", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(addressDTO)
        });
        if (!response.ok) {
            throw new Error("Failed to add new address");
        }

        await fetchAddresses();
        const addresses = await fetch("/api/address/get").then(r => r.json());
        if (addresses.length > 0) {
            let lastAddress = addresses[addresses.length - 1];
            $addressSelect.val(lastAddress.id);
            return lastAddress.id;
        }
        return null;
    }

    function isNewAddressEntered() {
        return (
            $newStreet.val().trim() !== "" ||
            $newCity.val().trim() !== "" ||
            $newState.val().trim() !== "" ||
            $newCountry.val().trim() !== ""
        );
    }

    function validateFields() {
        const chosenAddress = $addressSelect.val();
        const addressFieldsFilled = ($newStreet.val().trim() !== "" &&
                                     $newCity.val().trim() !== "" &&
                                     $newState.val().trim() !== "" &&
                                     $newCountry.val().trim() !== "");

        if (chosenAddress === "none" && !addressFieldsFilled) {
            return "Please select an existing address or fill in all new address fields.";
        }

        if (isNewAddressEntered() && !addressFieldsFilled) {
            return "Please fill in all address fields or clear them if you want to use an existing address.";
        }

        if ($cardHolder.val().trim() === "" ||
            $cardNumber.val().trim() === "" ||
            $cardExpiry.val().trim() === "" ||
            $cardCVV.val().trim() === "") {
            return "Please fill in all payment fields.";
        }

        return null;
    }

    async function placeOrder(addressId) {
        const requestBody = {
            addressId: addressId,
            payment: {
                cardHolder: $cardHolder.val(),
                cardNumber: $cardNumber.val(),
                cardExpiry: $cardExpiry.val(),
                paymentMethod: "Card",
                amount: parseFloat($basketTotal.text().replace("$", ""))
            }
        };

        const response = await fetch("/api/checkout", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(requestBody)
        });

        if (!response.ok) {
            throw new Error("Checkout failed");
        }
    }

    function showError(msg) {
        $errorMessage.show();
        $errorDetails.text(msg);
    }
});
