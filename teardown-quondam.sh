#!/bin/bash

kubectl scale deployment quondam-web --replicas 0

kubectl delete deployment quondam-web

kubectl delete service quondam-web
