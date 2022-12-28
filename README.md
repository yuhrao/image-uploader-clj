# Image Uploader

[![wakatime](https://wakatime.com/badge/user/c71c63dd-80b0-4866-9efa-d5cb7ae83bfb/project/8b2a85ec-363e-4efe-bec8-92f799c9c634.svg)](https://wakatime.com/badge/user/c71c63dd-80b0-4866-9efa-d5cb7ae83bfb/project/8b2a85ec-363e-4efe-bec8-92f799c9c634)
[![run-tests](https://github.com/yuhrao/image-uploader-clj/actions/workflows/tests.yml/badge.svg?branch=main)](https://github.com/yuhrao/image-uploader-clj/actions/workflows/tests.yml)
![Docker Image Version (latest semver)](https://img.shields.io/docker/v/yuhribernardes/image-uploader-clj?logo=docker&sort=semver)

Simple clojure full stack app for file upload using clojure and clojurescript

## Libraries and Frameworks

### Clojure
- Ring
- Reitit

### Clojurescript
- Reagent

## Development guide

Access the app in your browser from `http://localhost:3000` after start the application
### Running locally

1. Start your REPL
2. Execute `dev` namespace stuff
```clojure
;; Start Shadow server and watch `:app` build
(start-shadow)
;; Switch your repl to shadow's clojurescript REPL
(switch-cljs-repl)
```

3. Startup server (go to `imup.backend.core` namespace)
```clojure
(start-backend)

(stop-backend)

(restart-backend)
```

Now you can access the application from both `http://localhost:3000`
(reitit server provides the web UI) or `http://localhost:8080`
(shadow-clj provides the web UI)

### Running from binaries

1. Build the app
```shell
# Using babashka
bb build-local

# Manually
clojure -A:dev -m shadow.cljs.devtools.cli compile app && \
clojure -A:dev -T:build uber
```

2. Run uberjar
```shell
java -jar ./target/imup-1.0.0-standalone.jar
```

3. From a docker file

```shell
# Building the image and running-locally
# you can also use bb build-container
docker build -t image-uploader-clj:local .

# you can also use bb start-container
docker run -p 3000:8080 image-uploader-clj:local

# Running from docker hub
docker run -p 3000:8080 yuhribernardes/image-uploader-clj:latest
```

## Running tests

```shell
# Using babashka
bb test

# Using clojure cli
clojure -X:test-runner
```