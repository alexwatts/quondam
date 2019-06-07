#!/bin/bash

PODS=$(./get-redis-pods.sh )

IFS=' '
read -ra POD_LIST <<< "$PODS"

randompod=${POD_LIST[$RANDOM % ${#POD_LIST[@]} ]}

export KUBE_TOKEN=$(cat /var/run/secrets/kubernetes.io/serviceaccount/token)

curl -XDELETE -sSk -H "Authorization: Bearer $KUBE_TOKEN" https://$KUBERNETES_SERVICE_HOST:$KUBERNETES_PORT_443_TCP_PORT/api/v1/namespaces/default/pods/$randompod