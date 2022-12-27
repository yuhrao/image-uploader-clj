(ns imup.backend.core
  (:gen-class)
  (:require [imup.backend.server.core :as server.core]
            [imup.backend.server.router :as server.router]
            [imup.backend.db.core :as db]
            [imup.backend.config :as config]))

(defonce system (atom nil))

(defn shutdown []
  (-> @system
      (update :xtdb/node #(.close %))
      server.core/stop))

(defn start [env]
  (-> env
      config/initialize
      db/init
      server.core/start
      server.router/ring-routes))

(defn -main [& [env]]
  (reset! system (start env)))