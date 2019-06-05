#!/bin/bash

kubectl scale deployment bond-web --replicas 0

kubectl delete deployment bond-web

kubectl delete service bond-web
