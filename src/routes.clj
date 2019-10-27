(ns routes
  (:require
   [io.pedestal.http.route :as route]
   [response]
   [http.bookmarks]
   [echo]))

(def routes
  [(concat []
          (http.bookmarks/routes)
          (echo/routes))])

(def url-for
  (io.pedestal.http.route/url-for-routes (route/expand-routes routes)))
