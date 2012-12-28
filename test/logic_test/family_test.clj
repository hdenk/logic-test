(ns logic-test.family-test
  (:refer-clojure :exclude [==])
  (:require [logic-test.family :as family]
    [clojure.core.logic :as logic]
    [clojure.test :as test]))

(defn populate-db [test-function]
	;; :pam is :bob's parent
	(logic/fact family/parent :pam :bob)
	(logic/fact family/parent :tom :bob)
	(logic/fact family/parent :tom :liz)
	(logic/fact family/parent :bob :ann)
	(logic/fact family/parent :bob :pat)
	(logic/fact family/parent :pat :jim)
	
	;; and :pam is female
	(logic/fact family/female :pam)
	(logic/fact family/male :tom)
	(logic/fact family/male :bob)
	(logic/fact family/female :liz)
	(logic/fact family/female :ann)
	(logic/fact family/female :pat)
	(logic/fact family/male :jim)

  (test-function))

(test/use-fixtures :once populate-db)

(test/deftest bobs-parents
  (test/is
    (=
      #{:pam :tom}
      (set 
        (logic/run* 
          [q]
          (family/parent q :bob))))))

(test/deftest bobs-mother
  (test/is
    (=
      '(:pam)
      (logic/run* 
        [q]
        (family/parent q :bob)
        (family/female q)))))

(test/deftest bobs-father
  (test/is
    (=
      '(:tom)
      (logic/run* 
        [q]
        (family/parent q :bob)
        (family/male q)))))

(test/deftest bobs-sisters
  (test/is
    (=
      #{:liz}
      (set (logic/run* [q]
        (family/sister q :bob))))))

(test/deftest lizs-brothers
  (test/is
    (=
      #{:bob}
      (set (logic/run* [q]
        (family/brother q :liz))))))

(test/deftest pams-children
  (test/is
    (=
      #{:bob}
      (set (logic/run* [q]
        (family/parent :pam q))))))

(test/deftest anns-grandparents
  (test/is
    (=
      #{:pam :tom}
      (set (logic/run* [q]
        (family/grandparent q :ann))))))

(test/deftest anns-grandmother
  (test/is
    (=
      #{:pam}
      (set (logic/run* [q]
        (family/grandparent q :ann)
        (family/female q))))))

(test/deftest jims-ancestors 
  (test/is
    (=
      #{:pam :tom :bob :pat}
      (set (logic/run* [q]
        (family/ancestor q :jim))))))

(test/deftest toms-descendants 
  (test/is
    (=
      #{:ann :jim :bob :liz :pat}
      (set (logic/run* [q]
        (family/ancestor :tom q))))))  