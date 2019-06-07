#!/bin/bash

export KUBE_TOKEN=$(cat /var/run/secrets/kubernetes.io/serviceaccount/token)

export REDIS_PODS=$(curl -sSk -H "Authorization: Bearer $KUBE_TOKEN" https://$KUBERNETES_SERVICE_HOST:$KUBERNETES_PORT_443_TCP_PORT/api/v1/namespaces/default/pods/ | jq -r .items[].metadata.name | fgrep redis | fgrep -v sentinel)

echo $REDIS_PODS