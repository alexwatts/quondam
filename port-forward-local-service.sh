#!/bin/bash

kubectl port-forward svc/redis-sentinel 26379:26379

kubectl port-forward svc/bond-web 8080:8080