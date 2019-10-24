(ns routes
  (:require
   [io.pedestal.http.route :as route]
   [response]
   [bookmarks.http]))

(def routes (bookmarks.http/routes))
