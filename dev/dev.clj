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

(defn start-backend []
  (imup.backend.core/-main "dev"))

(defn stop-backend []
  (imup.backend.server.core/stop @imup.backend.core/system))

(defn restart-backend []
  (when @imup.backend.core/system
    (stop-backend))
  (start-backend))

(comment
  ;; Start shadow tooling and watch frontend build
  (stop-shadow)

  (start-shadow)
  (switch-cljs-repl)

  ;; Backend stuff
  (start-backend)
  (stop-backend)
  (restart-backend)

  )

