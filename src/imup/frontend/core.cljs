(ns imup.frontend.core
  (:require [imup.frontend.api.dropzone :as dropzone]
            ["dropzone" :as Dropzone]
            [imup.frontend.components.file-input :as file-input]
            [imup.frontend.components.images-list :as images-list]
            [reagent.core :as r.core]
            [reagent.dom :as r.dom]))

(defonce dz (r.core/atom nil))
(defonce uploaded-images (r.core/atom []))

(reset! uploaded-images
          [{:id "b84c4d00-f6a2-438f-8c81-acdf122197d8",
            :path "/assets/b84c4d00-f6a2-438f-8c81-acdf122197d8-profile.jpg",
            :name "profile.jpg",
            :type "image/jpeg"}
           {:id "d77d8b44-005e-4dc1-91e6-8bfb81c871a0",
            :path "/assets/d77d8b44-005e-4dc1-91e6-8bfb81c871a0-this-is-fine-zoom-background.jpg",
            :name "this-is-fine-zoom-background.jpg",
            :type "image/jpeg"}]
)

(defn on-upload-finish [result]
  (swap! uploaded-images conj result))

(defn app []
  [:div {:class "container p-4 mx-auto"}
   [:h1 {:class "text-center lg:text-left text-4xl font-bold mb-4"}
    "Image uploader"]

   [file-input/upload-form {:on-submit (fn []
                                         (.processQueue ^Dropzone @dz))}]

   [:hr {:class "my-4 border-2 border-slate-200"}]
   [images-list/images-list {:images @uploaded-images}]])

(defn main []
  (r.dom/render
    [app]
    (.getElementById js/document "app"))
  ;; It needs to be called after the component is rendered
  (when-not @dz
    (reset! dz (dropzone/setup-dropzone {:on-upload-finish on-upload-finish}))))

(defn ^:dev/after-load hot-reload []
  (main))