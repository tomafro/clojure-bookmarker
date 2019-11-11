(ns http.routes
  (:require
   [io.pedestal.http.route :as route]
   [bookmarks.http]
   [http.echo]))

(def routes
  [(concat []
          (bookmarks.http/routes)
          (http.echo/routes))])

(def url-for
  (io.pedestal.http.route/url-for-routes (route/expand-routes routes)))
