#!/bin/bash
 
kubectl run payments-web "--image=ajw/payments:1.0-SNAPSHOT"  "--port=8080"

sleep 5s # Waits 5 seconds

