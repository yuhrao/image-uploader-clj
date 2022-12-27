(ns imup.backend.server.core
  (:require [reitit.ring :as ring]
            [ring.adapter.jetty :as jetty])
  (:import org.eclipse.jetty.server.Server))

(defn handler [{{:keys [router ring-routes]} :server/router
                :as                          sys-map}]
  (assoc sys-map :server/handler
                 (ring/ring-handler
                   router
                   ring-routes)))

(defn start [{opts :server/opts
              app  :server/handler
              :as  sys-map}]
  (assoc sys-map
    :server/instance
    (jetty/run-jetty app
                                  (assoc opts
                                    :host "0.0.0.0"))))

(defn stop [{^Server server :server/instance
             :as sys-map}]
  (.stop server)
  sys-map)