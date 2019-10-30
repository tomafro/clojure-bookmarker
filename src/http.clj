(ns http
  (:require
   [aero.core :refer (read-config)]
   [clojure.walk]
   [io.pedestal.http :as http]
   [io.pedestal.http.body-params]
   [io.pedestal.http.route :as route]
   [io.pedestal.interceptor.helpers :as interceptor]
   [io.pedestal.test :as test]
   [routes]
   [database]
   [com.stuartsierra.component :as component]))

(defn uuid [] (.toString (java.util.UUID/randomUUID)))

(def x-request-id
  (interceptor/around
   ::x-request-id
   (fn [context]
     (update-in context [:request  :headers "x-request-id"] #(or % (uuid))))
   (fn [context]
     (->> (get-in context [:request :headers "x-request-id"])
          (assoc-in context [:response :headers "X-Request-Id"])))))

(def set-database-connection
  (interceptor/before
   ::set-database-connection
   (fn [context] (assoc-in context [:bindings #'database/db] database/db))))

(def keywordize-params
  (interceptor/on-request
   ::keywordize-params
   (fn [request]
     (prn request)
     (assoc-in request [:params] (clojure.walk/keywordize-keys (:params request))))))
  
(defn app-interceptors
  [service]
  (update-in service [::http/interceptors]
             #(vec (->> %
                        (cons keywordize-params)
                        (cons x-request-id)
                        (cons set-database-connection)
                        (cons (io.pedestal.http.body-params/body-params))))))

(def service-map
  (-> {::http/routes routes/routes
       ::http/type   :jetty
       ::http/port   8890
       ::http/join?  false}
      (http/default-interceptors)
      (app-interceptors)))

(defrecord Server [server]
  component/Lifecycle

  (start [component]
    (println (format "Starting http server"))
    (let [server (http/start (http/create-server service-map))]
      (assoc component :server server)))

  (stop [component]
    (http/stop server)
    (assoc component :server nil)))