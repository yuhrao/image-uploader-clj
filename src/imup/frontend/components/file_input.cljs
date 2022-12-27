(ns imup.frontend.components.file-input)

(defn upload-form [{:keys [dropzone]}]
  [:div
   [:form {:class "dropzone"
           :id    "upload-form"}]
   [:button {:on-click (fn [_]
                         (.processQueue dropzone))}
    "Upload"]])
