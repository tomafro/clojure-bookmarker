(ns bookmarks.views
  (:require
   [clojure.java.io]
   [clostache.parser :as clostache]
   [net.cgrand.enlive-html :as html]))

(html/deftemplate layout "layouts/application.html"
  [])

(defn view
  ([name] (view name {}))
  ([name context]
   (clostache/render-resource name context)))
