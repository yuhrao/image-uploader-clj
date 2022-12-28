(ns imup.backend.domain.images
  (:require [clojure.java.io :as io]
            [xtdb.api :as xtdb]))

(defn upload [{node :xtdb/node
               :as  sys-map} file-data]
  (let [assets-path (-> sys-map
                        :assets/opts
                        :path)
        image-id (random-uuid)
        img-name (file-data "name")
        tmp-file (:tempfile (file-data "file"))
        target-name (str image-id "-" img-name)
        target-path (str assets-path "/" target-name)]

    (with-open [in (io/input-stream tmp-file)
                out (io/output-stream (io/file target-path))]
      (io/copy in out))
    (let [db-entity {:xt/id             image-id
                     :image/path        (str "/assets/" target-name)
                     :image/size        (Integer/parseInt (file-data "size"))
                     :image/width       (Integer/parseInt (file-data "width"))
                     :image/description (when-not (empty? (file-data "description"))
                                          (file-data "description"))
                     :image/type        (file-data "type")
                     :image/name        (file-data "name")
                     :image/height      (Integer/parseInt (file-data "height"))}]
      (xtdb/submit-tx node
                      [[::xtdb/put db-entity]])

      {:id          (:xt/id db-entity)
       :path        (:image/path db-entity)
       :description (:image/description db-entity)
       :size        (:image/size db-entity)
       :width       (:image/width db-entity)
       :height      (:image/height db-entity)
       :name        (:image/name db-entity)
       :type        (:image/type db-entity)})))

(defn list-images [{node :xtdb/node}]
  (let [db (xtdb/db node)]
    (->> (xtdb/q db '{:find  [(pull ?e [*])]
                      :where [[?e :xt/id _]]})
         (mapcat identity)
         (mapv (fn [img]
                 (clojure.set/rename-keys img {:xt/id             :id
                                               :image/name        :name
                                               :image/description :description
                                               :image/path        :path
                                               :image/type        :type
                                               :image/size        :size
                                               :image/width       :width
                                               :image/height      :height}))))))