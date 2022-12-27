(ns imup.frontend.components.file-input)

(defn upload-form [{:keys [on-submit]}]
  [:div
   [:form {:class "dropzone"
           :id    "upload-form"}]
   [:button {:on-click on-submit}
    "Upload"]])
