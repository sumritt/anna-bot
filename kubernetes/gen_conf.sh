#!/bin/bash

NAME="anna-config"

kubectl delete configmap $NAME
kubectl create configmap $NAME --from-file=../conf