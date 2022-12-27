(ns imup.frontend.api.dropzone
  (:require ["dropzone" :as Dropzone]))


(defn params [files _xhr _chunk]
  (->> files
       (map (fn [f]
              #js {:name   (.-name f)
                   :type   (.-type f)
                   :width  (.-width f)
                   :height (.-height f)
                   :size   (.-size f)}))
       first
       clj->js))

(defn setup-dropzone-events [dz]
  (.on dz "complete" (fn [file]
                       (let [result (-> file
                                        (.-xhr)
                                        (.-response)
                                        (js/JSON.parse)
                                        (js->clj :keywordize-keys true))]
                         (js/console.log "complete")
                         (println result)

                         (swap! images conj result)
                         (.removeFile dz file)))))

(defn setup-dropzone! [dropzone-atom]
  (let [dz (new (.-default Dropzone)
                "#upload-form"
                #js{:url              "/api/images/upload"
                    :maxFileSize      100000
                    :maxFiles         5
                    :parallelUploads  5
                    :autoProcessQueue false
                    :acceptedFiles    "image/*"
                    :addRemoveLinks   true
                    :params           params})]
    (reset! dropzone-atom dz)
    (setup-dropzone-events dz)))
