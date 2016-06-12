(ns video-store.customer-test
  (:require [clojure.test :refer :all]
            [video-store.customer :as c]
            [video-store.movie :as m]
            [video-store.rental :as r]))

(def customer (c/->Customer "Martin" []))
(def regular-movie (m/->Movie "Mad Max" m/REGULAR))
(def new-movie (m/->Movie "The Hobbit" m/NEW-RELEASE))
(def childrens-movie (m/->Movie "Bambi" m/CHILDRENS))

(deftest statement-test
  (testing "empty rentals"
    (is (= (c/statement customer)
           "Rental Record for Martin\nAmount owed is 0.0\nYou earned 0 frequent renter points"))
    (testing "with one regular rental"
      (testing "for 1 day"
        (let [c (c/add-rental customer (r/->Rental regular-movie 1))]
          (is (= (c/statement c)
                 "Rental Record for Martin\n\tMad Max\t2.0\nAmount owed is 2.0\nYou earned 1 frequent renter points"))))
      (testing "for 2 day"
        (let [c (c/add-rental customer (r/->Rental regular-movie 2))]
          (is (= (c/statement c)
                 "Rental Record for Martin\n\tMad Max\t2.0\nAmount owed is 2.0\nYou earned 1 frequent renter points"))))
      (testing "for 3 day"
        (let [c (c/add-rental customer (r/->Rental regular-movie 3))]
          (is (= (c/statement c)
                 "Rental Record for Martin\n\tMad Max\t3.5\nAmount owed is 3.5\nYou earned 1 frequent renter points"))))
      (testing "with one new release rental"
        (testing "for 1 day"
          (let [c (c/add-rental customer (r/->Rental new-movie 1))]
            (is (= (c/statement c)
                   "Rental Record for Martin\n\tThe Hobbit\t3.0\nAmount owed is 3.0\nYou earned 1 frequent renter points"))))
        (testing "for 2 day"
          (let [c (c/add-rental customer (r/->Rental new-movie 2))]
            (is (= (c/statement c)
                   "Rental Record for Martin\n\tThe Hobbit\t6.0\nAmount owed is 6.0\nYou earned 2 frequent renter points"))))
        (testing "for 3 day"
          (let [c (c/add-rental customer (r/->Rental new-movie 3))]
            (is (= (c/statement c)
                   "Rental Record for Martin\n\tThe Hobbit\t9.0\nAmount owed is 9.0\nYou earned 2 frequent renter points")))))
      (testing "with one childrens rental"
        (testing "for 1 day"
          (let [c (c/add-rental customer (r/->Rental childrens-movie 1))]
            (is (= (c/statement c)
                   "Rental Record for Martin\n\tBambi\t1.5\nAmount owed is 1.5\nYou earned 1 frequent renter points"))))
        (testing "for 3 day"
          (let [c (c/add-rental customer (r/->Rental childrens-movie 3))]
            (is (= (c/statement c)
                   "Rental Record for Martin\n\tBambi\t1.5\nAmount owed is 1.5\nYou earned 1 frequent renter points"))))
        (testing "for 4 day"
          (let [c (c/add-rental customer (r/->Rental childrens-movie 4))]
            (is (= (c/statement c)
                   "Rental Record for Martin\n\tBambi\t3.0\nAmount owed is 3.0\nYou earned 1 frequent renter points"))))))))

