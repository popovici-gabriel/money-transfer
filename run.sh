#!/bin/bash

echo '--> About to test the REST API...'
echo '------------------------------------------'
echo '------------------------------------------'

echo '{
  "accountId": 1,
  "userId": "GP",
  "balance": {
    "amount": 100.00,
    "currency": "USD"
  }
}
' |  \
  http POST http://localhost:8080/accounts \
  Accept:'*/*' \
  Accept-Encoding:'gzip, deflate' \
  Cache-Control:no-cache \
  Connection:keep-alive \
  Content-Length:102 \
  Content-Type:application/json \
  Host:localhost:8080 \
  Postman-Token:'2b3aa6aa-6c15-44b4-94c3-013b20f54ab0,0dce41ea-db3b-4dc7-9bb8-47cfac3e142a' \
  User-Agent:PostmanRuntime/7.19.0 \
  cache-control:no-cache


echo '{
  "accountId": 2,
  "userId": "GP",
  "balance": {
    "amount": 50.00,
    "currency": "USD"
  }
}
' |  \
  http POST http://localhost:8080/accounts \
  Accept:'*/*' \
  Accept-Encoding:'gzip, deflate' \
  Cache-Control:no-cache \
  Connection:keep-alive \
  Content-Length:102 \
  Content-Type:application/json \
  Host:localhost:8080 \
  Postman-Token:'2b3aa6aa-6c15-44b4-94c3-013b20f54ab0,0dce41ea-db3b-4dc7-9bb8-47cfac3e142a' \
  User-Agent:PostmanRuntime/7.19.0 \
  cache-control:no-cache

  http GET http://localhost:8080/accounts/1 \
  Accept:'*/*' \
  Accept-Encoding:'gzip, deflate' \
  Cache-Control:no-cache \
  Connection:keep-alive \
  Host:localhost:8080 \
  Postman-Token:'e13a7057-7c42-471a-801c-46ccf988e419,c163fc3c-6c5b-4afb-a46c-2ca0b40e59f8' \
  User-Agent:PostmanRuntime/7.19.0 \
  cache-control:no-cache

  http GET http://localhost:8080/accounts/2 \
  Accept:'*/*' \
  Accept-Encoding:'gzip, deflate' \
  Cache-Control:no-cache \
  Connection:keep-alive \
  Host:localhost:8080 \
  Postman-Token:'e13a7057-7c42-471a-801c-46ccf988e419,c163fc3c-6c5b-4afb-a46c-2ca0b40e59f8' \
  User-Agent:PostmanRuntime/7.19.0 \
  cache-control:no-cache

  http POST http://localhost:8080/transfer/1/2/50

 http GET http://localhost:8080/accounts/2 \
  Accept:'*/*' \
  Accept-Encoding:'gzip, deflate' \
  Cache-Control:no-cache \
  Connection:keep-alive \
  Host:localhost:8080 \
  Postman-Token:'e13a7057-7c42-471a-801c-46ccf988e419,c163fc3c-6c5b-4afb-a46c-2ca0b40e59f8' \
  User-Agent:PostmanRuntime/7.19.0 \
  cache-control:no-cache
echo '------------------------------------------'
echo '----------------THE END-------------------'