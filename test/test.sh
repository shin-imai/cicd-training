#!/bin/sh

set -ex

curl -fsq http://localhost:3000 | grep -sq "Hello World"

if [ $? -ne 0 ];then
  echo "Failed"
  exit 1
fi

