(ns imup.backend.config)

(defn- server-opts [env]
  (if (contains? #{"dev" "test"} env)
    {:port  3000
     :join? false}
    {:port 8080}))

(defn- aws-opts [env]
  (if (contains? #{"dev" "test"} env)
    {:bucket-name "dev.image.uploader.app"}
    {:bucket-name (System/getenv "S3_BUCKET_NAME")}))

(defn initialize [env]
  {:app/env env
   :server/opts (server-opts env)
   :aws/opts    (aws-opts env)})