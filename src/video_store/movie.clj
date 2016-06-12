(ns video-store.movie
  (:require [clojure.spec :as s]
            [clojure.spec.test :as test]))

(s/def ::price-code #{::regular ::new-release ::childrens})
(s/def ::title (s/and string? (complement empty?)))
(s/def ::movie (s/keys :req [::title ::price-code]))

(defn make-movie [title price-code]
  {::title title ::price-code price-code})

(s/fdef make-movie
        :args (s/cat :title ::title :price-code ::price-code)
        :ret ::movie)

(defmulti amount (fn [movie _] (::price-code movie)))

(defmethod amount ::regular
  [_movie days-rented]
  (+ 2.0 (if (> days-rented 2)
           (* 1.5 (- days-rented 2))
           0)))

(defmethod amount ::new-release
  [_movie days-rented]
  (* 3.0 days-rented))

(defmethod amount ::childrens
  [_movie days-rented]
  (+ 1.5 (if (> days-rented 3)
           (* 1.5 (- days-rented 3))
           0)))

(defmulti valid-return-amount? (fn [price-code _] price-code))
(defmethod valid-return-amount? ::regular
  [_ amount]
  (or (= 2.0 amount) (= 0.0 (rem (- amount 2) 1.5))))

(defmethod valid-return-amount? ::new-release
  [_ amount]
  (= 0.0 (rem amount 3.0)))

(defmethod valid-return-amount? ::childrens
  [_ amount]
  (= 0.0 (rem amount 1.5)))

(s/fdef amount
        :args (s/cat :movie ::movie :days (s/and integer? #(> % 0)))
        :ret (s/and double? #(>= % 1.5))
        :fn #(valid-return-amount? (-> % :args :movie ::price-code)
                                   (-> % :ret)))

(defmulti frequent-renter-points (fn [movie _] (::price-code movie)))

(defmethod frequent-renter-points ::new-release
  [_movie days-rented]
  (if (> days-rented 1) 2 1))

(defmethod frequent-renter-points :default
  [_movie _days-rented]
  1)

(s/fdef frequent-renter-points
        :args (s/cat :movie ::movie :days (s/and integer? #(> % 0)))
        :ret #{1 2})