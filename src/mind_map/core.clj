(ns mind-map.core
  (:gen-class)
  (:require [clojure.tools.logging :as log]
            [org.httpkit.server :refer [run-server]]
            [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.util.response :as resp]
            ;[ring.middleware.json :as middleware]
            [clojure.data.json :as json]
            [clojure.repl :as repl]
            [clojure.java.io :as io]
            [clojure.java.jdbc :as cjdb]))

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

(def dbConnection
  (delay (json/read-str (str (slurp (io/resource "settings/DbConnection.json" ))) :key-fn keyword)))

(def dbTransactionSettings
  {:isolation :serializable :read-only? true})
(def dbTransactionWriteSettings
  {:isolation :serializable :read-only? false})

(defn update-or-insert!
  "Updates columns or inserts a new row in the specified table"
  [trans-conn table row where-clause]
    (let [result (cjdb/update! trans-conn table row where-clause)]
      (if (zero? (first result))
        (cjdb/insert! trans-conn table row)
        result)))

(defn answer-wrap [answerBodyMaker]
  (try
    (let [answerBody (answerBodyMaker)]
      {:ErrorCode 200, :ErrorDescription "OK", :Answer answerBody})
  (catch Exception e
    {:ErrorCode 100500, :ErrorDescription (.getMessage e), :StackTrace (map str (.getStackTrace e))}))

(defn make-answer [answerBodyMaker]
  (json/write-str (answer-wrap answerBodyMaker)))

(defn update-tree-dictionary [updaterDictionary]
  updaterDictionary)

(defn update-tree-json [jsonStr]
  (let [input (json/read-str jsonStr :key-fn keyword)]
    (update-tree-dictionary input)))

(defn update-node [body]
  (make-answer
   (fn []
     (update-tree-json (str (slurp body))))))

(defn get-node-parent [trans-conn id]
  (:idnode (first (cjdb/query trans-conn ["select \"idNode\" from \"Links\" where \"idSubnode\"= ? limit 1" id]))))

(defn get-tree-top [trans-conn]
  (let [anyNodeId (:id (first (cjdb/query trans-conn ["select \"id\" from \"Nodes\" limit 1"])))]
    (if anyNodeId
      (first (take-last 1 (take-while some? (iterate (partial get-node-parent trans-conn) anyNodeId))))
      nil)))

(defn kv-explicit-to-kv-implicit [m]
  (for [v m] {(:key v) (:value v)}))

(defn make-tree [trans-conn currentNode]
  (let [keyValues (doall (cjdb/query trans-conn ["select \"key\", \"value\" from \"KeyValueNodeData\" where \"idNode\"= ?" currentNode]))]
    (let [subNodes (doall (cjdb/query trans-conn ["select \"idSubnode\" from \"Links\" where \"idNode\"= ?" currentNode]))]
      (let [dictionaredKeyValues (into {}(kv-explicit-to-kv-implicit keyValues))]
        (let [dictionaredsSubNodes (doall (for [subNode subNodes] (make-tree trans-conn subNode)))]
          (into dictionaredKeyValues {:Nodes dictionaredsSubNodes}))))))

(defn get-tree [body]
  (make-answer
   (fn []
     (let [input (json/read-str (str (slurp body)) :key-fn keyword)]
       (cjdb/with-db-transaction [trans-conn @dbConnection dbTransactionSettings]
         (let [topNode (get-tree-top [trans-conn])]
           (if topNode
             (make-tree trans-conn topNode)
             "[]")))))))

(defroutes mind-map-server
  (GET "/" [] (resp/resource-response "MindMap.html" {:root "public"}))
  (GET "/ping" [] "<b>Pong!</b>")
  (GET "/forceShutdown" [] (force-shutdown))
  (POST "/updateNode" {body :body} (update-node body));;to three arrays of nodes
  (POST "/getTree" {body :body} (get-tree body))
  (route/resources "/")
  (route/not-found "(404) Page not found"))

  (defn -main
  "MAIN CODE"
    [& args]
  (println "Prepare server")
  (log/trace "Prepare server")
  (repl/set-break-handler! stop-server)
  (reset! serverThread (run-server mind-map-server {:port 8080}))
  (log/trace "Prepare server ended")
  (println "Prepare server ended"))
