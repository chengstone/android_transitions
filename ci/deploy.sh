#!/usr/bin/env bash

if [[ $TRAVIS_BRANCH =~ .*master.* ]]; then
  ./gradlew :library:bintrayUpload
fi