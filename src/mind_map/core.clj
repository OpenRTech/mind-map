(ns mind-map.core
  (:gen-class)
  (:require [clojure.tools.logging :as log]
            [org.httpkit.server :refer [run-server]]
            [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.util.response :as resp]
            ;[ring.middleware.json :as middleware]
            [clojure.data.json :as json]))

(defroutes maind-map-server
  (GET "/" [] (resp/resource-response "MindMap.html" {:root "public"}))
  (GET "/hello" [] "Hello World")
  (POST "/updateNode" {body :body} 
           (let [input (json/read-str (str (slurp body)))]
           (str input)) )
  (route/resources "/")
  (route/not-found "(404) <b>Page not found</b>"))
     
  (defn -main
  "MAIN CODE"
    [& args]
  (println "Prepare server")
  (log/trace "Prepare server")
  (run-server maind-map-server {:port 8080})
  (log/trace "run-server!")
  (println "run-server!"))
