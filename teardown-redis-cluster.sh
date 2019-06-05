#!/bin/bash

kubectl scale rc redis --replicas 0
kubectl scale rc redis-sentinel --replicas 0 
kubectl delete rc redis
kubectl delete rc redis-sentinel
kubectl delete service redis-sentinel
