(ns imup.backend.server.router
  (:require [muuntaja.core :as mtj]
            [reitit.dev.pretty :as pretty]
            [imup.backend.domain.images :as images]
            [reitit.ring :as ring]
            [reitit.ring.middleware.multipart :as multipart]
            [reitit.ring.middleware.muuntaja :as mtj.middleware]
            [reitit.ring.middleware.parameters :as params.middleware]
            [reitit.swagger :as swagger]
            [reitit.swagger-ui :as swagger-ui]))

(defn- create-routes [{{assets-path :path}             :assets/opts
                       {web-page-path :resources-path} :web/opts
                       :as                             system-map}]
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

   ["/api"
    ["/images/upload"
     ;; this [:parameters :multipart] is required for multipart middleware
     {:post {:parameters {:multipart {}}
             :handler    (fn [{file-data :multipart-params}]
                           {:status 200
                            :body   (images/upload system-map
                                                   file-data)})}}]]

   ["/assets/*"
    (ring/create-file-handler {:root assets-path})]

   ["/app/*"
    (ring/create-file-handler {:root web-page-path})]])

(def ring-routes
  (ring/routes
    (ring/create-default-handler)))

(def route-config {:exception pretty/exception
                   :data      {:muuntaja   mtj/instance
                               :middleware [swagger/swagger-feature

                                            params.middleware/parameters-middleware

                                            mtj.middleware/format-negotiate-middleware
                                            mtj.middleware/format-response-middleware
                                            mtj.middleware/format-request-middleware

                                            multipart/multipart-middleware]}})

(defn create-router [sys-map]
  (assoc sys-map
    :server/router {:router      (ring/router
                                   (create-routes sys-map)
                                   route-config)
                    :ring-routes ring-routes}))
