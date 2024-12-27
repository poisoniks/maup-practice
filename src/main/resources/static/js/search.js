$(document).ready(function() {
    function updateTrack() {
        const minPrice = parseInt($('#minPrice').val());
        const maxPrice = parseInt($('#maxPrice').val());
        const minPercent = (minPrice / 5000) * 100;
        const maxPercent = (maxPrice / 5000) * 100;

        $('.slider-track').css({
            left: minPercent + '%',
            right: (100 - maxPercent) + '%'
        });
    }

    function updatePriceDisplay() {
        const minPrice = $('#minPrice').val();
        const maxPrice = $('#maxPrice').val();
        $('#minPriceDisplay').val(minPrice);
        $('#maxPriceDisplay').val(maxPrice);
        updateTrack();
    }

    $('#minPrice, #maxPrice').on('input', function() {
        const minPrice = parseInt($('#minPrice').val());
        const maxPrice = parseInt($('#maxPrice').val());

        if (minPrice > maxPrice) {
            if ($(this).attr('id') === 'minPrice') {
                $('#minPrice').val(maxPrice);
            } else {
                $('#maxPrice').val(minPrice);
            }
        }
        updatePriceDisplay();
    });

    $('.price-input').change(function() {
        if ($(this).attr('id') === 'minPriceDisplay') {
            $('#minPrice').val($(this).val());
        } else {
            $('#maxPrice').val($(this).val());
        }
        updatePriceDisplay();
    });

    updatePriceDisplay();

    let currentPage = 0;
    const pageSize = 12;

    const $minPrice = $('#minPrice');
    const $maxPrice = $('#maxPrice');
    const $searchQuery = $('#searchQuery');
    const $searchButton = $('#search-button');

    const $filtersBrand = $('input[id^="brand-"]');
    const $filtersSupplier = $('input[id^="sup-"]');
    const $filtersCategory = $('input[id^="cat-"]');

    const $spinner = $('#spinner');
    const $resultsContainer = $('.search-results');
    const $paginationContainer = $('#pagination');

    function showSpinner() {
        $spinner.show();
    }
    function hideSpinner() {
        $spinner.hide();
    }

    function buildQueryParams(page) {
        const selectedBrandIds = $filtersBrand
            .filter(':checked')
            .map(function() { return $(this).val(); })
            .get();

        const selectedSupplierIds = $filtersSupplier
            .filter(':checked')
            .map(function() { return $(this).val(); })
            .get();

        const selectedCategoryIds = $filtersCategory
            .filter(':checked')
            .map(function() { return $(this).val(); })
            .get();

        const minPriceVal = $minPrice.val() || '';
        const maxPriceVal = $maxPrice.val() || '';
        const name = $searchQuery.val() || '';

        let params = new URLSearchParams();
        if (minPriceVal) params.append('minPrice', minPriceVal);
        if (maxPriceVal) params.append('maxPrice', maxPriceVal);

        selectedBrandIds.forEach(id => params.append('brandIds', id));
        selectedSupplierIds.forEach(id => params.append('supplierIds', id));
        selectedCategoryIds.forEach(id => params.append('categoryIds', id));

        if (name) params.append('name', name);

        params.append('page', page);
        params.append('size', pageSize);

        return params.toString();
    }

    function fetchProducts(page) {
        showSpinner();
        $resultsContainer.empty();
        $paginationContainer.empty();

        const queryString = buildQueryParams(page);
        const url = '/api/products/search?' + queryString;

        fetch(url)
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not OK');
                }
                return response.json();
            })
            .then(data => {
                hideSpinner();
                let products = [];

                if (data._embedded && data._embedded.productDTOList) {
                    products = data._embedded.productDTOList;
                } else {

                }

                renderProducts(products);

                if (data.page) {
                    renderPagination(data.page.number, data.page.totalPages);
                }
            })
            .catch(err => {
                hideSpinner();
                console.error('Error fetching products:', err);
                $resultsContainer.html(
                  '<p style="color:red;">Error loading products.</p>'
                );
            });
    }

    function renderProducts(products) {
        const $grid = $('<div>').addClass('products-grid');

        products.forEach(productDTO => {
            const id = productDTO.id;
            const name = productDTO.name;
            const price = productDTO.price;

            const $card = $('<div>').addClass('product-card');

            const $img = $('<img>')
                .attr('src', '/images/product-default.jpg')
                .attr('alt', name);

            const $productName = $('<div>')
                .addClass('product-name')
                .text(name);

            const $productPrice = $('<div>')
                .addClass('product-price')
                .text(`$${price}`);

            $card.on('click', function() {
                window.location.href = '/product/' + id;
            });

            $card.append($img, $productName, $productPrice);
            $grid.append($card);
        });

        $resultsContainer.append($grid);
    }

    function renderPagination(current, totalPages) {
        if (current > 0) {
            const $prev = $('<span>')
                .addClass('page-link')
                .text('Prev')
                .on('click', () => {
                    currentPage = currentPage - 1;
                    fetchProducts(currentPage);
                });
            $paginationContainer.append($prev);
        }

        for (let page = 0; page < totalPages; page++) {
            const $pageLink = $('<span>')
                .addClass('page-link')
                .text(page + 1)
                .on('click', () => {
                    currentPage = page;
                    fetchProducts(currentPage);
                });
            if (page === current) {
                $pageLink.addClass('active');
            }
            $paginationContainer.append($pageLink);
        }

        if (current < totalPages - 1) {
            const $next = $('<span>')
                .addClass('page-link')
                .text('Next')
                .on('click', () => {
                    currentPage = currentPage + 1;
                    fetchProducts(currentPage);
                });
            $paginationContainer.append($next);
        }
    }

    $searchButton.on('click', function() {
        currentPage = 0;
        fetchProducts(currentPage);
    });

    $filtersBrand.on('change', function() {
        currentPage = 0;
        fetchProducts(currentPage);
    });
    $filtersSupplier.on('change', function() {
        currentPage = 0;
        fetchProducts(currentPage);
    });
    $filtersCategory.on('change', function() {
        currentPage = 0;
        fetchProducts(currentPage);
    });

    $minPrice.on('change', function() {
        currentPage = 0;
        fetchProducts(currentPage);
    });
    $maxPrice.on('change', function() {
        currentPage = 0;
        fetchProducts(currentPage);
    });

    fetchProducts(currentPage);
});
