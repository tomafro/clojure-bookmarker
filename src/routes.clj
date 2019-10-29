(ns routes
  (:require
   [io.pedestal.http.route :as route]
   [response]
   [bookmarks.http]
   [echo]))

(def routes
  [(concat []
          (bookmarks.http/routes)
          (echo/routes))])

(def url-for
  (io.pedestal.http.route/url-for-routes (route/expand-routes routes)))
