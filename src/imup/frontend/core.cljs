(ns imup.frontend.core
  (:require [reagent.dom :as r.dom]
            ["dropzone" :as Dropzone]
            [reagent.core :as r.core]
            [imup.frontend.components.file-input :as file-input]
            [imup.frontend.api.dropzone :as dropzone]))

(defonce dropzone-instance (r.core/atom nil))
(defonce images (r.core/atom []))

(defn main []
  (r.dom/render [:div
                 [file-input/upload-form {:dropzone @dropzone-instance}]]
                (.getElementById js/document "app"))
  ;; It needs to be called after the component is rendered
  (dropzone/setup-dropzone! dropzone-instance))

(defn ^:dev/after-load hot-reload []
  (main))