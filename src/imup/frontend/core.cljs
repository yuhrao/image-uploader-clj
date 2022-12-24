(ns imup.frontend.core
  (:require [reagent.dom :as r.dom]
            ["dropzone" :as Dropzone]
            [reagent.core :as r.core]))

(defonce dropzone (r.core/atom nil))


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
  (js/console.log "params")
  (js/console.log files)

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
                    :autoProcessQueue false
                    :acceptedFiles    "image/*"
                    :addRemoveLinks   true
                    :params           params})]
    (reset! dropzone dz)))


(defn ^:dev/after-load hot-reload []
  (main))