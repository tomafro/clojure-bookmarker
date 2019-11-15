(ns http.server
  (:require
   [clojure.walk]
   [io.pedestal.http :as http]
   [io.pedestal.http.body-params]
   [io.pedestal.interceptor.helpers :as interceptor]
   [http.routes]
   [database]
   [reitit.core :as reitit]
   [reitit.http :as reitit-http]
   [reitit.pedestal :as reitit-pedestal]
   [reitit.coercion :as coercion]
   [reitit.coercion.spec :as reitit-spec]
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

(def x-timing
  (interceptor/around
   ::x-timing
   (fn [context]
     (assoc-in context [:x-timing-start] (. System nanoTime)))
   (fn [context]
     (let [start (get-in context [:x-timing-start])]
       (assoc-in context [:response :headers "X-Timing"] (str (- (. System nanoTime) start) "ns"))))))

(def set-database-connection
  (interceptor/before
   ::set-database-connection
   (fn [context] (assoc-in context [:bindings #'database/db] database/db))))

(def keywordize-params
  (interceptor/on-request
   ::keywordize-params
   (fn [request]
     (assoc-in request [:params] (clojure.walk/keywordize-keys (:params request))))))
  
(defn app-interceptors
  [service]
  (update-in service [::http/interceptors]
             #(vec (->> %
                        (cons keywordize-params)
                        (cons x-timing)
                        (cons x-request-id)
                        (cons set-database-connection)
                        (cons (io.pedestal.http.body-params/body-params))))))

(def service-map
  (-> {::http/routes []
       ::http/type   :jetty
       ::http/port   8890
       ::http/join?  false}
      (http/default-interceptors)
      (reitit-pedestal/replace-last-interceptor (reitit-pedestal/routing-interceptor http.routes/router))
      ;;(app-interceptors)
      ;;(http/dev-interceptors)
      ))

(defrecord Server [server]
  component/Lifecycle

  (start [component]
    (println (format "Starting http server %s" component))
    (let [server (http/start (http/create-server (assoc service-map ::server component)))]
      (assoc component :server server)))

  (stop [component]
    (http/stop server)
    (assoc component :server nil)))
