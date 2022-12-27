(ns imup.frontend.core
  (:require [reagent.dom :as r.dom]
            ["dropzone" :as Dropzone]
            [reagent.core :as r.core]))

(defonce dropzone (r.core/atom nil))
(defonce images (r.core/atom []))

(defn upload-form []
  [:div
   [:form {:class "dropzone"
           :id    "upload-form"}]
   [:button {:on-click (fn [_]
                         (.processQueue @dropzone))}
    "Upload"]])

(defn sending [file xhr]
  (js/console.log "Sending")
  (js/console.log file)
  (js/console.log xhr))

(defonce debug (atom nil))

(comment
  (->> @debug
       (map (fn [f]
              {:name   (.-name f)
               :type   (.-type f)
               :width  (.-width f)
               :height (.-height f)
               :size   (.-size f)}))
       clj->js)

  )
(defn params [files xhr _chunk]
  (reset! debug files)

  (->> files
       (map (fn [f]
              #js {:name   (.-name f)
                   :type   (.-type f)
                   :width  (.-width f)
                   :height (.-height f)
                   :size   (.-size f)}))
       first
       clj->js))

(defn main []
  (r.dom/render [:<>
                 [upload-form]]
                (.getElementById js/document "app"))
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
    (reset! dropzone dz)
    (.on dz "complete" (fn [file]
                         (let [result (-> file
                                          (.-xhr)
                                          (.-response)
                                          (js/JSON.parse)
                                          (js->clj :keywordize-keys true))]
                           (js/console.log "complete")
                           (println result)

                           (swap! images conj result)
                           (.removeFile dz file))))))

(defn ^:dev/after-load hot-reload []
  (main))