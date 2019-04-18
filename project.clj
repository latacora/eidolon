(defproject eidolon "0.1.0"
  :description "A collection of specter navigators"
  :url "https://github.com/latacora/eidolon"
  :license {:name "EPL-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [com.rpl/specter "1.1.2"]]
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}}
  :deploy-repositories [["releases" :clojars]
                        ["snapshots" :clojars]])
