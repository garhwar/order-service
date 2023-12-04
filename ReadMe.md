# Build and deploy
`mvn clean install`

`docker build -t order-service-image:v1 .`

`kubectl apply -f ./build/kube/secrets.yaml`

`kubectl apply -f ./build/kube/service.yaml`

`kubectl apply -f ./build/kube/deployment.yaml`

# Create an order
`curl --location 'http://localhost:<node_port>/api/v1/orders' \
--header 'Content-Type: application/json' \
--header 'Cookie: JSESSIONID=FBA2401B41EC131FBC6A1FE5DEA3B8D6' \
--data '{
"userId": 1,
"orderProducts": [
{
"productId": 1,
"quantity": 2
}
],
"orderDate": "2023-12-01",
"totalAmount": 1000,
"status": "CREATED"
}'`