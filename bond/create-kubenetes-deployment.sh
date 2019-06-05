#!/bin/bash
 
kubectl run bond-web "--image=ajw/bond:1.0-SNAPSHOT"  "--port=8080"

sleep 5s # Waits 5 seconds

