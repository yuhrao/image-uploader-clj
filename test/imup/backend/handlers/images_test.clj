(ns imup.backend.handlers.images-test
  (:require
    [clj-http.client :as http]
    [clojure.data.json :as json]
    [clojure.java.io :as io]
    [clojure.set :as set]
    [clojure.string :as string]
    [clojure.test :as t]
    [test-utils :as test-utils]
    [xtdb.api :as xtdb])
  (:import (org.apache.http.entity.mime HttpMultipartMode)))

(t/use-fixtures :once test-utils/xtdb-cleanup-fixture)
(t/use-fixtures :each test-utils/system-fixture)

(def sample-image-path "test/assets/clojure_logo.png")

(t/deftest upload-image
  (let [image (io/file sample-image-path)
        server-host (str "http://localhost:"
                         (-> test-utils/*test-system*
                             :server/opts
                             :port))
        file-data {:file        image
                   :name        "clojure_logo.png"
                   :size        (str (.length ^java.io.File image))
                   :description "My file description"
                   :width       "123"
                   :height      "456"
                   :type        "image/png"}
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
      (t/is (= (:description file-data) (:description body)))
      (t/is (= (:size file-data) (str (:size body))))
      (t/is (= (:width file-data) (str (:width body))))
      (t/is (= (:height file-data) (str (:height body))))
      (t/is (= (:name file-data) (:name body))))
    (t/testing "database entity"
      (let [node (:xtdb/node test-utils/*test-system*)
            entity (xtdb/entity (xtdb/db node) (parse-uuid (:id body)))]
        (prn body)
        (prn entity)
        (prn (xtdb.api/q
               (xtdb.api/db node)
               '{:find  [(pull ?id [*])]
                 :where [[?id :xt/id _]]}))
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

(defn create-image [{node :xtdb/node} {img-name :name
                                       img-type :type}]
  (xtdb/submit-tx
    node
    [[::xtdb/put {:xt/id             (random-uuid)
                  :image/name        img-name
                  :image/type        img-type
                  :image/path        (str "/assets/" img-name)
                  :image/description (str "Description for " img-name)
                  :image/width       (rand-int 1000)
                  :image/height      (rand-int 1000)}]]))

(t/deftest list-images
  (let [server-host (str "http://localhost:"
                         (-> test-utils/*test-system*
                             :server/opts
                             :port))

        samples [{:name "image1.jpg"
                  :type "image/jpeg"}
                 {:name "image2.png"
                  :type "image/png"}
                 {:name "image3.gif"
                  :type "image/gif"}]
        _ (doseq [sample samples]
            (create-image test-utils/*test-system* sample))

        {:keys [status body]}
        (-> (http/get (str server-host "/api/images"))
            (update :body (fn [b] (when-not (empty? b)
                                    (json/read-str b :key-fn keyword)))))]

    (t/is (= 200 status))
    (t/is (= (count samples) (count body)))
    (let [required-fields #{:id :name :path :type :width :height :size :description}]
      (t/is (->> body
                 (map keys)
                 (map set)
                 (map #(set/subset? % required-fields))
                 (every? true?))))))