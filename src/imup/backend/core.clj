(ns imup.backend.core
  (:gen-class)
  (:require [reitit.ring :as ring]
            [reitit.swagger :as swagger]
            [reitit.swagger-ui :as swagger-ui]
            [reitit.ring.middleware.multipart :as multipart]
            [reitit.ring.middleware.muuntaja :as mtj.middleware]
            [muuntaja.core :as mtj]
            [ring.adapter.jetty :as jetty]))

(def routes [["/swagger.json"
              {:get {:no-doc  true
                     :swagger {:info        "Imup"
                               :description "API to upload files"}
                     :handler (swagger/create-swagger-handler)}}]
             ["/docs/*"
              {:get {:no-doc  :true
                     :handler (swagger-ui/create-swagger-ui-handler
                                {:config {:validatorUrl     nil
                                          :operationsSorter "alpha"}})}}]

             ["/app/*"
              (ring/create-file-handler {:root "resources/public"})]])

(def ring-routes
  (ring/routes
    (ring/create-default-handler)))

(def route-config {:data {:muuntaja   mtj/instance
                          :middleware [swagger/swagger-feature

                                       mtj.middleware/format-negotiate-middleware
                                       mtj.middleware/format-response-middleware
                                       mtj.middleware/format-request-middleware

                                       multipart/multipart-middleware]}})

(defn app []
  (ring/ring-handler
    (ring/router
      routes
      route-config)
    ring-routes))

(defonce server (atom nil))

(defn start-server! []
  (if @server
    (.stop @server))
  (reset! server (jetty/run-jetty (app)
                                  {:join? false
                                   :port  3000
                                   :host  "0.0.0.0"})))

(defn -main [& _]
  (start-server!))