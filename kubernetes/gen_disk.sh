#!/bin/bash

NAME="anna-disk"

gcloud compute disks create $NAME \
    --zone europe-west1-c \
    --size 1GB \
    --type pd-ssd