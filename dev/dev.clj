(ns dev
  (:require [shadow.cljs.devtools.server :as shadow.server]
            [shadow.cljs.devtools.api :as shadow.api]))

(def build-id :app)

(defn start-shadow []
  (shadow.server/start!)
  (shadow.api/watch build-id))

(defn stop-shadow []
  (shadow.server/stop!))

(defn switch-cljs-repl []
  (shadow.api/nrepl-select build-id))

(comment
  ;; Start shadow tooling and watch frontend build
  (stop-shadow)

  (start-shadow)
  (switch-cljs-repl)

  )