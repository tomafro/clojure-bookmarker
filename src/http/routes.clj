(ns http.routes
  (:require
   [io.pedestal.http.route :as route]
   [bookmarks.http]
   [http.echo]
   [reitit.core :as reitit]))

(def routes
  [(concat []
           (bookmarks.http/routes)
           (http.echo/routes))])

(def reitit-routes
  (http.echo/reitit-routes))

(def router
  (reitit/router (concat (http.echo/reitit-routes))))

(def url-for
  (io.pedestal.http.route/url-for-routes (route/expand-routes routes)))

(reitit/routes router)
