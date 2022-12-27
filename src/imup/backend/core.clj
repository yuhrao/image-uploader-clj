(ns imup.backend.core
  (:gen-class)
  (:require [imup.backend.server.core :as server.core]
            [imup.backend.server.router :as server.router]
            [imup.backend.db.core :as db]
            [imup.backend.config :as config]))

(defonce system (atom nil))

(defn shutdown [sys-map]
  (-> sys-map
      server.core/stop
      (update :xtdb/node #(.close %))))

(defn start [env]
  (-> env
      config/initialize
      db/init
      server.router/create-router
      server.core/handler
      server.core/start))

(defn -main [& [env]]
  (reset! system (start env)))