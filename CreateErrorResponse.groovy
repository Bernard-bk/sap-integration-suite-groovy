import com.sap.gateway.ip.core.customdev.util.Message
import groovy.json.JsonOutput

// Use in Exception Subprocess ending with End Message.
Message processData(Message message) {
    message.setHeader('CamelHttpResponseCode', 500)
    message.setHeader('Content-Type', 'application/json; charset=UTF-8')
    message.setBody(JsonOutput.toJson([
        error: 'INTEGRATION_ERROR',
        message: 'The message could not be processed.',
        correlationId: message.getProperty('CorrelationId') ?: ''
    ]))
    return message
}

