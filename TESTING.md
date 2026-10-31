# Setup and verification

## Prerequisites

A Cloud Integration test tenant, permission to deploy and call an HTTPS iFlow, and Script 1.x compatibility. Follow [the deployment outline](ARCHITECTURE.md). No SAP credentials are bundled.

## Happy path

Send order.json as application/json to your deployed endpoint. Supply an X-Correlation-ID such as demo-1001.

Expected:
- Response body is structurally equal to mapped-order.json.
- Correlation ID is preserved.
- OrderId property is DEMO-1001 and ItemCount is 2.
- Content-Type is application/json; charset=UTF-8.

Compare parsed JSON objects, not raw whitespace or key order.

## Test matrix

All scenarios below are planned checks, not recorded successful executions.

| Case | Input/change | Expected behavior |
| --- | --- | --- |
| Valid order | order.json | Mapped JSON |
| Generated correlation | Omit X-Correlation-ID | Non-empty UUID header/property |
| Retained correlation | X-Correlation-ID demo-1001 | Same ID |
| Empty identifier | orderId empty or whitespace | Validation failure |
| Wrong identifier type | orderId 123 | Validation failure |
| No items | items [] or missing | Validation failure |
| Invalid SKU | sku empty, null or non-string | Validation failure |
| Invalid quantity | 0, negative, 1.5, string or null | Validation failure |
| Invalid item | items contains null or a string | Validation failure |
| Root array | [] | Validation failure |
| Malformed body | Invalid JSON syntax | Parser failure |
| Extra fields | Add unused fields | Only mapped fields in response |
| Handled exception | Trigger validation failure | Generic error body and HTTP 500 |
| Information exposure | Trigger parser failure | No payload/stack trace in response |

## Evidence to capture

Record tenant/runtime version, deployed artifact version, date, sanitized request and response, HTTP status, correlation ID and observed MPL status. Mark cases PASS/FAIL only after execution.

Do not attach credentials, authorization headers, customer payloads or unrestricted monitoring exports.

## Known validation boundaries

No maximum field lengths, currency, dates, unique SKU requirement or schema validation are enforced. Integer-valued JSON numbers such as 2.0 are accepted by the quantity check. Large message and performance testing remain outstanding.

