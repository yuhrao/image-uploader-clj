(ns imup.frontend.api.images)

(defn fetch-images []
  (-> (.then (js/fetch "/api/images")
          (fn [res]
            (.json res)))
      (.then (fn [images]
               (js->clj images :keywordize-keys true)))))
