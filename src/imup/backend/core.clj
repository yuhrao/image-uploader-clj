(ns imup.backend.core
  (:gen-class)
  (:require [imup.backend.server.core :as server.core]
            [imup.backend.server.router :as server.router]
            [imup.backend.config :as config]))

(defonce system (atom nil))

(defn -main [& [env]]
  (let [-sys (-> (config/initialize env)
                   server.router/create-router
                   server.core/handler
                   server.core/start)]
    (reset! system -sys)))