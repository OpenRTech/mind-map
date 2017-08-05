(defproject mind-map "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"] 
                 [org.clojure/tools.logging "0.3.1"]
                 [org.clojure/java.jdbc "0.7.0-alpha1"]
                 [org.jsoup/jsoup "1.8.3"]
                 [org.clojure/data.json "0.2.6"]
                 [http-kit "2.2.0"]
                 [ring/ring-core "1.6.0"]
                 [compojure "1.6.0"]
                 [postgresql/postgresql "9.3-1102.jdbc41"]
                 [org.clojure/java.jdbc "0.7.0"]
                 [log4j/log4j "1.2.17" :exclusions [javax.mail/mail
                                                 javax.jms/jms
                                                 com.sun.jmdk/jmxtools
                                                 com.sun.jmx/jmxri]]]
  :main ^:skip-aot mind-map.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
