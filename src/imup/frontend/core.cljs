(ns imup.frontend.core
  (:require [reagent.dom :as r.dom]
            [reagent.core :as r.core]))

(defn main []
  (r.dom/render [:h1 "Hello World!!!"]
                (.getElementById js/document "app")))

(defn ^:dev/after-load hot-reload []
  (main))