(ns dev
  (:require [shadow.cljs.devtools.server :as shadow.server]
            [shadow.cljs.devtools.api :as shadow.api]
            [imup.backend.server.core :as backend.server]
            [imup.backend.core :as backend.core]))


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
  (when-let [sys @imup.backend.core/system]
    (imup.backend.core/shutdown sys)))

(defn restart-backend []
  (stop-backend)
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

  (do
    (require '[portal.api :as portal])

    (portal/open {:launcher :intellij})

    (portal/tap))


  (let [node (:xtdb/node @imup.backend.core/system)]

    (xtdb.api/q
      (xtdb.api/db node)
      '{:find  [(pull ?id [*])]
        :where [[?id :xt/id _]]}
      ))

  )

