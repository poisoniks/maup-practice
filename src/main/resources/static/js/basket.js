function fetchBasket() {
    fetch('/basket/get')
        .then(response => response.json())
        .then(data => {
            const basketContents = document.getElementById('basketContents');
            basketContents.innerHTML = '';

            data.items.forEach(item => {
                const row = `
                    <tr>
                        <td>${item.productId}</td>
                        <td>${item.productName}</td>
                        <td>${item.quantity}</td>
                        <td>${item.price}</td>
                    </tr>
                `;
                basketContents.innerHTML += row;
            });
        })
        .catch(error => console.error('Error fetching basket:', error));
}

function addToBasket(productId, quantity) {
    fetch(`/basket/add?productId=${productId}&quantity=${quantity}`, {
        method: 'POST',
    })
        .then(response => {
            if (response.ok) {
                alert('Product added to basket.');
                fetchBasket();
            } else {
                response.text().then(message => alert(message));
            }
        })
        .catch(error => console.error('Error adding product to basket:', error));
}

function removeFromBasket(productId) {
    fetch(`/basket/remove?productId=${productId}`, {
        method: 'DELETE',
    })
        .then(response => {
            if (response.ok) {
                alert('Product removed from basket.');
                fetchBasket();
            } else {
                alert('Failed to remove product.');
            }
        })
        .catch(error => console.error('Error removing product from basket:', error));
}

function clearBasket() {
    fetch('/basket/clear', {
        method: 'DELETE',
    })
        .then(response => {
            if (response.ok) {
                alert('Basket cleared.');
                fetchBasket();
            } else {
                alert('Failed to clear basket.');
            }
        })
        .catch(error => console.error('Error clearing basket:', error));
}
