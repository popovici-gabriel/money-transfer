#!/bin/bash

echo '--> About to test the REST API...'
echo '------------------------------------------'
echo '------------------------------------------'


curl -X POST \
  http://localhost:8080/accounts \
  -H 'Accept: */*' \
  -H 'Accept-Encoding: gzip, deflate' \
  -H 'Cache-Control: no-cache' \
  -H 'Connection: keep-alive' \
  -H 'Content-Length: 102' \
  -H 'Content-Type: application/json' \
  -H 'Host: localhost:8080' \
  -H 'Postman-Token: ba94ff81-b03b-4239-a046-52225c6b7e88,14d7a8b7-78fe-460e-ae81-076b2c365936' \
  -H 'User-Agent: PostmanRuntime/7.19.0' \
  -H 'cache-control: no-cache' \
  -d '{
  "accountId": 1,
  "userId": "GP",
  "balance": {
    "amount": 120.00,
    "currency": "USD"
  }
}
' | jq
curl -X POST \
  http://localhost:8080/accounts \
  -H 'Accept: */*' \
  -H 'Accept-Encoding: gzip, deflate' \
  -H 'Cache-Control: no-cache' \
  -H 'Connection: keep-alive' \
  -H 'Content-Length: 102' \
  -H 'Content-Type: application/json' \
  -H 'Host: localhost:8080' \
  -H 'Postman-Token: ba94ff81-b03b-4239-a046-52225c6b7e88,14d7a8b7-78fe-460e-ae81-076b2c365936' \
  -H 'User-Agent: PostmanRuntime/7.19.0' \
  -H 'cache-control: no-cache' \
  -d '{
  "accountId": 2,
  "userId": "GP",
  "balance": {
    "amount": 20.00,
    "currency": "USD"
  }
}
' | jq

curl -X POST \
  http://localhost:8080/transfer/1/2/10 \
  -H 'Accept: */*' \
  -H 'Accept-Encoding: gzip, deflate' \
  -H 'Cache-Control: no-cache' \
  -H 'Connection: keep-alive' \
  -H 'Content-Length: 102' \
  -H 'Content-Type: application/json' \
  -H 'Host: localhost:8080' \
  -H 'Postman-Token: 4c7f4879-4ea5-4437-8838-1810ecbb5893,351599fa-08ba-4bd3-9cee-af53e8d79792' \
  -H 'User-Agent: PostmanRuntime/7.19.0' \
  -H 'cache-control: no-cache' | jq


curl -X GET \
  http://localhost:8080/accounts/1 \
  -H 'Accept: */*' \
  -H 'Accept-Encoding: gzip, deflate' \
  -H 'Cache-Control: no-cache' \
  -H 'Connection: keep-alive' \
  -H 'Host: localhost:8080' \
  -H 'Postman-Token: 35205acd-b6a7-41d2-87ac-d66c61186ca3,45d72db4-0d82-4097-9fc9-8a69e04d5f71' \
  -H 'User-Agent: PostmanRuntime/7.19.0' \
  -H 'cache-control: no-cache' | jq


curl -X GET \
  http://localhost:8080/accounts/2 \
  -H 'Accept: */*' \
  -H 'Accept-Encoding: gzip, deflate' \
  -H 'Cache-Control: no-cache' \
  -H 'Connection: keep-alive' \
  -H 'Host: localhost:8080' \
  -H 'Postman-Token: 35205acd-b6a7-41d2-87ac-d66c61186ca3,45d72db4-0d82-4097-9fc9-8a69e04d5f71' \
  -H 'User-Agent: PostmanRuntime/7.19.0' \
  -H 'cache-control: no-cache' | jq

echo '------------------------------------------'
echo '----------------THE END-------------------'