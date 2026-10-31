import com.sap.gateway.ip.core.customdev.util.Message
import groovy.json.JsonSlurper
import groovy.json.JsonOutput

// Run after ValidateOrder.
Message processData(Message message) {
    def order = new JsonSlurper().parseText(message.getBody(String))
    def result = [externalId: order.orderId, lines: order.items.collect { item ->
        [productCode: item.sku, quantity: item.quantity]
    }]
    message.setBody(JsonOutput.toJson(result))
    message.setHeader('Content-Type', 'application/json; charset=UTF-8')
    return message
}

