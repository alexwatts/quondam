#!/bin/bash

kubectl scale deployment quondam-nft --replicas 0

kubectl delete deployment quondam-nft
