#!/bin/bash
 
kubectl run payments-web "--image=eu.gcr.io/quondam/payments:1.0-SNAPSHOT"  "--port=8080"

sleep 5s # Waits 5 seconds

kubectl expose deployment payments-web --type=NodePort

