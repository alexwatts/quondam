#!/bin/bash
 
kubectl run -v /Users/alex/bond/results:/opt/gatling/results quondam-nft "--image=ajw/quondam-nft:1.0-SNAPSHOT" 
