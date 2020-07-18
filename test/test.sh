#!/bin/bash

WORD=$(curl -fsq http://localhost:3000)

if [[ "$WORD" != "Hello World" ]];then
  echo "Failed"
  exit 1
fi

exit 0
