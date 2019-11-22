(ns dev.repl
  (:require [nrepl.server :as nrepl-server]
            [cider.nrepl :refer [cider-nrepl-handler]]
            [rebel-readline.main :as rebel]
            [clojure.java.io :as io]
            [io.aviso.repl]))

(defn save-port-file
  [server]
  (let [port (:port server)
        port-file (io/file ".nrepl-port")]
    (.deleteOnExit port-file)
    (spit port-file port)))

(defn -main []
  (let [server (nrepl-server/start-server :handler cider-nrepl-handler)]
    (println "Started nrepl on port" (:port server))
    (save-port-file server)
    (io.aviso.repl/install-pretty-exceptions)
    (rebel/-main)
    (System/exit 0)))

(defn throw-error []
  (throw (Exception. "my exception message")))
