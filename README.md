# Stocker

A stock market desktop client for the [Finnhub](https://finnhub.io) API - built as a university project (FernUniversität in Hagen, 2022).

<img width="1669" alt="stocker_screenshot" src="https://user-images.githubusercontent.com/75016494/161539467-e3c54627-3690-4b9d-82c7-bdab2f2afb33.png">

## Features

- Real-time quotes via WebSocket push, historical data via REST pull
- Candlestick and line charts with indicators (Simple Moving Average, Bollinger Bands)
- Watchlist and configurable price alarms
- Persistent settings, data provider profiles and window layout

## Tech

- Java with Swing, MVC architecture with an observer-based model layer
- Gson with custom serializers/deserializers, java-websocket

Source lives in `Stocker_sourcecode/src`.
