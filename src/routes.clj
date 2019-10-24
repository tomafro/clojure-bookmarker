(ns routes
  (:require
   [io.pedestal.http.route :as route]
   [response]
   [http.bookmarks]))

(def routes (http.bookmarks/routes))
