#!/bin/bash

pods=( $(kubectl get pods -o jsonpath="{.items[*].metadata.name}" | tr -s '[[:space:]]' '\n' | fgrep redis | fgrep -v sentinel) )
randompod=${pods[$RANDOM % ${#pods[@]} ]}
kubectl delete pod ${randompod}

