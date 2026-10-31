import com.sap.gateway.ip.core.customdev.util.Message
import java.util.UUID

Message processData(Message message) {
    String id = message.getHeader('X-Correlation-ID', String)?.trim()
    if (!id) id = UUID.randomUUID().toString()
    message.setHeader('X-Correlation-ID', id)
    message.setProperty('CorrelationId', id)
    return message
}

