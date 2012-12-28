(ns logic-test.facts-and-rels
   (:refer-clojure :exclude [==])
   (:use clojure.core.logic))

(defrel grade person course g)

(fact grade 'Bob 'Algebra 'B)
(fact grade 'Bob 'Art 'C)
(fact grade 'John 'Algebra 'A)
(fact grade 'John 'Art 'A)
(fact grade 'Ricky 'Algebra 'D)
(fact grade 'Ricky 'Art 'A)

(defn matches [{:keys [course g]}]
  (run* [q]
    (fresh [p]
      (grade p course g)
      (== q [p course g]))))

(defn meets-requirements [person [k & nowledge]]
  (if-let [{:keys [course g]} k]
    (all
      (grade person course g)
      (meets-requirements person nowledge))
    succeed))

(comment
  
(run* [person]
      (meets-requirements
        person
        [{:course 'Algebra :g 'B}
         {:course 'Art :g 'C}]))


(matches {:course 'Algebra :g 'B})) 
