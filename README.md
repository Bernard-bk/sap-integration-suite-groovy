# SAP Integration Suite – przykłady Groovy

Przykłady dla Cloud Integration z klasycznym API
`com.sap.gateway.ip.core.customdev.util.Message` (Script 1.x).
Script 2.x wymaga dostosowania importów i sprawdzenia zgodności.

## Skrypty

- SetCorrelationId: zachowuje X-Correlation-ID lub generuje UUID; ustawia property CorrelationId.
- ValidateOrder: sprawdza JSON zamówienia, ustawia OrderId i ItemCount, zachowuje body.
- MapOrderToJson: mapuje poprawne zamówienie do formatu odbiorcy.
- CreateErrorResponse: ogólna odpowiedź HTTP 500 w Exception Subprocess.

## Użycie

1. Utwórz testowy iFlow z nadawcą HTTPS.
2. Dodaj pliki .groovy jako zasoby iFlow i wybierz w krokach Groovy Script.
3. Kolejność: SetCorrelationId → ValidateOrder → MapOrderToJson.
4. Pozostaw domyślną funkcję processData.
5. W Exception Subprocess dodaj CreateErrorResponse i zakończenie End Message,
   jeśli chcesz zwracać ogólną odpowiedź błędu. Przykład zwraca 500 dla wszystkich
   wyjątków; rozróżnienie walidacji i błędów systemowych wymaga osobnej konfiguracji.
6. Wyślij order.json z Content-Type application/json do endpointu iFlow
   z uwierzytelnieniem skonfigurowanym na swoim tenantcie.
7. Porównaj wynik z mapped-order.json (kolejność kluczy bez znaczenia).

## Sprawdzenie na tenantcie

- Poprawny przykład: dwa elementy, OrderId DEMO-1001, wynik jak mapped-order.json.
- Brak X-Correlation-ID: nowy UUID; podany identyfikator zostaje zachowany.
- Puste orderId, puste items, quantity 0 lub 1.5: wyjątek walidacji.
- Niepoprawny JSON: wyjątek parsowania.
- Odpowiedź błędu nie ujawnia payloadu ani szczegółów wyjątku.

Skrypty wczytują cały JSON do pamięci: stosuj do małych wiadomości.
Nie zawierają danych dostępowych i nie zapisują payloadu do logów.
Nie były uruchamiane na tenantcie SAP; wymagają sprawdzenia w środowisku testowym.

## Dokumentacja

[SAP: Message body, headers, properties i processData](https://help.sap.com/docs/integration-suite/sap-integration-suite/read-and-modify-message-header-message-body-and-exchange-properties)


