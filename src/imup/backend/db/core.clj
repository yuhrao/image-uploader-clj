(ns imup.backend.db.core
  (:require [xtdb.api :as xtdb]))

(defn init [{db-cfg :xtdb/config
             :as sys-map}]
  (assoc sys-map :xtdb/node (xtdb/start-node db-cfg)))
