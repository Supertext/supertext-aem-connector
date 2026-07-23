# Supertext AEM Connector

A translation connector for [Adobe Experience Manager](https://business.adobe.com/products/experience-manager/adobe-experience-manager.html) (AEM) 6.5. It integrates the [Supertext](https://www.supertext.com) translation services directly into AEM's Translation Cloud Services, so editors can order human translations for their content without leaving AEM.

## Repository layout

| Path | Description |
| --- | --- |
| [`aem-6.5-connector/`](aem-6.5-connector) | The connector source — a Maven multi-module project (`core`, `ui.apps`, `ui.content`). |
| [`docs/`](docs) | Installation and usage documentation, with screenshots. |

## Documentation

- [Installation guide](docs/installation.md) — installing the connector packages and creating a connector configuration.
- [Usage guide](docs/usage.md) — ordering, scoping, running and reviewing a translation job from within AEM.

## Building

The connector is built with Maven from the [`aem-6.5-connector/`](aem-6.5-connector) directory:

```bash
cd aem-6.5-connector
mvn clean install
```

This produces two AEM content packages:

- `ui.content/target/supertext-connector.ui.content-2.5.zip`
- `ui.apps/target/supertext-connector.ui.apps-2.5.zip`

Install both through the AEM Package Manager as described in the [installation guide](docs/installation.md).

## License

Licensed under the Apache License, Version 2.0. See [`aem-6.5-connector/LICENSE.txt`](aem-6.5-connector/LICENSE.txt).
