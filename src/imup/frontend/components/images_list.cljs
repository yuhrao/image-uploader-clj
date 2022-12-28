(ns imup.frontend.components.images-list)


(def no-images-message
  [:p {:class "font-semibold sm:text-center lg:text-left text-xl text-amber-700"}
   "No images uploaded yet!"])

(defn list-item [{id :id
                  path :path
                  img-name :name
                  img-type :type}]
  [:div {:class "flex flex-row items-center gap-x-3"
         :key id}
   [:img {:class "max-w-sm"
          :src   path}]
   [:div {:class "flex flex-col gap-y-3"}
    [:div
     [:p {:class "font-semibold"}
      img-name]
     [:span {:class "text-xs text-gray-500"}
      img-type]]
    [:a {:class "transition-all rounded px-2 py-1 text-center font-bold hover:bg-sky-600 bg-sky-500 text-white"
         :href path :download img-name} "DOWNLOAD"]]])

(defn images-list [{:keys [images]}]
  [:div
    [:h1
     {:class "sm:text-center lg:text-left text-2xl font-bold mb-4"}
     "Uploaded images"]

    (if (empty? images)
      no-images-message
      [:div
       {:class "flex flex-col gap-y-4"}
       (doall
         (for [image images]
           [list-item image]))])])
