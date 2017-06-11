(ns mind-map.core
  (:gen-class)
  (:require [clojure.tools.logging :as log]
            [org.httpkit.server :refer [run-server]]
            [compojure.core :refer :all]
            [compojure.route :as route]))
            ;;[ring.middleware.json :as middleware]
;;            [clojure.data.json :as json])

(defroutes maind-map-server
(GET "/hello" [] "Hello World")
(POST "/updateNode" {body :body} 
           (
            ;;            json-response (slurp body)
            ;;json/read-str 
            str (slurp body);;"Ololo"
           )
)
(route/resources "/"))
     
  (defn -main
  "I don't do a whole lot ... yet."
    [& args]
  (println "Hello, World test!")
  (log/error "test error logging!")
  (run-server maind-map-server {:port 8080})
  (println "Hello, World!"))
