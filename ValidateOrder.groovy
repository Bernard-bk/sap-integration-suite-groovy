import com.sap.gateway.ip.core.customdev.util.Message
import groovy.json.JsonSlurper

Message processData(Message message) {
    def order = new JsonSlurper().parseText(message.getBody(String))
    if (!(order instanceof Map)) throw new IllegalArgumentException('Expected JSON object')
    if (!(order.orderId instanceof String) || !order.orderId.trim())
        throw new IllegalArgumentException('orderId must be a non-empty string')
    if (!(order.items instanceof List) || order.items.isEmpty())
        throw new IllegalArgumentException('items must be a non-empty array')
    order.items.each { item ->
        if (!(item instanceof Map) || !(item.sku instanceof String) || !item.sku.trim() ||
            !(item.quantity instanceof Number) || item.quantity <= 0 ||
            new BigDecimal(item.quantity.toString()).stripTrailingZeros().scale() > 0)
            throw new IllegalArgumentException('Expected sku and positive integer quantity')
    }
    message.setProperty('OrderId', order.orderId)
    message.setProperty('ItemCount', order.items.size())
    return message
}

