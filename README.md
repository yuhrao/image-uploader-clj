# Image Uploader

Simple clojure full stack app for file upload using clojure and clojurescript


## Libraries and Frameworks

### Clojure
- Ring
- Reitit

### Clojurescript
- Reagent
- Re-frame

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
3. Startup server (go to `imup.server.core` namespace)
```clojure
;; Start server
(-main)
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