// Groovy service implementations for DurSaleOrderServices

def createSaleOrderWithItems() {
    // Create order
    def orderId = ec.service.sync().name("durion.sales.DurSaleOrderServices.create#SaleOrder")
            .parameters(context).call().orderId
    
    // Add items if provided
    if (context.items) {
        context.items.each { item ->
            ec.service.sync().name("durion.sales.DurSaleOrderServices.add#OrderItem")
                    .parameters([orderId: orderId] + item).call()
        }
    }
    
    // Calculate totals
    calculateOrderTotals(orderId)
    
    return [orderId: orderId]
}

def calculateOrderTotals(String orderId) {
    def order = ec.entity.find("durion.sales.DurSaleOrder")
            .condition("orderId", orderId).one()
    
    def items = ec.entity.find("durion.sales.DurSaleOrderItem")
            .condition("orderId", orderId).list()
    
    def totalAmount = 0.0
    def taxAmount = 0.0
    
    items.each { item ->
        totalAmount += item.lineAmount ?: 0.0
        taxAmount += item.taxAmount ?: 0.0
    }
    
    order.totalAmount = totalAmount
    order.taxAmount = taxAmount
    order.update()
}
