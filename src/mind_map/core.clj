(ns mind-map.core
  (:gen-class)
  (:require [clojure.tools.logging :as log]
            [org.httpkit.server :refer [run-server]]
            [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.util.response :as resp]
            ;[ring.middleware.json :as middleware]
            [clojure.data.json :as json]
            [clojure.repl :as repl]))

(defonce serverThread (atom nil))

(defn async-stop-server []
  reset! serverThread nil)

(defn stop-server [_]
  (println "stop-server")
  (log/trace "stop-server")
  (when-not (nil? @serverThread)
    ;; graceful shutdown: wait 100ms for existing requests to be finished
    ;; :timeout is optional, when no timeout, stop immediately
    (@serverThread :timeout 100)
    (async-stop-server)))

(defn force-shutdown []
  (
   (println "force-shutdown")
   (log/trace "force-shutdown")
   (async-stop-server)
   (System/exit 0)
   ""))

(defn update-node [body]
  (let [input (json/read-str (str (slurp body)))]
           (str input))
  )

(defroutes mind-map-server
  (GET "/" [] (resp/resource-response "MindMap.html" {:root "public"}))
  (GET "/ping" [] "<b>Pong!</b>")
  (GET "/forceShutdown" [] (force-shutdown))
  (POST "/updateNode" {body :body} (update-node body))
  (route/resources "/")
  (route/not-found "(404) <b>Page not found</b>"))

  (defn -main
  "MAIN CODE"
    [& args]
  (println "Prepare server")
  (log/trace "Prepare server")
  (repl/set-break-handler! stop-server)
  (reset! serverThread (run-server mind-map-server {:port 8080}))
  (log/trace "Prepare server ended")
  (println "Prepare server ended"))
