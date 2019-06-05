#!/bin/bash

kubectl create -f redis-master.yaml

sleep 5s # Waits 5 seconds.

kubectl create -f redis-sentinel-service.yaml

kubectl create -f redis-sentinel-controller.yaml

kubectl create -f redis-controller.yaml

kubectl scale rc redis --replicas 3
kubectl scale rc redis-sentinel --replicas 3 
