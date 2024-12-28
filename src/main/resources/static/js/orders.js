document.addEventListener("DOMContentLoaded", function() {
    const ordersList = document.getElementById("orders-list");
    const paginationContainer = document.getElementById("orders-pagination");

    let currentPage = 0;
    const pageSize = 10;

    fetchOrders(currentPage);

    function fetchOrders(page) {
        const url = `/api/orders/getOrders?page=${page}&size=${pageSize}`;

        fetch(url)
            .then(response => {
                if (!response.ok) {
                    throw new Error(msgErrorLoadingOrders);
                }
                return response.json();
            })
            .then(data => {
                renderOrders(data);
                renderPagination(data);
            })
            .catch(err => {
                console.error("Error fetching orders:", err);
                ordersList.innerHTML = `<p style="color:red;">${msgErrorLoadingOrders}</p>`;
            });
    }

    function renderOrders(data) {
        ordersList.innerHTML = "";

        let orders = [];
        if (data._embedded && data._embedded.orderDetailsDTOList) {
            orders = data._embedded.orderDetailsDTOList;
        } else if (data.content) {
            orders = data.content;
        }

        if (orders.length === 0) {
            ordersList.innerHTML = `<p>${msgNoOrders}</p>`;
            return;
        }

        orders.forEach(order => {
            const orderCard = document.createElement("div");
            orderCard.classList.add("order-card");

            const headerHtml = `
                <div class="order-header">
                    <span><strong>${msgOrderId}</strong> ${order.id}</span>
                    <span class="order-status">
                        <strong>${msgStatus}</strong> ${order.status}
                    </span>
                    <span><strong>${msgPaymentMethod}</strong> ${order.paymentMethod}</span>
                    <span><strong>${msgAddress}</strong> ${order.address}</span>
                    <span><strong>${msgOrderDate}</strong> ${order.orderDate}</span>
                </div>
            `;

            let itemsHtml = `
                <table class="order-items-table">
                    <thead>
                        <tr>
                            <th>${msgTableHeaderProduct}</th>
                            <th>${msgTableHeaderPrice}</th>
                            <th>${msgTableHeaderQuantity}</th>
                            <th>${msgTableHeaderLineTotal}</th>
                        </tr>
                    </thead>
                    <tbody>
            `;

            if (order.orderItems && order.orderItems.length > 0) {
                order.orderItems.forEach(item => {
                    itemsHtml += `
                        <tr>
                            <td>${item.productName}</td>
                            <td>$${(item.price || 0).toFixed(2)}</td>
                            <td>${item.quantity}</td>
                            <td>$${(item.totalPrice || 0).toFixed(2)}</td>
                        </tr>
                    `;
                });
            }

            itemsHtml += `
                    </tbody>
                </table>
            `;

            const summaryHtml = `
                <div class="order-summary">
                    ${msgSummaryTotal} $${(order.total || 0).toFixed(2)}
                </div>
            `;

            orderCard.innerHTML = headerHtml + itemsHtml + summaryHtml;
            ordersList.appendChild(orderCard);
        });
    }

    function renderPagination(data) {
        paginationContainer.innerHTML = "";

        if (data.page) {
            const pageInfo = data.page;
            const current = pageInfo.number;
            const totalPages = pageInfo.totalPages;

            const prevBtn = document.createElement("span");
            prevBtn.classList.add("page-link");
            prevBtn.textContent = msgPaginationPrev;

            if (current > 0) {
                prevBtn.addEventListener("click", () => {
                    currentPage = currentPage - 1;
                    fetchOrders(currentPage);
                });
            } else {
                prevBtn.classList.add("disabled");
            }
            paginationContainer.appendChild(prevBtn);

            for (let i = 0; i < totalPages; i++) {
                const pageBtn = document.createElement("span");
                pageBtn.classList.add("page-link");
                pageBtn.textContent = (i + 1);
                if (i === current) {
                    pageBtn.classList.add("disabled");
                } else {
                    pageBtn.addEventListener("click", () => {
                        currentPage = i;
                        fetchOrders(currentPage);
                    });
                }
                paginationContainer.appendChild(pageBtn);
            }

            const nextBtn = document.createElement("span");
            nextBtn.classList.add("page-link");
            nextBtn.textContent = msgPaginationNext;

            if (current < totalPages - 1) {
                nextBtn.addEventListener("click", () => {
                    currentPage = currentPage + 1;
                    fetchOrders(currentPage);
                });
            } else {
                nextBtn.classList.add("disabled");
            }
            paginationContainer.appendChild(nextBtn);
        }
    }
});
