# Image Uploader

[![wakatime](https://wakatime.com/badge/user/c71c63dd-80b0-4866-9efa-d5cb7ae83bfb/project/8b2a85ec-363e-4efe-bec8-92f799c9c634.svg)](https://wakatime.com/badge/user/c71c63dd-80b0-4866-9efa-d5cb7ae83bfb/project/8b2a85ec-363e-4efe-bec8-92f799c9c634)

Simple clojure full stack app for file upload using clojure and clojurescript

## Libraries and Frameworks

### Clojure
- Ring
- Reitit

### Clojurescript
- Reagent

## Development guide

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
bb build

# Manually
clojure -A:dev -m shadow.cljs.devtools.cli compile app && \
clojure -A:dev -T:build uber
```

2. Run uberjar
```shell
java -jar ./target/imup-1.0.0-standalone.jar
```

Access the app in your browser from `http://localhost:3000`

3. From a docker file

[//]: # (TODO: Dockerfile to run the app)
[//]: # (TODO: Dockerfile to run tests)