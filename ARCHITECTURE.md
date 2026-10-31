# Architecture and operational decisions

## Scope

Synchronous HTTPS order transformation in SAP Cloud Integration. The implementation covers the script layer; adapter configuration must be created in a test tenant.

| Concern | Demo behavior | Production decision still required |
| --- | --- | --- |
| Contract | orderId string; non-empty items; sku string; positive integer quantity | Schema, maximum lengths, item count and payload limits |
| Correlation | Reuse X-Correlation-ID or generate UUID | Validate inbound IDs and propagate a trusted trace context |
| Mapping | Explicit field selection; extra fields dropped | Versioning and backward compatibility |
| Errors | Generic JSON, HTTP 500 | Classify caller failures vs system failures |
| Observability | Correlation header and property | Searchable MPL metadata, retention, alerting and ownership |
| Delivery | No receiver call | Receiver authentication, timeouts and reconciliation |
| Duplicates | Not handled | Stable business key and atomic idempotency policy |
| Retries | Not implemented | Retry transient failures only; bound attempts and backoff |

## Deployment outline

1. Create an integration package and a test iFlow with an HTTPS sender.
2. Add each .groovy file as a script resource.
3. Add Groovy Script steps in this order: SetCorrelationId, ValidateOrder, MapOrderToJson.
4. Leave the function at processData.
5. Add an Exception Subprocess containing CreateErrorResponse and an End Message event.
6. Deploy to the test tenant and use its generated endpoint with authorized test credentials.
7. Verify the response body, X-Correlation-ID and message processing log.
8. Keep credentials in SAP security material; do not commit them.

For an outbound receiver, explicitly configure transport headers. An inbound header must not silently determine the receiver URL or credentials.

## Error handling tradeoff

The demonstration returns HTTP 500 for every caught exception. A real API should distinguish malformed input from downstream/system failures using explicit error categories. Avoid classifying every IllegalArgumentException as a client error: dependencies can throw the same exception type.

An End Message event can return a handled error to the caller. Agree separately on the required monitoring status and alerting behavior; handling an exception is not the same as resolving the business failure.

## Delivery semantics

A timeout can occur after a receiver accepted an order. Blindly retrying can create duplicates. Define a business key, retention window, atomic duplicate check, receiver idempotency support and reconciliation procedure before enabling retries.

## Data and memory

No payload logging is implemented. Limit body size at a suitable entry layer and test resource consumption. JSON parsing creates an in-memory representation; stream-friendly parsing is a separate design choice.

## SAP references

- [Message API usage](https://help.sap.com/docs/integration-suite/sap-integration-suite/read-and-modify-message-header-message-body-and-exchange-properties)

