(ns imup.frontend.core
  (:require [imup.frontend.api.dropzone :as dropzone]
            [imup.frontend.components.file-input :as file-input]
            [reagent.core :as r.core]
            [reagent.dom :as r.dom]))

(defonce dz (r.core/atom nil))
(defonce uploaded-images (r.core/atom []))

(defn on-upload-finish [result]
  (swap! uploaded-images conj result))

(defn app []
  [:div {:class "container p-4"}
   [:h1 {:class "sm:text-center lg:text-left text-4xl font-bold mb-4"}
    "Image uploader"]

   [file-input/upload-form {:on-submit (fn []
                                         (.processQueue @dz))}]

   [:div
    [:hr {:class "my-4 border-2 border-slate-200"}]
    [:h1
     {:class "sm:text-center lg:text-left text-2xl font-bold mb-4"}
     "Uploaded images"]
    (if (empty? @uploaded-images)
      [:p {:class "font-semibold sm:text-center lg:text-left text-xl text-amber-700"}
       "No images uploaded yet!"]
      (doall
        (for [image @uploaded-images]
          [:div {:class "flex flex-row items-center gap-x-3"
                 :key (:id image)}
           [:img {:class "h-40"
                  :src   (:path image)}]
           [:div {:class "flex flex-col gap-y-3"}
            [:div
             [:p {:class "font-semibold"}
              (:name image)]
             [:span {:class "text-xs text-gray-500"}
              (:type image)]]
            [:a {:class "transition-all rounded px-2 py-1 text-center font-bold hover:bg-sky-600 bg-sky-500 text-white"
                 :href (:path image) :download (:name image)} "DOWNLOAD"]]])))]])

(defn main []
  (r.dom/render
    [app]
    (.getElementById js/document "app"))
  ;; It needs to be called after the component is rendered
  (when-not @dz
    (reset! dz (dropzone/setup-dropzone {:on-upload-finish on-upload-finish}))))

(defn ^:dev/after-load hot-reload []
  (main))