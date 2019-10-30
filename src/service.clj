(ns service
  (:require [com.stuartsierra.component :as component]
            [http]
            [config]
            [next.jdbc :as jdbc]))

(defrecord Database [url datasource]
  component/Lifecycle

  (start [component]
    (println (format "Using database '%s'" url))
    (let [datasource (jdbc/get-datasource url)]
      (assoc component :datasource datasource)))

  (stop [component]
    (assoc component :datasource nil)))

(defn system [config]
  (let [{:keys [database]} config]
    (component/system-map
     :database (->Database database nil)
     :http (component/using
           (http/->Server nil)
           [:database]))))

(defn start []
  (component/start (system (config/load-config))))
