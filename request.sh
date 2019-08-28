#!/usr/bin/env bash

set -x
URL=https://pal-tracker-anant.apps.evans.pal.pivotal.io/time-entries

http POST $URL projectId=1 userId=2 date="2019-01-01" hours=8

TIME_ENTRY_ID=$(http ==body $URL |jq| )

https $URL/$TIME_ENTRY_ID
