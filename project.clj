(defproject logic-tutorial "0.1.0"
  :description "Testing core.logic"

  :repositories {"sonatype" 
                  {:url "http://oss.sonatype.org/content/repositories/snapshots"
                  :snapshots true
                  :releases {:checksum :fail :update :always}}}
                  :dependencies [[org.clojure/core.logic "0.8.0-rc2"]])

