(ns imup.backend.config)

(defn- server-opts [env]
  (condp = env
    "test" {:port  4000
            :join? false}
    "dev"
    {:port  3000
     :join? false}
    "prod"
    {:port 8080}))

(defn initialize [env]
  (let [xtdb-base-path
        (str ".xtdb/" env)
        #_(cond-> (str ".xtdb/" env)

                               (= env "test")
                               (str "/" (random-uuid)))]
    {:app/env         env
     :server/opts     (server-opts env)
     :xtdb/cache-path xtdb-base-path
     :xtdb/config
     {:xtdb/tx-log         {:kv-store {:xtdb/module 'xtdb.rocksdb/->kv-store
                                       :db-dir      (str xtdb-base-path "/tx-log")
                                       :sync?       true}}
      :xtdb/document-store {:kv-store {:xtdb/module 'xtdb.rocksdb/->kv-store
                                       :db-dir      (str xtdb-base-path "/doc-store")
                                       :sync?       true}}
      :xtdb/index-store    {:kv-store {:xtdb/module 'xtdb.rocksdb/->kv-store
                                       :db-dir      (str xtdb-base-path "/index-store")
                                       :sync?       true}}}
     :web/opts        {:resources-path (or (System/getenv "WEB_RESOURCES_PATH")
                                           "resources/public")}
     :assets/opts     {:path (or (System/getenv "ASSETS_PATH")
                                 "resources/assets")}}))