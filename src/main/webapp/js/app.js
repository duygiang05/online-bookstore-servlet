/**
 * BookNest UI polish — no business logic / no form action changes.
 */
(function () {
    'use strict';

    var reduceMotion = window.matchMedia('(prefers-reduced-motion: reduce)').matches;

    function onReady(fn) {
        if (document.readyState !== 'loading') {
            fn();
        } else {
            document.addEventListener('DOMContentLoaded', fn);
        }
    }

    function initHeaderScroll() {
        var header = document.querySelector('.site-header');
        if (!header) return;

        var ticking = false;
        function update() {
            header.classList.toggle('is-scrolled', window.scrollY > 8);
            ticking = false;
        }

        window.addEventListener('scroll', function () {
            if (!ticking) {
                window.requestAnimationFrame(update);
                ticking = true;
            }
        }, { passive: true });
        update();
    }

    function markRevealTargets() {
        var selectors = [
            '.book-card',
            '.admin-item',
            '.order-card',
            '.dashboard-card',
            '.cart-item',
            '.form-card',
            '.search-bar',
            '.detail-layout',
            '.profile-card',
            '.store-masthead',
            '.login-card'
        ];

        selectors.forEach(function (sel) {
            document.querySelectorAll(sel).forEach(function (el) {
                if (!el.classList.contains('reveal')) {
                    el.classList.add('reveal');
                }
            });
        });

        document.querySelectorAll('.book-grid, .admin-list, .order-list, .dashboard-grid, .cart-list')
            .forEach(function (el) {
                el.classList.add('stagger');
            });
    }

    function initReveal() {
        markRevealTargets();
        var nodes = document.querySelectorAll('.reveal');
        if (!nodes.length) return;

        if (reduceMotion || !('IntersectionObserver' in window)) {
            nodes.forEach(function (el) {
                el.classList.add('is-visible');
            });
            return;
        }

        var observer = new IntersectionObserver(function (entries) {
            entries.forEach(function (entry) {
                if (entry.isIntersecting) {
                    entry.target.classList.add('is-visible');
                    observer.unobserve(entry.target);
                }
            });
        }, {
            rootMargin: '0px 0px -40px 0px',
            threshold: 0.08
        });

        nodes.forEach(function (el) {
            observer.observe(el);
        });
    }

    function initRipple() {
        if (reduceMotion) return;

        document.addEventListener('click', function (e) {
            var btn = e.target.closest('button, .btn, a.btn-outline, a.btn-primary');
            if (!btn || btn.disabled) return;

            var rect = btn.getBoundingClientRect();
            var size = Math.max(rect.width, rect.height);
            var ripple = document.createElement('span');
            ripple.className = 'btn-ripple';
            ripple.style.width = size + 'px';
            ripple.style.height = size + 'px';
            ripple.style.left = (e.clientX - rect.left - size / 2) + 'px';
            ripple.style.top = (e.clientY - rect.top - size / 2) + 'px';
            btn.appendChild(ripple);
            window.setTimeout(function () {
                ripple.remove();
            }, 600);
        });
    }

    function initNavActive() {
        var path = window.location.pathname.replace(/\/+$/, '');
        document.querySelectorAll('.nav-links a').forEach(function (link) {
            try {
                var href = link.getAttribute('href');
                if (!href || href === '#' || href.indexOf('logout') !== -1) return;
                var linkPath = new URL(href, window.location.origin).pathname.replace(/\/+$/, '');
                if (path === linkPath || (linkPath !== '' && path.indexOf(linkPath) === 0 && linkPath.length > 1)) {
                    link.style.borderColor = 'var(--border-strong)';
                    link.style.background = 'var(--primary-soft)';
                    link.style.color = 'var(--primary-dark)';
                }
            } catch (err) {
                /* ignore bad href */
            }
        });
    }

    function initImageFade() {
        document.querySelectorAll('.book-card__cover img, .detail-cover img, .cart-item__cover img')
            .forEach(function (img) {
                if (img.complete) {
                    img.style.opacity = '1';
                    return;
                }
                img.style.opacity = '0';
                img.style.transition = 'opacity 0.4s ease';
                img.addEventListener('load', function () {
                    img.style.opacity = '1';
                });
                img.addEventListener('error', function () {
                    img.style.opacity = '1';
                });
            });
    }

    function initQtyPickers() {
        document.querySelectorAll('[data-qty-picker]').forEach(function (picker) {
            var input = picker.querySelector('input[type="number"]');
            if (!input) return;

            picker.querySelectorAll('[data-qty-delta]').forEach(function (btn) {
                btn.addEventListener('click', function () {
                    var delta = parseInt(btn.getAttribute('data-qty-delta'), 10) || 0;
                    var min = parseInt(input.getAttribute('min'), 10);
                    var max = parseInt(input.getAttribute('max'), 10);
                    if (isNaN(min)) min = 1;
                    var current = parseInt(input.value, 10);
                    if (isNaN(current) || current < min) current = min;
                    var next = current + delta;
                    if (next < min) next = min;
                    if (!isNaN(max) && next > max) next = max;
                    input.value = String(next);
                    input.dispatchEvent(new Event('change', { bubbles: true }));
                    input.dispatchEvent(new Event('input', { bubbles: true }));
                });
            });
        });
    }

    function syncBuyQuantityForms() {
        var panel = document.querySelector('[data-buy-panel]');
        if (!panel) return;
        var master = panel.querySelector('#buyQuantity');
        if (!master) return;

        function sync() {
            var value = master.value || '1';
            panel.querySelectorAll('form[data-sync-qty] input[name="quantity"]').forEach(function (input) {
                input.value = value;
            });
        }

        master.addEventListener('change', sync);
        master.addEventListener('input', sync);
        panel.querySelectorAll('[data-qty-delta]').forEach(function (btn) {
            btn.addEventListener('click', function () {
                window.setTimeout(sync, 0);
            });
        });
        sync();
    }

    function formatVnd(amount) {
        try {
            return new Intl.NumberFormat('vi-VN').format(Math.round(amount)) + ' đ';
        } catch (e) {
            return Math.round(amount) + ' đ';
        }
    }

    function initCartCheckoutSelection() {
        var form = document.getElementById('cartCheckoutForm');
        if (!form) return;

        var selectAll = document.getElementById('selectAllCart');
        var totalEl = document.getElementById('cartSelectedTotal');
        var submitBtn = document.getElementById('cartCheckoutBtn');

        function rows() {
            return Array.prototype.slice.call(document.querySelectorAll('.cart-item--selectable'));
        }

        function updateTotal() {
            var total = 0;
            var checkedCount = 0;
            rows().forEach(function (row) {
                var check = row.querySelector('.cart-select');
                var qty = row.querySelector('.cart-buy-qty');
                if (!check || !qty || !check.checked) return;
                checkedCount += 1;
                var price = parseFloat(qty.getAttribute('data-price')) || 0;
                var quantity = parseInt(qty.value, 10) || 0;
                total += price * quantity;
            });
            if (totalEl) {
                totalEl.textContent = formatVnd(total);
            }
            if (submitBtn) {
                submitBtn.disabled = checkedCount === 0;
            }
            if (selectAll) {
                var all = document.querySelectorAll('.cart-select');
                var checked = document.querySelectorAll('.cart-select:checked');
                selectAll.checked = all.length > 0 && all.length === checked.length;
            }
        }

        if (selectAll) {
            selectAll.addEventListener('change', function () {
                document.querySelectorAll('.cart-select').forEach(function (cb) {
                    cb.checked = selectAll.checked;
                });
                updateTotal();
            });
        }

        document.addEventListener('change', function (e) {
            if (e.target && (e.target.classList.contains('cart-select')
                || e.target.classList.contains('cart-buy-qty'))) {
                updateTotal();
            }
        });
        document.addEventListener('input', function (e) {
            if (e.target && e.target.classList.contains('cart-buy-qty')) {
                updateTotal();
            }
        });

        form.addEventListener('submit', function (e) {
            var checked = document.querySelectorAll('.cart-select:checked');
            if (!checked.length) {
                e.preventDefault();
                window.alert('Vui lòng chọn ít nhất một sách để thanh toán.');
            }
        });

        updateTotal();
    }

    function initAdminFormValidation() {
        document.querySelectorAll('form.admin-form').forEach(function (form) {
            form.addEventListener('submit', function (e) {
                if (!form.checkValidity()) {
                    e.preventDefault();
                    form.classList.add('was-validated');
                    var firstInvalid = form.querySelector(':invalid');
                    if (firstInvalid && typeof firstInvalid.focus === 'function') {
                        firstInvalid.focus();
                    }
                }
            });

            form.querySelectorAll('input, select, textarea').forEach(function (field) {
                field.addEventListener('input', function () {
                    if (field.checkValidity()) {
                        field.classList.remove('field-invalid');
                    }
                });
            });
        });
    }

    onReady(function () {
        document.documentElement.classList.add('js-ready');
        initHeaderScroll();
        initReveal();
        initRipple();
        initNavActive();
        initImageFade();
        initQtyPickers();
        syncBuyQuantityForms();
        initCartCheckoutSelection();
        initAdminFormValidation();
    });
})();
