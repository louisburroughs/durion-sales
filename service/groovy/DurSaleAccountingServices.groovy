// Groovy service implementations for DurSaleAccountingServices

def syncInvoiceToAccounting() {
    def invoiceId = context.invoiceId
    
    def invoice = ec.entity.find("durion.sales.DurSaleInvoice")
            .condition("invoiceId", invoiceId).one()
    
    if (!invoice) {
        return [syncStatus: "failed", message: "Invoice not found"]
    }
    
    try {
        // TODO: Integrate with accounting system API
        // This is a stub implementation
        
        def accountingPayload = [
            invoiceNumber: invoice.invoiceNumber,
            invoiceDate: invoice.invoiceDate,
            customerId: invoice.customerId,
            customerName: invoice.customerName,
            totalAmount: invoice.totalAmount,
            status: invoice.status
        ]
        
        // Simulate accounting system call
        def accountingInvoiceId = "ACC-" + invoice.invoiceId
        
        // Update invoice with accounting reference
        ec.service.sync().name("durion.sales.DurSaleAccountingServices.update#InvoiceAccountingStatus")
                .parameters([
                    invoiceId: invoiceId,
                    accountingStatus: "synced"
                ]).call()
        
        ec.logger.info("Invoice ${invoiceId} synced to accounting system as ${accountingInvoiceId}")
        
        return [
            accountingInvoiceId: accountingInvoiceId,
            syncStatus: "success"
        ]
        
    } catch (Exception e) {
        ec.logger.error("Accounting sync error for invoice ${invoiceId}", e)
        
        ec.service.sync().name("durion.sales.DurSaleAccountingServices.update#InvoiceAccountingStatus")
                .parameters([
                    invoiceId: invoiceId,
                    accountingStatus: "sync_failed"
                ]).call()
        
        return [
            syncStatus: "failed",
            message: e.message
        ]
    }
}

def handleAccountingWebhook() {
    // Handle webhooks from accounting system for invoice status updates
    def webhookData = context.webhookData
    def invoiceId = webhookData.invoiceId
    def newStatus = webhookData.status
    
    ec.service.sync().name("durion.sales.DurSaleAccountingServices.update#InvoiceAccountingStatus")
            .parameters([
                invoiceId: invoiceId,
                accountingStatus: newStatus
            ]).call()
    
    return [success: true]
}
