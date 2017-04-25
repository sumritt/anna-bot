#!/bin/bash

SECRET_NAME="anna-knows"
AUTH_TOKEN=""
KEYSTORE_PASSWORD=""
ANNA_PANDORA_ID=""
ANNA_PANDORA_KEY=""

kubectl delete secret $SECRET_NAME
kubectl create secret generic $SECRET_NAME \
    --from-literal=token=$AUTH_TOKEN \
    --from-literal=keystore_password=$KEYSTORE_PASSWORD \
    --from-literal=pandora_key=$ANNA_PANDORA_KEY \
    --from-literal=pandora_app_id=$ANNA_PANDORA_ID