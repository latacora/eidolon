(def deps
  (->>
    (with-open [rdr (clojure.java.io/reader "deps.edn")
                pbr (java.io.PushbackReader. rdr)]
      (clojure.edn/read pbr))
    :deps
    (mapv (comp vec (juxt key (comp :mvn/version val))))))

(defproject eidolon "0.2.0"
  :description "A collection of specter navigators"
  :url "https://github.com/latacora/eidolon"
  :license {:name "EPL-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies ~deps
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}
             :dev {:dependencies [[clj-kondo "RELEASE"]]}}
  :aliases {"clj-kondo" ["run" "-m" "clj-kondo.main"]}
  :deploy-repositories [["releases" :clojars]
                        ["snapshots" :clojars]])
