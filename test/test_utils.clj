(ns test-utils
  (:require [imup.backend.core :as backend]
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
    (delete-directory-recursive dir)))

(defn xtdb-cleanup-fixture [form]
  (form)
  (delete-xtdb-cache ".xtdb/test"))

(defn system-fixture [form]
  (binding [*test-system* (backend/start "test")]
    (try
      (form)
      (finally
        (backend/shutdown *test-system*)))))
