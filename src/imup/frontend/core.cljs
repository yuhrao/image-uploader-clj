(ns imup.frontend.core
  (:require [reagent.dom :as r.dom]
            [reagent.core :as r.core]
    [imup.frontend.components.file-input :as file-input]
            [imup.frontend.api.dropzone :as dropzone]))

(defonce dz (r.core/atom nil))
(defonce uploaded-images (r.core/atom []))
(comment
  (reset! uploaded-images [{:id "c00d5cfc-25cd-44e5-aca7-db0793474a58",
  :path "/assets/c00d5cfc-25cd-44e5-aca7-db0793474a58-Amigos_Do_Bem_Logo.png",
  :name "Amigos_Do_Bem_Logo.png",
  :type "image/png"}])


  )

(defn on-upload-finish [result]
  (swap! uploaded-images conj result))

(defn app []
  [:div
   [file-input/upload-form {:on-submit (fn []
                                         (.processQueue @dz))}]

   [:div
    [:h1 "Uploaded images"]
    (if (empty? @uploaded-images)
      [:p "No images uploaded"]
      (doall
        (for [image @uploaded-images]
          [:div {:key (:id image)}
           [:img {:style {:height "100px"}
                  :src (:path image)}]
           [:p (:name image)]
           [:a {:href (str "http://localhost:3000" (:path image)) :download (:name image)} "Download"]])))]])

(defn main []
  (r.dom/render
    [app]
    (.getElementById js/document "app"))
  ;; It needs to be called after the component is rendered
  (reset! dz (dropzone/setup-dropzone {:on-upload-finish on-upload-finish})))

(defn ^:dev/after-load hot-reload []
  (main))