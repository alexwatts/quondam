#!/bin/bash

kubectl port-forward svc/redis-sentinel 26379:26379

kubectl expose deployment bond-web --type=NodePort