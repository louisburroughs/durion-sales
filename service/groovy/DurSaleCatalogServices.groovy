// Groovy service implementations for DurSaleCatalogServices

def searchProductsAdvanced() {
    def searchTerm = context.searchTerm
    def categoryId = context.categoryId
    def minPrice = context.minPrice
    def maxPrice = context.maxPrice
    
    def finder = ec.entity.find("durion.sales.DurSaleProduct")
            .condition("isActive", "Y")
    
    if (searchTerm) {
        finder.condition(
            ec.entity.conditionFactory.makeCondition([
                ec.entity.conditionFactory.makeCondition("productName", "like", "%${searchTerm}%"),
                ec.entity.conditionFactory.makeCondition("description", "like", "%${searchTerm}%")
            ], "or")
        )
    }
    
    if (categoryId) {
        finder.condition("categoryId", categoryId)
    }
    
    if (minPrice) {
        finder.condition("price", ">=", minPrice)
    }
    
    if (maxPrice) {
        finder.condition("price", "<=", maxPrice)
    }
    
    def productList = finder.list()
    
    return [
        productList: productList,
        productListCount: productList.size()
    ]
}

def getProductWithInventory() {
    def productId = context.productId
    
    def product = ec.entity.find("durion.sales.DurSaleProduct")
            .condition("productId", productId).one()
    
    // Add inventory check logic here
    def inventoryStatus = [
        available: product?.quantityOnHand ?: 0,
        status: (product?.quantityOnHand ?: 0) > 0 ? "In Stock" : "Out of Stock"
    ]
    
    return [
        product: product,
        inventory: inventoryStatus
    ]
}
