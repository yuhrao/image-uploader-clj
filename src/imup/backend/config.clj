(ns imup.backend.config)

(defn- server-opts [env]
  (if (contains? #{"dev" "test"} env)
    {:port  3000
     :join? false}
    {:port 8080}))

(defn initialize [env]
  {:app/env     env
   :server/opts (server-opts env)
   :xtdb/config {:xtdb/tx-log         {:kv-store {:xtdb/module 'xtdb.rocksdb/->kv-store
                                                  :db-dir      ".xtdb/dev/tx-log"
                                                  :sync?       true}}
                 :xtdb/document-store {:kv-store {:xtdb/module 'xtdb.rocksdb/->kv-store
                                                  :db-dir      ".xtdb/dev/doc-store"
                                                  :sync?       true}}
                 :xtdb/index-store    {:kv-store {:xtdb/module 'xtdb.rocksdb/->kv-store
                                                  :db-dir      ".xtdb/dev/index-store"
                                                  :sync?       true}}}
   :web/opts    {:resources-path (or (System/getenv "WEB_RESOURCES_PATH")
                                     "resources/public")}
   :assets/opts {:path (or (System/getenv "ASSETS_PATH")
                           "resources/assets")}})