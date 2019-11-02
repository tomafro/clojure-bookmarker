(ns bookmarks.views
  (:require
   [clostache.parser :as clostache]))

(defn view
  ([name] (view name {}))
  ([name context]
   (prn context)
   (clostache/render-resource name context)))
