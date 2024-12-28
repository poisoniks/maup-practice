document.addEventListener("DOMContentLoaded", function() {
    const searchNameInput = document.getElementById("searchName");
    const searchBtn = document.getElementById("searchBtn");
    const createProductBtn = document.getElementById("createProductBtn");

    const productsList = document.getElementById("products-list");
    const paginationContainer = document.getElementById("products-pagination");

    const productForm = document.getElementById("product-form");
    const productIdField = document.getElementById("productId");
    const productNameField = document.getElementById("productName");
    const productPriceField = document.getElementById("productPrice");
    const productStockField = document.getElementById("productStock");
    const productCategoryField = document.getElementById("productCategory");
    const productBrandField = document.getElementById("productBrand");
    const productSupplierField = document.getElementById("productSupplier");
    const productDescriptionField = document.getElementById("productDescription");

    const saveProductBtn = document.getElementById("saveProductBtn");
    const cancelFormBtn = document.getElementById("cancelFormBtn");
    const formTitle = document.getElementById("form-title");

    let currentPage = 0;
    const pageSize = 10;

    loadDropdowns();

    searchBtn.addEventListener("click", function() {
        currentPage = 0;
        fetchProducts();
    });

    saveProductBtn.addEventListener("click", function() {
        saveProduct();
    });

    cancelFormBtn.addEventListener("click", function() {
        hideForm();
    });

    fetchProducts();

    function fetchProducts() {
        const name = searchNameInput.value.trim();
        let url = `/api/backoffice/selectProducts?page=${currentPage}&size=${pageSize}`;
        if (name) {
            url += `&name=${encodeURIComponent(name)}`;
        }

        fetch(url)
            .then(response => {
                if (!response.ok) {
                    throw new Error("Failed to fetch products");
                }
                return response.json();
            })
            .then(data => {
                renderProducts(data);
                renderPagination(data);
            })
            .catch(err => {
                console.error("Error fetching products:", err);
                productsList.innerHTML = `<p style="color: red;">Error loading products.</p>`;
            });
    }

    function renderProducts(data) {
        productsList.innerHTML = "";

        let products = [];
        if (data._embedded && data._embedded.productDTOList) {
            products = data._embedded.productDTOList;
        } else if (data.content) {
            products = data.content;
        }

        if (products.length === 0) {
            productsList.innerHTML = "<p>No products found.</p>";
            return;
        }

        products.forEach(product => {
            const row = document.createElement("div");
            row.classList.add("product-row");

            row.innerHTML = `
                <span><strong>ID:</strong> ${product.id}</span> |
                <span><strong>Name:</strong> ${product.name}</span> |
                <span><strong>Price:</strong> ${product.price}</span> |
                <span><strong>Stock:</strong> ${product.stockQuantity}</span>
                <button class="edit-btn">Edit</button>
                <button class="delete-btn" style="color:red;">Delete</button>
            `;

            const editBtn = row.querySelector(".edit-btn");
            editBtn.addEventListener("click", function() {
                showForm(product);
            });

            const deleteBtn = row.querySelector(".delete-btn");
            deleteBtn.addEventListener("click", function() {
                if (confirm("Are you sure to delete this product?")) {
                    deleteProduct(product.id);
                }
            });

            productsList.appendChild(row);
        });
    }

    function renderPagination(data) {
        paginationContainer.innerHTML = "";

        if (!data.page) {
            return;
        }

        const pageInfo = data.page;
        const current = pageInfo.number;
        const totalPages = pageInfo.totalPages;

        const prevBtn = document.createElement("button");
        prevBtn.textContent = "Prev";
        prevBtn.disabled = (current === 0);
        if (!prevBtn.disabled) {
            prevBtn.addEventListener("click", function() {
                currentPage = currentPage - 1;
                fetchProducts();
            });
        }
        paginationContainer.appendChild(prevBtn);

        const nextBtn = document.createElement("button");
        nextBtn.textContent = "Next";
        nextBtn.disabled = (current >= totalPages - 1);
        if (!nextBtn.disabled) {
            nextBtn.addEventListener("click", function() {
                currentPage = currentPage + 1;
                fetchProducts();
            });
        }
        paginationContainer.appendChild(nextBtn);
    }

    function loadDropdowns() {
        fetch("/api/backoffice/selectCategories")
            .then(r => r.json())
            .then(data => {
                renderDropdown(productCategoryField, data, "Select Category");
            })
            .catch(err => console.error("Error loading categories:", err));

        fetch("/api/backoffice/selectBrands")
            .then(r => r.json())
            .then(data => {
                renderDropdown(productBrandField, data, "Select Brand");
            })
            .catch(err => console.error("Error loading brands:", err));

        fetch("/api/backoffice/selectSuppliers")
            .then(r => r.json())
            .then(data => {
                renderDropdown(productSupplierField, data, "Select Supplier");
            })
            .catch(err => console.error("Error loading suppliers:", err));
    }

    function renderDropdown(selectEl, dataArr, placeholder) {
        selectEl.innerHTML = "";
        const placeholderOption = document.createElement("option");
        placeholderOption.value = "";
        placeholderOption.textContent = placeholder;
        selectEl.appendChild(placeholderOption);

        dataArr.forEach(obj => {
            const opt = document.createElement("option");
            opt.value = obj.id;
            opt.textContent = obj.name;
            selectEl.appendChild(opt);
        });
    }

    function showForm(product) {
        productForm.style.display = "block";
        if (product) {
            formTitle.textContent = "Update Product";
            productIdField.value = product.id || "";
            productNameField.value = product.name || "";
            productPriceField.value = product.price || 0;
            productStockField.value = product.stockQuantity || 0;
            productDescriptionField.value = product.description || "";
            productCategoryField.value = product.category ? product.category.id : "";
            productBrandField.value = product.brand ? product.brand.id : "";
            productSupplierField.value = product.supplier ? product.supplier.id : "";
        } else {
            formTitle.textContent = "Create New Product";
            productIdField.value = "";
            productNameField.value = "";
            productPriceField.value = 0;
            productStockField.value = 0;
            productDescriptionField.value = "";
            productCategoryField.value = "";
            productBrandField.value = "";
            productSupplierField.value = "";
        }
    }

    function hideForm() {
        productForm.style.display = "none";
    }

    function saveProduct() {
        const productDTO = {
            id: productIdField.value ? parseInt(productIdField.value) : null,
            name: productNameField.value.trim(),
            price: parseFloat(productPriceField.value),
            stockQuantity: parseInt(productStockField.value),
            description: productDescriptionField.value.trim() || null,
            category: { id: productCategoryField.value || null },
            brand: { id: productBrandField.value || null },
            supplier: { id: productSupplierField.value || null }
        };

        if (productDTO.id) {
            fetch("/api/backoffice/updateProduct", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(productDTO)
            })
            .then(response => {
                if (!response.ok) throw new Error("Failed to update product");
                hideForm();
                fetchProducts();
            })
            .catch(err => console.error(err));
        } else {
            fetch("/api/backoffice/createProduct", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(productDTO)
            })
            .then(response => {
                if (!response.ok) throw new Error("Failed to create product");
                hideForm();
                fetchProducts();
            })
            .catch(err => console.error(err));
        }
    }

    function deleteProduct(productId) {
        fetch(`/api/backoffice/deleteProduct?id=${productId}`, {
            method: "POST"
        })
        .then(response => {
            if (response.ok) {
                fetchProducts();
            } else if (response.status === 409) {
                alert("Cannot delete product that has orders and baskets.");
            } else {
                throw new Error("Failed to delete product");
            }
        })
        .catch(err => console.error(err));
    }

    createProductBtn.addEventListener("click", function() {
        showForm(null);
    });
});
