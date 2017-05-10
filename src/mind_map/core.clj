(ns mind-map.core
  (:gen-class)
  (:require [clojure.tools.logging :as log]))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World test!")
  (log/error "test error logging!")
  (println "Hello, World!"))
