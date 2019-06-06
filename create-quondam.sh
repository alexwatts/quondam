#!/bin/bash
 
kubectl run quondam-web "--image=ajw/quondam:1.0-SNAPSHOT"  "--port=8080"

sleep 5s # Waits 5 seconds

kubectl expose deployment quondam-web --type=NodePort

