(ns imup.frontend.core
  (:require [imup.frontend.api.dropzone :as dropzone]
            ["dropzone" :as Dropzone]
            [imup.frontend.components.file-input :as file-input]
            [imup.frontend.components.images-list :as images-list]
            [reagent.core :as r.core]
            [reagent.dom :as r.dom]))

(defonce dz (r.core/atom nil))
(defonce uploaded-images (r.core/atom []))

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