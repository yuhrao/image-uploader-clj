(ns imup.backend.domain.images
  (:require [clojure.java.io :as io]))


(defn upload [sys-map file-data]
  (let [assets-path (-> sys-map
                        :assets/opts
                        :path)
        img-name (file-data "name")
        tmp-file (:tempfile (file-data "file"))
        target-path (str assets-path "/" img-name)]
    (tap> {:in tmp-file })
    (try
      (with-open [in (io/input-stream tmp-file)
                  out (io/output-stream (io/file target-path))]
        (io/copy in out))
      (catch Throwable e
        (tap> e)))))
