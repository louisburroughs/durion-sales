// Durion Sales Component JavaScript

// Point of Sale functionality
const PointOfSale = {
    currentOrder: null,
    cartItems: [],
    
    init: function() {
        console.log('Point of Sale initialized');
        this.bindEvents();
    },
    
    bindEvents: function() {
        // Add product to cart
        document.querySelectorAll('.add-to-cart-btn').forEach(btn => {
            btn.addEventListener('click', (e) => {
                const productId = e.target.dataset.productId;
                this.addToCart(productId);
            });
        });
    },
    
    addToCart: function(productId) {
        // Add item to cart logic
        console.log('Adding product to cart:', productId);
        this.cartItems.push(productId);
        this.updateCartDisplay();
    },
    
    updateCartDisplay: function() {
        const cartCount = document.getElementById('cart-count');
        if (cartCount) {
            cartCount.textContent = this.cartItems.length;
        }
    },
    
    checkout: function() {
        // Process checkout
        console.log('Processing checkout for items:', this.cartItems);
    }
};

// Appointment Calendar functionality
const AppointmentCalendar = {
    selectedDate: null,
    
    init: function() {
        console.log('Appointment Calendar initialized');
        this.renderCalendar();
    },
    
    renderCalendar: function() {
        // Calendar rendering logic
        console.log('Rendering appointment calendar');
    },
    
    selectTimeSlot: function(date, time) {
        this.selectedDate = date;
        console.log('Selected time slot:', date, time);
    }
};

// Payment Terminal functionality
const PaymentTerminal = {
    init: function() {
        console.log('Payment Terminal initialized');
        this.setupPaymentForm();
    },
    
    setupPaymentForm: function() {
        const paymentForm = document.getElementById('payment-form');
        if (paymentForm) {
            paymentForm.addEventListener('submit', (e) => {
                e.preventDefault();
                this.processPayment();
            });
        }
    },
    
    processPayment: function() {
        // Payment processing logic
        const cardNumber = document.getElementById('card-number')?.value;
        const amount = document.getElementById('amount')?.value;
        
        console.log('Processing payment:', { cardNumber, amount });
        
        // TODO: Integrate with actual payment gateway
        this.showPaymentResult(true);
    },
    
    showPaymentResult: function(success) {
        const resultDiv = document.getElementById('payment-result');
        if (resultDiv) {
            resultDiv.textContent = success ? 'Payment Successful!' : 'Payment Failed';
            resultDiv.className = success ? 'alert-success' : 'alert-error';
        }
    }
};

// Initialize components when DOM is ready
document.addEventListener('DOMContentLoaded', function() {
    // Check which page we're on and initialize appropriate component
    const pagePath = window.location.pathname;
    
    if (pagePath.includes('PointOfSale')) {
        PointOfSale.init();
    } else if (pagePath.includes('AppointmentCalendar')) {
        AppointmentCalendar.init();
    } else if (pagePath.includes('PaymentTerminal')) {
        PaymentTerminal.init();
    }
});

// Export for use in other modules
if (typeof module !== 'undefined' && module.exports) {
    module.exports = { PointOfSale, AppointmentCalendar, PaymentTerminal };
}
