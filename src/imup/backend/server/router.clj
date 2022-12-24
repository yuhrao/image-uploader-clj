(ns imup.backend.server.router
  (:require [muuntaja.core :as mtj]
            [reitit.ring :as ring]
            [reitit.ring.middleware.multipart :as multipart]
            [reitit.ring.middleware.muuntaja :as mtj.middleware]
            [reitit.swagger :as swagger]
            [reitit.swagger-ui :as swagger-ui]))

(defn- create-routes [_config]
  [["/"
    {:get {:no-doc  true
           :handler (constantly
                      {:status  301
                       :headers {"Location" "/app/index.html"}})}}]
   ["/swagger.json"
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

(defn create-router [sys-map]
  (assoc sys-map
    :server/router {:router (ring/router
                              (create-routes sys-map)
                              route-config)
                    :ring-routes ring-routes}))
