#!/usr/bin/env bash

docker run -ti --rm -v "$PWD:/app" -v "$HOME/.ivy2":/root/.ivy2 1science/sbt sbt fastOptJS
cp target/scala-2.12/scala-hello-world-fastopt.js route_def.js
cp target/scala-2.12/scala-hello-world-jsdeps.js jsdeps.js