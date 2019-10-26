(ns server
  (:require
   [aero.core :refer (read-config)]
   [io.pedestal.http :as http]
   [io.pedestal.http.route :as route]
   [io.pedestal.interceptor.helpers :as interceptor]
   [io.pedestal.test :as test]
   [routes]
   [database]))

(defn uuid [] (.toString (java.util.UUID/randomUUID)))

(def x-request-id
  (interceptor/around
   ::x-request-id
   (fn [context]
     (update-in context [:request  :headers "x-request-id"] #(or % (uuid))))
   (fn [context]
     (assoc-in context [:response :headers "X-Request-Id"] (get-in context [:request :headers "x-request-id"])))))

(def set-database-connection
  (interceptor/on-request
   ::set-database-connection
   (fn [request] (assoc-in request [:bookmarker :db] database/db))))
  
(defn app-interceptors
  [service]
  (update-in service [::http/interceptors]
             #(vec (->> %
                        (cons x-request-id)
                        (cons set-database-connection)))))

(def server (atom nil))

(def service-map
  (-> {::http/routes routes/routes
       ::http/type   :jetty
       ::http/port   8890
       ::http/join?  false}
      (http/default-interceptors)
      (app-interceptors)))

(defn start
  []
  (reset! server
          (http/start (http/create-server service-map))))

(defn stop
  []
  (http/stop @server))

(defn restart
  []
  (stop)
  (start))
