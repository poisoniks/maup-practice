function fetchBasket() {
    fetch('/api/basket/get')
        .then(response => response.json())
        .then(data => {
            const basketContents = document.getElementById('basketContents');
            basketContents.innerHTML = '';

            data.items.forEach(item => {
                const row = `
                    <tr>
                        <td>${item.product.name}</td>
                        <td>${item.product.description}</td>
                        <td>${item.quantity}</td>
                        <td>${item.product.price}</td>
                    </tr>
                `;
                basketContents.innerHTML += row;
            });
        })
        .catch(error => console.error('Error fetching basket:', error));
}

function addToBasket(productId, quantity) {
    fetch(`/api/basket/add?productId=${productId}&quantity=${quantity}`, {
        method: 'PUT',
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
    fetch(`/api/basket/remove?productId=${productId}`, {
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
    fetch('/api/basket/clear', {
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
