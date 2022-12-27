(ns test-utils
  (:require [imup.backend.core :as backend]))

(def ^:dynamic *test-system*)

(defn system-fixture [form]
  (binding [*test-system* (backend/start "test")]
    (try
      (form)
      (finally
        (backend/shutdown *test-system*)))))
