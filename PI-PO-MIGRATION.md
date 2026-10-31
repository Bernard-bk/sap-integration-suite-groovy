# PI/PO to SAP Integration Suite — design study

This is a hypothetical migration assessment, not evidence of an executed customer migration.

## Example to assess

An existing PI/PO synchronous order interface receives a request, applies validation and mapping, and calls a business receiver. The target design uses Cloud Integration for orchestration and mapping. The repository demonstrates only the script layer.

| PI/PO area | Candidate Cloud Integration concern | Review point |
| --- | --- | --- |
| ESR data/message types and service interfaces | Message contracts and schemas | Preserve namespaces, cardinality, optional fields and faults |
| Message mapping / UDF / Java mapping | Graphical mapping, standard steps or Groovy | Select by maintainability; verify context/queue semantics and dependencies |
| Integration Directory / ICO routing | iFlow routing and externalized configuration | Recreate conditions and receiver determination deliberately |
| Communication channels | Sender/receiver adapters | Validate actual adapter support, QoS, authentication and limits |
| Value mapping | Value Mapping artifact or approved configuration | Ownership, transport and missing-value behavior |
| Monitoring and reprocessing | MPL, alerting and operational process | Retention, correlation, retry ownership and business reconciliation |
| EO / EOIO requirements | Persistence, idempotency and ordering design | Do not assume a direct equivalence from adapter names |

These are design correspondences, not one-to-one migration guarantees.

## Assessment sequence

1. Inventory interfaces, dependencies, traffic, SLAs and business owners.
2. Use SAP Migration Assessment to evaluate the actual source landscape.
3. Review Migration Tooling coverage and limitations separately from assessment readiness.
4. Select a low-risk pilot and preserve a baseline of representative requests/responses.
5. Rebuild unsupported behavior explicitly; avoid carrying unnecessary custom code forward.
6. Test happy paths, faults, namespaces, ordering, duplicates, peak load and credential expiry.
7. Define cutover, rollback criteria, reconciliation and support ownership.

## Acceptance gates

- Equivalent business output on agreed golden payloads.
- Confirmed fault contract and agreed monitoring behavior.
- Measured latency/throughput under representative traffic.
- Evidence for duplicate handling and ordering where required.
- Approved security material, networking and least-privilege access.
- Operational runbook and rollback rehearsal.

## Discussion example

For a synchronous order create call, the important question is what happens when the receiver commits the order but the response is lost. Neither moving the mapping to Groovy nor changing an adapter solves that problem. The target needs an agreed idempotency and reconciliation strategy.

## Official references

- [SAP Migration Tooling](https://help.sap.com/docs/SAP_INTEGRATION_SUITE/9519789d5664487f8b9cd89eba514477/what-is-migration-tooling)
- [Assessment concepts](https://help.sap.com/docs/integration-suite/sap-integration-suite/concepts)
- [SAP migration tutorial](https://developers.sap.com/tutorials/migration-tool/)

