(ns imup.frontend.api.dropzone
  (:require ["dropzone" :as Dropzone]))


(defn params [files _xhr _chunk]
  (->> files
       (map (fn [f]
              (let [image-description (js/prompt (str "Provide a description for image: " (.-name f)))]
                #js {:name        (.-name f)
                     :description image-description
                     :type        (.-type f)
                     :width       (.-width f)
                     :height      (.-height f)
                     :size        (.-size f)})))
       first
       clj->js))

(defn setup-dropzone-events [^Dropzone dz on-upload-finish]
  (.on dz "complete" (fn [file]
                       (let [result (-> file
                                        (.-xhr)
                                        (.-response)
                                        (js/JSON.parse)
                                        (js->clj :keywordize-keys true))]

                         (on-upload-finish result)
                         (.removeFile dz file)))))

(defn setup-dropzone [{:keys [on-upload-finish]}]
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
    (setup-dropzone-events dz on-upload-finish)
    dz))
