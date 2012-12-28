(ns logic-test.condx-test
  (:refer-clojure :exclude [==])
  (:require [clojure.core.logic :as logic]
    [clojure.test :as test]))

;;
;; conde
;;

(test/deftest test-conde-1
  (test/is (=  (into #{}
            (logic/run* [x]
              (logic/conde
                [(logic/== x 'olive) logic/succeed]
                [logic/succeed logic/succeed]
                [(logic/== x 'oil) logic/succeed])))
          (into #{}
            '[olive _0 oil]))))

(test/deftest test-conde-2
  (test/is (= (into #{}
           (logic/run* [r]
             (logic/fresh [x y]
               (logic/conde
                 [(logic/== 'split x) (logic/== 'pea y)]
                 [(logic/== 'navy x) (logic/== 'bean y)])
               (logic/== (cons x (cons y ())) r))))
         (into #{}
           '[(split pea) (navy bean)]))))

(defn teacupo [x]
  (logic/conde
    [(logic/== 'tea x) logic/s#]
    [(logic/== 'cup x) logic/s#]))

(test/deftest test-conde-3
  (test/is (= (into #{}
           (logic/run* [r]
             (logic/fresh [x y]
               (logic/conde
                 [(teacupo x) (logic/== true y) logic/s#]
                 [(logic/== false x) (logic/== true y)])
               (logic/== (cons x (cons y ())) r))))
         (into #{} '((false true) (tea true) (cup true))))))

;;
;; conda (soft-cut)
;;

(test/deftest test-conda-1
  (test/is (= (logic/run* [x]
           (logic/conda
             [(logic/== 'olive x) logic/s#]
             [(logic/== 'oil x) logic/s#]
             [logic/u#]))
         '(olive))))

(test/deftest test-conda-2
  (test/is (= (logic/run* [x]
           (logic/conda
             [(logic/== 'virgin x) logic/u#]
             [(logic/== 'olive x) logic/s#]
             [(logic/== 'oil x) logic/s#]
             [logic/u#]))
         '())))

(test/deftest test-conda-2a
  (test/is (= (logic/run* [x]
           (logic/conda
             [(logic/== 'virgin x)]
             [(logic/== 'olive x) logic/s#]
             [(logic/== 'oil x) logic/s#]
             [logic/u#]))
         '(virgin))))

(test/deftest test-conda-3
  (test/is (= (logic/run* [x]
           (logic/fresh (x y)
             (logic/== 'split x)
             (logic/== 'pea y)
             (logic/conda
               [(logic/== 'split x) (logic/== x y)]
               [logic/s#]))
           (logic/== true x))
         '())))

(test/deftest test-conda-4
  (test/is (= (logic/run* [x]
           (logic/fresh (x y)
             (logic/== 'split x)
             (logic/== 'pea y)
             (logic/conda
               [(logic/== x y) (logic/== 'split x)]
               [logic/s#]))
           (logic/== true x))
         '(true))))

(defn not-pastao [x]
  (logic/conda
    [(logic/== 'pasta x) logic/u#]
    [logic/s#]))

(test/deftest test-conda-5
  (test/is (= (logic/run* [x]
           (logic/conda
             [(not-pastao x)]
             [(logic/== 'spaghetti x)]))
         '(spaghetti))))

;;
;; condu (committed-choice)
;;

(defn onceo [g]
  (logic/condu
    (g logic/s#)))

(test/deftest test-condu-1
  (test/is (= (logic/run* [x]
           (onceo (teacupo x)))
         '(tea))))

(test/deftest test-condu-2
  (test/is (= (into #{}
           (logic/run* [r]
             (logic/conde
               [(teacupo r) logic/s#]
               [(logic/== false r) logic/s#])))
         (into #{}
           '(false tea cup)))))

(test/deftest test-condu-3
  (test/is (= (into #{}
           (logic/run* [r]
             (logic/conda
               [(teacupo r) logic/s#]
               [(logic/== false r) logic/s#])))
         (into #{} '(tea cup)))))


(test/deftest test-condu-3
  (test/is (= (into #{}
           (logic/run* [r]
             (logic/condu
               [(teacupo r) logic/s#]
               [(logic/== false r) logic/s#])))
         (into #{} '(tea)))))


