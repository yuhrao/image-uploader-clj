(ns imup.backend.config)

(defn- server-opts [env]
  (condp = env
    "dev" {:port  3000
           :join? false}
    "prod" {:port 8080}))

(defn initialize [env]
  {:server/opts (server-opts env)})