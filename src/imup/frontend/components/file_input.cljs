(ns imup.frontend.components.file-input)

(defn upload-form [{:keys [on-submit]}]
  [:div {:class "flex flex-col gap-y-3"}
   [:div
    [:form {:class "dropzone"
            :id    "upload-form"}]]
   [:button {:class  "transition-all rounded px-2 py-1 text-center font-bold hover:bg-sky-600 bg-sky-500 text-white"
             :on-click on-submit}
    "Upload"]])
