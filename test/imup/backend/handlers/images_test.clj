(ns imup.backend.handlers.images-test
  (:require
    [clj-http.client :as http]
    [clojure.data.json :as json]
    [clojure.java.io :as io]
    [clojure.test :as t]
    [test-utils :as test-utils]
    [xtdb.api :as xtdb]
    [clojure.string :as string])
  (:import (org.apache.http.entity.mime HttpMultipartMode)))


(t/use-fixtures :each test-utils/system-fixture)

(def sample-image-path "test/assets/clojure_logo.png")

(t/deftest upload-image
  (let [image (io/file sample-image-path)
        server-host (str "http://localhost:"
                         (-> test-utils/*test-system*
                             :server/opts
                             :port))
        file-data {:file   image
                   :name   "clojure_logo.png"
                   :size   (str (.length ^java.io.File image))
                   :width  "123"
                   :height "456"
                   :type   "image/png"}
        multipart-params (->> file-data
                              (map (fn [[k v]] {:name (name k) :content v})))
        {:keys [status body]}
        (-> (http/post
              (str server-host "/api/images/upload")
              {:multipart         multipart-params
               :multipart-charset "UTF-8"
               :multipart-mode    HttpMultipartMode/BROWSER_COMPATIBLE})

            (update :body (fn [b] (when-not (empty? b)
                                    (json/read-str b :key-fn keyword)))))]

    (t/testing "response body"
      (t/is (= 200 status))
      (t/is (uuid? (parse-uuid (:id body))))
      (t/is (= (:type file-data) (:type body)))
      (t/is (and (contains? body :path)
                 (not (empty? (:path body)))))
      (t/is (= (:name file-data) (:name body))))
    (t/testing "database entity"
      (let [node (:xtdb/node test-utils/*test-system*)
            entity (xtdb/pull (xtdb/db node) '[*] (parse-uuid (:id body)))]
        (t/is (= (:type file-data) (:image/type entity)))
        (t/is (= (:id body) (str (:xt/id entity))))
        (t/is (= (:name file-data) (:image/name entity)))))
    (t/testing "generated asset"
      (let [asset-name (-> body
                           :path
                           (string/split #"\/")
                           last)
            assets-path (-> test-utils/*test-system*
                            :assets/opts
                            :path)
            full-asset-path (str assets-path "/" asset-name)]
        (t/is (true? (-> full-asset-path
                         io/file
                         .exists)))))))