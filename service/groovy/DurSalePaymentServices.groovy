// Groovy service implementations for DurSalePaymentServices

def processCreditCardPayment() {
    def invoiceId = context.invoiceId
    def amount = context.amount
    def cardNumber = context.cardNumber
    def expirationDate = context.expirationDate
    def cvv = context.cvv
    
    // TODO: Integrate with actual payment gateway (Stripe, Square, etc.)
    // This is a stub implementation
    
    try {
        // Simulate payment gateway call
        def transactionId = UUID.randomUUID().toString()
        def authCode = "AUTH" + (new Random().nextInt(999999)).toString().padLeft(6, '0')
        
        // For now, simulate successful payment
        def paymentResult = [
            success: true,
            transactionId: transactionId,
            authorizationCode: authCode,
            status: "completed",
            message: "Payment processed successfully"
        ]
        
        if (paymentResult.success) {
            // Record the payment
            def paymentId = ec.service.sync().name("durion.sales.DurSalePaymentServices.record#Payment")
                    .parameters([
                        invoiceId: invoiceId,
                        amount: amount,
                        paymentMethod: "credit_card",
                        transactionId: transactionId,
                        status: paymentResult.status,
                        authorizationCode: authCode,
                        cardLast4: cardNumber.substring(cardNumber.length() - 4)
                    ]).call().paymentId
            
            // Update invoice paid amount
            updateInvoicePaidAmount(invoiceId, amount)
            
            return [
                paymentId: paymentId,
                transactionId: transactionId,
                status: paymentResult.status,
                message: paymentResult.message
            ]
        } else {
            return [
                status: "failed",
                message: paymentResult.message ?: "Payment processing failed"
            ]
        }
        
    } catch (Exception e) {
        ec.logger.error("Payment processing error", e)
        return [
            status: "error",
            message: "Payment processing error: ${e.message}"
        ]
    }
}

def updateInvoicePaidAmount(String invoiceId, BigDecimal amount) {
    def invoice = ec.entity.find("durion.sales.DurSaleInvoice")
            .condition("invoiceId", invoiceId).one()
    
    def currentPaid = invoice.paidAmount ?: 0.0
    invoice.paidAmount = currentPaid + amount
    invoice.balanceDue = invoice.totalAmount - invoice.paidAmount
    
    if (invoice.balanceDue <= 0) {
        invoice.status = "paid"
    } else {
        invoice.status = "partial"
    }
    
    invoice.update()
}
