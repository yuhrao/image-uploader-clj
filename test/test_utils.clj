(ns test-utils
  (:require [imup.backend.core :as backend]
            [xtdb.api :as xtdb]
            [clojure.java.io :as io]))

(def ^:dynamic *test-system* {})

(defn- delete-directory-recursive
  "Recursively delete a directory."
  [^java.io.File file]

  (when (.isDirectory file)
    (run! delete-directory-recursive (.listFiles file)))

  (io/delete-file file))
(defn- delete-xtdb-cache [cache-path]
  (let [dir (io/file cache-path)]
    (when (.exists dir)
      (delete-directory-recursive dir))))

(defn xtdb-cleanup-fixture [form]
  (let [node (:xtdb/node *test-system*)]

    (->> (xtdb.api/q
           (xtdb.api/db node)
           '{:find  [(pull ?id [*])]
             :where [[?id :xt/id _]]}
           )
         (mapcat identity)
         (map :xt/id)
         (map (fn [id]
                [::xtdb/evict id]))
         (into [])
         (xtdb/submit-tx node)))

  (form))

(defn system-fixture [form]
  (delete-xtdb-cache ".xtdb/test")
  (binding [*test-system* (backend/start "test")]
    (try
      (form)
      (finally
        (backend/shutdown *test-system*)))))
