#!/bin/bash

kubectl scale deployment payments-web --replicas 0

kubectl delete deployment payments-web

kubectl delete service payments-web
