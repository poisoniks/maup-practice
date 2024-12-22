let currentPage = 0;
const pageSize = 10;
let totalPages = 0;

function loadProducts(page) {
    const name = document.getElementById('name').value;
    const minPrice = document.getElementById('minPrice').value;
    const maxPrice = document.getElementById('maxPrice').value;
    const brandId = document.getElementById('brandId').value;
    const supplierId = document.getElementById('supplierId').value;
    const categoryId = document.getElementById('categoryId').value;

    const queryParams = new URLSearchParams({
        page: page,
        size: pageSize,
        name: name || '',
        minPrice: minPrice || '',
        maxPrice: maxPrice || '',
        brandId: brandId || '',
        supplierId: supplierId || '',
        categoryId: categoryId || ''
    });

    fetch(`/products/search?${queryParams.toString()}`)
        .then(response => response.json())
        .then(data => {
            const productTableBody = document.getElementById('productTableBody');
            productTableBody.innerHTML = '';

            const products = data._embedded ? data._embedded.productDTOList : [];

            products.forEach(product => {
                const row = `<tr>
                    <td>${product.id}</td>
                    <td>${product.name}</td>
                    <td>${product.description}</td>
                    <td>${product.price}</td>
                    <td>${product.stockQuantity}</td>
                </tr>`;
                productTableBody.innerHTML += row;
            });

            totalPages = data.page.totalPages;
            document.getElementById('pageInfo').innerText = `Page ${data.page.number + 1} of ${data.page.totalPages}`;

            document.getElementById('prevPageBtn').disabled = data.page.number === 0;
            document.getElementById('nextPageBtn').disabled = data.page.number === totalPages - 1;

            currentPage = data.page.number;
        })
        .catch(error => console.error('Error:', error));
}

document.addEventListener('DOMContentLoaded', () => {
    document.getElementById('prevPageBtn').addEventListener('click', () => {
        if (currentPage > 0) {
            loadProducts(currentPage - 1);
        }
    });

    document.getElementById('nextPageBtn').addEventListener('click', () => {
        if (currentPage < totalPages - 1) {
            loadProducts(currentPage + 1);
        }
    });

    document.getElementById('searchBtn').addEventListener('click', () => {
        loadProducts(0);
    });

    loadProducts(0);
});
